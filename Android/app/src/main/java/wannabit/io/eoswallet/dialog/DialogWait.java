package wannabit.io.eoswallet.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.utils.WLog;


/**
 * Created by jungyongjoo on 15. 6. 26..
 */
public class DialogWait extends DialogFragment {

    public static DialogWait newInstance() {
        DialogWait frag = new DialogWait();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wait, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

//    public void dismissSelf() {
//        if(isAdded()) {
//            WLog.w("isAdded TRUE");
//        } else {
//            WLog.w("isAdded FALSE");
//        }
//
//        if(getDialog() == null) {
//            WLog.w("getDialog NULL");
//        } else {
//            WLog.w("getDialog NOT NULL");
//        }
//        getDialog().dismiss();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        WLog.w("Dialog_Wait onAttach");
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onDetach() {
//        WLog.w("Dialog_Wait onDetach");
//        super.onDetach();
//    }
}