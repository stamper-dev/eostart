package wannabit.io.eoswallet.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.RamTradeActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class DialogTradeRamReceipt extends DialogFragment {

    private Button singleOK;
    private Boolean mWithEos;
    private BigDecimal mFinalChangeRam, mFinalResultRam, mFinalChangeEos, mFinalResultEos;


    public static DialogTradeRamReceipt newInstance(Bundle bundle) {
        DialogTradeRamReceipt frag = new DialogTradeRamReceipt();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView        = View.inflate(getContext(), R.layout.dialog_traderam_receipt, null);

        TextView    mTitle          = contentView.findViewById(R.id.trade_title);

        TextView    mBeforeBalance  = contentView.findViewById(R.id.beforeBalance);
        TextView    mChangeBalance  = contentView.findViewById(R.id.changeBalance);
        TextView    mResultBalance  = contentView.findViewById(R.id.resultBalance);

        TextView    mBeforeRam      = contentView.findViewById(R.id.beforeRam);
        TextView    mChangeRam      = contentView.findViewById(R.id.changeRam);
        TextView    mResultRam      = contentView.findViewById(R.id.resultRam);

        mWithEos = getArguments().getBoolean("withEOS");
        mFinalChangeRam = new BigDecimal(getArguments().getString("changeRam"));
        mFinalResultRam = new BigDecimal(getArguments().getString("resultRam"));
        mFinalChangeEos = new BigDecimal(getArguments().getString("changeEOS"));
        mFinalResultEos = new BigDecimal(getArguments().getString("resultEOS"));


        if(mFinalChangeRam.compareTo(BigDecimal.ZERO) > 0) {
            //Buying Ram
            mTitle.setText(R.string.str_dialog_title_buy_ram);
            mChangeBalance.setTextColor(getResources().getColor(R.color.colorRed));
            mChangeRam.setTextColor(getResources().getColor(R.color.colorGreen));

        } else {
            //Sell Ram
            mTitle.setText(R.string.str_dialog_title_sell_ram);
            mChangeBalance.setTextColor(getResources().getColor(R.color.colorGreen));
            mChangeRam.setTextColor(getResources().getColor(R.color.colorRed));
        }

        mBeforeBalance.setText(WUtil.unSignAmoutSpanFormat(mFinalResultEos.subtract(mFinalChangeEos).doubleValue(), 0.8f));
        mChangeBalance.setText(WUtil.signAmoutSpanFormat(mFinalChangeEos.doubleValue(), 0.8f));
        mResultBalance.setText(WUtil.unSignAmoutSpanFormat(mFinalResultEos.doubleValue(), 0.8f));

        mBeforeRam.setText(WUtil.FormatKByte(mFinalResultRam.subtract(mFinalChangeRam).doubleValue()));
        mChangeRam.setText(WUtil.signSpanFormatKByte(mFinalChangeRam.doubleValue()));
        mResultRam.setText(WUtil.FormatKByte(mFinalResultRam.doubleValue()));
        singleOK = contentView.findViewById(R.id.singleOK);

        singleOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFinalChangeRam.compareTo(BigDecimal.ZERO) > 0) {
                    if(mWithEos) {
                        getPActivity().onStartRamTrade(BaseConstant.CONST_BUY_RAM, mFinalChangeEos.abs().toString());
                    } else {
                        getPActivity().onStartRamTrade(BaseConstant.CONST_BUY_RAM_BYTE, mFinalChangeRam.abs().toBigInteger().toString());

                    }

                } else {
                    getPActivity().onStartRamTrade(BaseConstant.CONST_SELL_RAM, mFinalChangeRam.abs().toBigInteger().toString());
                }
                getDialog().dismiss();

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView);
        return builder.create();
    }

    private RamTradeActivity getPActivity() {
        return (RamTradeActivity)getActivity();
    }
}
