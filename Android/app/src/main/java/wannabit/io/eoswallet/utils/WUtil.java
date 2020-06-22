package wannabit.io.eoswallet.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.base.BaseConstant;
import wannabit.io.eoswallet.crypto.AES256Cipher;
import wannabit.io.eoswallet.crypto.WannabitKeyStore;
import wannabit.io.eoswallet.crypto.ec.EosPrivateKey;
import wannabit.io.eoswallet.model.WBAction;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ReqPushTxn;
import wannabit.io.eoswallet.network.ResAbiToBin;
import wannabit.io.eoswallet.network.ResBlock;
import wannabit.io.eoswallet.network.ResCoinGecko;
import wannabit.io.eoswallet.network.ResEosTick;
import wannabit.io.eoswallet.type.TypeChainId;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class WUtil {

    public static boolean checkPasscodePattern(String pincode) {
        if(pincode.length() != 5)
            return false;
        String regex = "^\\d{4}+[A-Z]{1}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pincode);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public static boolean checkAccountPattern(String account){
        if(account.length() != 12)
            return false;
        String regex = "^[_a-z1-5]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(account);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public static boolean checkAccountDuringPattern(String account){
        if(account == null || account.length() == 0 ) return true;
        String regex = "^[_a-z1-5]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(account);
        boolean isNormal = m.matches();
        return isNormal;
    }


    public static boolean checkPrivateKeyPattern(String account){
        if(account.length() != 51)
            return false;
        String regex = "^[_a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(account);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public static String EOSAmoutFormat (Double amout) {
        String sPattern = "###,###,###,###,##0.0000";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        return decimalformat.format(amout) + " EOS";
    }

    public static SpannableString EOSAmoutSpanFormat(Double amout, float rate) {
        String sPattern = "###,###,###,###,###,###,###,##0.0000";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        String formated = decimalformat.format(amout) + " EOS";
        SpannableString result =  new SpannableString(formated);
        result.setSpan(new RelativeSizeSpan(rate), result.length() - 8, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString KBAmoutSpanFormat(Double amout) {
        String sPattern = "###,###,###,###,###,###,###,##0.00";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        String formated = decimalformat.format(amout / BaseConstant.CONSTANT_KBYTE) + " KB";
        SpannableString result =  new SpannableString(formated);
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - 5, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }



    public static SpannableString signAmoutSpanFormat(Double amout, float rate) {
        double realAmount = Math.abs(amout);
        if(realAmount < 0.0001)
            realAmount = 0;
        String sPattern = "###,###,###,###,###,###,###,##0.0000";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        String formated = decimalformat.format(realAmount);
        if(amout > 0) {
            formated = "+ " +formated;
        } else if (amout < 0) {
            formated = "- " +formated;
        }
        SpannableString result =  new SpannableString(formated);
        result.setSpan(new RelativeSizeSpan(rate), result.length() - 4, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString unSignAmoutSpanFormat(Double amout, float rate) {
        double realAmount = Math.abs(amout);
        if(realAmount < 0.0001)
            realAmount = 0;
        String sPattern = "###,###,###,###,###,###,###,##0.0000";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        String formated = decimalformat.format(realAmount);
        SpannableString result =  new SpannableString(formated);
        result.setSpan(new RelativeSizeSpan(rate), result.length() - 4, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString EOSAmoutSpanFormatBraket(String amount, float rate) {
        SpannableString result =  new SpannableString(amount);
        result.setSpan(new RelativeSizeSpan(rate), result.length() - 9, result.length()-1, SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }


    public static SpannableString AmountSpanFormat(Context c, Double amount, WBToken token) {
        final int decimal = Integer.parseInt(token.getDecimals());
        DecimalFormat decimalformat = getDecimalFormat(c, decimal);
        String formated = decimalformat.format(amount) + " " + token.getSymbol();
        SpannableString result =  new SpannableString(formated);
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - (decimal + 1 + token.getSymbol().length()), result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString AmountSpanFormatWithoutSymbol(Context c, Double amount, WBToken token) {
        final int decimal = Integer.parseInt(token.getDecimals());
        DecimalFormat decimalformat = getDecimalFormat(c, decimal);
        String formated = decimalformat.format(amount);
        SpannableString result =  new SpannableString(formated);
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - (decimal + 1), result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static String AmountFormat(Context c, Double amount, WBToken token) {
        return getDecimalFormat(c, Integer.parseInt(token.getDecimals())).format(amount) + " " + token.getSymbol();
    }

    public static SpannableString AmountSpanFormat(Context c, String amount, WBToken token) {
        Double value =  Double.parseDouble(amount.toLowerCase().replace(token.getSymbol().toLowerCase(), "").replace(",", "").trim());
        return AmountSpanFormat(c, value, token);
    }


    public static String AmountFormat(Context c, String amount, WBToken token) {
        Double value =  Double.parseDouble(amount.replace(token.getSymbol(), "").replace(",", "").trim());
        return AmountFormat(c, value, token);
    }

    public static String ValueFormat(Context c, String amount, WBToken token) {
        Double value =  Double.parseDouble(amount.replace(token.getSymbol(), "").replace(",", "").trim());
        return AmountFormat(c, value, token).replace(",","");
    }


    public static String ReplaceFormat(Context c, String input, WBToken token) {
        String userinput = input.trim().replace(",","");
        if(TextUtils.isEmpty(userinput)) {
            userinput = "0";
        } else if (userinput.startsWith(".")) {
            userinput = "0" + userinput;
        } else if (userinput.endsWith(".")) {
            userinput = userinput + 0;
        }
        Double result = Double.parseDouble(userinput);
        if(result == 0d) {
            return "";
        } else {
            return getDecimalFormat(c, Integer.parseInt(token.getDecimals())).format(result);
        }
    }

    public static DecimalFormat getDecimalFormat(Context c, int cnt) {
        DecimalFormat decimalformat = null;
        switch (cnt) {
            case 0:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_0));
                break;
            case 1:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_1));
                break;
            case 2:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_2));
                break;
            case 3:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_3));
                break;
            case 4:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_4));
                break;
            case 5:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_5));
                break;
            case 6:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_6));
                break;
            case 7:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_7));
                break;
            case 8:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_8));
                break;
            case 9:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_9));
                break;
            case 10:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_10));
                break;
            case 11:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_11));
                break;
            case 12:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_12));
                break;

            default:
                decimalformat = new DecimalFormat(c.getString(R.string.str_decimal_pattern_4));
                break;
        }
        return decimalformat;
    }


    public static SpannableString AccountCharCnt(Context c, String s) {
        int length = 0;
        if(s == null) {
            length = 0;
        } else {
            length = s.length();
        }
        String format = "" + length + "/12";
        SpannableString result =  new SpannableString(format);

        String regex = "^[_a-z1-5]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        if(m.matches() || length == 0) {
            if(length > 9) {
                result.setSpan(new ForegroundColorSpan(c.getResources().getColor(R.color.colorAccent)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                result.setSpan(new ForegroundColorSpan(c.getResources().getColor(R.color.colorAccent)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            if(length > 9) {
                result.setSpan(new ForegroundColorSpan(c.getResources().getColor(R.color.colorRed)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                result.setSpan(new ForegroundColorSpan(c.getResources().getColor(R.color.colorRed)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return result;
    }

    public static String FormatByte(Double amount) {
        if(amount <= 0) {
            return "0.00 KB";
        }

        if(amount >= BaseConstant.CONSTANT_TBYTE ) {
            Double result = amount / BaseConstant.CONSTANT_TBYTE;
            return  (Math.round(result*100)/100.0) + " TB";

        } else if (amount >= BaseConstant.CONSTANT_GBYTE) {
            Double result = amount / BaseConstant.CONSTANT_GBYTE;
            return  (Math.round(result*100)/100.0) + " GB";

        } else if (amount >= BaseConstant.CONSTANT_MBYTE) {
            Double result = amount / BaseConstant.CONSTANT_GBYTE;
            return  (Math.round(result*100)/100.0) + " MB";

        } else {
            Double result = amount / BaseConstant.CONSTANT_KBYTE;
            return  (Math.round(result*100)/100.0) + " KB";
        }
    }

    public static String SpanFormatKByte(Double amount) {
        String sPattern = "###,###,###,###,###,###,###,##0.00";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        Double result = amount / BaseConstant.CONSTANT_KBYTE;
        return decimalformat.format(result);
    }

    public static String signFormatKByte(Double amount) {
        String sPattern = "###,###,###,###,###,###,###,##0.00";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        Double result = Math.abs(amount) / BaseConstant.CONSTANT_KBYTE;
        String formated = decimalformat.format(result);
        if(amount < 0) {
            return   "- " + formated;
        } else if (amount > 0){
            return  "+ " + formated;
        } else {
            return  formated;
        }
    }

    public static SpannableString FormatKByte(Double amount) {
        String sPattern = "###,###,###,###,###,###,###,##0.00";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        Double divide = amount / BaseConstant.CONSTANT_KBYTE;
        String formated = decimalformat.format(divide);

        SpannableString result =  new SpannableString(formated);
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - 2, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }

    public static SpannableString signSpanFormatKByte(Double amount) {
        String sPattern = "###,###,###,###,###,###,###,##0.00";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        Double divide = Math.abs(amount) / BaseConstant.CONSTANT_KBYTE;
        String formated = decimalformat.format(divide);
        if(amount < 0) {
            formated = "- " + formated;
        } else if (amount > 0){
            formated = "+ " + formated;
        }

        SpannableString result =  new SpannableString(formated);
        result.setSpan(new RelativeSizeSpan(0.8f), result.length() - 2, result.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return result;
    }



    public static String FormatTime(Double amount) {
        if(amount <= 0) {
            return "0.00 μs";
        }

        if(amount >= BaseConstant.CONSTANT_D ) {
            Double result = amount / BaseConstant.CONSTANT_D;
            return  (Math.round(result*100)/100.0) + " Day";

        } else if(amount >= BaseConstant.CONSTANT_H ) {
            Double result = amount / BaseConstant.CONSTANT_H;
            return  (Math.round(result*100)/100.0) + " Hour";

        } else if(amount >= BaseConstant.CONSTANT_M ) {
            Double result = amount / BaseConstant.CONSTANT_M;
            return  (Math.round(result*100)/100.0) + " Min";

        } else if(amount >= BaseConstant.CONSTANT_S ) {
            Double result = amount / BaseConstant.CONSTANT_S;
            return  (Math.round(result*100)/100.0) + " Sec";

        } else if(amount >= BaseConstant.CONSTANT_MS ) {
            Double result = amount / BaseConstant.CONSTANT_MS;
            return  (Math.round(result*100)/100.0) + " ms";

        } else {
            return  (Math.round(amount*100)/100.0) + " μs";
        }

    }



    public static WBUser onSavePivateKey(Context context, String privateKey, String pincode, String account) {
        WBUser result = new WBUser();
        byte[] uuidByte = new DeviceUuidFactory(context).getUuidString().getBytes();
        byte[] uuidByteIv = new byte[16];
        System.arraycopy(uuidByte, 0, uuidByteIv, 0, 16);
        String signature = null;
        String encryptedKey = null;

        try {
            WannabitKeyStore.createKeys(context, account);
            signature = WannabitKeyStore.signData(pincode, account);

            byte[] signDataKey = new byte[16];
            System.arraycopy(signature.getBytes(), 8, signDataKey, 0, 16);

            encryptedKey = AES256Cipher.encrypt(uuidByteIv, signDataKey, privateKey.getBytes());

        } catch (Exception e) {
//            e.printStackTrace();

        } finally {
            if(!TextUtils.isEmpty(signature) && !TextUtils.isEmpty(encryptedKey)) {
                result.setAccount(account);
                result.setUserinfo(encryptedKey);
            }
            return result;
        }

    }

    public static String onGetPrivateKey(Context context, String pincode, String account, String info) {
        String result = null;
        byte[] uuidByte = new DeviceUuidFactory(context).getUuidString().getBytes();
        byte[] uuidByteIv = new byte[16];
        System.arraycopy(uuidByte, 0, uuidByteIv, 0, 16);
        String signature = null;

        try {
            signature = WannabitKeyStore.signData(pincode, account);

            byte[] signDataKey = new byte[16];
            System.arraycopy(signature.getBytes(), 8, signDataKey, 0, 16);

            result = AES256Cipher.decrypt(uuidByteIv, signDataKey, info);

        } catch (Exception e) {
//            e.printStackTrace();

        }
        return result;
    }

    public static String onGetAppLockKey(String Password) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(Password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            WLog.w("error : " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }


    public static  String getDollarFormat(double price) {
        String sPattern = "###,###,###,###,##0.00";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        return  "$ " + decimalformat.format(price);
    }

    public static  String getCashFormat(double price) {
        String sPattern = "###,###,###,###,##0.00";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        return decimalformat.format(price);
    }


    public static DecimalFormat getPriceFormat(Context c, String currency) {
        String sPattern;
        if (currency.equals(c.getString(R.string.str_krw))) {
            sPattern = c.getString(R.string.str_format_krw);
        } else if (currency.equals(c.getString(R.string.str_btc))) {
            sPattern = c.getString(R.string.str_format_btc);
        } else {
            sPattern = c.getString(R.string.str_format_usd);
        }
        return new DecimalFormat(sPattern);
    }

    public static String getDisplayPriceStr(Context c, ResCoinGecko tic, String currency) {
        String result = "";
        if (tic != null) {
            result =  getPriceFormat(c, currency).format(tic.market_data.getPrice(currency))  + " " + currency;
        }
        return result;
    }

    public static String getDisplayPriceSumStr(Context c, ResCoinGecko tic, String currency, Double amount) {
        String result = "";
        if (tic != null) {
            result = getPriceFormat(c, currency).format(tic.market_data.getPrice(currency) * amount) + " " + currency;
        }
        return result;
    }


    public static class WBActionDescCompare implements Comparator<WBAction> {
        @Override
        public int compare(WBAction arg0, WBAction arg1) {
            return arg0.account_action_seq > arg1.account_action_seq ? -1 : arg0.account_action_seq < arg1.account_action_seq ? 1:0;
        }

    }

    public static String getTimeformat(Context c, String rawValue) {
        String result = "??";
        try {
            SimpleDateFormat eosDateFormat = new SimpleDateFormat(c.getString(R.string.str_eos_time_format));
            SimpleDateFormat myFormat = new SimpleDateFormat(c.getString(R.string.str_action_time_format));
            eosDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            result = myFormat.format(eosDateFormat.parse(rawValue));
        } catch (Exception e) {};

        return result;
    }

    public static  Double getMinDecimal(WBToken token) {
        Double result = BaseConstant.CONSTANT_MIN_DECIMAL_4;
        switch (Integer.parseInt(token.getDecimals())) {
            case 0:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_0;
                break;
            case 1:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_1;
                break;
            case 2:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_2;
                break;
            case 3:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_3;
                break;
            case 4:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_4;
                break;
            case 5:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_5;
                break;
            case 6:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_6;
                break;
            case 7:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_7;
                break;
            case 8:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_8;
                break;
            case 9:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_9;
                break;
            case 10:
                result = BaseConstant.CONSTANT_MIN_DECIMAL_10;
                break;
        }

        return result;

    }



    public static ReqPushTxn.Transaction getReqTransaction(Context context, ResBlock resBlock, ResAbiToBin resAbiToBin, String actionCode, String account, String contractCode) throws Exception {
        ReqPushTxn.Authorization authorization = new ReqPushTxn.Authorization(account, "active");
        List<ReqPushTxn.Authorization> authList= new ArrayList<>();
        authList.add(authorization);
        ReqPushTxn.Action action = new ReqPushTxn.Action(
                contractCode,
                actionCode,
                authList ,
                resAbiToBin.getBinargs());

        List<ReqPushTxn.Action> actionList = new ArrayList<>();
        actionList.add(action);

        return new ReqPushTxn.Transaction(
                getExpiration(context, resBlock),
                resBlock.getBlock_num(),
                resBlock.getRef_block_prefix(),
                actionList
        );
    }

    public static List<String> getSignatures(Context context, ResBlock resBlock, ResAbiToBin resAbiToBin, String actionCode, String account, String key, String contractCode) throws Exception  {
        SignedTransaction beforeTxn = createTransaction(
                contractCode,
                actionCode,
                resAbiToBin.getBinargs(),
                new String[] { account + "@active"},
                resBlock.getBlock_num(),
                resBlock.getRef_block_prefix(),
                getExpiration(context, resBlock)
        );

        return signTransaction(beforeTxn, key, context.getString(R.string.chain_id));
    }


    public static String getExpiration(Context context, ResBlock resBlock) throws Exception{
        String date = resBlock.getTimestamp();
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.str_eos_time_format));
        Date d = dateFormat.parse(date);
        long time = d.getTime() + 1000 * 120 ;
        return dateFormat.format(time);
    }



    private static SignedTransaction createTransaction(String contract, String actionName, String dataAsHex, String[] permissions , int blockNum, long blockPrefix, String expiration){
        Action action = new Action(contract, actionName);
        action.setAuthorization(permissions);
        action.setData( dataAsHex );

        SignedTransaction txn = new SignedTransaction();
        txn.addAction( action );
        txn.putSignatures( new ArrayList<String>());

        txn.setReferenceBlock(blockNum, blockPrefix );
        txn.setExpiration(expiration);

        return txn;
    }

    private static List<String> signTransaction(SignedTransaction txn, String privateKey, String chainId) throws IllegalStateException{
        EosPrivateKey privKey = new EosPrivateKey(privateKey);
        TypeChainId id = new TypeChainId(chainId);

        SignedTransaction stxn = new SignedTransaction( txn );
        stxn.sign( privKey, id);

        return stxn.getSignatures();
    }
}
