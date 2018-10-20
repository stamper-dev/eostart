package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqAbiToBinUndelegate {

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("action")
    @Expose
    public String action;

    @SerializedName("args")
    @Expose
    public Args args;

    public ReqAbiToBinUndelegate(String code, String action, Args args) {
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

        @SerializedName("unstake_net_quantity")
        @Expose
        public String unstake_net_quantity;

        @SerializedName("unstake_cpu_quantity")
        @Expose
        public String unstake_cpu_quantity;


        public Args(String from, String receiver, String unstake_net_quantity, String unstake_cpu_quantity) {
            this.from = from;
            this.receiver = receiver;
            this.unstake_net_quantity = unstake_net_quantity;
            this.unstake_cpu_quantity = unstake_cpu_quantity;
        }
    }
}
