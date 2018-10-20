package wannabit.io.eoswallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.view.SquareButton;
import wannabit.io.eoswallet.view.SquareImageButton;

public class PasscodeNumberFragment extends KeyboardFragment implements View.OnClickListener{

    private View                mRootView;
    private SquareButton[]      mSquareButtons = new SquareButton[10];
    private SquareImageButton   mBackButton;
    private ArrayList<String>   mNumberArray = new ArrayList<>();
    private PasscodeListener    mListner;

    private Animation fadeOutAni, fadeInAnin;

    public static PasscodeNumberFragment newInstance() {
        PasscodeNumberFragment fragment = new PasscodeNumberFragment();
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
                Collections.shuffle(mNumberArray, new Random(System.nanoTime()));
                for(int i = 0; i < mSquareButtons.length; i++) {
                    mSquareButtons[i] = mRootView.findViewById(getResources().getIdentifier("pincode_number" + i , "id", getBaseActivity().getPackageName()));
                    mSquareButtons[i].setText(mNumberArray.get(i));
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
        mRootView = inflater.inflate(R.layout.fragment_passcode_number, container, false);
        mNumberArray = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.passcode_number)));
        Collections.shuffle(mNumberArray, new Random(System.nanoTime()));
        for(int i = 0; i < mSquareButtons.length; i++) {
            mSquareButtons[i] = mRootView.findViewById(getResources().getIdentifier("pincode_number" + i , "id", getBaseActivity().getPackageName()));
            mSquareButtons[i].setText(mNumberArray.get(i));
            mSquareButtons[i].setOnClickListener(this);
        }

        mBackButton = mRootView.findViewById(R.id.pincode_back);
        mBackButton.setOnClickListener(this);
        return mRootView;
    }


    @Override
    public void onClick(View view) {
        if(view instanceof SquareButton) {
            if(mListner != null) {
                mListner.onUserInsertKey(((SquareButton)view).getText().toString().trim().toCharArray()[0]);
            }

        } else if (view instanceof SquareImageButton) {
            if(mListner != null) {
                mListner.onUserDeleteKey();
            }
        }

    }
}
