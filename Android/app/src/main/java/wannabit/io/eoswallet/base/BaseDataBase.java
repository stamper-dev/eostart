package wannabit.io.eoswallet.base;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import wannabit.io.eoswallet.utils.WLog;

public class BaseDataBase extends SQLiteOpenHelper {

    private static BaseDataBase instance;

    static public synchronized BaseDataBase getInstance(Context context) {
        if (instance == null) {
            instance = new BaseDataBase(context, BaseConstant.DB_NAME, null, BaseConstant.DB_VERSION);
        }
        return instance;
    }

    public BaseDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE [" + BaseConstant.DB_TABLE_USER +
                "] ([id] INTEGER PRIMARY KEY AUTOINCREMENT, [account] TEXT, [userinfo] TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE [" + BaseConstant.DB_TABLE_TOKEN +
                "] ([id] INTEGER PRIMARY KEY AUTOINCREMENT, [name] TEXT, [symbol] TEXT, [iconUrl] TEXT, [contractAddr] TEXT, [decimals] TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE [" + BaseConstant.DB_TABLE_ACTION +
                "] ([id] INTEGER PRIMARY KEY AUTOINCREMENT, [account] TEXT, [seq] INTEGER, [action] TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion == 1 && newVersion == 2) {
            sqLiteDatabase.execSQL("CREATE TABLE [" + BaseConstant.DB_TABLE_ACTION +
                    "] ([id] INTEGER PRIMARY KEY AUTOINCREMENT, [account] TEXT, [seq] INTEGER, [action] TEXT)");
        } else {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS [" + BaseConstant.DB_TABLE_USER + "] ");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS [" + BaseConstant.DB_TABLE_TOKEN + "] ");
            onCreate(sqLiteDatabase);
        }
    }
}
