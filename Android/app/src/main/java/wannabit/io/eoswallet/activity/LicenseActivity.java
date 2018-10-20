package wannabit.io.eoswallet.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseActivity;
import wannabit.io.eoswallet.model.WBOpenSrc;

public class LicenseActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    ArrayList<WBOpenSrc> mOpenSrcs = new ArrayList<>();
    OpenSrcAdapter mOpenSrcAdapter;
    private  int mInitPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_src);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);

        mToolbarTitle.setText(R.string.str_service_licenses);
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

        ArrayList<String> openSrcList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.support_openSrc)));
        mOpenSrcs.clear();
        for(int i = 0; i < openSrcList.size(); i++) {
            String[] opentmp = openSrcList.get(i).split("/");
            WBOpenSrc tmp = new WBOpenSrc(i, opentmp[0], opentmp[1]);
            mOpenSrcs.add(tmp);
        }

        mOpenSrcAdapter = new OpenSrcAdapter(mOpenSrcs);
        recyclerView.setAdapter(mOpenSrcAdapter);
    }


    class OpenSrcAdapter extends RecyclerView.Adapter<OpenSrcAdapter.OpenSrcAdapterItemHolder> {

        ArrayList<WBOpenSrc> openSrcs = new ArrayList<>();

        public OpenSrcAdapter(ArrayList<WBOpenSrc> srcs) { this.openSrcs =  srcs; }

        @Override
        public int getItemCount() {
            return openSrcs.size();
        }

        @NonNull
        @Override
        public OpenSrcAdapterItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.item_open_src, viewGroup, false);
            return new OpenSrcAdapterItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull OpenSrcAdapterItemHolder holder, int position) {
            WBOpenSrc openSrc = openSrcs.get(position);

            holder.itemOpenSrcName.setText(openSrc.getOpenSrcName());
            holder.itemOpenSrcLicenseName.setText(openSrc.getLicenseType());

            if(position == openSrcs.size() - 1 ) {
                holder.itemOpenSrcLine.setVisibility(View.GONE);
            } else {
                holder.itemOpenSrcLine.setVisibility(View.VISIBLE);
            }

        }

        public class OpenSrcAdapterItemHolder extends RecyclerView.ViewHolder{
            TextView itemOpenSrcName;
            TextView itemOpenSrcLicenseName;
            View itemOpenSrcLine;

            public OpenSrcAdapterItemHolder(View v) {
                super(v);
                itemOpenSrcName = itemView.findViewById(R.id.openSrcNameTvT);
                itemOpenSrcLicenseName = itemView.findViewById(R.id.lecenseNameTvT);
                itemOpenSrcLine = itemView.findViewById(R.id.openSrcLine);
            }
        }
    }


    class ResourceHelper {

    }
}
