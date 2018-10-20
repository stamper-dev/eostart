package wannabit.io.eoswallet.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.MainActivity;
import wannabit.io.eoswallet.activity.WalletDetailActivity;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.model.WBAction;
import wannabit.io.eoswallet.model.WBTransaction;
import wannabit.io.eoswallet.network.BPService;
import wannabit.io.eoswallet.network.ReqActions;
import wannabit.io.eoswallet.network.ResActions;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;


public class MainHistoryFragment extends BaseFragment {

    private static final Long           pageCnt = 50l;

    private SwipeRefreshLayout          swipeRefreshLayout;
    private RecyclerView                recyclerView;
    private RelativeLayout              mEmptyView, mInitLoadingView;

    private LinearLayoutManager         linearLayoutManager;
    private HistoryAdapter              mHistoryAdapter;
    ArrayList<WBAction>                 mActions = new ArrayList<>();
    private boolean                     mHasMore = false;
    private boolean                     mLoading = false;
    private String                      mCurrentAccount;

    public static MainHistoryFragment newInstance() {
        MainHistoryFragment fragment = new MainHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_history, container, false);
        swipeRefreshLayout  = rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView        = rootView.findViewById(R.id.recyclerView);
        mEmptyView          = rootView.findViewById(R.id.detailEmpty);
        mInitLoadingView    = rootView.findViewById(R.id.detailInitLoading);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRequestActionsInit(getMainActivity().getCurrentUser().getAccount());
            }
        });


        linearLayoutManager = new LinearLayoutManager(getMainActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mHistoryAdapter = new HistoryAdapter();
        recyclerView.setAdapter(mHistoryAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(mLoading) return;

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount && mHasMore) {
                    long startPosition = 0l;
                    long offset = pageCnt;

                    if(mActions.get(mActions.size() - 1).account_action_seq <= pageCnt) {
                        offset = mActions.get(mActions.size() - 1).account_action_seq - 1;
                    } else {
                        startPosition = mActions.get(mActions.size() - 1).account_action_seq - pageCnt -1;
                    }
                    onRequestActionMore(getMainActivity().getCurrentUser().getAccount(), startPosition, offset);
                }
            }

        });

        return rootView;
    }


    @Override
    public void onRefreshByMainTab(boolean deep) {
//        WLog.w("onRefreshByMainTab History " + deep);
        if(isAdded() && getMainActivity() != null && getMainActivity().getCurrentUser() != null && !mLoading) {
            willBeDisplayed();
            if(getMainActivity().getCurrentUser().getAccount().equals(mCurrentAccount)) {
                if(deep && linearLayoutManager != null) {
                    mInitLoadingView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                    linearLayoutManager.scrollToPositionWithOffset(0, 0);
                } else if (!deep && mActions.size() > 0 && swipeRefreshLayout.getVisibility() == View.VISIBLE) {
                    if(swipeRefreshLayout != null)
                        swipeRefreshLayout.setRefreshing(false);
                    return;
                }
            } else {
                mInitLoadingView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                if (linearLayoutManager != null)
                    linearLayoutManager.scrollToPositionWithOffset(0, 0);
                mCurrentAccount = getMainActivity().getCurrentUser().getAccount();
            }
            onInitHistoryList();

        } else {
            if(swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onInitHistoryList() {
        onRequestActionsInit(getMainActivity().getCurrentUser().getAccount());

        if(swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }



    private void onRequestActionsInit(final String userId) {
        WLog.w("onRequestActionsInit");
        mLoading = true;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.bp_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReqActions request = new ReqActions(userId, -1l, -pageCnt);
        BPService service = retrofit.create(BPService.class);

        service.getActions(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    ResActions temp = new Gson().fromJson(response.body(), ResActions.class);
                    if(temp.getTime_limit_exceeded_error() == null) {
                        mInitLoadingView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        mActions.clear();
                        if(temp.getActions() != null && temp.getActions().size() > 0) {
                            ArrayList<WBAction> tempActions = temp.getActions();
                            Collections.sort(tempActions, new WUtil.WBActionDescCompare());
//                            for(WBAction action: tempActions) {
//                                WLog.w("action : " + action.getAccount_action_seq());
//                            }

                            if(tempActions.size() < pageCnt || tempActions.get(tempActions.size()-1).account_action_seq == 0) {
                                mHasMore = false;
                            } else {
                                mHasMore = true;
                            }
                            mActions.addAll(tempActions);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            mEmptyView.setVisibility(View.GONE);
                            mHistoryAdapter.notifyDataSetChanged();


                        } else {
                            swipeRefreshLayout.setVisibility(View.GONE);
                            mEmptyView.setVisibility(View.VISIBLE);
                        }

                    } else {
                        WLog.w("onRequestActionsInit time error");
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    WLog.w("onRequestActionsInit !isSuccessful");
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                mLoading = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onRequestActionsInit onFailure : " + t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                mLoading = false;
                swipeRefreshLayout.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }
        });
    }


    private void onRequestActionMore(final String userId, long startPosition, long offset) {
        WLog.w("onRequestActionMore " + startPosition + "  " + offset);
        mLoading = true;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.bp_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ReqActions request = new ReqActions(userId, startPosition, offset);
        BPService service = retrofit.create(BPService.class);

        service.getActions(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    ResActions temp = new Gson().fromJson(response.body(), ResActions.class);
                    if(temp.getTime_limit_exceeded_error() == null) {
                        if(temp.getActions() != null && temp.getActions().size() > 0) {
                            ArrayList<WBAction> tempActions = temp.getActions();
                            Collections.sort(tempActions, new WUtil.WBActionDescCompare());
//                            for(WBAction action: tempActions) {
//                                WLog.w("action : " + action.getAccount_action_seq());
//                            }

                            if(tempActions.size() < pageCnt || tempActions.get(tempActions.size()-1).account_action_seq == 0) {
                                mHasMore = false;
                            } else {
                                mHasMore = true;
                            }
                            mActions.addAll(tempActions);
                            mHistoryAdapter.notifyDataSetChanged();


                        } else {

                        }
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        WLog.w("onRequestActionsInit time error");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    WLog.w("onRequestActionsInit !isSuccessful");
                    swipeRefreshLayout.setRefreshing(false);
                }
                mLoading = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onRequestActionsInit onFailure : " + t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                mLoading = false;
            }
        });

    }

    private MainActivity getMainActivity() {
        return (MainActivity)getBaseActivity();
    }




    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryAdapterItemHolder> {

        public HistoryAdapter() {
        }

        @Override
        public int getItemCount() {
            return mActions.size();
        }

        @Override
        public HistoryAdapterItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.item_action, viewGroup, false);
            return new HistoryAdapterItemHolder(v);
        }

        @Override
        public void onBindViewHolder(final HistoryAdapterItemHolder holder, int position) {
            final WBAction action = mActions.get(position);
            final String userAccount = getMainActivity().getCurrentUser().getAccount();
            final String actName = action.action_trace.act.name;

            holder.itemActionId.setText("" + action.account_action_seq + "/" + mActions.get(0).account_action_seq);
            holder.itemActionDate.setText(WUtil.getTimeformat(getMainActivity(), action.block_time));
            holder.itemActionTxid.setText(getString(R.string.str_txid_dash) + action.action_trace.trx_id);

            final WBAction.ActionTrace.Act.Data data = action.action_trace.act.getData();

            if(data != null && !TextUtils.isEmpty(data.memo)) {
                holder.itemActionMemo.setText(getString(R.string.str_memo_dash) + data.memo);
                holder.itemActionMemo.setVisibility(View.VISIBLE);
            } else {
                holder.itemActionMemo.setVisibility(View.GONE);
            }

            try {
                if(actName.equals("transfer")) {
                    if (data.from.equals(userAccount)) {
                        holder.itemActionType.setText("Sent");
                        holder.itemActionAmount.setTextColor(getResources().getColor(R.color.colorRed));
                        holder.itemActionAmount.setText("-"+ data.quantity);
                        holder.itemActionAccount.setText(data.to);

                    } else if (data.to.equals(userAccount)) {
                        holder.itemActionType.setText("Received");
                        holder.itemActionAmount.setTextColor(getResources().getColor(R.color.colorGreen));
                        holder.itemActionAmount.setText(""+data.quantity);
                        holder.itemActionAccount.setText(data.from);
                    }

                }  else if(!TextUtils.isEmpty(actName)){
                    holder.itemActionType.setText(actName);
                    holder.itemActionAmount.setText("");
                    holder.itemActionAccount.setText("");
                    if (data.from.equals(userAccount)) {
                        holder.itemActionAccount.setText(data.to);
                    } else {
                        holder.itemActionAccount.setText(data.from);
                    }
                } else {
                    holder.itemActionType.setText("ETC");
                    holder.itemActionAmount.setText("");
                    holder.itemActionAccount.setText("");
                }

            } catch (Exception e) {
                holder.itemActionType.setText("ETC");
                holder.itemActionAmount.setText("");
                holder.itemActionAccount.setText("");

            }

            holder.itemClickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(action.action_trace.trx_id)) {
                        Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eospark.com/MainNet/tx/"+action.action_trace.trx_id));
                        startActivity(webintent);
                    }
                }
            });
        }



        public class HistoryAdapterItemHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemClickView;
            TextView itemActionId, itemActionDate, itemActionTxid, itemActionType, itemActionAmount, itemActionAccount, itemActionMemo;

            public HistoryAdapterItemHolder(View v) {
                super(v);
                itemClickView   = itemView.findViewById(R.id.clickView);
                itemActionId    = itemView.findViewById(R.id.actionId);
                itemActionDate  = itemView.findViewById(R.id.actionDate);
                itemActionTxid  = itemView.findViewById(R.id.actionTxid);
                itemActionType  = itemView.findViewById(R.id.actionType);
                itemActionAmount = itemView.findViewById(R.id.actionAmount);
                itemActionAccount  = itemView.findViewById(R.id.actionAccount);
                itemActionMemo = itemView.findViewById(R.id.actionMemo);

            }
        }
    }
}
