package wannabit.io.eoswallet.dialog;

import android.app.Activity;
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
import wannabit.io.eoswallet.utils.WUtil;

public class DialogUndelegateReceipt extends DialogFragment {

    private Button singleOK;

    public static DialogUndelegateReceipt newInstance(Bundle bundle) {
        DialogUndelegateReceipt frag = new DialogUndelegateReceipt();
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
        View contentView = View.inflate(getContext(), R.layout.dialog_undelegate_receipt, null);
        singleOK = contentView.findViewById(R.id.singleOK);

        TextView    mBeforeCpu      = contentView.findViewById(R.id.beforeCpu);
        TextView    mChangeCpu      = contentView.findViewById(R.id.changeCpu);
        TextView    mResultCpu      = contentView.findViewById(R.id.resultCpu);
        TextView    mBeforeNet      = contentView.findViewById(R.id.beforeNet);
        TextView    mChangeNet      = contentView.findViewById(R.id.changeNet);
        TextView    mResultNet      = contentView.findViewById(R.id.resultNet);
        TextView    mBeforeRefund   = contentView.findViewById(R.id.beforeRefund);
        TextView    mChangeRefund   = contentView.findViewById(R.id.changeRefund);
        TextView    mResultRefund   = contentView.findViewById(R.id.resultRefund);

        mBeforeCpu.setText(WUtil.unSignAmoutSpanFormat(getArguments().getDouble("BeforeCpu"), 0.8f));
        mChangeCpu.setText(WUtil.signAmoutSpanFormat(getArguments().getDouble("ChangeCpu"), 0.8f));
        mResultCpu.setText(WUtil.unSignAmoutSpanFormat(getArguments().getDouble("ResultCpu"), 0.8f));

        mBeforeNet.setText(WUtil.unSignAmoutSpanFormat(getArguments().getDouble("BeforeNet"), 0.8f));
        mChangeNet.setText(WUtil.signAmoutSpanFormat(getArguments().getDouble("ChangeNet"), 0.8f));
        mResultNet.setText(WUtil.unSignAmoutSpanFormat(getArguments().getDouble("ResultNet"), 0.8f));

        mBeforeRefund.setText(WUtil.unSignAmoutSpanFormat(getArguments().getDouble("BeforeRefund"), 0.8f));
        mChangeRefund.setText(WUtil.signAmoutSpanFormat(getArguments().getDouble("ChangeRefund"), 0.8f));
        mResultRefund.setText(WUtil.unSignAmoutSpanFormat(getArguments().getDouble("ResultRefund"), 0.8f));

        singleOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                getDialog().dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView);
        return builder.create();
    }
}
