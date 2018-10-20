package wannabit.io.eoswallet.task;

import android.os.AsyncTask;

import wannabit.io.eoswallet.base.BaseApplication;
import wannabit.io.eoswallet.utils.WUtil;

public class CheckKeyTask extends AsyncTask<String, Void, String> {

    private BaseApplication         mApp;
    private TaskCallback.TaskType   mType;
    private TaskCallback            mCallback;

    public CheckKeyTask(BaseApplication app, TaskCallback mCallback) {
        this.mApp = app;
        this.mType = TaskCallback.TaskType.CheckKey;
        this.mCallback = mCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        try {
            result = WUtil.onGetPrivateKey(mApp, strings[0], strings[1], strings[2]);
        } catch (Exception e) {
            result = null;
        } finally {
            return result;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) {
            mCallback.onSuccessTask(mType, result);
        } else {
            mCallback.onFailTask(mType, -1);
        }
    }
}
