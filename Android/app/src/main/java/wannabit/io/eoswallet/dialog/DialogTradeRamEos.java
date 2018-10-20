package wannabit.io.eoswallet.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.RamTradeActivity;
import wannabit.io.eoswallet.utils.WLog;

public class DialogTradeRamEos extends DialogFragment {

    private EditText    mBuyAmount, mSellAmount;
    private TextView    mBuyMax, mSellMax;
    private Button      singleOK;
    private double      mBuyMaxValue, mBuyMinValue;

    public static DialogTradeRamEos newInstance(Bundle bundle) {
        DialogTradeRamEos frag = new DialogTradeRamEos();
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
        View contentView    = View.inflate(getContext(), R.layout.dialog_traderam_eos, null);
        singleOK = contentView.findViewById(R.id.singleOK);
        mBuyAmount = contentView.findViewById(R.id.buyram_eos);
        mSellAmount = contentView.findViewById(R.id.buyram_kb);
        mBuyMax = contentView.findViewById(R.id.buyram_eos_range);
        mSellMax = contentView.findViewById(R.id.buyram_kb_range);

        mBuyMaxValue = getArguments().getDouble("buyMaxEos");
        mBuyMinValue = getArguments().getDouble("sellMaxEos");

        mBuyMax.setText("~ " + getArguments().getDouble("buyMaxEos"));
        mSellMax.setText("~ " + getArguments().getDouble("sellMaxEos"));


        mBuyAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mSellAmount.setText("");
                }
            }
        });

        mSellAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mBuyAmount.setText("");
                }

            }
        });

        singleOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mBuyAmount.getText())) {
                    getPActivity().setAmountByEos(mBuyAmount.getText().toString().trim(), null);


                } else if (!TextUtils.isEmpty(mSellAmount.getText())) {
                    getPActivity().setAmountByEos(null, mSellAmount.getText().toString().trim());

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

//    @Override
//    public void dismiss() {
//        WLog.w("dismiss");
//        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//        View v = getActivity().getCurrentFocus();
//        if (v == null) {
//            v = new View(getActivity());
//        }
//        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//        super.dismiss();
//    }
}
