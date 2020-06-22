package wannabit.io.eoswallet.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.AddKeyActivity;
import wannabit.io.eoswallet.activity.AppLockActivity;
import wannabit.io.eoswallet.activity.IntroActivity;
import wannabit.io.eoswallet.activity.MainActivity;
import wannabit.io.eoswallet.activity.PasscodeActivity;
import wannabit.io.eoswallet.activity.WalletDetailActivity;
import wannabit.io.eoswallet.dialog.DialogCommonError;
import wannabit.io.eoswallet.dialog.DialogRequestKey;
import wannabit.io.eoswallet.dialog.DialogRooted;
import wannabit.io.eoswallet.dialog.DialogWait;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.utils.LocaleHelper;
import wannabit.io.eoswallet.utils.RootCheckHelper;
import wannabit.io.eoswallet.utils.WLog;


/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */

public class BaseActivity extends AppCompatActivity {

    protected BaseApplication               mApplication;
    protected BaseDao                       mDao;
    protected DialogWait                    mDialogWait;
    protected DialogCommonError             mDialogCommonError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        Locale myLocale = new Locale(LocaleHelper.getLanguage(getBaseContext()));
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public BaseApplication getBaseApplication() {
        if (mApplication == null)
            mApplication = (BaseApplication) getApplication();
        return mApplication;
    }

    public BaseDao getBaseDao() {
        if (mDao == null)
            mDao = getBaseApplication().getBaseDao();
        return mDao;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (RootCheckHelper.isRooted()) {
//            DialogRooted dialog  = new DialogRooted();
//            dialog.setCancelable(false);
//            dialog.show(getSupportFragmentManager(), "dialog");
//            return;
//        }

        if(!(this instanceof AppLockActivity) &&
                !(this instanceof IntroActivity) &&
                !(this instanceof PasscodeActivity)) {
            if(getBaseApplication().needShowLockScreen()) {
                Intent intent = new Intent(this, AppLockActivity.class);
                intent.putExtra("locktype", AppLockActivity.LockType.Lock);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
        }
    }

    public BaseApplication.AppStatus getAppStatus() {
        return getBaseApplication().getAppStatus();
    }


    public void onShowWaitDialog() {
        mDialogWait = new DialogWait();
        mDialogWait.setCancelable(false);
        getSupportFragmentManager().beginTransaction().add(mDialogWait, "wait").commitNowAllowingStateLoss();
    }

    public void onHideWaitDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("wait");
        if (prev != null) {
            ft.remove(prev).commitNowAllowingStateLoss();
        }
    }

    public void onShowErrorDialog() {
        if(mDialogCommonError != null && mDialogCommonError.isAdded()) return;
        mDialogCommonError = new DialogCommonError();
        mDialogCommonError.setCancelable(false);
        getSupportFragmentManager().beginTransaction().add(mDialogCommonError, "error").commitNowAllowingStateLoss();
    }

    public void onErrorChecked() {

    }


    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    public void onHideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v == null) {
            v = new View(getBaseContext());
        }
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    protected void onDeleteAccount(String account) {
        try {
            ArrayList<String> emptyList = new ArrayList<String>();
            getBaseDao().setUserHideTokens(getBaseDao().onSelectByAccount(account).getId(), emptyList);
            getBaseDao().onDeleteUser(account);

            if(getBaseDao().onSelectAllUser().size() == 0 ) {
                getBaseDao().setUsingFingerPrint(false);
                getBaseDao().setUsingAppLock(false);
            }

        } catch (Exception e) {
            WLog.r("Error during delete: " + e.getMessage());

        } finally {
            Toast.makeText(BaseActivity.this, R.string.str_account_delete, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finishAffinity();
        }
    }

    public void onStartAddKey(String account) {
        Intent intent = new Intent(BaseActivity.this, AddKeyActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);

    }
}
