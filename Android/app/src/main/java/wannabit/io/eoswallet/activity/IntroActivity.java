package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wannabit.io.eoswallet.BuildConfig;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseApplication;
import wannabit.io.eoswallet.dialog.DialogUpdate;
import wannabit.io.eoswallet.dialog.DialogUpdateForce;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.ResTokenInfo;
import wannabit.io.eoswallet.network.ResVersion;
import wannabit.io.eoswallet.utils.WLog;


/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */

public class IntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getBaseApplication().needShowLockScreen()) {
            Intent intent = new Intent(this, AppLockActivity.class);
            intent.putExtra("locktype", AppLockActivity.LockType.Lock);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            onRequestVersionCheck();
        }

    }

    @Override
    public void onErrorChecked() {
        finish();
    }

    private void onShowUpdateDialog(boolean force) {
        if(isFinishing()) return;
        if(force) {
            DialogUpdateForce dialog  = new DialogUpdateForce();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "update");

        } else {
            DialogUpdate dialog  = new DialogUpdate();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "update");

        }
    }


    public void onNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getAppStatus() != BaseApplication.AppStatus.BACKGROUND && !isFinishing()) {
                    if(getBaseDao().onSelectAllUser().size() > 0) {
                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else{
                        startActivity(new Intent(IntroActivity.this, WellcomeActivity.class));
                    }
                    finish();
                }
            }
        }, 1500);
    }


    public void onRequestVersionCheck() {
        WLog.w("onRequestVersionCheck");
        ApiClient.getVersion(this).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                WLog.w("response : " + response.body());
                WLog.w("response : " + response.errorBody());
                if(response.isSuccessful()) {
                    ResVersion res = new Gson().fromJson(response.body(), ResVersion.class);
                    int versionCode = BuildConfig.VERSION_CODE;
                    int acceptableVersion = Integer.parseInt(res.getAcceptableVersion());
                    int latestVersion = Integer.parseInt(res.getLatestVersion());
                    if(versionCode < acceptableVersion) {
                        onShowUpdateDialog(true);

                    } else if(versionCode < latestVersion) {
                        onShowUpdateDialog(false);

                    } else {
                        onRequestTokensInfo();
                    }

                }  else {
                    onShowErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onRequestVersionCheck onFailure : " + t.getMessage());
                onShowErrorDialog();
            }
        });
    }


    public void onRequestTokensInfo() {
        WLog.w("onRequestTokensInfo");
        ApiClient.getTokenInfo(this).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    ResTokenInfo res = new Gson().fromJson(response.body(), ResTokenInfo.class);
                    if(getBaseDao().onReInsertTokens(res.getTokenList())) {
                        onNextActivity();
                    } else {
                        onShowErrorDialog();
                    }

                } else {
                    onShowErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onRequestTokensInfo onFailure : " + t.getMessage());
                onShowErrorDialog();
            }
        });
    }
}
