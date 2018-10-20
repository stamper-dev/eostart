package wannabit.io.eoswallet.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.AppLockActivity;
import wannabit.io.eoswallet.activity.WalletDetailActivity;
import wannabit.io.eoswallet.utils.WLog;

public class DialogFingerPrint extends DialogFragment {

    private TextView mFingerMsg, mFingerHelp;
    private Button singleOK;

    private AppLockActivity mAppLockActivity;

    public static DialogFingerPrint newInstance(Bundle bundle) {
        DialogFingerPrint frag = new DialogFingerPrint();
        frag.setArguments(bundle);
        return frag;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppLockActivity){
            mAppLockActivity = (AppLockActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        setRetainInstance(true);
        View contentView = View.inflate(getContext(), R.layout.dialog_fingerprint, null);
        mFingerMsg = contentView.findViewById(R.id.fingerMsg);
        mFingerHelp = contentView.findViewById(R.id.fingerHelp);
        singleOK = contentView.findViewById(R.id.singleOK);

        singleOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();

            }
        });


        FingerprintManagerCompat mFingerprintManagerCompat = FingerprintManagerCompat.from(mAppLockActivity);
        mFingerprintManagerCompat.authenticate(null, 0, new CancellationSignal(), new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                WLog.w("onAuthenticationError : " + errMsgId + " " +errString);
                Toast.makeText(mAppLockActivity, errString, Toast.LENGTH_SHORT).show();
                if(getDialog() != null)
                    getDialog().dismiss();
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                mFingerHelp.setText(helpString);
                super.onAuthenticationHelp(helpMsgId, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                mAppLockActivity.onSuccessResult();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        }, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mAppLockActivity);
        builder.setView(contentView);
        return builder.create();
    }

//    private AppLockActivity getPActivity() {
//        return (AppLockActivity)getActivity();
//    }
}
