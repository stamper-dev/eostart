package wannabit.io.eoswallet.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.fragment.MainWalletFragment;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.utils.WLog;

public class WalletListEditActivity extends BaseActivity {

    private Toolbar                 mToolbar;
    private TextView                mToolbarTitle;


    private SwipeRefreshLayout      swipeRefreshLayout;
    private RecyclerView            recyclerView;
    private LinearLayoutManager     linearLayoutManager;
    private WalletEditAdapter       walletEditAdapter;

    private WBUser                  mSelectedUser;
    private ArrayList<WBUser>       mRegistedUsers;
    private ArrayList<WBToken>      mSupportTokens = new ArrayList<>();
    private ArrayList<String>       mUserHideTokens;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_list_edit);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        swipeRefreshLayout  = findViewById(R.id.swipeRefreshLayout);
        recyclerView        = findViewById(R.id.recyclerView);

        mToolbarTitle.setText(R.string.str_title_edit_token);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onInitData();
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

        onInitData();
    }


    private void onInitData() {
        Long lastUserId = getBaseDao().getRecentAccountId();
        mRegistedUsers  = getBaseDao().onSelectAllUser();
        mSupportTokens  = getBaseDao().onSelectAllTokens();
        if(mRegistedUsers == null || mRegistedUsers.size() < 1) {
            WLog.r("WalletListEditActivity NO Account Error!!");
            finish();
        } else if (lastUserId < 0) {
            mSelectedUser = mRegistedUsers.get(0);

        } else {
            mSelectedUser = mRegistedUsers.get(0);
            for(WBUser user:mRegistedUsers) {
                if(user.getId() == lastUserId) {
                    mSelectedUser = user;
                    break;
                }
            }
        }
        mUserHideTokens = getBaseDao().getUserHideTokens(mSelectedUser.getId());
        walletEditAdapter = new WalletEditAdapter(mSupportTokens);
        recyclerView.setAdapter(walletEditAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }



    class WalletEditAdapter extends RecyclerView.Adapter<WalletEditAdapter.WalletEditAdapterItemHolder>{

        ArrayList<WBToken> tokens = new ArrayList<>();

        public WalletEditAdapter(ArrayList<WBToken> tokens) {
            this.tokens = tokens;
        }

        @Override
        public int getItemCount() {
            return tokens.size();
        }

        @Override
        public WalletEditAdapterItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.item_wallet_token_edit, viewGroup, false);
            return new WalletEditAdapterItemHolder(v);
        }

        @Override
        public void onBindViewHolder(final WalletEditAdapterItemHolder holder, int position) {
            final WBToken wbToken = tokens.get(position);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(WalletListEditActivity.this)
                            .load(wbToken.getIconUrl())
                            .centerCrop()
                            .override(78, 78)
                            .placeholder(R.drawable.dialog_bg)
                            .into(holder.itemImageToken);
                }
            });
            if(wbToken.getContractAddr().equals(getString(R.string.str_eos_contract))) {
                holder.itemSwitchToken.setVisibility(View.INVISIBLE);

            } else {
                holder.itemSwitchToken.setVisibility(View.VISIBLE);
                if(mUserHideTokens.contains(wbToken.getContractAddr())) {
                    holder.itemSwitchToken.setChecked(false);
                } else {
                    holder.itemSwitchToken.setChecked(true);
                }
                holder.itemSwitchToken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(compoundButton.isPressed()) {
                            if(isChecked)
                                mUserHideTokens.remove(wbToken.getContractAddr());
                            else
                                mUserHideTokens.add(wbToken.getContractAddr());
                        }
                        getBaseDao().setUserHideTokens(mSelectedUser.getId(), mUserHideTokens);
                    }
                });

            }
            holder.itemDescribeToken.setText(wbToken.getName());
            holder.itemNameToken.setText(wbToken.getSymbol());
        }



        public class WalletEditAdapterItemHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemClickView;
            ImageView itemImageToken;
            TextView itemNameToken, itemDescribeToken;
            SwitchCompat itemSwitchToken;

            public WalletEditAdapterItemHolder(View v) {
                super(v);
                itemClickView = itemView.findViewById(R.id.clickView);
                itemImageToken = itemView.findViewById(R.id.imageToken);
                itemNameToken = itemView.findViewById(R.id.nameToken);
                itemDescribeToken = itemView.findViewById(R.id.describeToken);
                itemSwitchToken = itemView.findViewById(R.id.switchToken);

            }
        }
    }
}
