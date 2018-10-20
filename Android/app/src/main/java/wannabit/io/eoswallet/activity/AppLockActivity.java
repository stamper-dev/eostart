package wannabit.io.eoswallet.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.dialog.DialogFingerPrint;
import wannabit.io.eoswallet.dialog.DialogRecentSent;
import wannabit.io.eoswallet.fragment.PasscodeListener;
import wannabit.io.eoswallet.fragment.PasscodeNumberFragment;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class AppLockActivity extends BaseActivity implements PasscodeListener {

    public enum LockType {
        Init, Lock, Disable
    }

    private Toolbar     mToolbar;
    private TextView    mToolbartitle, mApplockTitle, mApplockMsg;
    private ImageView[] mCountImageView = new ImageView[4];

    private LockType                    mType;
    private String                      mUserInput = "";
    private String                      mConfirmInput = "";
    private boolean                     mIsConfirmSequence = false;

    private PasscodeNumberFragment      mNumberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);

        mNumberFragment = new PasscodeNumberFragment();
        mNumberFragment.setListener(AppLockActivity.this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.numbersLayer, mNumberFragment).commit();


        mToolbar        = findViewById(R.id.toolbar);
        mToolbartitle   = findViewById(R.id.toolbartitle);
        mApplockTitle   = findViewById(R.id.applockTitle);
        mApplockMsg     = findViewById(R.id.applockMsg);

        for(int i = 0; i < mCountImageView.length; i++) {
            mCountImageView[i] = findViewById(getResources().getIdentifier("applockCircle" + i , "id", getPackageName()));
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mType = (LockType)getIntent().getSerializableExtra("locktype");
        onInitView();
    }


    @Override
    public void onBackPressed() {
        if (mType == LockType.Init) {
            onCancelResult();
        } else if (mType == LockType.Lock) {
            onGiveUpResult();
        } else if (mType == LockType.Disable) {
            onCancelResult();
        }
    }

    public void onSuccessResult(){
        Intent result = new Intent();
        result.putExtra("type","OK");
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void onGiveUpResult() {
        moveTaskToBack(true);
    }

    private void onCancelResult() {
        Intent result = new Intent();
        result.putExtra("type","cancel");
        setResult(Activity.RESULT_CANCELED, result);
        finish();
    }



    private void onInitView() {
        onClearCircles();
        if (mType == LockType.Init) {
            mToolbartitle.setText(R.string.str_applock_set);
            mApplockMsg.setText(R.string.str_applock_des);
            if(mIsConfirmSequence) {
                mApplockTitle.setText(R.string.str_applock_title2);

            } else {
                mApplockTitle.setText(R.string.str_applock_title);
            }

        } else if (mType == LockType.Lock) {
            mToolbartitle.setText("");
            mApplockTitle.setText(R.string.str_unlock_title);
            mApplockMsg.setVisibility(View.INVISIBLE);
            onCheckFingerPrint();

        } else if (mType == LockType.Disable) {
            mToolbartitle.setText("");
            mApplockTitle.setText(R.string.str_unlock_title);
            mApplockMsg.setVisibility(View.INVISIBLE);

        }

    }

    private void onClearCircles() {
        for(int i = 0; i < mCountImageView.length; i++) {
            mCountImageView[i].setBackground(getDrawable(R.drawable.circle_pin_unselected));
        }
    }

    private void onUserInputFinished() {
        if (mType == LockType.Init) {
            if(mIsConfirmSequence) {
                if(mConfirmInput.equals(mUserInput)) {
                    getBaseDao().setUsingAppLockStr(WUtil.onGetAppLockKey(mUserInput));
                    getBaseDao().setUsingAppLock(true);
                    onSuccessResult();
                } else {
                    Toast.makeText(this, R.string.str_error_password_differ, Toast.LENGTH_SHORT).show();
                    mConfirmInput = "";
                    mUserInput = "";
                    mIsConfirmSequence = false;
                    mNumberFragment.onShuffeKeyboard();
                    onInitView();
                }

            } else {
                mConfirmInput = mUserInput;
                mUserInput = "";
                mIsConfirmSequence = true;
                mNumberFragment.onShuffeKeyboard();
                onInitView();
            }

        } else if (mType == LockType.Lock) {
            if(getBaseDao().getUsingAppLockStr().equals(WUtil.onGetAppLockKey(mUserInput))) {
                onSuccessResult();
            } else {
                Toast.makeText(this, R.string.str_error_password, Toast.LENGTH_SHORT).show();
                mUserInput = "";
                mNumberFragment.onShuffeKeyboard();
                onInitView();
            }

        } else if (mType == LockType.Disable) {
            if(getBaseDao().getUsingAppLockStr().equals(WUtil.onGetAppLockKey(mUserInput))) {
                getBaseDao().setUsingAppLockStr("");
                getBaseDao().setUsingAppLock(false);
                getBaseDao().setUsingFingerPrint(false);
                onSuccessResult();

            } else {
                Toast.makeText(this, R.string.str_error_password, Toast.LENGTH_SHORT).show();
                mUserInput = "";
                mNumberFragment.onShuffeKeyboard();
                onInitView();
            }
        }
    }

    private void onUserCancelInput() {
//        WLog.w("OnUserCancelInput");
        onBackPressed();
    }

    @Override
    public void onUserInsertKey(char input) {
        if(mUserInput == null || mUserInput.length() == 0) {
            mUserInput = String.valueOf(input);

        } else if (mUserInput.length() >= 4) {
            mIsConfirmSequence =false;
            onInitView();
            return;

        } else {
            mUserInput = mUserInput + input;

        }

        onUpdateCountView();
        if (mUserInput.length() == 4) {
            onUserInputFinished();
        }
    }

    @Override
    public void onUserDeleteKey() {
        if(mUserInput == null || mUserInput.length() <= 0) {
            if(mIsConfirmSequence) return;
            else onUserCancelInput();
        } else {
            mUserInput = mUserInput.substring(0, mUserInput.length()-1);
            onUpdateCountView();
        }
    }

    private void onUpdateCountView() {
        if(mUserInput == null)
            mUserInput = "";

        final int inputLength = mUserInput.length();
        for(int i = 0; i < mCountImageView.length; i++) {
            if(i < inputLength)
                mCountImageView[i].setBackground(getDrawable(R.drawable.circle_pin_selected));
            else
                mCountImageView[i].setBackground(getDrawable(R.drawable.circle_pin_unselected));
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void onCheckFingerPrint() {
        FingerprintManagerCompat mFingerprintManagerCompat = FingerprintManagerCompat.from(this);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
                mFingerprintManagerCompat.isHardwareDetected() &&
                mFingerprintManagerCompat.hasEnrolledFingerprints() &&
                getBaseDao().getUsingFingerPrint()) {

            DialogFingerPrint dialog = DialogFingerPrint.newInstance(null);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "dialog");

        }
    }
}
