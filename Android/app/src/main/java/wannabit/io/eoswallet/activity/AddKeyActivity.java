package wannabit.io.eoswallet.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.crypto.ec.EosPrivateKey;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.network.ResCheckAccount;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class AddKeyActivity extends BaseActivity implements View.OnClickListener {

    private String                      mAccount;

    private RelativeLayout              mLayerRoot;
    private Toolbar                     mToolbar;
    private TextView                    mToolbarTitle;
    private EditText                    mPrivateKeyInput;
    private TextView                    mPaste;
    private Button                      mBtnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_key);
        mLayerRoot          = findViewById(R.id.layerRoot);
        mToolbar            = findViewById(R.id.toolbar);
        mToolbarTitle       = findViewById(R.id.toolbar_title);
        mPrivateKeyInput    = findViewById(R.id.add_key);
        mPaste              = findViewById(R.id.add_paste);
        mBtnNext            = findViewById(R.id.add_Next);

        mAccount = getIntent().getStringExtra("account");

        mToolbarTitle.setText(R.string.str_title_restore);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPaste.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mLayerRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onHideKeyboard();
                mLayerRoot.requestFocus();
                return false;
            }
        });

        mPrivateKeyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                onUpdateBottomBtn(editable.toString().trim());

            }
        });
    }


    private void onUpdateBottomBtn(String input) {
        if(input.length() == 0) {
            mBtnNext.setText(R.string.str_skip);
            mBtnNext.setEnabled(true);
            mPrivateKeyInput.setBackground(getDrawable(R.drawable.edittext_correct));
        } else {
            mBtnNext.setText(R.string.str_confirm);
            if(WUtil.checkPrivateKeyPattern(input)) {
                mBtnNext.setEnabled(true);
                mPrivateKeyInput.setBackground(getDrawable(R.drawable.edittext_correct));
            } else {
                mBtnNext.setEnabled(false);
                mPrivateKeyInput.setBackground(getDrawable(R.drawable.edittext_incorrect));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(mPaste)) {
            ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

            if(clipboard.getPrimaryClip() != null && clipboard.getPrimaryClip().getItemCount() > 0) {
                mPrivateKeyInput.setText(clipboard.getPrimaryClip().getItemAt(0).coerceToText(this));
            } else {
                Toast.makeText(this, R.string.str_error_clipboard, Toast.LENGTH_SHORT).show();
            }

        } else if (view.equals(mBtnNext)) {
            onHideKeyboard();
            if(mPrivateKeyInput.getText().toString().trim().length() > 0) {
                String publicKey = null;
                try {
                    EosPrivateKey pkey = new EosPrivateKey(mPrivateKeyInput.getText().toString().trim());
                    publicKey = pkey.getPublicKey().toString();

                } catch (Exception e) {
                    Toast.makeText(AddKeyActivity.this, R.string.str_error_privatekey, Toast.LENGTH_SHORT).show();
                    return;
                }
                onShowWaitDialog();
                OnRequestCheckKey(publicKey);


            } else {
                onAddUserWithoutPrivateKey();
            }
        }

    }

    public void OnRequestCheckKey(String key) {
        ApiClient.checkAccount(this, key).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    ResCheckAccount res = new Gson().fromJson(response.body(), ResCheckAccount.class);
                    if(res != null && res.getAccount_names().contains(mAccount)) {
                        onAddUserWithPrivateKey();
                    } else {
                        Toast.makeText(AddKeyActivity.this, R.string.str_error_privatekey, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AddKeyActivity.this, R.string.str_error_privatekey, Toast.LENGTH_SHORT).show();

                }
                onHideWaitDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("OnRequestCheckKey onFailure : " + t.getMessage());
                onHideWaitDialog();
                Toast.makeText(AddKeyActivity.this, R.string.str_error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onAddUserWithoutPrivateKey() {
//        WLog.w("onAddUserWithoutPrivateKey");
        if(getBaseDao().isExistingAccount(mAccount)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            getBaseDao().setLastEosTicTime(0l);
            WBUser newUser = new WBUser(mAccount, "");
            if(getBaseDao().onInsertUser(newUser) > 0 ) {
                getBaseDao().setRecentAccountId(getBaseDao().onSelectByAccount(mAccount).getId());
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                WLog.r("new Account without insert Error");
            }
        }


    }

    private void onAddUserWithPrivateKey() {
//        WLog.w("onAddUserWithPrivateKey");
        getBaseDao().setLastEosTicTime(0l);
        Intent intent = new Intent(AddKeyActivity.this, PasscodeActivity.class);
        intent.putExtra("account", mAccount);
        if(mPrivateKeyInput.getText().toString().trim().length() > 0)
            intent.putExtra("privateKey", mPrivateKeyInput.getText().toString().trim());
        intent.putExtra(BaseConstant.CONST_KEY_TARGET, BaseConstant.CONST_ADD_USER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

    }
}
