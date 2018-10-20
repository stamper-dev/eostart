package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ReqAccountInfo {
    @SerializedName("account_name")
    String account_name;

    public ReqAccountInfo(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }
}
