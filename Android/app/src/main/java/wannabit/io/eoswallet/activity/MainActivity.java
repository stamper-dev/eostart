package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.databinding.ActivityMainBinding;
import wannabit.io.eoswallet.fragment.MainAssetFragment;
import wannabit.io.eoswallet.fragment.MainHistoryFragment;
import wannabit.io.eoswallet.fragment.MainSettingsFragment;
import wannabit.io.eoswallet.fragment.MainWalletFragment;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.ResAccountInfo;
import wannabit.io.eoswallet.network.ResCoinGecko;
import wannabit.io.eoswallet.network.ResEosTick;
import wannabit.io.eoswallet.utils.FadePageTransformer;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.view.TintableImageView;

/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private MainViewPagerAdapter            mViewPagerAdapter;
    private PopupWindow                     mAccoutsWindow;

    private WBUser                          mSelectedUser;
    private ArrayList<WBUser>               mRegistedUsers;

    private ResAccountInfo                  mResAccountInfo;
    private ResEosTick                      mResEosTick;
    private ResCoinGecko                    mResCoinGecko;

    private ActivityMainBinding             mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBinding.toolbarLayer.setOnClickListener(this);
        mBinding.viewPager.setOffscreenPageLimit(3);
        mViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mBinding.viewPager.setPageTransformer(false, new FadePageTransformer());
        mBinding.viewPager.setAdapter(mViewPagerAdapter);
        mBinding.bottomTab.setupWithViewPager(mBinding.viewPager);
        mBinding.bottomTab.setTabRippleColor(null);

        View tab0 = LayoutInflater.from(this).inflate(R.layout.view_tab_item, null);
        TintableImageView   tabItemIcon0  = tab0.findViewById(R.id.tabItemIcon);
        TextView            tabItemText0  = tab0.findViewById(R.id.tabItemText);
        tabItemIcon0.setImageResource(R.drawable.ic_person);
        tabItemText0.setText(R.string.str_menu_account);
        mBinding.bottomTab.getTabAt(0).setCustomView(tab0);

        View tab1 = LayoutInflater.from(this).inflate(R.layout.view_tab_item, null);
        TintableImageView   tabItemIcon1  = tab1.findViewById(R.id.tabItemIcon);
        TextView            tabItemText1  = tab1.findViewById(R.id.tabItemText);
        tabItemIcon1.setImageResource(R.drawable.ic_wallet);
        tabItemText1.setText(R.string.str_menu_wallet);
        mBinding.bottomTab.getTabAt(1).setCustomView(tab1);

        View tab2 = LayoutInflater.from(this).inflate(R.layout.view_tab_item, null);
        TintableImageView   tabItemIcon2  = tab2.findViewById(R.id.tabItemIcon);
        TextView            tabItemText2  = tab2.findViewById(R.id.tabItemText);
        tabItemIcon2.setImageResource(R.drawable.ic_transaction);
        tabItemText2.setText(R.string.str_menu_history);
        mBinding.bottomTab.getTabAt(2).setCustomView(tab2);

        View tab3 = LayoutInflater.from(this).inflate(R.layout.view_tab_item, null);
        TintableImageView   tabItemIcon3  = tab3.findViewById(R.id.tabItemIcon);
        TextView            tabItemText3  = tab3.findViewById(R.id.tabItemText);
        tabItemIcon3.setImageResource(R.drawable.ic_setting);
        tabItemText3.setText(R.string.str_menu_settings);
        mBinding.bottomTab.getTabAt(3).setCustomView(tab3);


        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 3) {
                    mBinding.toolbarLayer.setVisibility(View.GONE);
                    mBinding.toolbarSetting.setVisibility(View.VISIBLE);
                } else {
                    mBinding.toolbarLayer.setVisibility(View.VISIBLE);
                    mBinding.toolbarSetting.setVisibility(View.GONE);
                }
                if(mViewPagerAdapter != null && mViewPagerAdapter.getCurrentFragment() != null)
                    mViewPagerAdapter.getCurrentFragment().onRefreshByMainTab(false);
            }

            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });


        mBinding.viewPager.setCurrentItem(0, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Long lastUserId = getBaseDao().getRecentAccountId();
        mRegistedUsers  = getBaseDao().onSelectAllUser();
        if(mRegistedUsers == null || mRegistedUsers.size() < 1) {
            WLog.r("TEST NO Account Error ");
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
        onSwitchAccount();
    }

    public void onSwitchAccount() {
        mBinding.toolbarTitle.setText(mSelectedUser.getAccount());
//        if(getBaseDao().getLastEosTicTime() < (System.currentTimeMillis() - 30000)
//                || getBaseDao().getLastEosTic() == null
//                || getBaseDao().getLastUserInfo() == null) {
//            onShowWaitDialog();
//            onRequestEosTick();
//        } else {
//            mResAccountInfo = getBaseDao().getLastUserInfo();
//            mResEosTick = getBaseDao().getLastEosTic();
//            if(mViewPagerAdapter != null && mViewPagerAdapter.getCurrentFragment() != null){
//                mViewPagerAdapter.getCurrentFragment().onRefreshByMainTab(true);
//            }
//        }
        onRequestUserInfo(mSelectedUser.getAccount());
        onRequestEosTick();
    }

    @Override
    public void onBackPressed() {
        if(mBinding.viewPager.getCurrentItem() != 0) {
            mBinding.viewPager.setCurrentItem(0, false);
        } else {
            moveTaskToBack (true);
//            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View view) {
        if(view.equals(mBinding.toolbarLayer)) {
            onShowAccountsPopup();
        }
    }


    private void onShowAccountsPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
        mAccoutsWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mAccoutsWindow.setBackgroundDrawable(getDrawable(R.drawable.white_box));
        ListView listView = new ListView(this);
        if(mRegistedUsers.size() < BaseConstant.CONSTANT_MAX_ACCOUNT) {
            View footer = getLayoutInflater().inflate(R.layout.item_accounts_add, null, false);
            LinearLayout btn = footer.findViewById(R.id.account_new);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAccoutsWindow.dismiss();
                    startActivity(new Intent(MainActivity.this, AddAccountActivity.class));
                }
            });
            listView.addFooterView(footer);
        }
        listView.setDividerHeight(0);
        listView.setAdapter(new PopupAdapter());

        mAccoutsWindow.setFocusable(true);
        mAccoutsWindow.setContentView(listView);
        mAccoutsWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clearDim(mBinding.contentLayer);
            }
        });
        applyDim(mBinding.contentLayer, 0.7f);
        mAccoutsWindow.showAsDropDown(mBinding.toolbar);

    }


    public class MainViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(MainAssetFragment.newInstance());
            mFragments.add(MainWalletFragment.newInstance());
            mFragments.add(MainHistoryFragment.newInstance());
            mFragments.add(MainSettingsFragment.newInstance());
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((BaseFragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        public BaseFragment getCurrentFragment() {
            return mCurrentFragment;
        }

        public ArrayList<BaseFragment> getFragments() {
            return mFragments;
        }
    }


    public WBUser getCurrentUser() {
        return  mSelectedUser;
    }

    public ResAccountInfo getAccountInfo() {
        return mResAccountInfo;
    }

    public ResEosTick getEosTick() {
        return mResEosTick;
    }


    public ResCoinGecko getCoinGeckoTic() {
        return mResCoinGecko;
    }

    public void onRequestUserInfo(String userId) {
        ApiClient.getAccount(this, userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                WLog.w("onRequestUserInfo OK");
                if (response.isSuccessful()) {
                    mResAccountInfo = new Gson().fromJson(response.body(), ResAccountInfo.class);
                    getBaseDao().setLastUser(mResAccountInfo);
                    if(mViewPagerAdapter != null && mViewPagerAdapter.getCurrentFragment() != null)
                        mViewPagerAdapter.getCurrentFragment().onRefreshByMainTab(true);
                } else {
                    Toast.makeText(MainActivity.this, R.string.str_error_network_less, Toast.LENGTH_SHORT).show();
                }
                onHideWaitDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onRequestUserInfo onFailure : " + t.getMessage());
                Toast.makeText(MainActivity.this, R.string.str_error_network_less, Toast.LENGTH_SHORT).show();
                onHideWaitDialog();
            }
        });
    }

    public void onRequestEosTick() {
//        ApiClient.getEosTic(this, BaseConstant.COINMARKETCAP_EOS, getBaseDao().getUserCurrencyStr(getBaseContext())).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                WLog.w("onRequestEosTick OK");
//                if(response.isSuccessful()) {
//                    mResEosTick = new Gson().fromJson(response.body(), ResEosTick.class);
//                    getBaseDao().setLastEosTic(mResEosTick);
//                    if(mViewPagerAdapter != null && mViewPagerAdapter.getCurrentFragment() != null)
//                        mViewPagerAdapter.getCurrentFragment().onRefreshByMainTab(false);
//                }  else {
//                    Toast.makeText(MainActivity.this, R.string.str_error_network_less, Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                WLog.w("onRequestEosTick onFailure : " + t.getMessage());
//                Toast.makeText(MainActivity.this, R.string.str_error_network_less, Toast.LENGTH_SHORT).show();
//            }
//        });
        ApiClient.getPriceTic(this).enqueue(new Callback<ResCoinGecko>() {
            @Override
            public void onResponse(Call<ResCoinGecko> call, Response<ResCoinGecko> response) {
                if (response.isSuccessful()) {
                    mResCoinGecko = response.body();
                    getBaseDao().setLastPriceTic(mResCoinGecko);
                    if(mViewPagerAdapter != null && mViewPagerAdapter.getCurrentFragment() != null)
                        mViewPagerAdapter.getCurrentFragment().onRefreshByMainTab(false);
                } else {
                    Toast.makeText(MainActivity.this, R.string.str_error_network_less, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResCoinGecko> call, Throwable t) {
                WLog.w("onRequestEosTick onFailure : " + t.getMessage());
                Toast.makeText(MainActivity.this, R.string.str_error_network_less, Toast.LENGTH_SHORT).show();

            }
        });
    }

    class PopupAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mRegistedUsers.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View  row = inflater.inflate(R.layout.item_accounts_account, parent, false);
            LinearLayout root = row.findViewById(R.id.itemRoot);
            ImageView accountKey = row.findViewById(R.id.accountKey);
            TextView accountId = row.findViewById(R.id.accountId);
            if(getBaseDao().hasPrivateKey(mRegistedUsers.get(position).getAccount())) {
                accountKey.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_key));
            } else {
                accountKey.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_key_off));
            }
            accountId.setText(mRegistedUsers.get(position).getAccount());
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mAccoutsWindow != null) {
                        mAccoutsWindow.dismiss();
                        mSelectedUser = mRegistedUsers.get(position);
                        getBaseDao().setRecentAccountId(mSelectedUser.getId());
                        onSwitchAccount();
                    }

                }
            });
            return row;
        }
    }

}
