package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ReqRamPrice {
    @SerializedName("scope")
    String scope;

    @SerializedName("code")
    String code;

    @SerializedName("table")
    String table;

    @SerializedName("json")
    boolean json;

    public ReqRamPrice() {
        this.scope = "eosio";
        this.code = "eosio";
        this.table = "rammarket";
        this.json = true;
    }
}
