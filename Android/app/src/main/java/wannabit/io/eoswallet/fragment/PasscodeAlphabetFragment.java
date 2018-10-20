package wannabit.io.eoswallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import wannabit.io.eoswallet.R;

public class PasscodeAlphabetFragment extends KeyboardFragment implements View.OnClickListener{

    private View                mRootView;
    private Button[]            mAlphaButton = new Button[26];
    private ImageButton         mBackButton;
    private ArrayList<String>   mAlphabetArray = new ArrayList<>();
    private PasscodeListener    mListner;

    private Animation fadeOutAni, fadeInAnin;

    public static PasscodeAlphabetFragment newInstance() {
        PasscodeAlphabetFragment fragment = new PasscodeAlphabetFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListener(PasscodeListener listener) {
        mListner = listener;
    }

    @Override
    public void onShuffeKeyboard() {
        fadeInAnin = AnimationUtils.loadAnimation(getBaseActivity(), R.anim.fade_in);
        fadeInAnin.reset();

        fadeOutAni = AnimationUtils.loadAnimation(getBaseActivity(), R.anim.fade_out);
        fadeOutAni.reset();
        fadeOutAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                Collections.shuffle(mAlphabetArray, new Random(System.nanoTime()));
                for(int i = 0; i < mAlphaButton.length; i++) {
                    mAlphaButton[i] = mRootView.findViewById(getResources().getIdentifier("pincode_alpha" + i , "id", getBaseActivity().getPackageName()));
                    mAlphaButton[i].setText(mAlphabetArray.get(i));
                }
                mRootView.startAnimation(fadeInAnin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        mRootView.startAnimation(fadeOutAni);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_passcode_alphabet, container, false);
        mAlphabetArray = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.passcode_alphabet)));
        Collections.shuffle(mAlphabetArray, new Random(System.nanoTime()));
        for(int i = 0; i < mAlphaButton.length; i++) {
            mAlphaButton[i] = mRootView.findViewById(getResources().getIdentifier("pincode_alpha" + i , "id", getBaseActivity().getPackageName()));
            mAlphaButton[i].setText(mAlphabetArray.get(i));
            mAlphaButton[i].setOnClickListener(this);
        }
        mBackButton = mRootView.findViewById(R.id.pincode_alpha_back);
        mBackButton.setOnClickListener(this);
        return mRootView;
    }

    @Override
    public void onClick(View view) {
        if(view instanceof Button) {
            if(mListner != null) {
                mListner.onUserInsertKey(((Button)view).getText().toString().trim().toCharArray()[0]);
            }

        } else if (view instanceof ImageButton) {
            if(mListner != null) {
                mListner.onUserDeleteKey();
            }
        }
    }
}
