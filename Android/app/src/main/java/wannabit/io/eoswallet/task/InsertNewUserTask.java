package wannabit.io.eoswallet.task;

import android.os.AsyncTask;

import wannabit.io.eoswallet.base.BaseApplication;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class InsertNewUserTask extends AsyncTask<String, Void, Long> {

    private BaseApplication         mApp;
    private TaskCallback.TaskType   mType;
    private TaskCallback            mCallback;

    public InsertNewUserTask(BaseApplication app, TaskCallback mCallback) {
        this.mApp = app;
        this.mType = TaskCallback.TaskType.InsertUser;
        this.mCallback = mCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Long doInBackground(String... strings) {
        WBUser newUser = WUtil.onSavePivateKey(mApp, strings[0], strings[1], strings[2]);
        return mApp.getBaseDao().onInsertUser(newUser);
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        if(result > 0) {
            mCallback.onSuccessTask(mType, null);
        } else {
            mCallback.onFailTask(mType, -1);
        }
    }
}
