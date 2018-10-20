package wannabit.io.eoswallet.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.dialog.DialogRecentSent;
import wannabit.io.eoswallet.model.WBAction;
import wannabit.io.eoswallet.model.WBRecent;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.BPService;
import wannabit.io.eoswallet.network.ReqCurrencyBalance;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class SendActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener{

    private LinearLayout mContentLayer;
    private Toolbar     mToolbar;
    private TextView    mToolbarTitle;
    private ImageView   mSendTopImage;
    private TextView    mSendTotalAmount;
    private EditText    mSendReceiver;
    private TextView    mSendReceiverQr;
    private Button      mSendRecentRecivers;
    private EditText    mSendAmount;
    private TextView    mSendAmountClear;
    private Button[]    mSendAmoutAdd = new Button[5];
    private TextView    mSendAmoutToCash;
    private EditText    mSendMemo;
    private TextView    mSendMemoCnt;
    private Button      mSendBtn;

    private WBToken     mWBToken;
    private WBUser      mSelectedUser;
    private BigDecimal  mCurrentBalance  = BigDecimal.ZERO;
    private ArrayList<WBRecent> mRecentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        mContentLayer          = findViewById(R.id.contentLayer);
        mToolbar            = findViewById(R.id.toolbar);
        mToolbarTitle       = findViewById(R.id.toolbar_title);
        mSendTopImage       = findViewById(R.id.detailImage);
        mSendTotalAmount    = findViewById(R.id.unstakeInfo);
        mSendReceiver       = findViewById(R.id.receiver_account);
        mSendReceiverQr     = findViewById(R.id.receiver_camera);
        mSendRecentRecivers = findViewById(R.id.receiver_recent);
        mSendAmount         = findViewById(R.id.to_send_amount);
        mSendAmountClear    = findViewById(R.id.to_send_amount_clear);
        mSendAmoutToCash    = findViewById(R.id.to_send_amount_to_cash);
        mSendMemo           = findViewById(R.id.to_send_memo);
        mSendMemoCnt        = findViewById(R.id.memoCnt);
        mSendBtn            = findViewById(R.id.btn_send_request);
        for(int i = 0; i < mSendAmoutAdd.length; i++) {
            mSendAmoutAdd[i] = findViewById(getResources().getIdentifier("to_send_add_amount_" + i , "id", getPackageName()));
            mSendAmoutAdd[i].setOnClickListener(this);
        }
        mSendReceiverQr.setOnClickListener(this);
        mSendRecentRecivers.setOnClickListener(this);
        mSendAmountClear.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);

        mWBToken = getIntent().getParcelableExtra("token");
        mRecentList = getIntent().getParcelableArrayListExtra("list");


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnTouchListener(this);
        mContentLayer.setOnTouchListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSendReceiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String userinput = editable.toString().trim();
                if(userinput == null || userinput.length() == 0) {
                    mSendReceiver.setBackground(getDrawable(R.drawable.edittext_correct));
                    onUpdateSendBtn();
                } else {
                    if(userinput.endsWith(mSelectedUser.getAccount())) {
                        mSendReceiver.setBackground(getDrawable(R.drawable.edittext_red));
                        Toast.makeText(SendActivity.this, R.string.str_error_self_send, Toast.LENGTH_SHORT).show();
                        mSendReceiver.setBackground(getDrawable(R.drawable.edittext_red));
                        mSendBtn.setEnabled(false);
                        return;
                    }

                    if(WUtil.checkAccountPattern(userinput)) {
                        mSendReceiver.setBackground(getDrawable(R.drawable.edittext_correct));
                    } else {
                        mSendReceiver.setBackground(getDrawable(R.drawable.edittext_red));
                    }
                    onUpdateSendBtn();
                }
            }
        });
        mSendAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("00")) {
                    if (enteredString.length() > 0) {
                        mSendAmount.setText(enteredString.substring(1));
                        mSendAmount.setSelection(1);
                    } else {
                        mSendAmount.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                WLog.w("afterTextChanged " + editable.toString());
                String enteredString = editable.toString().replace(",","").trim();
                if(enteredString == null || enteredString.length() == 0) {
                    mSendAmoutToCash.setText(WUtil.getDisplayPriceSumStr(getBaseContext(), getBaseDao().getLastEosTic(), getBaseDao().getUserCurrencyStr(getBaseContext()), 0d));
                    mSendAmount.setBackground(getDrawable(R.drawable.edittext_correct));
                    onUpdateSendBtn();

                } else if(!enteredString.endsWith(".") && !enteredString.startsWith(".")) {
                    final BigDecimal inputAmout = new BigDecimal(enteredString);
                    mSendAmoutToCash.setText("~ " + WUtil.getDisplayPriceSumStr(getBaseContext(), getBaseDao().getLastEosTic(), getBaseDao().getUserCurrencyStr(getBaseContext()), inputAmout.doubleValue()));
                    if(inputAmout.compareTo(mCurrentBalance) > 0) {
                        mSendAmount.setBackground(getDrawable(R.drawable.edittext_red));
                    } else {
                        mSendAmount.setBackground(getDrawable(R.drawable.edittext_correct));
                    }
                    onUpdateSendBtn();

                } else {
                    onUpdateSendBtn();
                }
            }
        });
        mSendAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    mSendAmount.setText(WUtil.ReplaceFormat(getBaseContext(), mSendAmount.getText().toString().trim(), mWBToken));
                }
            }
        });
        mSendMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String enteredString = editable.toString().trim();
                try {
                    final byte[] utf8Bytes = enteredString.getBytes("UTF-8");
                    if(enteredString == null || enteredString.length() == 0) {
                        mSendMemo.setBackground(getDrawable(R.drawable.edittext_correct));
                    } else if (utf8Bytes.length > 255) {
                        mSendMemo.setBackground(getDrawable(R.drawable.edittext_red));
                    } else {
                        mSendMemo.setBackground(getDrawable(R.drawable.edittext_correct));
                    }
                    mSendMemoCnt.setText("(" + utf8Bytes.length + "/255)");

                } catch (Exception e) {
                    mSendMemo.setText("");
                }
                onUpdateSendBtn();
            }
        });


        onInitData();
    }

    @Override
    public void onErrorChecked() {
        onBackPressed();
    }

    private void onInitData() {
        Long lastUserId = getBaseDao().getRecentAccountId();
        ArrayList<WBUser> registedUsers  = getBaseDao().onSelectAllUser();
        if(registedUsers == null || registedUsers.size() < 1) {
            WLog.r("SendActivity NO Account Error!!");
            finish();
        } else if (lastUserId < 0) {
            mSelectedUser = registedUsers.get(0);

        } else {
            mSelectedUser = registedUsers.get(0);
            for(WBUser user:registedUsers) {
                if(user.getId() == lastUserId) {
                    mSelectedUser = user;
                    break;
                }
            }
        }
        onUpdateViews();
    }

    private void onUpdateViews() {
        mToolbarTitle.setText(getString(R.string.str_send) + " " + mWBToken.getSymbol());
        mSendAmount.setHint(mWBToken.getSymbol() + " " + getString(R.string.str_amount));
        mSendAmoutToCash.setText(WUtil.getDisplayPriceSumStr(getBaseContext(), getBaseDao().getLastEosTic(), getBaseDao().getUserCurrencyStr(getBaseContext()), 0d));
        if(mWBToken.getContractAddr().equals(getString(R.string.str_eos_contract))) {
            mSendAmoutToCash.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.logo_eos).asGif().into(new SimpleTarget<GifDrawable>() {
                @Override
                public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                    resource.start();
                    mSendTopImage.setImageDrawable(resource);
                }
                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    mSendTopImage.setImageDrawable(getDrawable(R.drawable.eos_logo_white));
                }
            });

        } else {
            mSendAmoutToCash.setVisibility(View.INVISIBLE);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(SendActivity.this)
                            .load(mWBToken.getIconUrl())
                            .override(108, 108)
                            .into(mSendTopImage);
                }
            });
        }

        ApiClient.getCurrency(this, mWBToken.getContractAddr(), mSelectedUser.getAccount(), mWBToken.getSymbol()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()) {
                    if(response.body().size() > 0) {
                        mCurrentBalance = new BigDecimal(response.body().get(0).toLowerCase().replace(mWBToken.getSymbol().toLowerCase(), "").trim());

                    } else {
                        mCurrentBalance = BigDecimal.ZERO;
                    }
                    mSendTotalAmount.setText(WUtil.AmountSpanFormat(getBaseContext(), mCurrentBalance.doubleValue(), mWBToken));
                } else {
                    onShowErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                WLog.w("ReqCurrencyBalance Fail " + t.getMessage());
                onShowErrorDialog();
            }
        });
    }

    private void onUpdateSendBtn() {
        if(onCheckValidation()) {
            mSendBtn.setEnabled(true);
        } else {
            mSendBtn.setEnabled(false);
        }
    }


    private boolean onCheckValidation() {
        if(!WUtil.checkAccountPattern(mSendReceiver.getText().toString().trim()))
            return false;


        try {
            BigDecimal userInput = new BigDecimal(mSendAmount.getText().toString().replace(",","").trim());
            if (userInput.compareTo(BigDecimal.ZERO) <= 0 || userInput.compareTo(mCurrentBalance) > 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }


        String enteredMemo = mSendMemo.getText().toString().trim();
        if(!TextUtils.isEmpty(enteredMemo)) {
            try {
                final byte[] utf8Bytes = enteredMemo.getBytes("UTF-8");
                if(utf8Bytes != null && utf8Bytes.length > 255)
                    return false;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        if(view.equals(mSendReceiverQr)) {
            new IntentIntegrator(this).initiateScan();

        } else if (view.equals(mSendRecentRecivers)) {
            if(mRecentList == null || mRecentList.size() == 0) {
                Toast.makeText(SendActivity.this, R.string.str_error_no_list, Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("accounts", mRecentList);
                DialogRecentSent dialog = DialogRecentSent.newInstance(bundle);
                dialog.setCancelable(true);
                dialog.show(getSupportFragmentManager(), "dialog");
                return;
            }

        } else if (view.equals(mSendAmountClear)) {
            mSendAmount.setText("");

        } else if (view.equals(mSendAmoutAdd[0])) {
            onAddAmount(new BigDecimal("1"));

        } else if (view.equals(mSendAmoutAdd[1])) {
            onAddAmount(new BigDecimal("10"));

        } else if (view.equals(mSendAmoutAdd[2])) {
            onAddAmount(new BigDecimal("50"));

        } else if (view.equals(mSendAmoutAdd[3])) {
            onAddAmount(new BigDecimal("100"));

        } else if (view.equals(mSendAmoutAdd[4])) {
            mSendAmount.setText(mCurrentBalance.toString());

        } else if (view.equals(mSendBtn)) {
            if(mSendReceiver.hasFocus() || mSendAmount.hasFocus() || mSendMemo.hasFocus()) {
                onHideKeyboard();
                mContentLayer.requestFocus();
                return;
            }

            Intent intent = new Intent(SendActivity.this, PasscodeActivity.class);
            intent.putExtra(BaseConstant.CONST_KEY_TARGET, BaseConstant.CONST_SEND);
            intent.putExtra("account", mSelectedUser.getAccount());
            intent.putExtra("targetAccount", mSendReceiver.getText().toString().trim());
            intent.putExtra("targetAmount", WUtil.ValueFormat(getBaseContext(), mSendAmount.getText().toString().trim(), mWBToken));
            intent.putExtra("targetMemo", mSendMemo.getText().toString().trim());
            intent.putExtra("targetToken", mWBToken);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        }
    }

    public void onUpdateReceiverFromRecent(String account) {
        mSendReceiver.setText(account);
    }

    private void onAddAmount(BigDecimal addAmount) {
        BigDecimal existedAmount = BigDecimal.ZERO;
        String userInput = mSendAmount.getText().toString().replace(",","").trim();
        if(!TextUtils.isEmpty(userInput) && userInput.endsWith(".")) {
            userInput = userInput.substring(0, userInput.length() - 1);
        }

        if(!TextUtils.isEmpty(userInput)) {
            existedAmount = new BigDecimal(userInput);
        }

        mSendAmount.setText(existedAmount.add(addAmount).toString());
        mSendAmount.setSelection(mSendAmount.getText().toString().trim().length());
        mSendAmount.requestFocus();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.equals(mContentLayer) || view.equals(mToolbar)) {
            onHideKeyboard();
        }
        return false;
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                mSendReceiver.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
