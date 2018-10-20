package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.eoswallet.model.WBTransaction;

public class ResParkAction {

    @SerializedName("errno")
    int errno;

    @SerializedName("errmsg")
    String errmsg;

    @SerializedName("data")
    Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public int getTotalCnt() {
        if(getData() != null && getData().getNum_of_token_transactions() > 0) {
            return getData().getNum_of_token_transactions();
        }
        return 0;
    }

    public ArrayList<WBTransaction> getFilteredTx() {
        ArrayList<WBTransaction> result = new ArrayList<>();
        if(getData() != null && getData().getToken_transactions() != null) {
            return getData().getToken_transactions();
        }
        return result;
    }


    public class Data {
        @SerializedName("num_of_token_transactions")
        int num_of_token_transactions;


        @SerializedName("num_of_bid_transactions")
        int num_of_bid_transactions;

        @SerializedName("token_transactions")
        ArrayList<WBTransaction> token_transactions;


        public int getNum_of_token_transactions() {
            return num_of_token_transactions;
        }

        public void setNum_of_token_transactions(int num_of_token_transactions) {
            this.num_of_token_transactions = num_of_token_transactions;
        }

        public int getNum_of_bid_transactions() {
            return num_of_bid_transactions;
        }

        public void setNum_of_bid_transactions(int num_of_bid_transactions) {
            this.num_of_bid_transactions = num_of_bid_transactions;
        }

        public ArrayList<WBTransaction> getToken_transactions() {
            return token_transactions;
        }

        public void setToken_transactions(ArrayList<WBTransaction> token_transactions) {
            this.token_transactions = token_transactions;
        }

    }
}
