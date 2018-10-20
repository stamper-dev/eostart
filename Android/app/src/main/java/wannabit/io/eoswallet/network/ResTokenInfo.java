package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.eoswallet.model.WBToken;

public class ResTokenInfo {

    @SerializedName("tokenList")
    ArrayList<WBToken> tokenList;

    public ArrayList<WBToken> getTokenList() {
        return tokenList;
    }

    public void setTokenList(ArrayList<WBToken> tokenList) {
        this.tokenList = tokenList;
    }

}
