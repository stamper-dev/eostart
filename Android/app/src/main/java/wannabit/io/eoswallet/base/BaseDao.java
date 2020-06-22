package wannabit.io.eoswallet.base;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import wannabit.io.eoswallet.R;
import wannabit.io.eoswallet.model.WBAction;
import wannabit.io.eoswallet.model.WBRecent;
import wannabit.io.eoswallet.model.WBToken;
import wannabit.io.eoswallet.model.WBUser;
import wannabit.io.eoswallet.network.ResAccountInfo;
import wannabit.io.eoswallet.network.ResCoinGecko;
import wannabit.io.eoswallet.network.ResEosTick;
import wannabit.io.eoswallet.utils.WLog;
import wannabit.io.eoswallet.utils.WUtil;


/**
 * Created by yongjoo@wannabit.io on 2017.  7. 11..
 */

public class BaseDao {

    private BaseApplication     mBaseApplication;
    private SharedPreferences   mSharedPreferences;
    private SQLiteDatabase      mSQLiteDatabase;

    public BaseDao(BaseApplication apps) {
        this.mBaseApplication = apps;
        this.mSharedPreferences = getSharedPreferences();
        SQLiteDatabase.loadLibs(mBaseApplication);
    }


    private SharedPreferences getSharedPreferences() {
        if(mSharedPreferences == null)
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mBaseApplication);
        return mSharedPreferences;
    }

    public ArrayList<String> getUserHideTokens(Long userId) {
        ArrayList<String> result = new ArrayList<>();
        String jsonText = getSharedPreferences().getString(BaseConstant.PREFER_USER_HIDE_TOKENS + userId, null);
        String[] text = new Gson().fromJson(jsonText, String[].class);
        if(text != null && text.length > 0) {
            for(String s:text)
                result.add(s);
        }
        return result;
    }

    public void setUserHideTokens(Long userId, ArrayList<String> tokens) {
        List<String> textList = new ArrayList<String>();
        textList.addAll(tokens);
        String jsonText = new Gson().toJson(textList);
        getSharedPreferences().edit().putString(BaseConstant.PREFER_USER_HIDE_TOKENS + userId, jsonText).commit();
    }

    public ResEosTick getLastEosTic(){
        ResEosTick result = null;
        try {
            String jsonText = getSharedPreferences().getString(BaseConstant.PREFER_LAST_EOS_TIC, null);
            result = new Gson().fromJson(jsonText, ResEosTick.class);
        } catch (Exception e) {
            WLog.r("Pre error lastEostic");
        }
        return result;
    }

    public void setLastEosTic(ResEosTick tic){
        String jsonText = new Gson().toJson(tic);
        getSharedPreferences().edit().putString(BaseConstant.PREFER_LAST_EOS_TIC, jsonText).commit();
    }

    public long getLastEosTicTime() {
        return getSharedPreferences().getLong(BaseConstant.PREFER_LAST_TIC_TIME, 0l);

    }

    public void setLastEosTicTime(long time) {
        getSharedPreferences().edit().putLong(BaseConstant.PREFER_LAST_TIC_TIME, time).commit();

    }


    public ResCoinGecko getLastPriceTic(){
        ResCoinGecko result = null;
        try {
            String jsonText = getSharedPreferences().getString(BaseConstant.PREFER_LAST_EOS_TIC, null);
            result = new Gson().fromJson(jsonText, ResCoinGecko.class);
        } catch (Exception e) {
            WLog.r("Pre error lastPricetic");
        }
        return result;
    }

    public void setLastPriceTic(ResCoinGecko tic){
        String jsonText = new Gson().toJson(tic);
        getSharedPreferences().edit().putString(BaseConstant.PREFER_LAST_EOS_TIC, jsonText).commit();
    }


    public ResAccountInfo getLastUserInfo(){
        ResAccountInfo result = null;
        try {
            String jsonText = getSharedPreferences().getString(BaseConstant.PREFER_LAST_USER_INFO, null);
            result = new Gson().fromJson(jsonText, ResAccountInfo.class);
        }catch (Exception e) {
            WLog.r("Pre error lastuserinfo");
        }
        return result;

    }


    public void setLastUser(ResAccountInfo info){
        String jsonText = new Gson().toJson(info);
        getSharedPreferences().edit().putString(BaseConstant.PREFER_LAST_USER_INFO, jsonText).commit();

    }




    public Long getRecentAccountId() {
        return getSharedPreferences().getLong(BaseConstant.PREFER_RECENT_ACCOUT_ID, -1l);

    }

    public void setRecentAccountId(Long id) {
        getSharedPreferences().edit().putLong(BaseConstant.PREFER_RECENT_ACCOUT_ID, id).commit();

    }


    public WBUser getRecentAccount() {
        return onSelectById(getRecentAccountId());
    }


    public void setUserCurrency(int id) {
        getSharedPreferences().edit().putInt(BaseConstant.PREFER_CURRENCY, id).commit();

    }

    public int getUserCurrency(Context c) {
        int result = getSharedPreferences().getInt(BaseConstant.PREFER_CURRENCY, -1);
        if(result != -1)
            return result;
        else {
            Locale current = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                current = c.getResources().getConfiguration().getLocales().get(0);
            } else{
                current = c.getResources().getConfiguration().locale;
            }

            if(current.toString().toLowerCase().contains("ko") || current.toString().toLowerCase().contains("kr")) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public String getUserCurrencyStr(Context c) {
        String result = "";
        ArrayList<String> supportList = new ArrayList<String>(Arrays.asList(c.getResources().getStringArray(R.array.support_currency)));
        result = supportList.get(getUserCurrency(c));
        return result;
    }

    public void setUserLanguage(int id) {
        getSharedPreferences().edit().putInt(BaseConstant.PREFER_LANGUAGE, id).commit();
    }

    public int getUserLanguage() {
        return getSharedPreferences().getInt(BaseConstant.PREFER_LANGUAGE, 0);

    }

    public String getUserLanguageStr(Context c) {
        String result = "";
        ArrayList<String> supportList = new ArrayList<String>(Arrays.asList(c.getResources().getStringArray(R.array.support_language)));
        result = supportList.get(getUserLanguage());
        return result;
    }


    public void setUsingAppLock(boolean use) {
        getSharedPreferences().edit().putBoolean(BaseConstant.PREFER_APPLOCK, use).commit();
    }

    public boolean getUsingAppLock(){
        boolean using = getSharedPreferences().getBoolean(BaseConstant.PREFER_APPLOCK, false);
        if(using && !TextUtils.isEmpty(getUsingAppLockStr())) {
            return true;
        } else {
            return false;
        }
    }

    public void setUsingFingerPrint(boolean use) {
        getSharedPreferences().edit().putBoolean(BaseConstant.PREFER_FINGERPRINT, use).commit();
    }

    public boolean getUsingFingerPrint(){
        return (getSharedPreferences().getBoolean(BaseConstant.PREFER_FINGERPRINT, false) && getUsingAppLock());
    }

    public void setUsingAppLockStr(String data) {
        getSharedPreferences().edit().putString(BaseConstant.PREFER_APPLOCK_SIG, data).commit();
    }

    public String getUsingAppLockStr(){
        return getSharedPreferences().getString(BaseConstant.PREFER_APPLOCK_SIG, null);
    }






    public SQLiteDatabase getUserDB() {
        if(mSQLiteDatabase == null) {
            mSQLiteDatabase = BaseDataBase.getInstance(mBaseApplication).getWritableDatabase(mBaseApplication.getString(R.string.db_pppw));
        }
        return mSQLiteDatabase;
    }

    public ArrayList<WBUser> onSelectAllUser() {
        ArrayList<WBUser> result = new ArrayList<>();
        Cursor cursor 	= getUserDB().query(BaseConstant.DB_TABLE_USER, new String[]{"id", "account", "userinfo"}, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                WBUser temp = new WBUser();
                temp.setId(cursor.getLong(0));
                temp.setAccount(cursor.getString(1));
                temp.setUserinfo(cursor.getString(2));
                result.add(temp);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public WBUser onSelectById(Long id) {
        WBUser result = null;
        Cursor cursor 	= getUserDB().query(BaseConstant.DB_TABLE_USER, new String[]{"id", "account", "userinfo"}, "id == ?", new String[]{"" + id}, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            result = new WBUser();
            result.setId(cursor.getLong(0));
            result.setAccount(cursor.getString(1));
            result.setUserinfo(cursor.getString(2));
        }
        cursor.close();
        return result;
    }

    public WBUser onSelectByAccount(String account) {
        WBUser result = null;
        Cursor cursor 	= getUserDB().query(BaseConstant.DB_TABLE_USER, new String[]{"id", "account", "userinfo"}, "account == ?", new String[]{"" + account}, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            result = new WBUser();
            result.setId(cursor.getLong(0));
            result.setAccount(cursor.getString(1));
            result.setUserinfo(cursor.getString(2));
        }
        cursor.close();
        return result;
    }

    public boolean isExistingAccount(String account) {
        boolean existed = false;
        Cursor cursor 	= getUserDB().query(BaseConstant.DB_TABLE_USER, new String[]{"id", "account", "userinfo"}, "account == ?", new String[]{"" + account}, null, null, null);
        if(cursor != null && cursor.getCount() > 0) {
            existed = true;
        }
        cursor.close();
        return existed;
    }

    public boolean hasPrivateKey(String account) {
        boolean hasKey = false;
        Cursor cursor 	= getUserDB().query(BaseConstant.DB_TABLE_USER, new String[]{"id", "account", "userinfo"}, "account == ?", new String[]{"" + account}, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            if(cursor.getString(2) != null && !TextUtils.isEmpty(cursor.getString(2))) {
                hasKey = true;
            }
        }
        cursor.close();
        return hasKey;
    }

    public long onInsertUser(WBUser user) {
        if(!isExistingAccount(user.getAccount())) {
            ContentValues values = new ContentValues();
            values.put("account",       user.getAccount());
            values.put("userinfo",      user.getUserinfo());
            return getUserDB().insertOrThrow(BaseConstant.DB_TABLE_USER, null, values);
        } else {
            return onUpdateUser(user);
        }
    }

    public long onUpdateUser(WBUser user) {
        ContentValues values = new ContentValues();
        values.put("account",    user.getAccount());
        if(user.getUserinfo() != null)
            values.put("userinfo",      user.getUserinfo());
        return getUserDB().update(BaseConstant.DB_TABLE_USER, values, "account = ?", new String[]{""+user.getAccount()} );
    }


    public long onDeleteUser(String account) {
        setRecentAccountId(-1l);
        return getUserDB().delete(BaseConstant.DB_TABLE_USER, "account = ?", new String[]{""+account});
    }




    public ArrayList<WBToken> onSelectAllTokens() {
        ArrayList<WBToken> result = new ArrayList<>();
//        result.add(WUtil.getBaseEOSToken());
        Cursor cursor 	= getUserDB().query(BaseConstant.DB_TABLE_TOKEN, new String[]{"name", "symbol", "iconUrl", "contractAddr", "decimals"}, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                WBToken temp = new WBToken();
                temp.setName(cursor.getString(0));
                temp.setSymbol(cursor.getString(1));
                temp.setIconUrl(cursor.getString(2));
                temp.setContractAddr(cursor.getString(3));
                temp.setDecimals(cursor.getString(4));

                result.add(temp);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    public long onDeleteAllTokens() {
        return getUserDB().delete(BaseConstant.DB_TABLE_TOKEN, null, null);
    }

    public long onInsertToken(WBToken token) {
        ContentValues values = new ContentValues();
        values.put("name",          token.getName());
        values.put("symbol",        token.getSymbol());
        values.put("iconUrl",       token.getIconUrl());
        values.put("contractAddr",  token.getContractAddr());
        values.put("decimals",      token.getDecimals());
        return getUserDB().insertOrThrow(BaseConstant.DB_TABLE_TOKEN, null, values);
    }

    public boolean onReInsertTokens(ArrayList<WBToken> tokens) {
        boolean result = true;
        try {
            onDeleteAllTokens();
            for(WBToken token:tokens) {
                onInsertToken(token);
            }
        } catch (Exception e) {
            result = false;
        }
        return  result;
    }




    public ArrayList<WBAction> onSelectActionByAccount(String account) {
        ArrayList<WBAction> result = new ArrayList<>();
        Cursor cursor 	= getUserDB().query(BaseConstant.DB_TABLE_ACTION, new String[]{"id", "account", "seq", "action"}, "account == ?", new String[]{"" + account}, null, null, "seq DESC");
        if(cursor != null && cursor.moveToFirst()) {
            do {
                WBAction temp = new Gson().fromJson(cursor.getString(3), WBAction.class);
                result.add(temp);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }


}
