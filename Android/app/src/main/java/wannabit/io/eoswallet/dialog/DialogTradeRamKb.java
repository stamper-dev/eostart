package wannabit.io.eoswallet.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.RamTradeActivity;
import wannabit.io.eoswallet.utils.WUtil;

public class DialogTradeRamKb extends DialogFragment {

    private EditText    mBuyRam, mSellRam;
    private TextView    mBuyMax, mSellMax;
    private Button      singleOK;
    private double      mBuyMaxValue, mBuyMinValue;

    public static DialogTradeRamKb newInstance(Bundle bundle) {
        DialogTradeRamKb frag = new DialogTradeRamKb();
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
        View contentView    = View.inflate(getContext(), R.layout.dialog_traderam_kb, null);
        singleOK = contentView.findViewById(R.id.singleOK);
        mBuyRam = contentView.findViewById(R.id.buyram_kb);
        mSellRam = contentView.findViewById(R.id.sellram_kb);
        mBuyMax = contentView.findViewById(R.id.buyram_eos_range);
        mSellMax = contentView.findViewById(R.id.buyram_kb_range);
        singleOK = contentView.findViewById(R.id.singleOK);


        mBuyMaxValue = getArguments().getDouble("buyMaxRam");
        mBuyMinValue = getArguments().getDouble("sellMaxRam");

        mBuyMax.setText("~ " + WUtil.FormatKByte(getArguments().getDouble("buyMaxRam")));
        mSellMax.setText("~ " + WUtil.FormatKByte(getArguments().getDouble("sellMaxRam")) );

        mBuyRam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mSellRam.setText("");
                }
            }
        });

        mSellRam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mBuyRam.setText("");
                }

            }
        });

        singleOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mBuyRam.getText())) {
                    getPActivity().setValueByRam(mBuyRam.getText().toString().trim(), null);


                } else if (!TextUtils.isEmpty(mSellRam.getText())) {
                    getPActivity().setValueByRam(null, mSellRam.getText().toString().trim());

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