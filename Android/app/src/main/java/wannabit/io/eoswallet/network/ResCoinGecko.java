package wannabit.io.eoswallet.network;

import com.google.gson.annotations.SerializedName;

import wannabit.io.eoswallet.utils.WLog;

public class ResCoinGecko {

    @SerializedName("market_data")
    public MarketData market_data;



    public class MarketData {
        @SerializedName("current_price")
        public CurrentPrice current_price;


        @SerializedName("price_change_percentage_24h_in_currency")
        public Change24Price price_change_24h;

        public Double getPrice(String currency) {
            if (currency.equals("USD")) {
                return current_price.usd;
            } else if (currency.equals("KRW")) {
                return current_price.krw;
            } else if (currency.equals("BTC")) {
                return current_price.btc;
            }
            return 0d;
        }

        public Double get24Price(String currency) {
            if (currency.equals("USD")) {
                return price_change_24h.usd;
            } else if (currency.equals("KRW")) {
                return price_change_24h.krw;
            } else if (currency.equals("BTC")) {
                return price_change_24h.btc;
            }
            return 0d;
        }
    }



    public class CurrentPrice {
        @SerializedName("usd")
        public Double usd;

        @SerializedName("eur")
        public Double eur;

        @SerializedName("krw")
        public Double krw;

        @SerializedName("jpy")
        public Double jpy;

        @SerializedName("cny")
        public Double cny;

        @SerializedName("btc")
        public Double btc;

    }

    public class Change24Price {
        @SerializedName("usd")
        public Double usd;

        @SerializedName("eur")
        public Double eur;

        @SerializedName("krw")
        public Double krw;

        @SerializedName("jpy")
        public Double jpy;

        @SerializedName("cny")
        public Double cny;

        @SerializedName("btc")
        public Double btc;

    }
}
