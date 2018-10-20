package wannabit.io.eoswallet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WBToken implements Parcelable {

    @SerializedName("name")
    String name;

    @SerializedName("symbol")
    String symbol;

    @SerializedName("iconUrl")
    String iconUrl;

    @SerializedName("contractAddr")
    String contractAddr;

    @SerializedName("decimals")
    String decimals;

    Double userAmount = -1d;


    public WBToken() {
    }

    public WBToken(String name, String symbol, String iconUrl, String contractAddr, String decimals) {
        this.name = name;
        this.symbol = symbol;
        this.iconUrl = iconUrl;
        this.contractAddr = contractAddr;
        this.decimals = decimals;
    }

    protected WBToken(Parcel in) {
        readFromParcel(in);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    public Double getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(Double userAmount) {
        this.userAmount = userAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public void readFromParcel(Parcel in) {
        name = in.readString();
        symbol = in.readString();
        iconUrl = in.readString();
        contractAddr = in.readString();
        decimals = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeString(iconUrl);
        dest.writeString(contractAddr);
        dest.writeString(decimals);
    }

    public static final Creator<WBToken> CREATOR = new Creator<WBToken>() {
        @Override
        public WBToken createFromParcel(Parcel in) {
            return new WBToken(in);
        }

        @Override
        public WBToken[] newArray(int size) {
            return new WBToken[size];
        }
    };

}
