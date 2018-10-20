package wannabit.io.eoswallet.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseApplication;
import wannabit.io.eoswallet.base.BaseDao;
import wannabit.io.eoswallet.model.WBAction;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.BPService;
import wannabit.io.eoswallet.network.ReqActions;
import wannabit.io.eoswallet.network.ResActions;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;


public class ActionTask {
    private static final Long       pageCnt = 2000l;
    private  boolean                mProgress;
    private BaseApplication         mApp;
    private BaseDao                 mDao;
    private WBUser                  mUser;
    private WBToken                 mToken;
    private ActionFetchListener     mListener;

    private ArrayList<WBAction>     mFetchedActions = new ArrayList<>();
    private ArrayList<WBAction>     mFilteredActions = new ArrayList<>();
    private Long                    mLocalTop;
    private Call<JsonObject>        mCall;

    public ActionTask(BaseApplication app, WBUser user, WBToken token, ActionFetchListener listner) {
        this.mApp = app;
        this.mDao = mApp.getBaseDao();
        this.mUser = user;
        this.mToken = token;
        this.mListener = listner;
    }

    public void onStartTask() {
        WLog.w("onStartTask : ");
        if(mProgress) return;
        mProgress = true;
        mFetchedActions.clear();
        mFilteredActions.clear();
        ArrayList<WBAction> mLocalActions = mDao.onSelectActionByAccount(mUser.getAccount());
        if(mLocalActions != null && mLocalActions.size() > 0) {
            mLocalTop = mLocalActions.get(0).account_action_seq + 1;
            mFetchedActions.addAll(mLocalActions);
        } else {
            mLocalTop = 0l;
        }
        onRequestActionsInit(mLocalTop);
    }

    public void onStopTask() {
        WLog.w("onStopTask : ");
        if(mCall != null)
            mCall.cancel();
        mProgress = false;
    }

    public boolean isRunning() {
        return mProgress;
    }

    private void onDone() {
        WLog.w("onDone mFetchedActions : " + mFetchedActions.size());
        String tempTxID = "";
        for(WBAction action:mFetchedActions) {
//            if((action.getAction_trace().getInline_traces() == null || action.getAction_trace().getInline_traces().size() == 0) &&
//                    action.getQuantity().contains(mToken.getSymbol()) &&
//                    !action.getAction_trace().getTrx_id().equals(tempTxID)) {
//                tempTxID = action.getAction_trace().getTrx_id();
//                mFilteredActions.add(action);
//            }

            if(action.action_trace.act.account.endsWith(mToken.getContractAddr()) &&
                    !action.action_trace.trx_id.equals(tempTxID)) {
                tempTxID = action.action_trace.trx_id;
                mFilteredActions.add(action);
            }
        }
        WLog.w("onDone mFilteredActions : " + mFilteredActions.size());

        if(mListener != null)
            mListener.onActionFetchCompleted();
        onStopTask();
    }

    private void onError() {
        if(mListener != null)
            mListener.onActionFetchError();
        onStopTask();

    }

    public ArrayList<WBAction> getFetchedData() {
        return mFetchedActions;
    }

    public ArrayList<WBAction> getFilteredData() {
        return mFilteredActions;
    }





    private void onRequestActionsInit(final long startPos) {
        WLog.w("onRequestActionsInit : " + startPos + "  " + mUser.getAccount());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.bp_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReqActions request = new ReqActions(mUser.getAccount(), startPos, pageCnt);
        BPService service = retrofit.create(BPService.class);


        mCall = service.getActions(request);
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    ResActions temp = new Gson().fromJson(response.body(), ResActions.class);
                    if(temp.getTime_limit_exceeded_error() == null) {
                        if(temp.getActions() != null && temp.getActions().size() > 0) {
                            ArrayList<WBAction> tempActions = temp.getActions();
                            Collections.sort(tempActions, new WUtil.WBActionDescCompare());

                            ArrayList<WBAction> tempFetched = new ArrayList<>();
                            tempFetched.addAll(tempActions);
                            tempFetched.addAll(mFetchedActions);
                            mFetchedActions.clear();
                            mFetchedActions.addAll(tempFetched);
//
//                            mDao.onInsertAllAction(mUser.getAccount(), tempActions);
//
                            if(tempActions.size() >= pageCnt) {
                                onRequestActionsInit(mFetchedActions.get(0).account_action_seq);
                            } else {
                                onDone();
                            }


                        } else {
                            WLog.w("onRequestActionsInit NO Actions");
                            onError();
                        }

                    } else {
                        WLog.w("onRequestActionsInit time error");
                        onError();
                    }
                } else {
                    WLog.w("onRequestActionsInit !isSuccessful");
                    onError();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onRequestActionsInit onFailure : " + t.getMessage());
                onError();
            }
        });
    }

}
