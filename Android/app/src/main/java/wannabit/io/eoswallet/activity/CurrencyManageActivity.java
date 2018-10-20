package wannabit.io.eoswallet.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.model.WBCurrency;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.utils.WLog;

public class CurrencyManageActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    ArrayList<WBCurrency> mCurrencies = new ArrayList<>();
    CurrencyAdapter mCurrencyAdapter;
    private int     mInitposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_manage);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);

        mToolbarTitle.setText(R.string.str_currency);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ArrayList<String> supportList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.support_currency)));

        mCurrencies.clear();
        for(int i = 0; i < supportList.size() ; i ++) {
            WBCurrency temp = new WBCurrency(i, supportList.get(i), getBaseDao().getUserCurrency(this) == i ? true : false);
            mCurrencies.add(temp);
        }
        mInitposition = getBaseDao().getUserCurrency(this);

        mCurrencyAdapter = new CurrencyAdapter(mCurrencies);
        recyclerView.setAdapter(mCurrencyAdapter);
    }

    @Override
    public void onBackPressed() {
        if(mCurrencyAdapter != null) {
            getBaseDao().setUserCurrency(mCurrencyAdapter.getCheckedPosition());
            if(mInitposition != mCurrencyAdapter.getCheckedPosition())
                getBaseDao().setLastEosTicTime(0l);

        }
        super.onBackPressed();
    }

    class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyAdapterItemHolder>{

        ArrayList<WBCurrency> currencies = new ArrayList<>();

        public CurrencyAdapter(ArrayList<WBCurrency> c) {
            this.currencies = c;
        }

        @Override
        public int getItemCount() {
            return currencies.size();
        }

        @Override
        public CurrencyAdapterItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.item_currency, viewGroup, false);
            return new CurrencyAdapterItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CurrencyAdapterItemHolder holder, final int position) {
            final WBCurrency currency = currencies.get(position);
            if(currency.getName().equals(getString(R.string.str_krw))) {
                holder.itemCurrencyIc.setImageDrawable(getDrawable(R.drawable.ic_krw));

            } else if (currency.getName().equals(getString(R.string.str_usd))) {
                holder.itemCurrencyIc.setImageDrawable(getDrawable(R.drawable.ic_usd));

            } else {
                holder.itemCurrencyIc.setImageDrawable(getDrawable(R.drawable.ic_btc));

            }
            holder.itemCurrencyTv.setText(currency.getName());

            if(currency.isChecked()) {
                holder.itemCurrencyCh.setChecked(true);
                holder.itemCurrencyCh.setClickable(false);
            } else {
                holder.itemCurrencyCh.setChecked(false);
                holder.itemCurrencyCh.setClickable(true);

            }

            holder.itemCurrencyCh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isPressed()) {
                        onToggleBtns(position);
                    }
                }
            });

            holder.itemCurrencyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!currency.isChecked()) {
                        onToggleBtns(position);
                    }
                }
            });

            if(position == currencies.size()-1) {
                holder.itemCurrencyLine.setVisibility(View.GONE);
            } else {
                holder.itemCurrencyLine.setVisibility(View.VISIBLE);

            }

        }

        private void onToggleBtns(int position) {
            for(WBCurrency data:mCurrencies) {
                data.setChecked(false);
            }
            mCurrencies.get(position).setChecked(true);
            notifyDataSetChanged();

        }

        private int getCheckedPosition() {
            int result = -1;
            for(int i = 0; i < currencies.size() ; i ++) {
                if(currencies.get(i).isChecked()) {
                    result = i;
                    break;
                }
            }
            return result;
        }


        public class CurrencyAdapterItemHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemCurrencyRoot;
            ImageView itemCurrencyIc;
            TextView itemCurrencyTv;
            AppCompatCheckBox itemCurrencyCh;
            View itemCurrencyLine;

            public CurrencyAdapterItemHolder(View v) {
                super(v);
                itemCurrencyRoot = itemView.findViewById(R.id.currencyRoot);
                itemCurrencyIc = itemView.findViewById(R.id.currencyIc);
                itemCurrencyTv = itemView.findViewById(R.id.currencyTv);
                itemCurrencyCh = itemView.findViewById(R.id.currencyCh);
                itemCurrencyLine = itemView.findViewById(R.id.currencyLine);

            }
        }
    }


}