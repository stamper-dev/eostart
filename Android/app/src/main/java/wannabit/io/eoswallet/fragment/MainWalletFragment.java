package wannabit.io.eoswallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.MainActivity;
import wannabit.io.eoswallet.activity.WalletDetailActivity;
import wannabit.io.eoswallet.activity.WalletListEditActivity;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class MainWalletFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private ConstraintLayout        rootLayer;
    private SwipeRefreshLayout      swipeRefreshLayout;
    private RecyclerView            recyclerView;
    private FloatingActionButton    faBtn;
    private LinearLayoutManager     linearLayoutManager;


    private WalletAdapter           walletAdapter;
    private ArrayList<WBToken>      mTokens = new ArrayList<>();
    private String                  mCurrentAccount;

    public static MainWalletFragment newInstance() {
        MainWalletFragment fragment = new MainWalletFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_wallet, container, false);
        rootLayer           = rootView.findViewById(R.id.rootLayer);
        swipeRefreshLayout  = rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView        = rootView.findViewById(R.id.recyclerView);
        faBtn               = rootView.findViewById(R.id.fb);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);
        faBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseActivity(), WalletListEditActivity.class));
            }
        });

        linearLayoutManager = new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        walletAdapter = new WalletAdapter();
        recyclerView.setAdapter(walletAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >0) {
                    if (faBtn.isShown()) {
                        faBtn.hide();
                    }
                }
                else if (dy <0) {
                    if (!faBtn.isShown()) {
                        faBtn.show();
                    }
                }
            }
        });

        return rootView;
    }


    @Override
    public void onRefreshByMainTab(boolean deep) {
        WLog.w("onRefreshByMainTab Wallet : " + deep);
        if(isAdded() && getMainActivity() != null && getMainActivity().getCurrentUser() != null) {
            willBeDisplayed();
            if(getMainActivity().getCurrentUser().getAccount().equals(mCurrentAccount)) {
                if(deep && linearLayoutManager != null) {
                    linearLayoutManager.scrollToPositionWithOffset(0, 0);
                } else if (!deep && mTokens.size() > 0){
                    if(swipeRefreshLayout != null)
                        swipeRefreshLayout.setRefreshing(false);
                    return;
                }
            } else {
                mCurrentAccount = getMainActivity().getCurrentUser().getAccount();
            }
            onInitWalletList();
        } else {
            if(swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
        }
    }


    private void onInitWalletList() {
        WLog.w("onInitWalletList");
        ArrayList<WBToken> temp = getBaseDao().onSelectAllTokens();
        ArrayList<String> toHide = getBaseDao().getUserHideTokens(getMainActivity().getCurrentUser().getId());
        ArrayList<WBToken> result = new ArrayList<>();
        result.clear();
        result.addAll(temp);
        if(toHide != null && toHide.size() > 0) {
            for(WBToken token:temp) {
                for(String s:toHide) {
                    if(token.getContractAddr().equals(s)) {
                        result.remove(token);
                    }
                }
            }
        }
        mTokens.clear();
        mTokens.addAll(result);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                walletAdapter.notifyDataSetChanged();
            }
        });
        if(swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void willBeDisplayed() {
        if (rootLayer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            rootLayer.startAnimation(fadeIn);
        }
    }

    @Override
    public void willBeHidden() {
        if (rootLayer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            rootLayer.startAnimation(fadeOut);
        }
    }

    @Override
    public void onRefresh() {
        getMainActivity().onRequestEosTick();
    }

    class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletAdapterItemHolder> {

        @Override
        public int getItemCount() {
            return mTokens.size();
        }

        @Override
        public WalletAdapterItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_wallet_token, viewGroup, false);
            return new WalletAdapterItemHolder(v);

        }

        @Override
        public void onBindViewHolder(final WalletAdapterItemHolder holder, int position) {
            final WBToken wbToken = mTokens.get(position);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getBaseActivity())
                            .load(wbToken.getIconUrl())
                            .fitCenter()
                            .override(108, 108)
                            .into(holder.itemImageToken);
                }
            });
            holder.itemDescribeToken.setText(wbToken.getName());
            holder.itemNameToken.setText(wbToken.getSymbol());
            holder.itemClickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseActivity(), WalletDetailActivity.class);
                    intent.putExtra("token", wbToken);
                    startActivity(intent);
                }
            });

            if(wbToken.getUserAmount() > -1d) {
                holder.itemAmountToken.setText(WUtil.AmountSpanFormatWithoutSymbol(getBaseActivity(), wbToken.getUserAmount(), wbToken));
                if(wbToken.getContractAddr().equals(getString(R.string.str_eos_contract))) {
//                    holder.itemPriceToken.setText(WUtil.getDisplayPriceSumStr(getBaseActivity(), getBaseDao().getLastEosTic(), getBaseDao().getUserCurrencyStr(getBaseActivity()), wbToken.getUserAmount()));
                    holder.itemPriceToken.setText(WUtil.getDisplayPriceSumStr(getBaseActivity(), getBaseDao().getLastPriceTic(), getBaseDao().getUserCurrencyStr(getBaseActivity()), wbToken.getUserAmount()));

                } else {
//                    holder.itemPriceToken.setText(WUtil.getDisplayPriceSumStr(getBaseActivity(), getBaseDao().getLastEosTic(), getBaseDao().getUserCurrencyStr(getBaseActivity()), 0d));
                    holder.itemPriceToken.setText(WUtil.getDisplayPriceSumStr(getBaseActivity(), getBaseDao().getLastPriceTic(), getBaseDao().getUserCurrencyStr(getBaseActivity()), 0d));

                }
                holder.itemClickView.setClickable(true);
            } else {
                onSetAmount(holder, position, wbToken);
            }


        }

        public class WalletAdapterItemHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemClickView;
            ImageView itemImageToken;
            TextView itemNameToken, itemAmountToken, itemDescribeToken, itemPriceToken;

            public WalletAdapterItemHolder(View v) {
                super(v);
                itemClickView = itemView.findViewById(R.id.clickView);
                itemImageToken = itemView.findViewById(R.id.imageToken);
                itemNameToken = itemView.findViewById(R.id.nameToken);
                itemAmountToken = itemView.findViewById(R.id.amountToken);
                itemDescribeToken = itemView.findViewById(R.id.describeToken);
                itemPriceToken = itemView.findViewById(R.id.priceToken);
            }
        }
    }

    private void onSetAmount(final WalletAdapter.WalletAdapterItemHolder holder, final int position, final WBToken wbToken) {
        ApiClient.getCurrency(getBaseActivity(), wbToken.getContractAddr(), getMainActivity().getCurrentUser().getAccount(), wbToken.getSymbol()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful() && getMainActivity() != null && !getMainActivity().isFinishing()) {
                    if(response.body().size() > 0) {
                        mTokens.get(position).setUserAmount(Double.parseDouble(response.body().get(0).toLowerCase().replace(wbToken.getSymbol().toLowerCase(), "")));
                    } else {
                        mTokens.get(position).setUserAmount(0d);
                    }

                    holder.itemAmountToken.setText(WUtil.AmountSpanFormatWithoutSymbol(getBaseActivity(), mTokens.get(position).getUserAmount(), wbToken));
                    if(wbToken.getContractAddr().equals(getString(R.string.str_eos_contract))) {
                        holder.itemPriceToken.setText(WUtil.getDisplayPriceSumStr(getBaseActivity(), getBaseDao().getLastPriceTic(), getBaseDao().getUserCurrencyStr(getBaseActivity()), wbToken.getUserAmount()));
                    } else {
                        holder.itemPriceToken.setText(WUtil.getDisplayPriceSumStr(getBaseActivity(), getBaseDao().getLastPriceTic(), getBaseDao().getUserCurrencyStr(getBaseActivity()), 0d));
                    }
                    holder.itemClickView.setClickable(true);



                } else {
                    //Ignore this Error
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                WLog.w("ReqCurrencyBalance Fail " + t.getMessage());
                //Ignore this Error
            }
        });

    }

    private MainActivity getMainActivity() {
        return (MainActivity)getBaseActivity();
    }
}
