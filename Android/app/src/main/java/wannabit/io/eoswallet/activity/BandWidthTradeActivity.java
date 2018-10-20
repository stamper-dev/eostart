package wannabit.io.eoswallet.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import java.util.ArrayList;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.fragment.DelegateFragment;
import wannabit.io.eoswallet.fragment.MainAssetFragment;
import wannabit.io.eoswallet.fragment.MainHistoryFragment;
import wannabit.io.eoswallet.fragment.MainSettingsFragment;
import wannabit.io.eoswallet.fragment.MainWalletFragment;
import wannabit.io.eoswallet.fragment.UndelegateFragment;

public class BandWidthTradeActivity extends BaseActivity {

    private Toolbar                     mToolbar;
    private TabLayout                   mTabLayout;
    private AHBottomNavigationViewPager mViewPager;


    private BandWidthViewPagerAdapter   mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandwidthtrade);

        mToolbar            = findViewById(R.id.toolbar);
        mTabLayout          = findViewById(R.id.tabLayout);
        mViewPager          = findViewById(R.id.viewPager);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mAdapter = new BandWidthViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabRippleColor(null);

        mTabLayout.getTabAt(0).setText("Stake");
        mTabLayout.getTabAt(1).setText("Unstake");
        mViewPager.setCurrentItem(0, false);
    }



    public class BandWidthViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public BandWidthViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(DelegateFragment.newInstance());
            mFragments.add(UndelegateFragment.newInstance());
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
}
