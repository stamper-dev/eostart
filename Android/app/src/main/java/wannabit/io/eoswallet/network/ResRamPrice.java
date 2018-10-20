package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.eoswallet.model.WBAction;

public class ResRamPrice {

    @SerializedName("rows")
    public ArrayList<Rows> rows;



    public class Rows {
        @SerializedName("supply")
        public String supply;

        @SerializedName("base")
        public Base base;

        @SerializedName("quote")
        public Quote quote;


        public class Base {
            @SerializedName("balance")
            public String balance;

            @SerializedName("weight")
            public String weight;
        }

        public class Quote {
            @SerializedName("balance")
            public String balance;

            @SerializedName("weight")
            public String weight;

        }
    }
}
