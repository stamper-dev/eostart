package wannabit.io.eoswallet.base;


/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */

public class BaseConstant {

    public final static boolean                 IS_SHOWLOG 					            = false;
    public final static String                  LOG_TAG                                 = "WannaBit";


    public final static String					DB_NAME			                        = "WannaBit";
    public final static int					    DB_VERSION			                    = 2;
    public final static String					DB_TABLE_USER			                = "user";
    public final static String					DB_TABLE_TOKEN			                = "token";
    public final static String					DB_TABLE_ACTION			                = "action";


    public final static String					PREFER_ACCOUNT_INITED			        = "PREFER_ACCOUNT_INITED";
    public final static String					PREFER_RECENT_ACCOUT_ID			        = "PREFER_RECENT_ACCOUT_ID";
    public final static String					PREFER_USER_HIDE_TOKENS			        = "PREFER_USER_HIDE_TOKENS";
    public final static String					PREFER_USER_RECENT			            = "PREFER_USER_RECENT";
    public final static String					PREFER_LAST_EOS_TIC			            = "PREFER_LAST_EOS_TIC";
    public final static String					PREFER_LAST_TIC_TIME		            = "PREFER_LAST_TIC_TIME";
    public final static String					PREFER_LAST_USER_INFO			        = "PREFER_LAST_USER_INFO";
    public final static String					PREFER_CURRENCY			                = "PREFER_CURRENCY";
    public final static String					PREFER_LANGUAGE			                = "PREFER_LANGUAGE";
    public final static String					PREFER_APPLOCK			                = "PREFER_APPLOCK";
    public final static String					PREFER_FINGERPRINT			            = "PREFER_FINGERPRINT";
    public final static String					PREFER_APPLOCK_SIG			            = "PREFER_APPLOCK_SIG";

    public final static String					CONST_KEY_TARGET		                = "CONST_KEY_TARGET";
    public final static String					CONST_ADD_USER		                    = "CONST_ADD_USER";
    public final static String					CONST_DELETE_USER		                = "CONST_DELETE_USER";
    public final static String					CONST_SEND		                        = "CONST_SEND";
    public final static String					CONST_KEY_CHECK		                    = "CONST_KEY_CHECK";
    public final static String					CONST_BUY_RAM_BYTE		                = "CONST_BUY_RAM_BYTE";
    public final static String					CONST_BUY_RAM		                    = "CONST_BUY_RAM";
    public final static String					CONST_SELL_RAM		                    = "CONST_SELL_RAM";
    public final static String					CONST_DELEGATE		                    = "CONST_DELEGATE";
    public final static String					CONST_UNDELEGATE		                = "CONST_UNDELEGATE";

    public final static int					    CONSTANT_MAX_ACCOUNT			        = 5;

    public final static Double					CONSTANT_BYTE			                = 1d;
    public final static Double					CONSTANT_KBYTE			                = CONSTANT_BYTE * 1024;
    public final static Double					CONSTANT_MBYTE			                = CONSTANT_KBYTE * 1024;
    public final static Double					CONSTANT_GBYTE			                = CONSTANT_MBYTE * 1024;
    public final static Double					CONSTANT_TBYTE			                = CONSTANT_GBYTE * 1024;


    public final static Double					CONSTANT_US			                    = 1d;
    public final static Double					CONSTANT_MS			                    = CONSTANT_US * 1000;
    public final static Double					CONSTANT_S			                    = CONSTANT_MS * 1000;
    public final static Double					CONSTANT_M			                    = CONSTANT_S * 60;
    public final static Double					CONSTANT_H			                    = CONSTANT_M * 60;
    public final static Double					CONSTANT_D			                    = CONSTANT_H * 24;


    public final static Double					CONSTANT_MIN_DECIMAL_0			        = 1d;
    public final static Double					CONSTANT_MIN_DECIMAL_1			        = 0.1d;
    public final static Double					CONSTANT_MIN_DECIMAL_2			        = 0.01d;
    public final static Double					CONSTANT_MIN_DECIMAL_3			        = 0.001d;
    public final static Double					CONSTANT_MIN_DECIMAL_4			        = 0.0001d;
    public final static Double					CONSTANT_MIN_DECIMAL_5			        = 0.00001d;
    public final static Double					CONSTANT_MIN_DECIMAL_6			        = 0.000001d;
    public final static Double					CONSTANT_MIN_DECIMAL_7			        = 0.0000001d;
    public final static Double					CONSTANT_MIN_DECIMAL_8			        = 0.00000001d;
    public final static Double					CONSTANT_MIN_DECIMAL_9			        = 0.000000001d;
    public final static Double					CONSTANT_MIN_DECIMAL_10			        = 0.0000000001d;



    public final static int				        COINMARKETCAP_EOS			            = 1765;



    public static class EosActions {
        public final static String					CONST_EOS_CODE		                    = "eosio";
        public final static String					CONST_EOS_ACTION_TRANSFER		        = "transfer";
        public final static String					CONST_EOS_ACTION_BUYRAMBYTE		        = "buyrambytes";
        public final static String					CONST_EOS_ACTION_BUYRAM		            = "buyram";
        public final static String					CONST_EOS_ACTION_SELLRAM		        = "sellram";
        public final static String					CONST_EOS_ACTION_DELEGATE		        = "delegatebw";
        public final static String					CONST_EOS_ACTION_UNDELEGATE		        = "undelegatebw";
    }
}
