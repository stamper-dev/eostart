package wannabit.io.eoswallet.task;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Response;
import wannabit.io.eoswallet.base.BaseApplication;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.ReqAbiToBinSellRam;
import wannabit.io.eoswallet.network.ResAbiToBin;
import wannabit.io.eoswallet.network.ResBlock;
import wannabit.io.eoswallet.network.ResInfo;
import wannabit.io.eoswallet.network.ResPushTxn;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;
import static wannabit.io.eoswallet.base.BaseConstant.EosActions.*;

public class SellRamTask extends AsyncTask<String, Void, String> {

    private BaseApplication mApp;
    private TaskCallback.TaskType   mType;
    private TaskCallback            mCallback;
    private int                     mErrorCode;

    public SellRamTask(BaseApplication app, TaskCallback mCallback) {
        this.mApp = app;
        this.mType = TaskCallback.TaskType.SellRam;
        this.mCallback = mCallback;
        this.mErrorCode = -1;
    }

    /**
     *
     * @param strings
     *  strings[0] : pincode
     *  strings[1] : account
     *  strings[2] : userInfo
     *  strings[3] : byte amount (ex : 1024)
     *
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        WLog.w("SellRamTask");
        try {
            String key = WUtil.onGetPrivateKey(mApp, strings[0], strings[1], strings[2]);
            Response<ResInfo> resInfo = ApiClient.getInfo(mApp).execute();
            Response<ResBlock> resBlock = ApiClient.getBlock(mApp, resInfo.body().getHeadBlockNum()).execute();

            ReqAbiToBinSellRam.Args args = new ReqAbiToBinSellRam.Args(strings[1], strings[3]);
            Response<ResAbiToBin> resAbiToBin = ApiClient.abiJsonToBinSellRam(mApp, CONST_EOS_CODE, CONST_EOS_ACTION_SELLRAM, args).execute();
            WLog.w("resAbiToBin : " + resAbiToBin.body().getBinargs());

            Response<JsonObject> resPush = ApiClient.pushTransaction(
                    mApp,
                    WUtil.getReqTransaction(mApp, resBlock.body(), resAbiToBin.body(), CONST_EOS_ACTION_SELLRAM, strings[1], CONST_EOS_CODE),
                    WUtil.getSignatures(mApp, resBlock.body(), resAbiToBin.body(), CONST_EOS_ACTION_SELLRAM, strings[1], key, CONST_EOS_CODE)
            ).execute();

            WLog.w("resPush : " + resPush);
            if(resPush.isSuccessful()) {
                WLog.w("isSuccessful : " + resPush.body());
                ResPushTxn resTxn = new Gson().fromJson(resPush.body(), ResPushTxn.class);
                result = resTxn.getTransactionId();

            } else {
                WLog.w("error : " + resPush.errorBody());
                JSONObject jObjError = new JSONObject(resPush.errorBody().string());
                mErrorCode = jObjError.getInt("code");
            }

        } catch (Exception e) {
            WLog.w("SellRamTask : " + e.getMessage());
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
