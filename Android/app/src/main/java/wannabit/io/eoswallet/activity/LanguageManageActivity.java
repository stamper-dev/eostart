package wannabit.io.eoswallet.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.model.WBLanguage;
import wannabit.io.eoswallet.utils.LocaleHelper;

public class LanguageManageActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    ArrayList<WBLanguage> mLanguages = new ArrayList<>();
    LanguageAdapter mLanguageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_manage);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);

        mToolbarTitle.setText(getString(R.string.str_language));
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

        ArrayList<String> supportList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.support_language)));

        mLanguages.clear();
        for(int i = 0; i < supportList.size() ; i ++) {
            WBLanguage temp = new WBLanguage(i, supportList.get(i), getBaseDao().getUserLanguage() == i ? true : false);
            mLanguages.add(temp);
        }
        mLanguageAdapter = new LanguageAdapter(mLanguages);
        recyclerView.setAdapter(mLanguageAdapter);
    }


    @Override
    public void onBackPressed() {
        if(mLanguageAdapter != null) {
            getBaseDao().setUserLanguage(mLanguageAdapter.getCheckedPosition());
        }
        super.onBackPressed();
    }


    class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageItemHolder>{

        ArrayList<WBLanguage> languages = new ArrayList<>();

        public LanguageAdapter(ArrayList<WBLanguage> l) {
            this.languages = l;
        }

        @Override
        public int getItemCount() {
            return languages.size();
        }

        @Override
        public LanguageItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.item_language, viewGroup, false);
            return new LanguageItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull LanguageItemHolder holder, final int position) {
            final WBLanguage language = languages.get(position);
            holder.itemLanguageTv.setText(language.getName());

            if(language.isChecked()) {
                holder.itemLanguageCh.setChecked(true);
                holder.itemLanguageCh.setClickable(false);
            } else {
                holder.itemLanguageCh.setChecked(false);
                holder.itemLanguageCh.setClickable(true);

            }

            holder.itemLanguageCh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isPressed()) {
                        onToggleBtns(position);
                    }
                }
            });

            holder.itemLanguageRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!language.isChecked()) {
                        onToggleBtns(position);
                    }
                }
            });

            if(position == languages.size()-1) {
                holder.itemLanguageLine.setVisibility(View.GONE);
            } else {
                holder.itemLanguageLine.setVisibility(View.VISIBLE);

            }
        }


        private void onToggleBtns(int position) {
            for(WBLanguage data:mLanguages) {
                data.setChecked(false);
            }
            mLanguages.get(position).setChecked(true);
            getBaseDao().setUserLanguage(mLanguageAdapter.getCheckedPosition());
            if(position == 0) {
                LocaleHelper.setLocale(LanguageManageActivity.this, Resources.getSystem().getConfiguration().locale.getLanguage());
            } else if (position == 1) {
                LocaleHelper.setLocale(LanguageManageActivity.this, "ko");
            } else if (position == 2) {
                LocaleHelper.setLocale(LanguageManageActivity.this, "en");
            }
            recreate();

        }

        private int getCheckedPosition() {
            int result = -1;
            for(int i = 0; i < languages.size() ; i ++) {
                if(languages.get(i).isChecked()) {
                    result = i;
                    break;
                }
            }
            return result;
        }


        public class LanguageItemHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemLanguageRoot;
            TextView itemLanguageTv;
            AppCompatCheckBox itemLanguageCh;
            View itemLanguageLine;

            public LanguageItemHolder(View v) {
                super(v);
                itemLanguageRoot = itemView.findViewById(R.id.languageRoot);
                itemLanguageTv = itemView.findViewById(R.id.languageTv);
                itemLanguageCh = itemView.findViewById(R.id.languageCh);
                itemLanguageLine = itemView.findViewById(R.id.languageLine);
            }

        }
    }
}
