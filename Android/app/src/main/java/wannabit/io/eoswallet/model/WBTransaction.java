package wannabit.io.eoswallet.model;

import com.google.gson.annotations.SerializedName;

public class WBTransaction {
    @SerializedName("hash")
    String hash;

    @SerializedName("trx_timestamp")
    String trx_timestamp;

    @SerializedName("another_account")
    String another_account;

    @SerializedName("direction")
    String direction;

    @SerializedName("quantity")
    String quantity;

    @SerializedName("memo")
    String memo;

    @SerializedName("symbol")
    String symbol;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTrx_timestamp() {
        return trx_timestamp;
    }

    public void setTrx_timestamp(String trx_timestamp) {
        this.trx_timestamp = trx_timestamp;
    }

    public String getAnother_account() {
        return another_account;
    }

    public void setAnother_account(String another_account) {
        this.another_account = another_account;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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
}
