package wannabit.io.eoswallet.utils;

import android.util.Log;


/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */

import com.crashlytics.android.Crashlytics;

import wannabit.io.eoswallet.base.BaseConstant;

public class WLog {

    public static void e(String msg) {
        if (BaseConstant.IS_SHOWLOG) {
            Log.e(BaseConstant.LOG_TAG, msg + "\n");
        }
    }

    public static void w(String msg) {
        if (BaseConstant.IS_SHOWLOG) {
            Log.w(BaseConstant.LOG_TAG, msg+ "\n");
        }
    }

    public static void d(String msg) {
        if (BaseConstant.IS_SHOWLOG) {
            Log.d(BaseConstant.LOG_TAG, msg+ "\n");
        }
    }

    public static void r(String msg) {
        e(msg);
        try {
            Crashlytics.log(msg);
        } catch (Exception e) {
            e("Failed to bug reprot");
        }
    }
}
