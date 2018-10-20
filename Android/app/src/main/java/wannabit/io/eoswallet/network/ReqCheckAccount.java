package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ReqCheckAccount {
    @SerializedName("public_key")
    String public_key;

    public ReqCheckAccount(String public_key) {
        this.public_key = public_key;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }
}
