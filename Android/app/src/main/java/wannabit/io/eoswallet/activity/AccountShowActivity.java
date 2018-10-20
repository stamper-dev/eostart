package wannabit.io.eoswallet.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;

public class AccountShowActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private Button mBtnCopy;
    private TextView mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_accountshow);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);

        mBtnCopy = findViewById(R.id.btn_copy);
        mKey = findViewById(R.id.showKey);

        mToolbarTitle.setText(getIntent().getStringExtra("account"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mKey.setText(getIntent().getStringExtra("key"));
        mBtnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getIntent().getStringExtra("account"), getIntent().getStringExtra("key"));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(AccountShowActivity.this, R.string.str_copyed_msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
