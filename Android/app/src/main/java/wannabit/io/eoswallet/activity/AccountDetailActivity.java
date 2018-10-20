package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.dialog.DialogAccountDelete;
import wannabit.io.eoswallet.dialog.DialogIntentAddKey;
import wannabit.io.eoswallet.model.WBUser;

public class AccountDetailActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private Button  mBtnAction, mBtnDelete;
    private LinearLayout mWarnningLayer;
    private TextView mAccountTv;

    private WBUser  mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountdetail);
        mUser = getBaseDao().onSelectById(getIntent().getLongExtra("accountId", -1));

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mBtnAction = findViewById(R.id.btn_action);
        mBtnDelete = findViewById(R.id.btn_delete);
        mAccountTv = findViewById(R.id.accountName);
        mWarnningLayer = findViewById(R.id.accountWarnning);

        mToolbarTitle.setText(getString(R.string.str_accounts));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnAction.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);

        if(mUser != null) {
            onInitViews();
        } else {
            onBackPressed();
        }


    }

    private void onInitViews() {
        mAccountTv.setText(mUser.getAccount());
        if(getBaseDao().hasPrivateKey(mUser.getAccount())) {
            mWarnningLayer.setVisibility(View.VISIBLE);
            mBtnAction.setText(getString(R.string.str_backup_key));

        } else {
            mWarnningLayer.setVisibility(View.INVISIBLE);
            mBtnAction.setText(getString(R.string.str_private_key_add));

        }

    }

    public void onAddKeyforAccount(String account) {
        Intent intent = new Intent(AccountDetailActivity.this, AddKeyActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }

    public void onDeleteAccount(String account) {
        super.onDeleteAccount(account);
    }


    @Override
    public void onClick(View view) {
        if (view.equals(mBtnAction)) {
            if(getBaseDao().hasPrivateKey(mUser.getAccount())) {
                Intent intent = new Intent(AccountDetailActivity.this, PasscodeActivity.class);
                intent.putExtra(BaseConstant.CONST_KEY_TARGET, BaseConstant.CONST_KEY_CHECK);
                intent.putExtra("account", mUser.getAccount());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("account", mUser.getAccount());
                DialogIntentAddKey dialog  = DialogIntentAddKey.newInstance(bundle);
                dialog.setCancelable(true);
                dialog.show(getSupportFragmentManager(), "dialog");
            }

        } else if (view.equals(mBtnDelete)) {
            if(getBaseDao().hasPrivateKey(mUser.getAccount())) {
                Intent intent = new Intent(AccountDetailActivity.this, PasscodeActivity.class);
                intent.putExtra(BaseConstant.CONST_KEY_TARGET, BaseConstant.CONST_DELETE_USER);
                intent.putExtra("account", mUser.getAccount());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

            } else {
                Bundle bundle = new Bundle();
                bundle.putString("account", mUser.getAccount());
                DialogAccountDelete dialog  = DialogAccountDelete.newInstance(bundle);
                dialog.setCancelable(true);
                dialog.show(getSupportFragmentManager(), "dialog");

            }
        }

    }
}
