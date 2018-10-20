package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.network.ApiClient;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;


/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */


public class AddAccountActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout  mLayerRoot;
    private Toolbar         mToolbar;
    private TextView        mToolbarTitle;
    private ImageView       mAddImage;
    private TextView        mAddMsg;
    private EditText        mAccountInput;
    private TextView        mCount;
    private Button          mBtnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        mLayerRoot      = findViewById(R.id.layerRoot);
        mToolbar        = findViewById(R.id.toolbar);
        mToolbarTitle   = findViewById(R.id.toolbar_title);
        mAddImage       = findViewById(R.id.add_image);
        mAddMsg         = findViewById(R.id.add_msg);
        mAccountInput   = findViewById(R.id.add_account);
        mCount          = findViewById(R.id.add_count);
        mBtnNext        = findViewById(R.id.add_Next);

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

        mBtnNext.setOnClickListener(this);
        mLayerRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onHideKeyboard();
                mLayerRoot.requestFocus();
                return false;
            }
        });

        mAccountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                final String userInput = editable.toString().trim();
                if(WUtil.checkAccountPattern(userInput)) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
                onUpdateViewStatus();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdateViewStatus();
    }

    private void onUpdateViewStatus() {
        if(WUtil.checkAccountDuringPattern(mAccountInput.getText().toString().trim())) {
            mAccountInput.setBackground(getDrawable(R.drawable.edittext_correct));
            mAddImage.setImageDrawable(getDrawable(R.drawable.img_addaccount_correct));
            mAddMsg.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            mAccountInput.setBackground(getDrawable(R.drawable.edittext_incorrect));
            mAddImage.setImageDrawable(getDrawable(R.drawable.img_addaccount_incorrect));
            mAddMsg.setTextColor(getResources().getColor(R.color.colorRed));
        }

        mCount.setText(WUtil.AccountCharCnt(this, mAccountInput.getText().toString().trim()));
    }

    @Override
    public void onClick(View view) {
        if(view.equals(mBtnNext)) {
            onHideKeyboard();
            if(getBaseDao().isExistingAccount(mAccountInput.getText().toString().trim())) {
                Toast.makeText(AddAccountActivity.this, R.string.str_already_existed_account, Toast.LENGTH_SHORT).show();
                return;
            }
            onShowWaitDialog();
            onRequestUserInfo(mAccountInput.getText().toString().trim());
        }
    }


    public void onRequestUserInfo(String userId) {
        ApiClient.getAccount(this, userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Intent intent = new Intent(AddAccountActivity.this, AddKeyActivity.class);
                    intent.putExtra("account", mAccountInput.getText().toString().trim());
                    startActivity(intent);

                } else {
                    Toast.makeText(AddAccountActivity.this, R.string.str_error_no_account, Toast.LENGTH_SHORT).show();

                }
                onHideWaitDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                WLog.w("onRequestUserInfo onFailure : " + t.getMessage());
                onHideWaitDialog();
                Toast.makeText(AddAccountActivity.this, R.string.str_error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
