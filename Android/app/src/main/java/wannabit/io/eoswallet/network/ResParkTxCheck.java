package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ResParkTxCheck {

    @SerializedName("errno")
    int errno;

    @SerializedName("errmsg")
    String errmsg;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
