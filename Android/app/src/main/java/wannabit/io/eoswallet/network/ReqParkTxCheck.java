package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

public class ReqParkTxCheck {
    @SerializedName("id")
    String id;

    @SerializedName("interface_name")
    String interface_name;

    public ReqParkTxCheck(String id, String interface_name) {
        this.id = id;
        this.interface_name = interface_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterface_name() {
        return interface_name;
    }

    public void setInterface_name(String interface_name) {
        this.interface_name = interface_name;
    }
}
