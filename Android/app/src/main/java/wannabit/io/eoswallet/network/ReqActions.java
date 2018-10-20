package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ReqActions {

    @SerializedName("account_name")
    String account_name;

    @SerializedName("pos")
    Long pos;

    @SerializedName("offset")
    Long offset;

    public ReqActions(String account_name, Long pos, Long offset) {
        this.account_name = account_name;
        this.pos = pos;
        this.offset = offset;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }
}
