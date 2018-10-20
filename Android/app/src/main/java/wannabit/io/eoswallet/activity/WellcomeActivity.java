package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.dialog.DialogIntentAddKey;
import wannabit.io.eoswallet.dialog.DialogNewAccount;
import wannabit.io.eoswallet.fragment.StartFragment;
import wannabit.io.eoswallet.fragment.WellcomeFragment;
import wannabit.io.eoswallet.utils.WLog;


/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */

public class WellcomeActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager               pager;
    private RelativeLayout          controllayer;
    private CircleIndicator         indicator;
    private Button                  btnBefore, btnNext;


    private WellcomePagerAdapter    pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        pager           = findViewById(R.id.wellcome_pager);
        controllayer    = findViewById(R.id.wellcome_control);
        indicator       = findViewById(R.id.wellcome_indicator);
        btnBefore       = findViewById(R.id.wellcome_before);
        btnNext         = findViewById(R.id.wellcome_next);
        btnBefore.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        pager.setOffscreenPageLimit(4);
        pagerAdapter = new WellcomePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        indicator.setViewPager(pager);
        pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0) {
                    controllayer.setVisibility(View.VISIBLE);
                    btnBefore.setVisibility(View.VISIBLE);
                    btnNext.setText(getString(R.string.str_next));

                } else if(i == 1) {
                    controllayer.setVisibility(View.VISIBLE);
                    btnBefore.setVisibility(View.VISIBLE);
                    btnNext.setText(getString(R.string.str_next));

                } else if(i == 2) {
                    controllayer.setVisibility(View.VISIBLE);
                    btnBefore.setVisibility(View.INVISIBLE);
                    btnNext.setText(getString(R.string.str_start));

                } else {
                    controllayer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        pager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnBefore)) {
            onStartPage();
        } else if (view.equals(btnNext)) {
            if (pager.getCurrentItem() == 2) {
                onStartPage();
            } else {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        }

    }
    
    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() > 0) {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
            return;
        }
        super.onBackPressed();
    }

    private void onStartPage() {
        pager.setCurrentItem(3);
    }

    public void onStartNewAccount() {
        DialogNewAccount dialog  = DialogNewAccount.newInstance(null);
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void onStartWebPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eostart.com/"));
        startActivity(intent);
    }

    public void onStartImportAccount() {
        startActivity(new Intent(WellcomeActivity.this, AddAccountActivity.class));
    }



    public class WellcomePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public WellcomePagerAdapter(FragmentManager fm) {
            super(fm);

            mFragments.clear();

            Bundle bundle0 = new Bundle();
            bundle0.putInt("page", 0);
            mFragments.add(WellcomeFragment.newInstance(bundle0));

            Bundle bundle1 = new Bundle();
            bundle1.putInt("page", 1);
            mFragments.add(WellcomeFragment.newInstance(bundle1));

            Bundle bundle2 = new Bundle();
            bundle2.putInt("page", 2);
            mFragments.add(WellcomeFragment.newInstance(bundle2));

            Bundle bundle4 = new Bundle();
            bundle4.putInt("page", 3);
            mFragments.add(StartFragment.newInstance());
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
    }
}
