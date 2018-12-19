package wannabit.io.eoswallet.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import wannabit.io.eoswallet.BuildConfig;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.AccountManageActivity;
import wannabit.io.eoswallet.activity.AppLockActivity;
import wannabit.io.eoswallet.activity.CurrencyManageActivity;
import wannabit.io.eoswallet.activity.LanguageManageActivity;
import wannabit.io.eoswallet.activity.LicenseActivity;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.databinding.FragmentMainSettingsBinding;
import wannabit.io.eoswallet.utils.WLog;

public class MainSettingsFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private final static int  REQ_ENABLE_APPLOCK    = 5001;
    private final static int  REQ_DISABLE_APPLOCK   = 5002;

    FragmentMainSettingsBinding         mBinding;
    private FingerprintManagerCompat    mFingerprintManagerCompat;

    public static MainSettingsFragment newInstance() {
        MainSettingsFragment fragment = new MainSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_settings, container, false);

        mFingerprintManagerCompat = FingerprintManagerCompat.from(getBaseActivity());

        mBinding = DataBindingUtil.bind(rootView);

        mBinding.setAccount.setOnClickListener(this);
        mBinding.setCurrency.setOnClickListener(this);
        mBinding.setLanguage.setOnClickListener(this);

        mBinding.setHamepage.setOnClickListener(this);
        mBinding.setTellegram.setOnClickListener(this);

        mBinding.setTerms.setOnClickListener(this);
        mBinding.setOpenSrc.setOnClickListener(this);
        mBinding.setGithub.setOnClickListener(this);

        mBinding.switchAppLock.setOnCheckedChangeListener(this);
        mBinding.switchFingerprint.setOnCheckedChangeListener(this);

        return rootView;
    }




    @Override
    public void onRefreshByMainTab(boolean deep) {
//        WLog.w("onRefreshByMainTab Settings");
        if(isAdded()) {
            mBinding.accountTvT.setText(getString(R.string.str_accounts));
            mBinding.currencyTvT.setText(getString(R.string.str_currency));
            mBinding.languageTvT.setText(getString(R.string.str_language));
            mBinding.appLockTvT.setText(getString(R.string.str_service_applock));
            mBinding.fingerprintTvT.setText(getString(R.string.str_service_finger));
            mBinding.termsTvT.setText(getString(R.string.str_service_terms));
            mBinding.openSrcTvT.setText(getString(R.string.str_service_licenses));
            mBinding.githubTvT.setText(getString(R.string.str_service_github));

            mBinding.currencyTv.setText(getBaseDao().getUserCurrencyStr(getBaseActivity()));
            mBinding.languageTv.setText(getBaseDao().getUserLanguageStr(getBaseActivity()));
            mBinding.setVersion.setText("v" + BuildConfig.VERSION_NAME);


            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
                    mFingerprintManagerCompat.isHardwareDetected() &&
                    mFingerprintManagerCompat.hasEnrolledFingerprints() &&
                    getBaseDao().getUsingAppLock()) {
                mBinding.setFingerprint.setVisibility(View.VISIBLE);
                if(getBaseDao().getUsingFingerPrint()) {
                    mBinding.switchFingerprint.setChecked(true);
                } else {
                    mBinding.switchFingerprint.setChecked(false);
                }

            } else {
                mBinding.setFingerprint.setVisibility(View.GONE);

            }


            if(getBaseDao().getUsingAppLock()) {
                mBinding.switchAppLock.setChecked(true);

            } else {
                mBinding.switchAppLock.setChecked(false);
            }
        }
    }



    @Override
    public void willBeDisplayed() {
        if (mBinding.rootLayer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            mBinding.rootLayer.startAnimation(fadeIn);
        }
    }

    @Override
    public void willBeHidden() {
        if (mBinding.rootLayer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            mBinding.rootLayer.startAnimation(fadeOut);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mBinding.setAccount)) {
            startActivity(new Intent(getBaseActivity(), AccountManageActivity.class));

        } else if (view.equals(mBinding.setCurrency)) {
            startActivity(new Intent(getBaseActivity(), CurrencyManageActivity.class));

        } else if (view.equals(mBinding.setLanguage)) {
            startActivity(new Intent(getBaseActivity(), LanguageManageActivity.class));

        } else if (view.equals(mBinding.setHamepage)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wannabit.io/"));
            startActivity(intent);

        } else if (view.equals(mBinding.setTellegram)) {
            Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/wannabitlabs"));
            startActivity(telegram);

        } else if (view.equals(mBinding.setTerms)) {
            onShowTerms();

        } else if (view.equals(mBinding.setGithub)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/wannabit-dev/eostart"));
            startActivity(intent);

        } else if (view.equals(mBinding.setOpenSrc)) {
            startActivity(new Intent(getContext(), LicenseActivity.class));
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        if(buttonView.isPressed()) {
            if(buttonView.equals(mBinding.switchAppLock)) {
                Intent lockIntent = new Intent(getBaseActivity(), AppLockActivity.class);
                if(isChecked) {
                    lockIntent.putExtra("locktype", AppLockActivity.LockType.Init);
                    startActivityForResult(lockIntent, REQ_ENABLE_APPLOCK);
                } else{
                    lockIntent.putExtra("locktype", AppLockActivity.LockType.Disable);
                    startActivityForResult(lockIntent, REQ_DISABLE_APPLOCK);
                }


            } else if (buttonView.equals(mBinding.switchFingerprint)) {
                if(isChecked) {
                    new TedPermission(getBaseActivity())
                            .setPermissionListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted() {
                                    getBaseDao().setUsingFingerPrint(isChecked);
                                }

                                @Override
                                public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                                }
                            })
                            .setPermissions(Manifest.permission.USE_FINGERPRINT)
                            .setRationaleMessage("need permission for finger print")
                            .check();
                } else {
                    getBaseDao().setUsingFingerPrint(isChecked);
                }

            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ENABLE_APPLOCK && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getBaseActivity(), R.string.str_applock_enabled, Toast.LENGTH_SHORT).show();

        } else if (requestCode == REQ_DISABLE_APPLOCK && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getBaseActivity(), R.string.str_applock_diaabled, Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onShowTerms() {
        if(getBaseDao().getUserLanguage() == 1) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eostart.com/policy_ko"));
            startActivity(intent);

        } else if (getBaseDao().getUserLanguage() == 2) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eostart.com/policy_en"));
            startActivity(intent);

        } else {
            Locale current = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                current = getResources().getConfiguration().getLocales().get(0);
            } else{
                current = getResources().getConfiguration().locale;
            }

            if(current.toString().toLowerCase().contains("ko") || current.toString().toLowerCase().contains("kr")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eostart.com/policy_ko"));
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eostart.com/policy_en"));
                startActivity(intent);
            }
        }
    }


}
