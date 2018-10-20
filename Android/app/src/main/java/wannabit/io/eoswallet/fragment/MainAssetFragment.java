package wannabit.io.eoswallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.BandWidthTradeActivity;
import wannabit.io.eoswallet.activity.CurrencyManageActivity;
import wannabit.io.eoswallet.activity.MainActivity;
import wannabit.io.eoswallet.activity.RamTradeActivity;
import wannabit.io.eoswallet.activity.SendActivity;
import wannabit.io.eoswallet.activity.WalletDetailActivity;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.dialog.DialogRequestKey;
import wannabit.io.eoswallet.network.ResAccountInfo;
import wannabit.io.eoswallet.network.ResEosTick;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class MainAssetFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ConstraintLayout rootLayer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView totalEos, totalCash, unstakeInfo, stakeInfo, refundInfo;
    private ProgressBar ramProgress, cpuProgress, netProgress;
    private TextView ramInfo, cpuInfo, netInfo, eosPrice, cpuAmount, netAmount;
    private CardView    mCardRam, mCardCpu, mCardNet;

    private ResAccountInfo  mResAccountInfo;
    private ResEosTick      mResEosTick;


    public static MainAssetFragment newInstance() {
        MainAssetFragment fragment = new MainAssetFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_asset, container, false);
        rootLayer = rootView.findViewById(R.id.rootLayer);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        totalEos = rootView.findViewById(R.id.asset_total);
        totalCash = rootView.findViewById(R.id.asset_total_cash);
        unstakeInfo = rootView.findViewById(R.id.unstakeInfo);
        stakeInfo = rootView.findViewById(R.id.stakeInfo);
        refundInfo = rootView.findViewById(R.id.refundInfo);
        ramProgress = rootView.findViewById(R.id.ramProgress);
        cpuProgress = rootView.findViewById(R.id.cpuProgress);
        netProgress = rootView.findViewById(R.id.netProgress);
        ramInfo = rootView.findViewById(R.id.ramInfo);
        cpuInfo = rootView.findViewById(R.id.cpuInfo);
        cpuAmount = rootView.findViewById(R.id.cpuAmount);
        netInfo = rootView.findViewById(R.id.netInfo);
        netAmount = rootView.findViewById(R.id.netAmount);
        eosPrice = rootView.findViewById(R.id.eosPrice);
        mCardRam = rootView.findViewById(R.id.cardRam);
        mCardCpu = rootView.findViewById(R.id.cardCpu);
        mCardNet = rootView.findViewById(R.id.cardNet);

        mCardRam.setOnClickListener(this);
        mCardCpu.setOnClickListener(this);
        mCardNet.setOnClickListener(this);


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        totalEos.setText(WUtil.EOSAmoutSpanFormat(0d, 0.8f));
        unstakeInfo.setText(WUtil.EOSAmoutSpanFormat(0d, 0.7f));
        stakeInfo.setText(WUtil.EOSAmoutSpanFormat(0d, 0.7f));
        refundInfo.setText(WUtil.EOSAmoutSpanFormat(0d, 0.7f));

        return rootView;
    }


    private void onUpdateView() {
        if(mResAccountInfo != null) {
            totalEos.setText(WUtil.EOSAmoutSpanFormat(mResAccountInfo.getTotalAmout(), 0.8f));
            unstakeInfo.setText(WUtil.EOSAmoutSpanFormat(mResAccountInfo.getTotalUnstakedAmout(), 0.7f));
            stakeInfo.setText(WUtil.EOSAmoutSpanFormat(mResAccountInfo.getTotalStakedAmout(), 0.7f));
            refundInfo.setText(WUtil.EOSAmoutSpanFormat(mResAccountInfo.getTotalRefundAmout(), 0.7f));

            ramProgress.setProgress(mResAccountInfo.getRamProgress());
            ramInfo.setText(mResAccountInfo.getRamInfo());

            cpuProgress.setProgress(mResAccountInfo.getCpuProgress());
            cpuInfo.setText(mResAccountInfo.getCpuInfo());
            cpuAmount.setText(WUtil.EOSAmoutSpanFormatBraket(mResAccountInfo.getCpuAmount(), 0.7f));

            netProgress.setProgress(mResAccountInfo.getNetProgress());
            netInfo.setText(mResAccountInfo.getNetInfo());
            netAmount.setText(WUtil.EOSAmoutSpanFormatBraket(mResAccountInfo.getNetAmount(), 0.7f));


        }

        if(mResEosTick != null) {
            eosPrice.setText(getString(R.string.str_one_eos) + WUtil.getDisplayPriceStr(getBaseActivity(), mResEosTick, getBaseDao().getUserCurrencyStr(getBaseActivity())));
        }

        if(mResEosTick != null && mResAccountInfo != null) {
            totalCash.setText(WUtil.getDisplayPriceSumStr(getBaseActivity(), mResEosTick, getBaseDao().getUserCurrencyStr(getBaseActivity()), mResAccountInfo.getTotalAmout()));
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        onRefreshByMainTab(true);
    }

    @Override
    public void onRefresh() {
        if(getMainActivity() != null) {
            getMainActivity().onRequestEosTick();
        }
    }

    @Override
    public void onRefreshByMainTab(boolean deep) {
        if(isAdded()) {
            willBeDisplayed();
            mResAccountInfo = getMainActivity().getAccountInfo();
            mResEosTick = getMainActivity().getEosTick();
            onUpdateView();

            if(swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
        }
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

    private MainActivity getMainActivity() {
        return (MainActivity)getBaseActivity();
    }


    @Override
    public void onClick(View v) {
        if(v.equals(mCardRam)) {
            WLog.w("RamCard Clicked");
            if(getBaseDao().hasPrivateKey(getMainActivity().getCurrentUser().getAccount())) {
                startActivity(new Intent(getBaseActivity(), RamTradeActivity.class));

            } else {
                Bundle bundle = new Bundle();
                bundle.putString("account", getMainActivity().getCurrentUser().getAccount());
                DialogRequestKey dialog  = DialogRequestKey.newInstance(bundle);
                dialog.setCancelable(true);
                dialog.show(getChildFragmentManager(), "dialog");
            }

        } else if(v.equals(mCardCpu) || v.equals(mCardNet)) {
            WLog.w("Bandwidth Clicked");
            if(getBaseDao().hasPrivateKey(getMainActivity().getCurrentUser().getAccount())) {
                startActivity(new Intent(getBaseActivity(), BandWidthTradeActivity.class));

            } else {
                Bundle bundle = new Bundle();
                bundle.putString("account", getMainActivity().getCurrentUser().getAccount());
                DialogRequestKey dialog  = DialogRequestKey.newInstance(bundle);
                dialog.setCancelable(true);
                dialog.show(getChildFragmentManager(), "dialog");
            }
        }

    }
}
