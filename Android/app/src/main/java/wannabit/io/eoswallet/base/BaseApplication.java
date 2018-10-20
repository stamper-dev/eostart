package wannabit.io.eoswallet.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import wannabit.io.eoswallet.BuildConfig;
import wannabit.io.eoswallet.utils.DeviceUuidFactory;


/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */

public class BaseApplication extends Application {

    private BaseDao     mBaseDao;
    private AppStatus   mAppStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        new DeviceUuidFactory(this);
        registerActivityLifecycleCallbacks(new LifecycleCallbacks());

        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());

    }


    public BaseDao getBaseDao() {
        if (mBaseDao == null)
            mBaseDao = new BaseDao(this);
        return mBaseDao;
    }

    public AppStatus getAppStatus() {
        return mAppStatus;
    }



    public boolean isReturnedForground() {
        return mAppStatus.ordinal() == AppStatus.RETURNED_TO_FOREGROUND.ordinal();
    }

    public boolean needShowLockScreen() {
        return isReturnedForground() && getBaseDao().getUsingAppLock();
    }


    public enum AppStatus {
        BACKGROUND,
        RETURNED_TO_FOREGROUND,
        FOREGROUND;
    }


    public class LifecycleCallbacks implements ActivityLifecycleCallbacks {

        private int running = 0;

        @Override
        public void onActivityStarted(Activity activity) {
            if (++running == 1) {
                mAppStatus = AppStatus.RETURNED_TO_FOREGROUND;
            } else if (running > 1) {
                mAppStatus = AppStatus.FOREGROUND;
            }

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (--running == 0) {
                mAppStatus = AppStatus.BACKGROUND;
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) { }

        @Override
        public void onActivityResumed(Activity activity) { }

        @Override
        public void onActivityPaused(Activity activity) { }


        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }

        @Override
        public void onActivityDestroyed(Activity activity) { }
    }
}
