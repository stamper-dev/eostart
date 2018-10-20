package wannabit.io.eoswallet.task;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Response;
import wannabit.io.eoswallet.base.BaseApplication;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.ReqAbiToBinUndelegate;
import wannabit.io.eoswallet.network.ResAbiToBin;
import wannabit.io.eoswallet.network.ResBlock;
import wannabit.io.eoswallet.network.ResInfo;
import wannabit.io.eoswallet.network.ResPushTxn;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

import static wannabit.io.eoswallet.base.BaseConstant.EosActions.CONST_EOS_ACTION_UNDELEGATE;
import static wannabit.io.eoswallet.base.BaseConstant.EosActions.CONST_EOS_CODE;

public class UndelegateTask extends AsyncTask<String, Void, String> {

    private BaseApplication mApp;
    private TaskCallback.TaskType   mType;
    private TaskCallback            mCallback;
    private int                     mErrorCode;

    public UndelegateTask(BaseApplication app, TaskCallback mCallback) {
        this.mApp = app;
        this.mType = TaskCallback.TaskType.Delegate;
        this.mCallback = mCallback;
        this.mErrorCode = -1;
    }


    /**
     *
     * @param strings
     *  strings[0] : pincode
     *  strings[1] : account
     *  strings[2] : userInfo
     *  strings[3] : for NET amount (ex 0.0001 EOS)
     *  strings[4] : for CPU amount (ex 0.0001 EOS)
     *
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        try {
            String key = WUtil.onGetPrivateKey(mApp, strings[0], strings[1], strings[2]);

            Response<ResInfo> resInfo = ApiClient.getInfo(mApp).execute();

            Response<ResBlock> resBlock = ApiClient.getBlock(mApp, resInfo.body().getHeadBlockNum()).execute();

            ReqAbiToBinUndelegate.Args args = new ReqAbiToBinUndelegate.Args(strings[1], strings[1], strings[3], strings[4]);
            Response<ResAbiToBin> resAbiToBin = ApiClient.abiJsonToBinUndelegate(mApp, CONST_EOS_CODE, CONST_EOS_ACTION_UNDELEGATE, args).execute();


            Response<JsonObject> resPush = ApiClient.pushTransaction(
                    mApp,
                    WUtil.getReqTransaction(mApp, resBlock.body(), resAbiToBin.body(), CONST_EOS_ACTION_UNDELEGATE, strings[1], CONST_EOS_CODE),
                    WUtil.getSignatures(mApp, resBlock.body(), resAbiToBin.body(), CONST_EOS_ACTION_UNDELEGATE, strings[1], key, CONST_EOS_CODE)
            ).execute();
            if(resPush.isSuccessful()) {
                ResPushTxn resTxn = new Gson().fromJson(resPush.body(), ResPushTxn.class);
                result = resTxn.getTransactionId();

            } else {
                JSONObject jObjError = new JSONObject(resPush.errorBody().string());
                mErrorCode = jObjError.getInt("code");
            }



        } catch (Exception e) {
            WLog.w("UndelegateTask : " + e.getMessage());
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
            mCallback.onFailTask(mType, mErrorCode);
        }

    }
}