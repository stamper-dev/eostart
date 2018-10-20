package wannabit.io.eoswallet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WBRecent implements Parcelable {

    @SerializedName("account")
    private String  account;

    @SerializedName("date")
    private String    date;



    public WBRecent() {
    }

    public WBRecent(String account, String date) {
        this.account = account;
        this.date = date;
    }

    protected WBRecent(Parcel in) {
        readFromParcel(in);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void readFromParcel(Parcel in) {
        account = in.readString();
        date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account);
        dest.writeString(date);
    }

    public static final Creator<WBRecent> CREATOR = new Creator<WBRecent>() {
        @Override
        public WBRecent createFromParcel(Parcel in) {
            return new WBRecent(in);
        }

        @Override
        public WBRecent[] newArray(int size) {
            return new WBRecent[size];
        }
    };
}
