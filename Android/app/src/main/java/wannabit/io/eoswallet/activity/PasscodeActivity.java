package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import java.util.ArrayList;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.fragment.KeyboardFragment;
import wannabit.io.eoswallet.dialog.DialogSendSuccess;
import wannabit.io.eoswallet.fragment.PasscodeAlphabetFragment;
import wannabit.io.eoswallet.fragment.PasscodeListener;
import wannabit.io.eoswallet.fragment.PasscodeNumberFragment;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.task.BuyRamByteTask;
import wannabit.io.eoswallet.task.BuyRamTask;
import wannabit.io.eoswallet.task.CheckKeyTask;
import wannabit.io.eoswallet.task.DelegateTask;
import wannabit.io.eoswallet.task.DeleteUserTask;
import wannabit.io.eoswallet.task.InsertNewUserTask;
import wannabit.io.eoswallet.task.SellRamTask;
import wannabit.io.eoswallet.task.SendTask;
import wannabit.io.eoswallet.task.TaskCallback;
import wannabit.io.eoswallet.task.UndelegateTask;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class PasscodeActivity extends BaseActivity implements PasscodeListener, TaskCallback {

    private Toolbar                     mToolbar;
    private TextView                    mContentsTitle;

    private RelativeLayout              mContentsLayer;
    private AHBottomNavigationViewPager mViewPager;
    private PassCodePagerAdapter        mViewPagerAdapter;
    private ImageView[]                 mCountImageView = new ImageView[5];


    private String                      mUserInput = "";
    private String                      mConfirmInput = "";
    private boolean                     mIsConfirmSequence;

    private String                      mAccount;
    private String                      mPrivateKey;
    private String                      mTargetAccount;
    private String                      mTargetAmount;
    private String                      mTargetMemo;
    private WBToken                     mTargetToken;
    private String                      mTarget;
    private String                      mTargetCpu;
    private String                      mTargetNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_passcode);

        mTarget         = getIntent().getStringExtra(BaseConstant.CONST_KEY_TARGET);
        mAccount        = getIntent().getStringExtra("account");
        mPrivateKey     = getIntent().getStringExtra("privateKey");

        mTargetAccount  = getIntent().getStringExtra("targetAccount");
        mTargetAmount   = getIntent().getStringExtra("targetAmount");
        mTargetMemo     = getIntent().getStringExtra("targetMemo");
        mTargetToken    = getIntent().getParcelableExtra("targetToken");
        mTargetCpu     = getIntent().getStringExtra("targetCpu");
        mTargetNet    = getIntent().getStringExtra("targetNet");
        WLog.w("mTargetCpu" + mTargetCpu);
        WLog.w("mTargetNet" + mTargetNet);

        mToolbar        = findViewById(R.id.toolbar);
        mContentsTitle  = findViewById(R.id.pincodeTitle);
        mContentsLayer  = findViewById(R.id.contentLayer);
        mViewPager      = findViewById(R.id.keyboard_pager);
        for(int i = 0; i < mCountImageView.length; i++) {
            mCountImageView[i] = findViewById(getResources().getIdentifier("pincodeCircle" + i , "id", getPackageName()));
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mViewPager.setOffscreenPageLimit(2);
        mViewPagerAdapter = new PassCodePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        onInitView();
    }

    @Override
    public void onErrorChecked() {
        finish();
    }


    @Override
    public void onBackPressed() {
        if(mUserInput != null && mUserInput.length() > 0) onUserDeleteKey();
        else super.onBackPressed();

    }

    private void onInitView() {
        if(mTarget.equals(BaseConstant.CONST_SEND)) {
            if(!getBaseDao().hasPrivateKey(mAccount)) {
                WLog.r("user try send transaction account without privatekey");
                return;
            }
        }

        if(mTarget.equals(BaseConstant.CONST_ADD_USER)) {
            mContentsTitle.setText(R.string.passcode_init);
        } else {
            mContentsTitle.setText(R.string.passcode_check);
        }


        mIsConfirmSequence = false;
        mUserInput = "";
        mConfirmInput = "";

        for(int i = 0; i < mCountImageView.length; i++) {
            mCountImageView[i].setBackground(getDrawable(R.drawable.circle_pin_unselected));
        }
        mViewPager.setCurrentItem(0, true);

    }

    private void onConfirmSequence() {
        mContentsTitle.setText(R.string.passcode_confirm);

        if(mViewPagerAdapter != null && mViewPagerAdapter.getFragments() != null) {
            for (KeyboardFragment frag: mViewPagerAdapter.getFragments()) {
                if(frag != null)
                    frag.onShuffeKeyboard();
            }
        }

        mIsConfirmSequence = true;
        mConfirmInput = mUserInput;
        mUserInput = "";

        for(int i = 0; i < mCountImageView.length; i++) {
            mCountImageView[i].setBackground(getDrawable(R.drawable.circle_pin_unselected));
        }
        mViewPager.setCurrentItem(0, true);

    }


    private void onUserInputFinished() {
//        WLog.w("onUserInputFinished");
        if (mTarget.equals(BaseConstant.CONST_ADD_USER)) {
            if(mIsConfirmSequence) {
                if(mConfirmInput.equals(mUserInput)) {
                    onShowWaitDialog();
                    new InsertNewUserTask(getBaseApplication(), this).execute(mPrivateKey, mConfirmInput, mAccount);
                } else {
                    onShakeView();
                }

            } else {
                onConfirmSequence();
            }
        } else if (mTarget.equals(BaseConstant.CONST_DELETE_USER)) {
            onShowWaitDialog();
            new DeleteUserTask(getBaseApplication(), this).execute(mUserInput, mAccount, getBaseDao().onSelectByAccount(mAccount).getUserinfo());

        } else if (mTarget.equals(BaseConstant.CONST_SEND)) {
            onShowWaitDialog();
            new SendTask(getBaseApplication(), this).execute(mUserInput,
                    mAccount,
                    getBaseDao().onSelectByAccount(mAccount).getUserinfo(),
                    mTargetAccount,
                    mTargetAmount,
                    mTargetMemo,
                    mTargetToken.getContractAddr());

        } else if (mTarget.equals(BaseConstant.CONST_KEY_CHECK)) {
            onShowWaitDialog();
            new CheckKeyTask(getBaseApplication(), this).execute(mUserInput, mAccount, getBaseDao().onSelectByAccount(mAccount).getUserinfo());

        } else if (mTarget.equals(BaseConstant.CONST_BUY_RAM_BYTE)) {
            onShowWaitDialog();
            new BuyRamByteTask(getBaseApplication(), this).execute(mUserInput,
                    mAccount,
                    getBaseDao().onSelectByAccount(mAccount).getUserinfo(),
                    mAccount,
                    mTargetAmount);

        } else if (mTarget.equals(BaseConstant.CONST_BUY_RAM)) {
            onShowWaitDialog();
            new BuyRamTask(getBaseApplication(), this).execute(mUserInput,
                    mAccount,
                    getBaseDao().onSelectByAccount(mAccount).getUserinfo(),
                    mAccount,
                    mTargetAmount + " EOS");

        } else if (mTarget.equals(BaseConstant.CONST_SELL_RAM)) {
            onShowWaitDialog();
            new SellRamTask(getBaseApplication(), this).execute(mUserInput,
                    mAccount,
                    getBaseDao().onSelectByAccount(mAccount).getUserinfo(),
                    mTargetAmount);

        } else if (mTarget.equals(BaseConstant.CONST_DELEGATE)) {
            onShowWaitDialog();
            new DelegateTask(getBaseApplication(), this).execute(mUserInput,
                    mAccount,
                    getBaseDao().onSelectByAccount(mAccount).getUserinfo(),
                    mTargetNet,
                    mTargetCpu);


        } else if (mTarget.equals(BaseConstant.CONST_UNDELEGATE)) {
            onShowWaitDialog();
            new UndelegateTask(getBaseApplication(), this).execute(mUserInput,
                    mAccount,
                    getBaseDao().onSelectByAccount(mAccount).getUserinfo(),
                    mTargetNet,
                    mTargetCpu);

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

    public void onSuccessTransaction(String txid) {
        Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        if(!TextUtils.isEmpty(txid)) {
//            Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bloks.io/transaction/"+txid));
            Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eospark.com/MainNet/tx/"+txid));
            startActivity(webintent);
        }

    }

    private void onShakeView() {
        mContentsLayer.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        animation.reset();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                onInitView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        mContentsLayer.startAnimation(animation);
    }



    @Override
    public void onUserInsertKey(char input) {
        if(mUserInput == null || mUserInput.length() == 0) {
            mUserInput = String.valueOf(input);
        } else if (mUserInput.length() >= 5) {
            WLog.r("PIN Error Overflow");
            onInitView();
            return;
        } else {
            mUserInput = mUserInput + input;
        }

        if (mUserInput.length() == 4) {
            mViewPager.setCurrentItem(1, true);
        } else if (mUserInput.length() == 5 && WUtil.checkPasscodePattern(mUserInput)) {
            onUserInputFinished();
        } else if (mUserInput.length() == 5 && !WUtil.checkPasscodePattern(mUserInput)) {
            WLog.r("PIN Error Pattern Fail");
            onInitView();
            return;
        }
        onUpdateCountView();

    }

    @Override
    public void onUserDeleteKey() {
        if(mUserInput == null || mUserInput.length() <= 0) {
            onBackPressed();
        } else if (mUserInput.length() == 4) {
            mUserInput = mUserInput.substring(0, mUserInput.length()-1);
            mViewPager.setCurrentItem(0, true);
        } else {
            mUserInput = mUserInput.substring(0, mUserInput.length()-1);
        }
        onUpdateCountView();

    }

    @Override
    public void onStartedTask(TaskType type) {
        if(type == TaskType.InsertUser) {

        } else if (type == TaskType.CheckKey) {

        } else if (type == TaskType.Send) {

        } else if (type == TaskType.DeleteUser) {

        }
    }

    @Override
    public void onSuccessTask(TaskType type, String result) {
        onHideWaitDialog();
        if(type == TaskType.InsertUser) {
            WLog.w("onSuccessTask InsertUser");
            getBaseDao().setRecentAccountId(getBaseDao().onSelectByAccount(mAccount).getId());
            Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (type == TaskType.CheckKey) {
            WLog.w("onSuccessTask CheckKey");
            Intent intent = new Intent(PasscodeActivity.this, AccountShowActivity.class);
            intent.putExtra("account", mAccount);
            intent.putExtra("key", result);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();

        } else if (type == TaskType.Send) {
            WLog.w("onSuccessTask Send");
            Bundle bundle = new Bundle();
            bundle.putString("txid", result);
            DialogSendSuccess dialog = DialogSendSuccess.newInstance(bundle);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "dialog");

        } else if (type == TaskType.DeleteUser) {
            WLog.w("onSuccessTask DeleteUser");
            onDeleteAccount(mAccount);

        } else if (type == TaskType.BuyRamByte) {
            WLog.w("onSuccessTask BuyRamByte");
            Toast.makeText(PasscodeActivity.this, R.string.str_ram_trade_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (type == TaskType.BuyRam) {
            WLog.w("onSuccessTask BuyRam");
            Toast.makeText(PasscodeActivity.this, R.string.str_ram_trade_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (type == TaskType.SellRam) {
            WLog.w("onSuccessTask SellRam");
            Toast.makeText(PasscodeActivity.this, R.string.str_ram_trade_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (type == TaskType.Delegate) {
            WLog.w("onSuccessTask Delegate");
            Toast.makeText(PasscodeActivity.this, R.string.str_delegate_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (type == TaskType.Undelegate) {
            WLog.w("onSuccessTask Undelegate");
            Toast.makeText(PasscodeActivity.this, R.string.str_undelegate_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

    }

    @Override
    public void onFailTask(TaskType type, int errorCode) {
        onHideWaitDialog();
        if(type == TaskType.InsertUser) {
            onInitView();

        } else if (type == TaskType.CheckKey) {
            Toast.makeText(PasscodeActivity.this, R.string.str_wrong_pincode, Toast.LENGTH_SHORT).show();
            onBackPressed();

        } else if (type == TaskType.Send) {
            if(errorCode == 500){
                Toast.makeText(PasscodeActivity.this, R.string.str_resource_less, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onShowErrorDialog();
            }


        } else if (type == TaskType.DeleteUser) {
            Toast.makeText(PasscodeActivity.this, R.string.str_wrong_pincode, Toast.LENGTH_SHORT).show();
            onBackPressed();

        } else if (type == TaskType.BuyRamByte) {
            if(errorCode == 500){
                Toast.makeText(PasscodeActivity.this, R.string.str_resource_less, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onShowErrorDialog();
            }

        } else if (type == TaskType.BuyRam) {
            if(errorCode == 500){
                Toast.makeText(PasscodeActivity.this, R.string.str_resource_less, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onShowErrorDialog();
            }

        } else if (type == TaskType.SellRam) {
            if(errorCode == 500){
                Toast.makeText(PasscodeActivity.this, R.string.str_resource_less, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onShowErrorDialog();
            }

        } else if (type == TaskType.Delegate) {
            if(errorCode == 500){
                Toast.makeText(PasscodeActivity.this, R.string.str_resource_less, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onShowErrorDialog();
            }

        } else if (type == TaskType.Undelegate) {
            if(errorCode == 500){
                Toast.makeText(PasscodeActivity.this, R.string.str_resource_less, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PasscodeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onShowErrorDialog();
            }

        }

    }


    public class PassCodePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<KeyboardFragment> mFragments = new ArrayList<>();

        public PassCodePagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            PasscodeNumberFragment number = PasscodeNumberFragment.newInstance();
            number.setListener(PasscodeActivity.this);
            mFragments.add(number);

            PasscodeAlphabetFragment alphabet = PasscodeAlphabetFragment.newInstance();
            alphabet.setListener(PasscodeActivity.this);
            mFragments.add(alphabet);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public ArrayList<KeyboardFragment> getFragments() {
            return mFragments;
        }
    }



}
