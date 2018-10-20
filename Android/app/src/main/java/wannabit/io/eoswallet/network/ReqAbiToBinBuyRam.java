package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqAbiToBinBuyRam {

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("action")
    @Expose
    public String action;

    @SerializedName("args")
    @Expose
    public Args args;

    public ReqAbiToBinBuyRam(String code, String action, Args args) {
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

        @SerializedName("quant")
        @Expose
        public String quant;

        public Args(String payer, String receiver, String quant) {
            super();
            this.payer = payer;
            this.receiver = receiver;
            this.quant = quant;
        }
    }
}
