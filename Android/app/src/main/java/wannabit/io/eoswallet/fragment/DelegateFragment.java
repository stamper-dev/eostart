package wannabit.io.eoswallet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.PasscodeActivity;
import wannabit.io.eoswallet.activity.SendActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.dialog.DialogDelegateReceipt;
import wannabit.io.eoswallet.dialog.DialogTradeRamEos;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ResAccountInfo;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class DelegateFragment extends BaseFragment implements View.OnClickListener{

    public final static int DELEGATE_DIALOG = 6000;

    private TextView        mAvailable;
    private EditText        mAmount;
    private LinearLayout    mAmountBtns;
    private Button          mAmount0, mAmount1, mAmount2, mAmount3, mAmount4;

    private LinearLayout    mSeekLayer;
    private RelativeLayout  mSeekBg;
    private TextView        mCpuRate, mCpuAmount, mCpuResult;
    private TextView        mNetRate, mNetAmout, mNetResult;
    private SeekBar         mSeekbar;
    private Button          mRate0, mRate1, mRate2, mRate3, mRate4;

    private Button          mConfirm;

    private WBUser          mUser;
    private ResAccountInfo  mUserInfo;
    private BigDecimal      mAvailableAmount, mToDelegateAmout, mToCpuAmout, mToNetAmout;

    private BigDecimal      mTotalCpu, mTotalNet;

    private boolean         mTargetSetted = false;

    public static DelegateFragment newInstance() {
        DelegateFragment fragment = new DelegateFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delegate, container, false);

        mAvailable      = rootView.findViewById(R.id.delegateAvailable);
        mAmount         = rootView.findViewById(R.id.delegateAmount);
        mAmountBtns     = rootView.findViewById(R.id.delegateAmountBtns);
        mAmount0        = rootView.findViewById(R.id.delegate_amount_0);
        mAmount1        = rootView.findViewById(R.id.delegate_amount_1);
        mAmount2        = rootView.findViewById(R.id.delegate_amount_2);
        mAmount3        = rootView.findViewById(R.id.delegate_amount_3);
        mAmount4        = rootView.findViewById(R.id.delegate_amount_4);

        mSeekLayer      = rootView.findViewById(R.id.seekLayer);
        mSeekBg         = rootView.findViewById(R.id.delegate_seekbarBg);
        mCpuRate        = rootView.findViewById(R.id.cpuPercent);
        mCpuAmount      = rootView.findViewById(R.id.cpuAmount);
        mCpuResult      = rootView.findViewById(R.id.cpuResult);
        mNetRate        = rootView.findViewById(R.id.netPercent);
        mNetAmout       = rootView.findViewById(R.id.netAmount);
        mNetResult      = rootView.findViewById(R.id.netResult);
        mSeekbar        = rootView.findViewById(R.id.delegate_seekbar);
        mRate0          = rootView.findViewById(R.id.quickSeek0);
        mRate1          = rootView.findViewById(R.id.quickSeek1);
        mRate2          = rootView.findViewById(R.id.quickSeek2);
        mRate3          = rootView.findViewById(R.id.quickSeek3);
        mRate4          = rootView.findViewById(R.id.quickSeek4);


        mConfirm        = rootView.findViewById(R.id.btn_confirm);

        mAmount0.setOnClickListener(this);
        mAmount1.setOnClickListener(this);
        mAmount2.setOnClickListener(this);
        mAmount3.setOnClickListener(this);
        mAmount4.setOnClickListener(this);

        mRate0.setOnClickListener(this);
        mRate1.setOnClickListener(this);
        mRate2.setOnClickListener(this);
        mRate3.setOnClickListener(this);
        mRate4.setOnClickListener(this);

        mConfirm.setOnClickListener(this);

        mAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mTargetSetted = false;
                    onDetailViewUpdate();
                    mToDelegateAmout = mToCpuAmout = mToNetAmout = BigDecimal.ZERO;
                    mAmount.setText("");

                }
            }
        });

        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("00")) {
                    if (enteredString.length() > 0) {
                        mAmount.setText(enteredString.substring(1));
                        mAmount.setSelection(1);
                    } else {
                        mAmount.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().replace(",","").trim();
                if(mAmount.getTag() == null) {
                    if(TextUtils.isEmpty(input)) {
                        mAmount.setBackground(getActivity().getDrawable(R.drawable.edittext_correct));
                        mConfirm.setEnabled(false);

                    } else if (!input.endsWith(".") && !input.startsWith(".")) {
                        mToDelegateAmout = new BigDecimal(input).setScale(4, BigDecimal.ROUND_FLOOR);
                        if(mAvailableAmount.compareTo(mToDelegateAmout) >= 0) {
                            mAmount.setBackground(getActivity().getDrawable(R.drawable.edittext_correct));
                            mConfirm.setEnabled(true);
                        } else {
                            mAmount.setBackground(getActivity().getDrawable(R.drawable.edittext_red));
                            mConfirm.setEnabled(false);
                        }
                        mAmount.setSelection(input.length());
                    } else {
                        mAmount.setBackground(getActivity().getDrawable(R.drawable.edittext_red));
                        mConfirm.setEnabled(false);
                    }

                } else {
                    mAmount.setTag(null);
                    mAmount.setText(mToDelegateAmout.toString());
                }
            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0) {
                    mToCpuAmout = mToDelegateAmout;
                    mToNetAmout = BigDecimal.ZERO;

                } else {
                    mToNetAmout = mToDelegateAmout.multiply(new BigDecimal(""+progress)).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_UP);
                    mToCpuAmout = mToDelegateAmout.subtract(mToNetAmout);
                }
                mCpuRate.setText("" + (100 - progress) + "%");
                mNetRate.setText("" + progress + "%");
                mCpuAmount.setText(WUtil.signAmoutSpanFormat(mToCpuAmout.doubleValue(), 0.8f));
                mNetAmout.setText(WUtil.signAmoutSpanFormat(mToNetAmout.doubleValue(), 0.8f));

                mCpuResult.setText(WUtil.unSignAmoutSpanFormat(mTotalCpu.add(mToCpuAmout).doubleValue(), 0.8f));
                mNetResult.setText(WUtil.unSignAmoutSpanFormat(mTotalNet.add(mToNetAmout).doubleValue(), 0.8f));

                onUpdateSeekbarBg(progress);
            }
        });

        onDetailViewUpdate();
        onInitData();
        return rootView;
    }


    private void onInitData() {
        Long lastUserId = getBaseDao().getRecentAccountId();
        ArrayList<WBUser> registedUsers = getBaseDao().onSelectAllUser();
        if (registedUsers == null || registedUsers.size() < 1) {
            getBaseActivity().finish();
        } else if (lastUserId < 0) {
            mUser = registedUsers.get(0);

        } else {
            mUser = registedUsers.get(0);
            for (WBUser user : registedUsers) {
                if (user.getId() == lastUserId) {
                    mUser = user;
                    break;
                }
            }
        }

        mUserInfo = getBaseDao().getLastUserInfo();
        mAvailableAmount = new BigDecimal(mUserInfo.getTotalUnstakedAmout().toString());
        mAvailable.setText(TextUtils.concat(getString(R.string.str_available), WUtil.EOSAmoutSpanFormat(mUserInfo.getTotalUnstakedAmout(), 0.8f)));

        mTotalCpu = new BigDecimal(mUserInfo.getTotal_resources().getCpu_weight().replace("EOS", "").replace(" ", ""));
        mTotalNet = new BigDecimal(mUserInfo.getTotal_resources().getNet_weight().replace("EOS", "").replace(" ", ""));
    }


    private void onDetailViewUpdate() {
        mConfirm.setEnabled(false);
        if(mTargetSetted) {
            mConfirm.setText(R.string.str_confirm);
            mAmountBtns.setVisibility(View.GONE);

            mSeekbar.setProgress(50);
            onUpdateSeekbarBg(50);
            mToCpuAmout = mToDelegateAmout.divide(new BigDecimal("2"), 4, BigDecimal.ROUND_UP);
            mToNetAmout = mToDelegateAmout.subtract(mToCpuAmout);
            mCpuRate.setText("50%");
            mNetRate.setText("50%");
            mCpuAmount.setText(WUtil.signAmoutSpanFormat(mToCpuAmout.doubleValue(), 0.8f));
            mNetAmout.setText(WUtil.signAmoutSpanFormat(mToNetAmout.doubleValue(), 0.8f));

            mCpuResult.setText(WUtil.unSignAmoutSpanFormat(mTotalCpu.add(mToCpuAmout).doubleValue(), 0.8f));
            mNetResult.setText(WUtil.unSignAmoutSpanFormat(mTotalNet.add(mToNetAmout).doubleValue(), 0.8f));

            mSeekLayer.setVisibility(View.VISIBLE);
            mConfirm.setEnabled(true);


        } else {
            mConfirm.setText(R.string.str_next);
            mAmountBtns.setVisibility(View.VISIBLE);
            mSeekLayer.setVisibility(View.INVISIBLE);

        }
    }

    private void onUpdateSeekbarBg(int progress) {
        if(progress == 10) {
            mSeekBg.setBackground(getActivity().getDrawable(R.drawable.seekbar_1));
        } else if (progress == 30) {
            mSeekBg.setBackground(getActivity().getDrawable(R.drawable.seekbar_2));
        } else if (progress == 50) {
            mSeekBg.setBackground(getActivity().getDrawable(R.drawable.seekbar_3));
        } else if (progress == 70) {
            mSeekBg.setBackground(getActivity().getDrawable(R.drawable.seekbar_4));
        } else if (progress == 90) {
            mSeekBg.setBackground(getActivity().getDrawable(R.drawable.seekbar_5));
        } else {
            mSeekBg.setBackground(getActivity().getDrawable(R.drawable.seekbar_none));
        }

    }

    @Override
    public void onClick(View v) {
        if(v.equals(mConfirm)) {
            if(mTargetSetted) {
                Bundle bundle = new Bundle();
                bundle.putDouble("BeforeBalance", mAvailableAmount.doubleValue());
                bundle.putDouble("ChangeBalance", (mToCpuAmout.add(mToNetAmout)).negate().doubleValue());
                bundle.putDouble("ResultBalance", mAvailableAmount.subtract(mToCpuAmout).subtract(mToNetAmout).doubleValue());

                bundle.putDouble("BeforeCpu", mTotalCpu.doubleValue());
                bundle.putDouble("ChangeCpu", mToCpuAmout.doubleValue());
                bundle.putDouble("ResultCpu", mTotalCpu.add(mToCpuAmout).doubleValue());

                bundle.putDouble("BeforeNet", mTotalNet.doubleValue());
                bundle.putDouble("ChangeNet", mToNetAmout.doubleValue());
                bundle.putDouble("ResultNet", mTotalNet.add(mToNetAmout).doubleValue());

                DialogDelegateReceipt dialog  = DialogDelegateReceipt.newInstance(bundle);
                dialog.setCancelable(true);
                dialog.setTargetFragment(this, DELEGATE_DIALOG);
                dialog.show(getFragmentManager().beginTransaction(), "dialog");

            } else {
                getBaseActivity().onHideKeyboard();
                mAmount.clearFocus();
                mAmount.setTag("Check");
                mAmount.setText(mToDelegateAmout.toString());
                mTargetSetted = true;
                onDetailViewUpdate();
            }

        } else if (v.equals(mAmount0)) {
            mAmount.requestFocus();
            mAmount.setTag("Check");
            mToDelegateAmout = mAvailableAmount.divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mAmount.setText(mToDelegateAmout.toString());


        } else if (v.equals(mAmount1)) {
            mAmount.requestFocus();
            mAmount.setTag("Check");
            mToDelegateAmout = mAvailableAmount.multiply(new BigDecimal("3")).divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mAmount.setText(mToDelegateAmout.toString());


        } else if (v.equals(mAmount2)) {
            mAmount.requestFocus();
            mAmount.setTag("Check");
            mToDelegateAmout = mAvailableAmount.divide(new BigDecimal("2"), 4, BigDecimal.ROUND_UP);
            mAmount.setText(mToDelegateAmout.toString());


        } else if (v.equals(mAmount3)) {
            mAmount.requestFocus();
            mAmount.setTag("Check");
            mToDelegateAmout = mAvailableAmount.multiply(new BigDecimal("7")).divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mAmount.setText(mToDelegateAmout.toString());


        } else if (v.equals(mAmount4)) {
            mAmount.requestFocus();
            mAmount.setTag("Check");
            mToDelegateAmout = mAvailableAmount;
            mAmount.setText(mToDelegateAmout.toString());

        } else if (v.equals(mRate0)) {
            mSeekbar.setProgress(10);

        } else if (v.equals(mRate1)) {
            mSeekbar.setProgress(30);

        } else if (v.equals(mRate2)) {
            mSeekbar.setProgress(50);

        } else if (v.equals(mRate3)) {
            mSeekbar.setProgress(70);

        } else if (v.equals(mRate4)) {
            mSeekbar.setProgress(90);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DELEGATE_DIALOG) {
            Intent intent = new Intent(getActivity(), PasscodeActivity.class);
            intent.putExtra(BaseConstant.CONST_KEY_TARGET, BaseConstant.CONST_DELEGATE);
            intent.putExtra("account", mUser.getAccount());
            intent.putExtra("targetNet", mToNetAmout.setScale(4).toString() + " EOS");
            intent.putExtra("targetCpu", mToCpuAmout.setScale(4).toString() + " EOS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }
}
