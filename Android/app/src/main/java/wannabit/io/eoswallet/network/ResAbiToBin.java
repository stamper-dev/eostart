package wannabit.io.eoswallet.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResAbiToBin {
    @SerializedName("binargs")
    @Expose
    private String binargs;

    public String getBinargs() {
        return binargs;
    }

    public void setBinargs(String binargs) {
        this.binargs = binargs;
    }

}
