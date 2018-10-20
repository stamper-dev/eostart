package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqAbiToBinSellRam {

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("action")
    @Expose
    public String action;

    @SerializedName("args")
    @Expose
    public Args args;

    public ReqAbiToBinSellRam(String code, String action, Args args) {
        super();
        this.code = code;
        this.action = action;
        this.args = args;
    }

    public static class Args {
        @SerializedName("account")
        @Expose
        public String account;

        @SerializedName("bytes")
        @Expose
        public String bytes;

        public Args(String account, String bytes) {
            super();
            this.account = account;
            this.bytes = bytes;
        }
    }
}
