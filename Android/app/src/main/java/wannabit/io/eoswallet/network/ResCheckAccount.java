package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResCheckAccount {

    @SerializedName("account_names")
    ArrayList<String> account_names;

    public ArrayList<String> getAccount_names() {
        return account_names;
    }

    public void setAccount_names(ArrayList<String> account_names) {
        this.account_names = account_names;
    }
}
