package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.dialog.DialogRequestKey;
import wannabit.io.eoswallet.model.WBAction;
import wannabit.io.eoswallet.model.WBEosParkTx;
import wannabit.io.eoswallet.model.WBRecent;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.model.WBTransaction;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.BPService;
import wannabit.io.eoswallet.network.EosParkService;
import wannabit.io.eoswallet.network.ReqActions;
import wannabit.io.eoswallet.network.ReqCurrencyBalance;
import wannabit.io.eoswallet.network.ReqParkAction;
import wannabit.io.eoswallet.network.ResActions;
import wannabit.io.eoswallet.network.ResParkAction;
import wannabit.io.eoswallet.network.ResParkNewAction;
import wannabit.io.eoswallet.task.ActionFetchListener;
import wannabit.io.eoswallet.task.ActionTask;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class WalletDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int    initPage = 1;
    private static final int    PageCnt = 30;

    private Toolbar             mToolbar;
    private TextView            mToolbarTitle;
    private ImageView           mTokenImage;
    private TextView            mTokenAmount, mTokenPrice, mTotalTxCnt;
    private SwipeRefreshLayout  swipeRefreshLayout;
    private RecyclerView        recyclerView;
    private LinearLayout        mControlLayout;
    private Button              mSendBtn, mReceiveBtn;
    private RelativeLayout      mEmptyView, mInitLoadingView;


    private LinearLayoutManager linearLayoutManager;
    private ActionAdapter       actionAdapter;


    private int                         mCurrentPage = initPage;
    private int                         mTotalSize = 0;
    private WBToken                     mWBToken;
    private WBUser                      mSelectedUser;
    private ArrayList<WBEosParkTx>      mFilteredActions = new ArrayList<>();
    private boolean                     mHasMore = false;
    private boolean                     mLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);
        mToolbar            = findViewById(R.id.toolbar);
        mToolbarTitle       = findViewById(R.id.toolbar_title);
        mTokenImage         = findViewById(R.id.detailImage);
        mTokenAmount        = findViewById(R.id.detailAmount);
        mTokenPrice         = findViewById(R.id.detailPrice);
        swipeRefreshLayout  = findViewById(R.id.swipeRefreshLayout);
        recyclerView        = findViewById(R.id.recyclerView);
        mSendBtn            = findViewById(R.id.btn_send);
        mReceiveBtn         = findViewById(R.id.btn_receive);
        mControlLayout      = findViewById(R.id.controlLayout);
        mTotalTxCnt         = findViewById(R.id.detailTxCnt);
        mEmptyView          = findViewById(R.id.detailEmpty);
        mInitLoadingView    = findViewById(R.id.detailInitLoading);

        mWBToken = getIntent().getParcelableExtra("token");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onReloadTransactions();
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        actionAdapter = new ActionAdapter(mFilteredActions);
        recyclerView.setAdapter(actionAdapter);

        mSendBtn.setOnClickListener(this);
        mReceiveBtn.setOnClickListener(this);

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
                    WLog.w("Hit Bottom");
                    mCurrentPage++;
                    onReqTransaction();

                }
            }

        });

        onInitView();
    }

    @Override
    public void onErrorChecked() {
        onBackPressed();
    }

    private void onInitView() {
        Long lastUserId = getBaseDao().getRecentAccountId();
        ArrayList<WBUser> registedUsers  = getBaseDao().onSelectAllUser();
        if(registedUsers == null || registedUsers.size() < 1) {
            WLog.r("WalletDetailActivity NO Account Error!!");
            finish();
        } else if (lastUserId < 0) {
            mSelectedUser = registedUsers.get(0);

        } else {
            mSelectedUser = registedUsers.get(0);
            for(WBUser user:registedUsers) {
                if(user.getId() == lastUserId) {
                    mSelectedUser = user;
                    break;
                }
            }
        }
        onUpdateHeaderView();
        onReloadTransactions();

    }

    private void onUpdateHeaderView() {
        mToolbarTitle.setText(mWBToken.getSymbol());
        if(mWBToken.getContractAddr().equals(getString(R.string.str_eos_contract))) {
            Glide.with(this).load(R.drawable.logo_eos).asGif().into(new SimpleTarget<GifDrawable>() {
                @Override
                public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                    resource.start();
                    mTokenImage.setImageDrawable(resource);
                }
                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    mTokenImage.setImageDrawable(getDrawable(R.drawable.eos_logo_white));
                }
            });

        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(WalletDetailActivity.this)
                            .load(mWBToken.getIconUrl())
                            .fitCenter()
                            .override(108, 108)
                            .into(mTokenImage);
                }
            });
        }

        ApiClient.getCurrency(this, mWBToken.getContractAddr(), mSelectedUser.getAccount(), mWBToken.getSymbol()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()) {
                    if(response.body().size() > 0) {
                        mTokenAmount.setText(WUtil.AmountSpanFormat(getBaseContext(), response.body().get(0), mWBToken));
                        if(mWBToken.getContractAddr().equals(getString(R.string.str_eos_contract))) {
                            final Double eosAmount = Double.parseDouble(response.body().get(0).toLowerCase().replace(mWBToken.getSymbol().toLowerCase(), ""));
                            mTokenPrice.setText(WUtil.getDisplayPriceSumStr(getBaseContext(), getBaseDao().getLastEosTic(), getBaseDao().getUserCurrencyStr(getBaseContext()), eosAmount));
                        } else {
                            mTokenPrice.setText(WUtil.getDisplayPriceSumStr(getBaseContext(), getBaseDao().getLastEosTic(), getBaseDao().getUserCurrencyStr(getBaseContext()), 0d));
                        }
                        mControlLayout.setVisibility(View.VISIBLE);

                    } else {
                        mTokenAmount.setText(WUtil.AmountSpanFormat(getBaseContext(), "0", mWBToken));
                        mTokenPrice.setText(WUtil.getDisplayPriceSumStr(getBaseContext(), getBaseDao().getLastEosTic(), getBaseDao().getUserCurrencyStr(getBaseContext()), 0d));
                    }

                } else {
                    onShowErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                WLog.w("ReqCurrencyBalance Fail " + t.getMessage());
                onShowErrorDialog();
            }
        });
    }

    private void onReloadTransactions() {
        mCurrentPage = initPage;
        onReqTransaction();
    }


    private ArrayList<WBRecent> onGetRecentList() {
        ArrayList<WBRecent> recentList = new ArrayList<>();
        for(WBEosParkTx action:mFilteredActions) {
            boolean duple = false;
            String target = "";
            if (action.getSender().equals(mSelectedUser.getAccount())) {
                target = action.getReceiver();
            } else {
                target = action.getSender();
            }

            for(WBRecent inner:recentList) {
                if(inner.getAccount().equals(target)){
                    duple = true;
                    break;
                }
            }
            if(!duple)
                recentList.add(new WBRecent(target, action.getTimestamp()));
            if(recentList.size() > 4)
                break;
        }
        return recentList;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(mSendBtn)) {
            if(getBaseDao().hasPrivateKey(mSelectedUser.getAccount())) {
                Intent intent = new Intent(WalletDetailActivity.this, SendActivity.class);
                intent.putExtra("token", mWBToken);
                intent.putParcelableArrayListExtra("list", onGetRecentList());
                startActivity(intent);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("account", mSelectedUser.getAccount());
                DialogRequestKey dialog  = DialogRequestKey.newInstance(bundle);
                dialog.setCancelable(true);
                dialog.show(getSupportFragmentManager(), "dialog");
            }


        } else if (view.equals(mReceiveBtn)) {
            Intent intent = new Intent(WalletDetailActivity.this, ReceiveActivity.class);
            intent.putExtra("accountId", mSelectedUser.getId());
            startActivity(intent);
        }

    }


    class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionAdapterItemHolder> {
        ArrayList<WBEosParkTx> transactions = new ArrayList<>();


        public ActionAdapter(ArrayList<WBEosParkTx> transactions) {
            this.transactions = transactions;
        }

        @Override
        public int getItemCount() {
            return transactions.size();
        }

        @Override
        public ActionAdapterItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.item_action, viewGroup, false);
            return new ActionAdapterItemHolder(v);
        }

        @Override
        public void onBindViewHolder(final ActionAdapterItemHolder holder, int position) {
            final WBEosParkTx mTx = transactions.get(position);
            holder.itemActionId.setText("" + (mTotalSize - position)+"/"+mTotalSize);
            holder.itemActionDate.setText(WUtil.getTimeformat(WalletDetailActivity.this, mTx.getTimestamp()));
            holder.itemActionTxid.setText(getString(R.string.str_txid_dash) + mTx.getTrx_id());
            if(!TextUtils.isEmpty(mTx.getMemo())) {
                holder.itemActionMemo.setText(getString(R.string.str_memo_dash) + mTx.getMemo());
                holder.itemActionMemo.setVisibility(View.VISIBLE);
            } else {
                holder.itemActionMemo.setVisibility(View.GONE);
            }

            if (mTx.getSender().equals(mSelectedUser.getAccount())) {
                holder.itemActionAccount.setText(mTx.getReceiver());
                holder.itemActionType.setText(getString(R.string.str_sent));
                holder.itemActionAmount.setTextColor(getResources().getColor(R.color.colorRed));
                holder.itemActionAmount.setText(WUtil.AmountSpanFormat(getBaseContext(), "-"+ mTx.getQuantity(), mWBToken));

            } else {
                holder.itemActionAccount.setText(mTx.getSender());
                holder.itemActionType.setText(getString(R.string.str_received));
                holder.itemActionAmount.setTextColor(getResources().getColor(R.color.colorGreen));
                holder.itemActionAmount.setText(WUtil.AmountSpanFormat(getBaseContext(), mTx.getQuantity(), mWBToken));
            }

        }



        public class ActionAdapterItemHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemClickView;
            TextView itemActionId, itemActionDate, itemActionTxid, itemActionType, itemActionAmount, itemActionAccount, itemActionMemo;

            public ActionAdapterItemHolder(View v) {
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

    private void onReqTransaction() {
        mLoading = true;
        ApiClient.getparkActions(this, mSelectedUser.getAccount(),
                mCurrentPage, PageCnt,
                mWBToken.getSymbol(), mWBToken.getContractAddr())
                .enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    ResParkNewAction temp = new Gson().fromJson(response.body(), ResParkNewAction.class);
                    mTotalSize = temp.getTotalCnt();
                    mTotalTxCnt.setText("" + mTotalSize);
                    if(mCurrentPage == initPage) {
                        mFilteredActions.clear();
                    }
                    mFilteredActions.addAll(temp.getFilteredTx());
                    mInitLoadingView.setVisibility(View.GONE);

                    if (mTotalSize == 0 || mFilteredActions.size() == 0){
                        swipeRefreshLayout.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        mSendBtn.setEnabled(false);

                    } else if(mTotalSize > mFilteredActions.size()) {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        mEmptyView.setVisibility(View.GONE);
                        mSendBtn.setEnabled(true);
                        mHasMore = true;
                        actionAdapter.notifyDataSetChanged();

                    } else {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        mEmptyView.setVisibility(View.GONE);
                        mSendBtn.setEnabled(true);
                        mHasMore = false;
                        actionAdapter.notifyDataSetChanged();
                    }


                } else {
                    Toast.makeText(WalletDetailActivity.this, R.string.str_error_network_less, Toast.LENGTH_SHORT).show();

                }
                mLoading = false;
                if(swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onReqTransaction onFailure : " + t.getMessage());
                Toast.makeText(WalletDetailActivity.this, R.string.str_error_network_less, Toast.LENGTH_SHORT).show();
                mLoading = false;
                if(swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
