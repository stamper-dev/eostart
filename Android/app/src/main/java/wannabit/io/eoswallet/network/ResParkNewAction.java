package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.eoswallet.model.WBEosParkTx;

public class ResParkNewAction {

    @SerializedName("data")
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public int getTotalCnt() {
        if(getData() != null && getData().getTrace_count() > 0) {
            return getData().getTrace_count();
        }
        return 0;
    }

    public ArrayList<WBEosParkTx> getFilteredTx() {
        ArrayList<WBEosParkTx> result = new ArrayList<>();
        if(getData() != null && getData().getTrace_list() != null) {
            return getData().getTrace_list();
        }
        return result;
    }

    public class Data {
        @SerializedName("trace_count")
        int trace_count;


        @SerializedName("trace_list")
        ArrayList<WBEosParkTx> trace_list;

        public int getTrace_count() {
            return trace_count;
        }

        public void setTrace_count(int trace_count) {
            this.trace_count = trace_count;
        }

        public ArrayList<WBEosParkTx> getTrace_list() {
            return trace_list;
        }

        public void setTrace_list(ArrayList<WBEosParkTx> trace_list) {
            this.trace_list = trace_list;
        }
    }
}
