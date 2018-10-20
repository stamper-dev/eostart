package wannabit.io.eoswallet.fragment;

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
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.activity.PasscodeActivity;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.base.BaseFragment;
import wannabit.io.eoswallet.dialog.DialogDelegateReceipt;
import wannabit.io.eoswallet.dialog.DialogUndelegateReceipt;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ResAccountInfo;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;

public class UndelegateFragment extends BaseFragment implements View.OnClickListener{

    public final static int UNDELEGATE_DIALOG = 6001;

    private static final BigDecimal MIN_RESOURCE = new BigDecimal("0.1");

    private TextView mUnDeleCpuAvailableTv, mUnDeleCpuTotalTv;
    private EditText mUnDeleCpuAmountEt;
    private Button   mUnDeleAmoutCpu0Btn, mUnDeleAmoutCpu1Btn, mUnDeleAmoutCpu2Btn, mUnDeleAmoutCpu3Btn, mUnDeleAmoutCpu4Btn;

    private TextView mUnDeleNetAvailableTv, mUnDeleNetTotalTv;
    private EditText mUnDeleNetAmountEt;
    private Button   mUnDeleAmoutNet0Btn, mUnDeleAmoutNet1Btn, mUnDeleAmoutNet2Btn, mUnDeleAmoutNet3Btn, mUnDeleAmoutNet4Btn;

    private Button   mConfirm;



    private WBUser          mUser;
    private ResAccountInfo  mUserInfo;

    private BigDecimal      mTotalCpu;
    private BigDecimal      mSelfCpu;
    private BigDecimal      mTotalNet;
    private BigDecimal      mSelfNet;
    private BigDecimal      mAvaiableCpu;
    private BigDecimal      mAvaiableNet;

    private BigDecimal      mTargetCpu, mTargetNet;

    public static UndelegateFragment newInstance() {
        UndelegateFragment fragment = new UndelegateFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frgament_undelegate, container, false);
        mUnDeleCpuAvailableTv = rootView.findViewById(R.id.undelegateCpuAvailable);
        mUnDeleCpuTotalTv = rootView.findViewById(R.id.undelegateCpuTotal);
        mUnDeleCpuAmountEt = rootView.findViewById(R.id.undelegateCpuAmount);
        mUnDeleAmoutCpu0Btn = rootView.findViewById(R.id.undelegateCpu_amount_0);
        mUnDeleAmoutCpu1Btn = rootView.findViewById(R.id.undelegateCpu_amount_1);
        mUnDeleAmoutCpu2Btn = rootView.findViewById(R.id.undelegateCpu_amount_2);
        mUnDeleAmoutCpu3Btn = rootView.findViewById(R.id.undelegateCpu_amount_3);
        mUnDeleAmoutCpu4Btn = rootView.findViewById(R.id.undelegateCpu_amount_4);
        mUnDeleNetAvailableTv = rootView.findViewById(R.id.undelegateNetAvailable);
        mUnDeleNetTotalTv = rootView.findViewById(R.id.undelegateNetTotal);
        mUnDeleNetAmountEt = rootView.findViewById(R.id.undelegateNetAmount);
        mUnDeleAmoutNet0Btn = rootView.findViewById(R.id.undelegateNet_amount_0);
        mUnDeleAmoutNet1Btn = rootView.findViewById(R.id.undelegateNet_amount_1);
        mUnDeleAmoutNet2Btn = rootView.findViewById(R.id.undelegateNet_amount_2);
        mUnDeleAmoutNet3Btn = rootView.findViewById(R.id.undelegateNet_amount_3);
        mUnDeleAmoutNet4Btn = rootView.findViewById(R.id.undelegateNet_amount_4);
        mConfirm = rootView.findViewById(R.id.btn_confirm);

        mUnDeleAmoutCpu0Btn.setOnClickListener(this);
        mUnDeleAmoutCpu1Btn.setOnClickListener(this);
        mUnDeleAmoutCpu2Btn.setOnClickListener(this);
        mUnDeleAmoutCpu3Btn.setOnClickListener(this);
        mUnDeleAmoutCpu4Btn.setOnClickListener(this);
        mUnDeleAmoutNet0Btn.setOnClickListener(this);
        mUnDeleAmoutNet1Btn.setOnClickListener(this);
        mUnDeleAmoutNet2Btn.setOnClickListener(this);
        mUnDeleAmoutNet3Btn.setOnClickListener(this);
        mUnDeleAmoutNet4Btn.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        mUnDeleCpuAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("00")) {
                    if (enteredString.length() > 0) {
                        mUnDeleCpuAmountEt.setText(enteredString.substring(1));
                        mUnDeleCpuAmountEt.setSelection(1);
                    } else {
                        mUnDeleCpuAmountEt.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().replace(",","").trim();
                if(mUnDeleCpuAmountEt.getTag() == null) {
                    if(TextUtils.isEmpty(input)) {
                        mUnDeleCpuAmountEt.setBackground(getActivity().getDrawable(R.drawable.edittext_correct));

                    } else if (!input.endsWith(".") && !input.startsWith(".")) {
                        mTargetCpu = new BigDecimal(input).setScale(4, BigDecimal.ROUND_FLOOR);
                        if(mAvaiableCpu.compareTo(mTargetCpu) >= 0) {
                            mUnDeleCpuAmountEt.setBackground(getActivity().getDrawable(R.drawable.edittext_correct));
                        } else {
                            mUnDeleCpuAmountEt.setBackground(getActivity().getDrawable(R.drawable.edittext_red));
                        }
                        mUnDeleCpuAmountEt.setSelection(input.length());
                    } else {
                        mUnDeleCpuAmountEt.setBackground(getActivity().getDrawable(R.drawable.edittext_red));
                    }

                } else {
                    mUnDeleCpuAmountEt.setTag(null);
                    mUnDeleCpuAmountEt.setText(mTargetCpu.toString());
                }
                onCheckConfirm();
            }
        });

        mUnDeleNetAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("00")) {
                    if (enteredString.length() > 0) {
                        mUnDeleNetAmountEt.setText(enteredString.substring(1));
                        mUnDeleNetAmountEt.setSelection(1);
                    } else {
                        mUnDeleNetAmountEt.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().replace(",","").trim();
                if(mUnDeleNetAmountEt.getTag() == null) {
                    if(TextUtils.isEmpty(input)) {
                        mUnDeleNetAmountEt.setBackground(getActivity().getDrawable(R.drawable.edittext_correct));

                    } else if (!input.endsWith(".") && !input.startsWith(".")) {
                        mTargetNet = new BigDecimal(input).setScale(4, BigDecimal.ROUND_FLOOR);
                        if(mAvaiableNet.compareTo(mTargetNet) >= 0) {
                            mUnDeleNetAmountEt.setBackground(getActivity().getDrawable(R.drawable.edittext_correct));
                        } else {
                            mUnDeleNetAmountEt.setBackground(getActivity().getDrawable(R.drawable.edittext_red));
                        }
                        mUnDeleNetAmountEt.setSelection(input.length());
                    } else {
                        mUnDeleNetAmountEt.setBackground(getActivity().getDrawable(R.drawable.edittext_red));
                    }

                } else {
                    mUnDeleNetAmountEt.setTag(null);
                    mUnDeleNetAmountEt.setText(mTargetNet.toString());
                }
                onCheckConfirm();
            }
        });


        onInitData();
        return rootView;
    }


    private void onInitData() {
        Long lastUserId = getBaseDao().getRecentAccountId();
        ArrayList<WBUser> registedUsers  = getBaseDao().onSelectAllUser();
        if(registedUsers == null || registedUsers.size() < 1) {
            getBaseActivity().finish();
        } else if (lastUserId < 0) {
            mUser = registedUsers.get(0);

        } else {
            mUser = registedUsers.get(0);
            for(WBUser user:registedUsers) {
                if(user.getId() == lastUserId) {
                    mUser = user;
                    break;
                }
            }
        }
        mUserInfo = getBaseDao().getLastUserInfo();

        mTotalCpu = BigDecimal.ZERO;
        mSelfCpu = BigDecimal.ZERO;
        mTotalNet = BigDecimal.ZERO;
        mSelfNet = BigDecimal.ZERO;
        mAvaiableCpu = BigDecimal.ZERO;
        mAvaiableNet = BigDecimal.ZERO;
        mTargetCpu = BigDecimal.ZERO;
        mTargetNet = BigDecimal.ZERO;

        if(mUserInfo.getTotal_resources() != null) {
            if(!TextUtils.isEmpty(mUserInfo.getTotal_resources().getCpu_weight()) &&
                    mUserInfo.getTotal_resources().getCpu_weight().endsWith("EOS")) {
                mTotalCpu = new BigDecimal(mUserInfo.getTotal_resources().getCpu_weight().replace("EOS", "").replace(" ", ""));
            }


            if(!TextUtils.isEmpty(mUserInfo.getTotal_resources().getNet_weight()) &&
                    mUserInfo.getTotal_resources().getNet_weight().endsWith("EOS")) {
                mTotalNet = new BigDecimal(mUserInfo.getTotal_resources().getNet_weight().replace("EOS", "").replace(" ", ""));
            }
        }

        if(mUserInfo.getSelf_delegated_bandwidth() != null) {
            if(!TextUtils.isEmpty(mUserInfo.getSelf_delegated_bandwidth().getCpu_weight()) &&
                    mUserInfo.getSelf_delegated_bandwidth().getCpu_weight().endsWith("EOS")) {
                mSelfCpu = new BigDecimal(mUserInfo.getSelf_delegated_bandwidth().getCpu_weight().replace("EOS", "").replace(" ", ""));
            }

            if(!TextUtils.isEmpty(mUserInfo.getSelf_delegated_bandwidth().getNet_weight()) &&
                    mUserInfo.getSelf_delegated_bandwidth().getNet_weight().endsWith("EOS")) {
                mSelfNet = new BigDecimal(mUserInfo.getSelf_delegated_bandwidth().getNet_weight().replace("EOS", "").replace(" ", ""));
            }
        }

        if(mSelfCpu.compareTo(BigDecimal.ZERO) == 0) {
            mAvaiableCpu = BigDecimal.ZERO;
        } else if(mTotalCpu.subtract(mSelfCpu).compareTo(MIN_RESOURCE) >= 0) {
            mAvaiableCpu = mSelfCpu;
        } else {
            mAvaiableCpu = mSelfCpu.add(mTotalCpu.subtract(mSelfCpu).subtract(MIN_RESOURCE));
        }

        if(mSelfNet.compareTo(BigDecimal.ZERO) == 0) {
            mAvaiableNet = BigDecimal.ZERO;
        } else if(mTotalNet.subtract(mSelfNet).compareTo(MIN_RESOURCE) >= 0) {
            mAvaiableNet = mSelfNet;
        } else {
            mAvaiableNet = mSelfNet.add(mTotalNet.subtract(mSelfNet).subtract(MIN_RESOURCE));
        }

        mUnDeleCpuAvailableTv.setText(TextUtils.concat(getString(R.string.str_available), WUtil.EOSAmoutSpanFormat(mAvaiableCpu.doubleValue(), 0.8f)));
        mUnDeleCpuTotalTv.setText(TextUtils.concat(getString(R.string.str_total), WUtil.EOSAmoutSpanFormat(mTotalCpu.doubleValue(), 0.8f)));

        mUnDeleNetAvailableTv.setText(TextUtils.concat(getString(R.string.str_available), WUtil.EOSAmoutSpanFormat(mAvaiableNet.doubleValue(), 0.8f)));
        mUnDeleNetTotalTv.setText(TextUtils.concat(getString(R.string.str_total), WUtil.EOSAmoutSpanFormat(mTotalNet.doubleValue(), 0.8f)));


    }

    private void onCheckConfirm() {
        if(mTargetCpu.compareTo(BigDecimal.ZERO) <= 0 && mTargetNet.compareTo(BigDecimal.ZERO) <= 0) {
            mConfirm.setEnabled(false);
            return;
        }
        if(mAvaiableCpu.compareTo(mTargetCpu) < 0 ||
                mAvaiableNet.compareTo(mTargetNet) < 0) {
            mConfirm.setEnabled(false);
            return;
        }
        mConfirm.setEnabled(true);

    }


    @Override
    public void onClick(View v) {
        if (v.equals(mUnDeleAmoutCpu0Btn)) {
            mTargetCpu = mAvaiableCpu.divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mUnDeleCpuAmountEt.setTag("Check");
            mUnDeleCpuAmountEt.setText(mTargetCpu.toString());

        } else if (v.equals(mUnDeleAmoutCpu1Btn)) {
            mTargetCpu = mAvaiableCpu.multiply(new BigDecimal("3")).divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mUnDeleCpuAmountEt.setTag("Check");
            mUnDeleCpuAmountEt.setText(mTargetCpu.toString());

        } else if (v.equals(mUnDeleAmoutCpu2Btn)) {
            mTargetCpu = mAvaiableCpu.divide(new BigDecimal("2"), 4, BigDecimal.ROUND_UP);
            mUnDeleCpuAmountEt.setTag("Check");
            mUnDeleCpuAmountEt.setText(mTargetCpu.toString());

        } else if (v.equals(mUnDeleAmoutCpu3Btn)) {
            mTargetCpu = mAvaiableCpu.multiply(new BigDecimal("7")).divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mUnDeleCpuAmountEt.setTag("Check");
            mUnDeleCpuAmountEt.setText(mTargetCpu.toString());

        } else if (v.equals(mUnDeleAmoutCpu4Btn)) {
            mTargetCpu = mAvaiableCpu;
            mUnDeleCpuAmountEt.setTag("Check");
            mUnDeleCpuAmountEt.setText(mTargetCpu.toString());

        } else if (v.equals(mUnDeleAmoutNet0Btn)) {
            mTargetNet = mAvaiableNet.divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mUnDeleNetAmountEt.setTag("Check");
            mUnDeleNetAmountEt.setText(mTargetNet.toString());

        } else if (v.equals(mUnDeleAmoutNet1Btn)) {
            mTargetNet = mAvaiableNet.multiply(new BigDecimal("3")).divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mUnDeleNetAmountEt.setTag("Check");
            mUnDeleNetAmountEt.setText(mTargetNet.toString());

        } else if (v.equals(mUnDeleAmoutNet2Btn)) {
            mTargetNet = mAvaiableNet.divide(new BigDecimal("2"), 4, BigDecimal.ROUND_UP);
            mUnDeleNetAmountEt.setTag("Check");
            mUnDeleNetAmountEt.setText(mTargetNet.toString());

        } else if (v.equals(mUnDeleAmoutNet3Btn)) {
            mTargetNet = mAvaiableNet.multiply(new BigDecimal("7")).divide(new BigDecimal("10"), 4, BigDecimal.ROUND_UP);
            mUnDeleNetAmountEt.setTag("Check");
            mUnDeleNetAmountEt.setText(mTargetNet.toString());

        } else if (v.equals(mUnDeleAmoutNet4Btn)) {
            mTargetNet = mAvaiableNet;
            mUnDeleNetAmountEt.setTag("Check");
            mUnDeleNetAmountEt.setText(mTargetNet.toString());

        } else if (v.equals(mConfirm)) {
            Bundle bundle = new Bundle();
            bundle.putDouble("BeforeCpu", mTotalCpu.doubleValue());
            bundle.putDouble("ChangeCpu", mTargetCpu.negate().doubleValue());
            bundle.putDouble("ResultCpu", mTotalCpu.add(mTargetCpu.negate()).doubleValue());

            bundle.putDouble("BeforeNet", mTotalNet.doubleValue());
            bundle.putDouble("ChangeNet", mTargetNet.negate().doubleValue());
            bundle.putDouble("ResultNet", mTotalNet.add(mTargetNet.negate()).doubleValue());

            BigDecimal totalRefund = new BigDecimal(""+mUserInfo.getTotalRefundAmout());
            bundle.putDouble("BeforeRefund", totalRefund.doubleValue());
            bundle.putDouble("ChangeRefund", mTargetCpu.add(mTargetNet).doubleValue());
            bundle.putDouble("ResultRefund", totalRefund.add(mTargetCpu).add(mTargetNet).doubleValue());

            DialogUndelegateReceipt dialog  = DialogUndelegateReceipt.newInstance(bundle);
            dialog.setCancelable(true);
            dialog.setTargetFragment(this, UNDELEGATE_DIALOG);
            dialog.show(getFragmentManager().beginTransaction(), "dialog");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == UNDELEGATE_DIALOG) {
            Intent intent = new Intent(getActivity(), PasscodeActivity.class);
            intent.putExtra(BaseConstant.CONST_KEY_TARGET, BaseConstant.CONST_UNDELEGATE);
            intent.putExtra("account", mUser.getAccount());
            intent.putExtra("targetNet", mTargetNet.setScale(4).toString() + " EOS");
            intent.putExtra("targetCpu", mTargetCpu.setScale(4).toString() + " EOS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }
}