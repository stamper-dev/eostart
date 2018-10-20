package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqAbiToBinBuyRamByte {

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("action")
    @Expose
    public String action;

    @SerializedName("args")
    @Expose
    public Args args;

    public ReqAbiToBinBuyRamByte(String code, String action, Args args) {
        super();
        this.code = code;
        this.action = action;
        this.args = args;
    }

    public static class Args {
        @SerializedName("payer")
        @Expose
        public String payer;

        @SerializedName("receiver")
        @Expose
        public String receiver;

        @SerializedName("bytes")
        @Expose
        public String bytes;

        public Args(String payer, String receiver, String bytes) {
            super();
            this.payer = payer;
            this.receiver = receiver;
            this.bytes = bytes;
        }
    }
}
