package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ReqCurrencyBalance {
    @SerializedName("code")
    String code;

    @SerializedName("account")
    String account;

    @SerializedName("symbol")
    String symbol;

    public ReqCurrencyBalance(String code, String account, String symbol) {
        this.code = code;
        this.account = account;
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
