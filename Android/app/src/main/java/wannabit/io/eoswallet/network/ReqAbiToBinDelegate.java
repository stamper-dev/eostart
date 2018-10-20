package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqAbiToBinDelegate {

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("action")
    @Expose
    public String action;

    @SerializedName("args")
    @Expose
    public Args args;

    public ReqAbiToBinDelegate(String code, String action, Args args) {
        this.code = code;
        this.action = action;
        this.args = args;
    }

    public static class Args {
        @SerializedName("from")
        @Expose
        public String from;

        @SerializedName("receiver")
        @Expose
        public String receiver;

        @SerializedName("stake_net_quantity")
        @Expose
        public String stake_net_quantity;

        @SerializedName("stake_cpu_quantity")
        @Expose
        public String stake_cpu_quantity;

        @SerializedName("transfer")
        @Expose
        public boolean transfer;

        public Args(String from, String receiver, String stake_net_quantity, String stake_cpu_quantity, boolean transfer) {
            this.from = from;
            this.receiver = receiver;
            this.stake_net_quantity = stake_net_quantity;
            this.stake_cpu_quantity = stake_cpu_quantity;
            this.transfer = transfer;
        }
    }
}
