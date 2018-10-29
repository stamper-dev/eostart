package wannabit.io.eoswallet.model;

import com.google.gson.annotations.SerializedName;

public class WBEosParkTx {
    @SerializedName("trx_id")
    String trx_id;

    @SerializedName("timestamp")
    String timestamp;

    @SerializedName("receiver")
    String receiver;

    @SerializedName("sender")
    String sender;

    @SerializedName("code")
    String code;

    @SerializedName("quantity")
    String quantity;

    @SerializedName("memo")
    String memo;

    @SerializedName("symbol")
    String symbol;

    @SerializedName("executed")
    String executed;

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExecuted() {
        return executed;
    }

    public void setExecuted(String executed) {
        this.executed = executed;
    }
}
