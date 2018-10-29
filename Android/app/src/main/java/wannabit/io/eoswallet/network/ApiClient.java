package wannabit.io.eoswallet.network;

import android.content.Context;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wannabit.io.eoswallet.R;


public class ApiClient {
    private static Retrofit retrofit_bp = null;
    private static Retrofit retrofit_cmc = null;
    private static Retrofit retrofit_wb = null;
    private static Retrofit retrofit_ep = null;
    private static Retrofit retrofit_eosaprk_api = null;

    public static Retrofit getBPClient(Context c) {
        if (retrofit_bp == null) {
            synchronized (ApiClient.class) {
                if (retrofit_bp == null)  {
                    retrofit_bp = new Retrofit.Builder()
                            .baseUrl(c.getString(R.string.bp_base_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit_bp;
    }

    public static Retrofit getCMCClient(Context c) {
        if (retrofit_cmc == null) {
            synchronized (ApiClient.class) {
                if (retrofit_cmc == null)  {
                    retrofit_cmc = new Retrofit.Builder()
                            .baseUrl(c.getString(R.string.coinmarketcap_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit_cmc;
    }

    public static Retrofit getWBClient(Context c) {
        if (retrofit_wb == null) {
            synchronized (ApiClient.class) {
                if (retrofit_wb == null)  {
                    retrofit_wb = new Retrofit.Builder()
                            .baseUrl(c.getString(R.string.wannabit_pro_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit_wb;
    }

    public static Retrofit getEPClient(Context c) {
        if (retrofit_ep == null) {
            synchronized (ApiClient.class) {
                if (retrofit_ep == null)  {
                    retrofit_ep = new Retrofit.Builder()
                            .baseUrl(c.getString(R.string.eospark_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit_ep;
    }

    public static Retrofit getEosParkClient(Context c) {
        if (retrofit_eosaprk_api == null) {
            synchronized (ApiClient.class) {
                if (retrofit_eosaprk_api == null)  {
                    retrofit_eosaprk_api = new Retrofit.Builder()
                            .baseUrl(c.getString(R.string.eospark_api_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit_eosaprk_api;
    }




    /**
     * with wannabit calls
     */
    public static Call<JsonObject> getVersion(Context c) {
        WannbitService service = getWBClient(c).create(WannbitService.class);
        return service.getVersion();
    }

    public static Call<JsonObject> getTokenInfo(Context c) {
        WannbitService service = getWBClient(c).create(WannbitService.class);
        return service.getTokenInfo();
    }






    /**
     * with BP calls
     */
    public static Call<JsonObject> getAccount(Context c, String userId) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.getAccount(new ReqAccountInfo(userId));
    }

    public static Call<List<String>> getCurrency(Context c, String code, String account, String symbol) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.getCurrency(new ReqCurrencyBalance(code, account, symbol));
    }


    public static Call<JsonObject> checkAccount(Context c, String key) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.checkAccount(new ReqCheckAccount(key));
    }

    public static Call<ResInfo> getInfo(Context c) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.getInfo();
    }

    public static Call<ResBlock> getBlock(Context c, int blockNum) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.getBlock(new ReqBlock(blockNum));
    }

    public static Call<ResAbiToBin> abiJsonToBin(Context c, String code, String action, ReqAbiToBin.Args args) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.abiJsonToBin(new ReqAbiToBin(code, action, args));
    }

    public static Call<JsonObject> pushTransaction(Context c, ReqPushTxn.Transaction transaction, List<String> signatures) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.pushTransaction(new ReqPushTxn(transaction, signatures));
    }

    public static Call<ResRamPrice> getRamPrice(Context c) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.getRamPrice(new ReqRamPrice());
    }

    public static Call<ResAbiToBin> abiJsonToBinBuyRamByte(Context c, String code, String action, ReqAbiToBinBuyRamByte.Args args) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.abiJsonToBinBuyRamByte(new ReqAbiToBinBuyRamByte(code, action, args));
    }

    public static Call<ResAbiToBin> abiJsonToBinBuyRam(Context c, String code, String action, ReqAbiToBinBuyRam.Args args) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.abiJsonToBinBuyRam(new ReqAbiToBinBuyRam(code, action, args));
    }

    public static Call<ResAbiToBin> abiJsonToBinSellRam(Context c, String code, String action, ReqAbiToBinSellRam.Args args) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.abiJsonToBinSellRam(new ReqAbiToBinSellRam(code, action, args));
    }


    public static Call<ResAbiToBin> abiJsonToBinDelegate(Context c, String code, String action, ReqAbiToBinDelegate.Args args) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.abiJsonToBinDelegate(new ReqAbiToBinDelegate(code, action, args));
    }

    public static Call<ResAbiToBin> abiJsonToBinUndelegate(Context c, String code, String action, ReqAbiToBinUndelegate.Args args) {
        BPService service = getBPClient(c).create(BPService.class);
        return service.abiJsonToBinUndelegate(new ReqAbiToBinUndelegate(code, action, args));
    }


    /**
     * with Coinmarketcap calls
     */
    public static Call<JsonObject> getEosTic(Context c, int id, String currency) {
        MarketCapService service = getCMCClient(c).create(MarketCapService.class);
        return service.getEosTic(id, currency);
    }




    /**
     * with eospark calls
     */
    public static Call<JsonObject> parkActions(Context c, String account_name, int page_num, int page_size, String tab_name, String symbol, String issue_account, String interface_name) {
        EosParkService service = getEPClient(c).create(EosParkService.class);
        return service.parkActions(new ReqParkAction(account_name, page_num, page_size, tab_name, symbol, issue_account, interface_name));
    }

    public static Call<JsonObject> parkTxCheck(Context c, String id, String interface_name) {
        EosParkService service = getEPClient(c).create(EosParkService.class);
        return service.parkTxCheck(new ReqParkTxCheck(id, interface_name));
    }


    /**
     * with eospark new api
     *
     */

    public static Call<JsonObject> getparkActions(Context c, String account_name, int page_num, int page_size, String symbol, String issue_account) {
        EosParkService service = getEosParkClient(c).create(EosParkService.class);
        return service.parkApiActions("account", c.getString(R.string.eospark_param_trx_info),
                c.getString(R.string.eospark_api_key), account_name,
                page_num, page_size,
                symbol, issue_account);
    }
}
