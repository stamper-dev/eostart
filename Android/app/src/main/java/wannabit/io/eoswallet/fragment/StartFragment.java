package wannabit.io.eoswallet.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.WellcomeActivity;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.utils.WLog;

public class StartFragment extends BaseFragment {

    private ImageView gifImage;
    private Button mRestoreBtn, mNewBtn;


    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        gifImage    = rootView.findViewById(R.id.startEosLogo);
        mRestoreBtn    = rootView.findViewById(R.id.btn_restore);
        mNewBtn    = rootView.findViewById(R.id.btn_new);

        Glide.with(this).load(R.drawable.logo_eos).asGif().into(new SimpleTarget<GifDrawable>() {
            @Override
            public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                resource.start();
                gifImage.setImageDrawable(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
            }
        });

        mRestoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPActivity().onStartImportAccount();
            }
        });

        mNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPActivity().onStartNewAccount();
            }
        });


        return rootView;
    }

    private WellcomeActivity getPActivity() {
        return (WellcomeActivity)getActivity();


    }
}
