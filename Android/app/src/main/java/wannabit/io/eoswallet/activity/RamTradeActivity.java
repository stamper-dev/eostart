package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.dialog.DialogTradeRamEos;
import wannabit.io.eoswallet.dialog.DialogTradeRamKb;
import wannabit.io.eoswallet.dialog.DialogTradeRamReceipt;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.ResAccountInfo;
import wannabit.io.eoswallet.network.ResRamPrice;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;
import wannabit.io.eoswallet.view.VerticalSeekBar;

public class RamTradeActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar             mToolbar;
    private VerticalSeekBar     mEosSeekbar, mRamSeekbar;
    private CardView            mCardEos, mCardRam;
    private TextView            mRamRate;
    private TextView            mEosBefore, mEosDiffer, mEosAfter;
    private TextView            mRamBefore, mRamDiffer, mRamAfter;
    private Button              mConfirm;


    private WBUser              mUser;
    private ResAccountInfo      mUserInfo;
    private BigDecimal          mRate;    //EOS/Byte
    private BigDecimal          mRamMin;
    private BigDecimal          mRamMax;
    private BigDecimal          mEosMax;
    private DecimalFormat       mDecimalFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ramtrade);
        mToolbar            = findViewById(R.id.toolbar);
        mEosSeekbar         = findViewById(R.id.eos_seekbar);
        mRamSeekbar         = findViewById(R.id.ram_seekbar);
        mCardEos            = findViewById(R.id.cardEos);
        mCardRam            = findViewById(R.id.cardRam);
        mRamRate            = findViewById(R.id.ramTrade_rate);
        mEosBefore          = findViewById(R.id.eosBeforeAmount);
        mEosDiffer          = findViewById(R.id.eosChangeAmount);
        mEosAfter           = findViewById(R.id.eosResultAmount);
        mRamBefore          = findViewById(R.id.ramBeforeAmount);
        mRamDiffer          = findViewById(R.id.ramChangeAmount);
        mRamAfter           = findViewById(R.id.ramResultAmount);
        mConfirm            = findViewById(R.id.btn_confirm);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEosSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int realprogress = progress;
                    if(realprogress < 0)
                        realprogress = 0;
                    if(realprogress > SecondEosProgress) {
                        realprogress = SecondEosProgress;
                        mEosSeekbar.setProgress(SecondEosProgress);
                    }

                    onUpdateView(1000 - realprogress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        mRamSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int realprogress = progress;
                    if(realprogress < SecondRamProgress) {
                        realprogress = SecondRamProgress;
                        mRamSeekbar.setProgress(SecondRamProgress);
                    }
                    if(realprogress > 1000)
                        realprogress = 1000;
                    onUpdateView(realprogress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        mCardEos.setOnClickListener(this);
        mCardRam.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mDecimalFormat.setMaximumFractionDigits(8);
        onInitData();
    }


    private void onInitData() {
        Long lastUserId = getBaseDao().getRecentAccountId();
        ArrayList<WBUser> registedUsers  = getBaseDao().onSelectAllUser();
        if(registedUsers == null || registedUsers.size() < 1) {
            finish();
        } else if (lastUserId < 0) {
            mUser = registedUsers.get(0);

        } else {
            mUser = registedUsers.get(0);
            for(WBUser user:registedUsers) {
                if(user.getId() == lastUserId) {
                    mUser = user;
                    break;
                }
            }
        }

        onShowWaitDialog();
        getRamPrice();
    }

    private int SecondEosProgress;
    private int InitEosProgress;
    private int SecondRamProgress;
    private int InitRamProgress;

    private BigDecimal mUserUsedRam;
    private BigDecimal mUserRentedRam;
    private BigDecimal mUserQuotaRam;

    private BigDecimal mUserUnStakedAmount;


    private BigDecimal mSellMaxAmout;
    private BigDecimal mSellMaxAmountFee;
    private BigDecimal mSellMaxAmountReal;

    private BigDecimal mBuyMaxAmountFee;
    private BigDecimal mBuyMaxAmountReal;

    private Boolean    mWithEos;


    private void onInitView() {
        mRamRate.setText(mRate.movePointRight(3).setScale(8, BigDecimal.ROUND_HALF_UP) + " EOS/KB");

        mUserUsedRam = BigDecimal.valueOf(mUserInfo.getRam_usage());
        mUserRentedRam = BigDecimal.valueOf(mUserInfo.getRentedResourceRam());
        if(mUserUsedRam.compareTo(mUserRentedRam) > 0) {
            mRamMin = mUserUsedRam.setScale(0);
        } else {
            mRamMin = mUserRentedRam.setScale(0);
        }

        mUserQuotaRam       = BigDecimal.valueOf(mUserInfo.getRam_quota());
        mUserUnStakedAmount = BigDecimal.valueOf(mUserInfo.getTotalUnstakedAmout());

        mBuyMaxAmountFee = mUserUnStakedAmount.multiply(new BigDecimal("5")).divide(new BigDecimal("1005"), 4, BigDecimal.ROUND_UP);
        mBuyMaxAmountReal = mUserUnStakedAmount.subtract(mBuyMaxAmountFee).setScale(4);
        mRamMax = mUserQuotaRam.add(mBuyMaxAmountReal.divide(mRate, 0, BigDecimal.ROUND_FLOOR));

        mSellMaxAmout = mUserQuotaRam.multiply(mRate).setScale(4, BigDecimal.ROUND_FLOOR);

        mSellMaxAmountFee =  mSellMaxAmout.multiply(new BigDecimal("5")).divide(new BigDecimal("1005"), 4, BigDecimal.ROUND_UP);
        mSellMaxAmountReal = mSellMaxAmout.subtract(mSellMaxAmountFee);
        mEosMax = mUserUnStakedAmount.add(mSellMaxAmountReal);



        mEosBefore.setText(WUtil.unSignAmoutSpanFormat(mUserInfo.getTotalUnstakedAmout(), 0.8f));
        mEosDiffer.setText(WUtil.unSignAmoutSpanFormat(0d, 0.8f));
        mEosAfter.setText(WUtil.unSignAmoutSpanFormat(mUserInfo.getTotalUnstakedAmout(), 0.8f));
        mRamBefore.setText(WUtil.FormatKByte(mUserInfo.getRam_quota()));
        mRamDiffer.setText(WUtil.FormatKByte(0d));
        mRamAfter.setText(WUtil.FormatKByte(mUserInfo.getRam_quota()));


        InitRamProgress = mUserQuotaRam.movePointRight(3).divide(mRamMax, 0, BigDecimal.ROUND_DOWN).intValue();

        SecondRamProgress = mRamMin.movePointRight(3).divide(mRamMax, 0, BigDecimal.ROUND_DOWN).intValue();
        if(SecondRamProgress == 0)
            SecondRamProgress = 1;


        InitEosProgress = 1000 - InitRamProgress;
        SecondEosProgress  = 1000 - SecondRamProgress;

        mEosSeekbar.setProgress(InitEosProgress);
        mEosSeekbar.setSecondaryProgress(SecondEosProgress);

        mRamSeekbar.setProgress(InitRamProgress);
        mRamSeekbar.setSecondaryProgress(SecondRamProgress);


    }


    BigDecimal changeEos = BigDecimal.ZERO;
    BigDecimal resultEos = BigDecimal.ZERO;
    BigDecimal changeRam = BigDecimal.ZERO;
    BigDecimal resultRam = BigDecimal.ZERO;

    private void onUpdateView(int ramProgress) {
        mConfirm.setEnabled(false);
        mWithEos = false;

        if (ramProgress == InitRamProgress)  {
            resultRam = mUserQuotaRam;
            changeRam = BigDecimal.ZERO;
            changeEos = BigDecimal.ZERO;
            resultEos = mUserUnStakedAmount;
            mEosSeekbar.setProgress(InitEosProgress);
            mRamSeekbar.setProgress(1000 - mEosSeekbar.getProgress());


        } else if (ramProgress ==  SecondRamProgress) {
            resultRam = mRamMin;
            changeRam = mUserQuotaRam.subtract(mRamMin).negate();
            resultEos = mEosMax;
            changeEos = resultEos.subtract(mUserUnStakedAmount);
            mEosSeekbar.setProgress(SecondEosProgress);
            mRamSeekbar.setProgress(1000 - mEosSeekbar.getProgress());
            mConfirm.setEnabled(true);

        } else if (ramProgress == 1000) {
            resultEos = BigDecimal.ZERO;
            changeEos = mUserUnStakedAmount.negate();
            resultRam = mRamMax;
            changeRam = resultRam.subtract(mUserQuotaRam);
            mEosSeekbar.setProgress(0);
            mRamSeekbar.setProgress(1000 - mEosSeekbar.getProgress());
            mConfirm.setEnabled(true);

        } else if (ramProgress > 0) {
            resultRam = mRamMax.movePointLeft(3).multiply(BigDecimal.valueOf(ramProgress));
            changeRam = resultRam.subtract(mUserQuotaRam);
            BigDecimal realchange = changeRam.multiply(mRate).negate();
            BigDecimal fee =  realchange.abs().multiply(new BigDecimal("5")).movePointLeft(3);
            changeEos = realchange.subtract(fee);

            resultEos = mUserUnStakedAmount.add(changeEos);
            mEosSeekbar.setProgress(resultEos.movePointRight(3).divide(mEosMax, 0, BigDecimal.ROUND_HALF_UP).intValue());
            mRamSeekbar.setProgress(1000 - mEosSeekbar.getProgress());
            mConfirm.setEnabled(true);
        }


        if(changeRam.compareTo(BigDecimal.ZERO) > 0) {
            mRamDiffer.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (changeRam.compareTo(BigDecimal.ZERO) == 0) {
            mRamDiffer.setTextColor(getResources().getColor(R.color.colorGray3));
        } else {
            mRamDiffer.setTextColor(getResources().getColor(R.color.colorRed));
        }
        mRamDiffer.setText(WUtil.signFormatKByte(changeRam.doubleValue()));
        mRamAfter.setText(WUtil.FormatKByte(resultRam.doubleValue()));


        if(changeEos.compareTo(BigDecimal.ZERO) > 0) {
            mEosDiffer.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (changeEos.compareTo(BigDecimal.ZERO) == 0) {
            mEosDiffer.setTextColor(getResources().getColor(R.color.colorGray3));
        } else {
            mEosDiffer.setTextColor(getResources().getColor(R.color.colorRed));
        }
        mEosDiffer.setText(WUtil.signAmoutSpanFormat(changeEos.doubleValue(), 0.8f));
        mEosAfter.setText(WUtil.unSignAmoutSpanFormat(resultEos.doubleValue(), 0.8f));
    }


    private void onUpdateViewByValue(BigDecimal eosValue, BigDecimal ramValue) {
        mConfirm.setEnabled(false);
        mWithEos = false;
        if(eosValue != null) {
            resultEos = eosValue;
            changeEos = eosValue.subtract(mUserUnStakedAmount);
            changeRam = changeEos.movePointLeft(3).multiply(new BigDecimal("995")).divide(mRate, 0, BigDecimal.ROUND_HALF_UP).negate();
            resultRam = mUserQuotaRam.add(changeRam);
            mWithEos = true;
            if(eosValue.compareTo(mUserUnStakedAmount) != 0)
                mConfirm.setEnabled(true);

        } else {
            resultRam = ramValue;
            changeRam = resultRam.subtract(mUserQuotaRam);

            BigDecimal realchange = changeRam.multiply(mRate).negate();
            BigDecimal fee =  realchange.abs().multiply(new BigDecimal("5")).movePointLeft(3);
            changeEos = realchange.subtract(fee);
            resultEos = mUserUnStakedAmount.add(changeEos);

            if(ramValue.compareTo(mUserQuotaRam) != 0)
                mConfirm.setEnabled(true);



        }

        mRamSeekbar.setProgress(resultRam.movePointRight(3).divide(mRamMax, 0, BigDecimal.ROUND_HALF_UP).intValue());
        mEosSeekbar.setProgress(resultEos.movePointRight(3).divide(mEosMax, 0, BigDecimal.ROUND_HALF_UP).intValue());

        if(changeRam.compareTo(BigDecimal.ZERO) > 0) {
            mRamDiffer.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (changeRam.compareTo(BigDecimal.ZERO) == 0) {
            mRamDiffer.setTextColor(getResources().getColor(R.color.colorGray3));
        } else {
            mRamDiffer.setTextColor(getResources().getColor(R.color.colorRed));
        }
        mRamDiffer.setText(WUtil.signFormatKByte(changeRam.doubleValue()));
        mRamAfter.setText(WUtil.FormatKByte(resultRam.doubleValue()));

        if(changeEos.compareTo(BigDecimal.ZERO) > 0) {
            mEosDiffer.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (changeEos.compareTo(BigDecimal.ZERO) == 0) {
            mEosDiffer.setTextColor(getResources().getColor(R.color.colorGray3));
        } else {
            mEosDiffer.setTextColor(getResources().getColor(R.color.colorRed));
        }
        mEosDiffer.setText(WUtil.signAmoutSpanFormat(changeEos.doubleValue(), 0.8f));
        mEosAfter.setText(WUtil.unSignAmoutSpanFormat(resultEos.doubleValue(), 0.8f));


    }

    @Override
    public void onClick(View v) {
        if(v.equals(mConfirm)) {

            Bundle bundle = new Bundle();

            BigDecimal finalChangeRam = changeRam.setScale(0, BigDecimal.ROUND_DOWN);
            BigDecimal finalResultRam = mUserQuotaRam.add(finalChangeRam).setScale(0);

            BigDecimal finalChangeEos = changeEos.setScale(4, BigDecimal.ROUND_HALF_UP);
            BigDecimal finalResultEos = mUserUnStakedAmount.add(finalChangeEos);

            bundle.putString("changeRam", finalChangeRam.toString());
            bundle.putString("resultRam", finalResultRam.toString());
            bundle.putString("changeEOS", finalChangeEos.toString());
            bundle.putString("resultEOS", finalResultEos.toString());
            bundle.putBoolean("withEOS", mWithEos);

            DialogTradeRamReceipt dialog  = DialogTradeRamReceipt.newInstance(bundle);
            dialog.setCancelable(true);
            dialog.show(getSupportFragmentManager(), "dialog");

        } else if (v.equals(mCardEos)) {
            Bundle bundle = new Bundle();
            bundle.putDouble("buyMaxEos", mUserUnStakedAmount.setScale(4).doubleValue());
            bundle.putDouble("sellMaxEos", mSellMaxAmountReal.setScale(4).doubleValue());
            DialogTradeRamEos dialog  = DialogTradeRamEos.newInstance(bundle);
            dialog.setCancelable(true);
            dialog.show(getSupportFragmentManager(), "dialog");

        } else if (v.equals(mCardRam)) {
            Bundle bundle = new Bundle();
            bundle.putDouble("buyMaxRam", mRamMax.subtract(mUserQuotaRam).setScale(0).doubleValue());
            bundle.putDouble("sellMaxRam", mUserQuotaRam.subtract(mRamMin).setScale(0).doubleValue());
            DialogTradeRamKb dialog  = DialogTradeRamKb.newInstance(bundle);
            dialog.setCancelable(true);
            dialog.show(getSupportFragmentManager(), "dialog");

        }
    }




    private void getRamPrice() {
        ApiClient.getRamPrice(this).enqueue(new Callback<ResRamPrice>() {
            @Override
            public void onResponse(Call<ResRamPrice> call, Response<ResRamPrice> response) {
//                WLog.w("getRamPrice ");
                if(response.isSuccessful()) {
                    BigDecimal ram = new BigDecimal(response.body().rows.get(0).base.balance.replace("RAM", "").replace(" ", ""));
                    BigDecimal eos = new BigDecimal(response.body().rows.get(0).quote.balance.replace("EOS", "").replace(" ", ""));

                    mRate = eos.divide(ram, 16, BigDecimal.ROUND_HALF_UP);
                    onRequestUserInfo(mUser.getAccount());
                } else {
                    onHideWaitDialog();
                    onShowErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<ResRamPrice> call, Throwable t) {
                WLog.w("getRamPrice onFailure ");
                onHideWaitDialog();
                onShowErrorDialog();
            }
        });
    }


    private  void onRequestUserInfo(String userId) {
        ApiClient.getAccount(this, userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                WLog.w("onRequestUserInfo OK : " + response.body());
                onHideWaitDialog();
                if(response.isSuccessful()) {
                    mUserInfo = new Gson().fromJson(response.body(), ResAccountInfo.class);
                    onInitView();
                } else {
                    onHideWaitDialog();
                    onShowErrorDialog();

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onRequestUserInfo onFailure : " + t.getMessage());
                onHideWaitDialog();
                onShowErrorDialog();
            }
        });
    }

    public void setAmountByEos(String buyAmount, String sellAmout) {

        try {
            if(buyAmount != null) {
                BigDecimal buyAmoutDecimal = new BigDecimal(buyAmount).setScale(4);
                if(buyAmoutDecimal.compareTo(mUserUnStakedAmount) < 1) {
                    onUpdateViewByValue(mUserUnStakedAmount.subtract(buyAmoutDecimal), null);
                }

            } else {
                BigDecimal sellAmoutDecimal = new BigDecimal(sellAmout).setScale(4);
                if(sellAmoutDecimal.compareTo(mSellMaxAmountReal) < 1) {
                    onUpdateViewByValue(mUserUnStakedAmount.add(sellAmoutDecimal), null);
                }
            }

        } catch (Exception e) { }

    }

    public void setValueByRam(String buyValue, String sellValue) {
        try {
            if(buyValue != null) {
                BigDecimal buyAmoutDecimal = new BigDecimal(buyValue).multiply(new BigDecimal("1024")).setScale(2);
                if(buyAmoutDecimal.compareTo(mRamMax.subtract(mUserQuotaRam)) < 1) {
                    onUpdateViewByValue(null, mUserQuotaRam.add(buyAmoutDecimal));
                }

            } else {
                BigDecimal sellAmoutDecimal = new BigDecimal(sellValue).multiply(new BigDecimal("1024")).setScale(2);
                if(sellAmoutDecimal.compareTo(mUserQuotaRam.subtract(mRamMin)) < 1) {
                    onUpdateViewByValue(null, mUserQuotaRam.subtract(sellAmoutDecimal));
                }
            }

        } catch (Exception e) { }
    }

    public void onStartRamTrade(String type, String amount) {
        Intent intent = new Intent(RamTradeActivity.this, PasscodeActivity.class);
        intent.putExtra(BaseConstant.CONST_KEY_TARGET, type);
        intent.putExtra("account", mUser.getAccount());
        intent.putExtra("targetAmount", amount);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

}