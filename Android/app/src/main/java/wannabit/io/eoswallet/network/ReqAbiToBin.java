package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqAbiToBin {


    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("args")
    @Expose
    private Args args;


    /**
     *
     * @param args
     * @param action
     * @param code
     */
    public ReqAbiToBin(String code, String action, Args args) {
        super();
        this.code = code;
        this.action = action;
        this.args = args;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Args getArgs() {
        return args;
    }

    public void setArgs(Args args) {
        this.args = args;
    }

    public static class Args {

        @SerializedName("from")
        @Expose
        private String from;
        @SerializedName("to")
        @Expose
        private String to;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("memo")
        @Expose
        private String memo;


        /**
         *
         * @param to
         * @param memo
         * @param quantity
         * @param from
         */
        public Args(String from, String to, String quantity, String memo) {
            super();
            this.from = from;
            this.to = to;
            this.quantity = quantity;
            this.memo = memo;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

    }

}
