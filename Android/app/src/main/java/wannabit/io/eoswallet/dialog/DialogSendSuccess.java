package wannabit.io.eoswallet.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.PasscodeActivity;
import wannabit.io.eoswallet.activity.WellcomeActivity;
import wannabit.io.eoswallet.utils.WLog;

public class DialogSendSuccess extends DialogFragment {

    private Button singleOK;
    private TextView txidTv;

    public static DialogSendSuccess newInstance(Bundle bundle) {
        DialogSendSuccess frag = new DialogSendSuccess();
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
        View contentView    = View.inflate(getContext(), R.layout.dialog_send_success, null);

        singleOK = contentView.findViewById(R.id.singleOK);
        txidTv = contentView.findViewById(R.id.sendTxid);
        txidTv.setText(getArguments().getString("txid"));

        singleOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PasscodeActivity)getActivity()).onSuccessTransaction(null);
                getDialog().dismiss();

            }
        });

        txidTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WLog.w("TXID clicked");
                ((PasscodeActivity)getActivity()).onSuccessTransaction(getArguments().getString("txid"));
                getDialog().dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView);
        return builder.create();
    }

}
