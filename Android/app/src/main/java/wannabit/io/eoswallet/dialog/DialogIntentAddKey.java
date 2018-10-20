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

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.AccountDetailActivity;
import wannabit.io.eoswallet.activity.AccountManageActivity;
import wannabit.io.eoswallet.activity.WalletDetailActivity;

public class DialogIntentAddKey extends DialogFragment {

    private Button singleCancel, singleOK;

    public static DialogIntentAddKey newInstance(Bundle bundle) {
        DialogIntentAddKey frag = new DialogIntentAddKey();
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
        View contentView    = View.inflate(getContext(), R.layout.dialog_intentaddkey, null);

        singleCancel = contentView.findViewById(R.id.singleCancel);
        singleOK = contentView.findViewById(R.id.singleOK);

        singleCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        singleOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AccountDetailActivity)getActivity()).onAddKeyforAccount(getArguments().getString("account"));
                getDialog().dismiss();

            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView);
        return builder.create();
    }

}
