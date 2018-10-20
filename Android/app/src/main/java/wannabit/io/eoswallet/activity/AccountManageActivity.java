package wannabit.io.eoswallet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.dialog.DialogIntentAddKey;
import wannabit.io.eoswallet.dialog.DialogRequestKey;
import wannabit.io.eoswallet.model.WBCurrency;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.utils.WLog;

public class AccountManageActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private Button mBtnRestore, mBtnNew;

    ArrayList<WBUser> mUsers = new ArrayList<>();
    SetAccountAdapter mSetAccountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);
        mBtnRestore = findViewById(R.id.btn_restore);
        mBtnNew = findViewById(R.id.btn_new);

        mToolbarTitle.setText(R.string.str_accounts);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBtnRestore.setOnClickListener(this);
        mBtnNew.setOnClickListener(this);
        if(getBaseDao().onSelectAllUser().size() >= BaseConstant.CONSTANT_MAX_ACCOUNT) {
            mBtnRestore.setEnabled(false);
        }

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mUsers = getBaseDao().onSelectAllUser();

        mSetAccountAdapter = new SetAccountAdapter(mUsers);
        recyclerView.setAdapter(mSetAccountAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(mBtnRestore)) {
            startActivity(new Intent(AccountManageActivity.this, AddAccountActivity.class));

        } else if(view.equals(mBtnNew)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eostart.com/"));
            startActivity(intent);
        }

    }


    class SetAccountAdapter extends RecyclerView.Adapter<SetAccountAdapter.SetAccountAdapterItemHolder>{

        ArrayList<WBUser> users = new ArrayList<>();

        public SetAccountAdapter(ArrayList<WBUser> u) {
            this.users = u;
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        @NonNull
        @Override
        public SetAccountAdapterItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.item_account_set, viewGroup, false);
            return new SetAccountAdapterItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull SetAccountAdapterItemHolder holder, final int position) {
            final WBUser user = users.get(position);
            if(getBaseDao().hasPrivateKey(user.getAccount())) {
                holder.itemAccountIc.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_key));
            } else {
                holder.itemAccountIc.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_key_off));
            }
            holder.itemAccountTv.setText(user.getAccount());

            holder.itemAccountRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AccountManageActivity.this, AccountDetailActivity.class);
                    intent.putExtra("accountId", user.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            });


            if(position == users.size()-1) {
                holder.itemAccountLine.setVisibility(View.GONE);
            } else {
                holder.itemAccountLine.setVisibility(View.VISIBLE);

            }

        }




        public class SetAccountAdapterItemHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemAccountRoot;
            ImageView itemAccountIc;
            TextView itemAccountTv;
            View itemAccountLine;

            public SetAccountAdapterItemHolder(View v) {
                super(v);
                itemAccountRoot = itemView.findViewById(R.id.accountRoot);
                itemAccountIc = itemView.findViewById(R.id.accountIc);
                itemAccountTv = itemView.findViewById(R.id.accountTv);
                itemAccountLine = itemView.findViewById(R.id.accountLine);

            }
        }
    }
}