package wannabit.io.eoswallet.task;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseApplication;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.crypto.ec.EosPrivateKey;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.ReqAbiToBin;
import wannabit.io.eoswallet.network.ReqPushTxn;
import wannabit.io.eoswallet.network.ResAbiToBin;
import wannabit.io.eoswallet.network.ResBlock;
import wannabit.io.eoswallet.network.ResInfo;
import wannabit.io.eoswallet.network.ResPushTxn;
import wannabit.io.eoswallet.type.TypeChainId;
import wannabit.io.eoswallet.utils.Action;
import wannabit.io.eoswallet.utils.SignedTransaction;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;
import static wannabit.io.eoswallet.base.BaseConstant.EosActions.*;

public class SendTask extends AsyncTask<String, Void, String> {

    private BaseApplication         mApp;
    private TaskCallback.TaskType   mType;
    private TaskCallback            mCallback;
    private int                     mErrorCode;

    public SendTask(BaseApplication app, TaskCallback mCallback) {
        this.mApp = app;
        this.mType = TaskCallback.TaskType.Send;
        this.mCallback = mCallback;
        this.mErrorCode = -1;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    /**
     *
     * @param strings
     *  strings[0] : pincode
     *  strings[1] : account
     *  strings[2] : userInfo
     *  strings[3] : receiver account
     *  strings[4] : amount
     *  strings[5] : memo
     *  strings[6] : contract_address
     *
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {
        WLog.w("SendTask doInBackground");
        String result = null;
        try {
            String key = WUtil.onGetPrivateKey(mApp, strings[0], strings[1], strings[2]);

            Response<ResInfo> resInfo = ApiClient.getInfo(mApp).execute();

            Response<ResBlock> resBlock = ApiClient.getBlock(mApp, resInfo.body().getHeadBlockNum()).execute();

            ReqAbiToBin.Args args = new ReqAbiToBin.Args(strings[1], strings[3], strings[4], strings[5]);
            Response<ResAbiToBin> resAbiToBin = ApiClient.abiJsonToBin(mApp, strings[6], CONST_EOS_ACTION_TRANSFER, args).execute();


            Response<JsonObject> resPush = ApiClient.pushTransaction(
                    mApp,
                    WUtil.getReqTransaction(mApp, resBlock.body(), resAbiToBin.body(), CONST_EOS_ACTION_TRANSFER, strings[1], strings[6]),
                    WUtil.getSignatures(mApp, resBlock.body(), resAbiToBin.body(), CONST_EOS_ACTION_TRANSFER, strings[1], key, strings[6])
            ).execute();

            WLog.w("resPush " + resPush.toString());
            if(resPush.isSuccessful()) {
                ResPushTxn resTxn = new Gson().fromJson(resPush.body(), ResPushTxn.class);
                result = resTxn.getTransactionId();

            } else {
                JSONObject jObjError = new JSONObject(resPush.errorBody().string());
                WLog.w("jObjError " + jObjError.toString());
                mErrorCode = jObjError.getInt("code");
            }

            /*
            String date = resBlock.body().getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat(mApp.getString(R.string.str_eos_time_format));
            Date d = dateFormat.parse(date);
            long time = d.getTime() + 1000 * 120 ;
            String expiration = dateFormat.format(time);

            SignedTransaction beforeTxn = createTransaction(strings[6],
                    BaseConstant.CONST_EOS_ACTION_TRANSFER,
                    resAbiToBin.body().getBinargs(),
                    new String[] { strings[1] + "@active"},
                    resBlock.body().getBlock_num(),
                    resBlock.body().getRef_block_prefix(),
                    expiration
            );

            List<String> signatures = signTransaction(beforeTxn, key, mApp.getString(R.string.chain_id));
            ReqPushTxn.Authorization authorization = new ReqPushTxn.Authorization(strings[1], "active");

            List<ReqPushTxn.Authorization> authList= new ArrayList<>();
            authList.add(authorization);
            ReqPushTxn.Action action = new ReqPushTxn.Action(strings[6], BaseConstant.CONST_EOS_ACTION_TRANSFER, authList ,resAbiToBin.body().getBinargs());

            List<ReqPushTxn.Action> actionList = new ArrayList<>();
            actionList.add(action);

            ReqPushTxn.Transaction transaction = new ReqPushTxn.Transaction(
                    expiration,
                    resBlock.body().getBlock_num(),
                    resBlock.body().getRef_block_prefix(),
                    actionList
            );

            Response<JsonObject> resPush = ApiClient.pushTransaction(mApp, transaction, signatures).execute();
            if(resPush.isSuccessful()) {
                ResPushTxn resTxn = new Gson().fromJson(resPush.body(), ResPushTxn.class);
                result = resTxn.getTransactionId();

            } else {
                JSONObject jObjError = new JSONObject(resPush.errorBody().string());
                mErrorCode = jObjError.getInt("code");
            }
            */



        } catch (Exception e) {
            WLog.w("SendTask : " + e.getMessage());
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