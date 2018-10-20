package wannabit.io.eoswallet.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.SendActivity;
import wannabit.io.eoswallet.activity.WalletDetailActivity;
import wannabit.io.eoswallet.model.WBAction;
import wannabit.io.eoswallet.model.WBRecent;
import wannabit.io.eoswallet.utils.WUtil;

public class DialogRecentSent extends DialogFragment {

    private RecyclerView        recyclerView;

    private ArrayList<WBRecent> mRecentList;
    private RecentAdapter mRecentAdapter;

    public static DialogRecentSent newInstance(Bundle bundle) {
        DialogRecentSent frag = new DialogRecentSent();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView    = View.inflate(getContext(), R.layout.dialog_recentsent, null);
        recyclerView        = contentView.findViewById(R.id.recyclerView);
        mRecentList = getArguments().getParcelableArrayList("accounts");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecentAdapter(mRecentList));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView);
        return builder.create();
    }



    class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentItemHolder> {
        ArrayList<WBRecent> list = new ArrayList<>();


        public RecentAdapter(ArrayList<WBRecent> l) {
            this.list = l;
        }

        @Override
        public RecentItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.item_dialog_recent, viewGroup, false);
            return new RecentItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecentItemHolder holder, final int position) {
            final WBRecent recent = list.get(position);
            holder.itemRecentAccountTv.setText(recent.getAccount());
            holder.itemRecentDateTv.setText(WUtil.getTimeformat(getActivity(), recent.getDate()));
            holder.itemRecentRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SendActivity)getActivity()).onUpdateReceiverFromRecent(recent.getAccount());
                    getDialog().dismiss();

                }
            });

            if(position == list.size()-1) {
                holder.itemRecentLine.setVisibility(View.GONE);
                holder.itemRecentRoot.setBackground(getResources().getDrawable(R.drawable.white_box_ripple_bottom_round));
            } else {
                holder.itemRecentLine.setVisibility(View.VISIBLE);
                holder.itemRecentRoot.setBackground(getResources().getDrawable(R.drawable.white_box_ripple));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class RecentItemHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemRecentRoot;
            TextView itemRecentAccountTv, itemRecentDateTv;
            View itemRecentLine;

            public RecentItemHolder(View v) {
                super(v);
                itemRecentRoot          = itemView.findViewById(R.id.recentRoot);
                itemRecentAccountTv     = itemView.findViewById(R.id.recentAccountTv);
                itemRecentDateTv        = itemView.findViewById(R.id.recentDateTv);
                itemRecentLine          = itemView.findViewById(R.id.recentLine);
            }
        }

    }

}