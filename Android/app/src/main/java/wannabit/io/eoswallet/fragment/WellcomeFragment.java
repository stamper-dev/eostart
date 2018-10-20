package wannabit.io.eoswallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.WellcomeActivity;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.utils.WLog;

public class WellcomeFragment extends BaseFragment {

    public static WellcomeFragment newInstance(Bundle bundle) {
        WellcomeFragment fragment = new WellcomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wellcome, container, false);
        ImageView wellcomeImg           = rootView.findViewById(R.id.wellcomeImg);
        TextView wellcomeTitle           = rootView.findViewById(R.id.wellcomeTitle);
        TextView wellcomeMsg           = rootView.findViewById(R.id.wellcomeMsg);

        if (getArguments().getInt("page") == 0) {
            wellcomeImg.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_img1));
            wellcomeTitle.setText(getString(R.string.str_wellcome_title0));
            wellcomeMsg.setText(getString(R.string.str_wellcome_msg0));

        } else if (getArguments().getInt("page") == 1) {
            wellcomeImg.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_img2));
            wellcomeTitle.setText(getString(R.string.str_wellcome_title1));
            wellcomeMsg.setText(getString(R.string.str_wellcome_msg1));

        } else if (getArguments().getInt("page") == 2) {
            wellcomeImg.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_img3));
            wellcomeTitle.setText(getString(R.string.str_wellcome_title0));
            wellcomeMsg.setText(getString(R.string.str_wellcome_msg2));

        }

        return rootView;
    }
}
