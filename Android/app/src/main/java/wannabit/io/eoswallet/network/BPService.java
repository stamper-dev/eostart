package wannabit.io.eoswallet.network;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BPService {

    @POST("v1/history/get_key_accounts")
    Call<JsonObject> checkAccount(@Body ReqCheckAccount check);

    @POST("v1/chain/get_account")
    Call<JsonObject> getAccount(@Body ReqAccountInfo account);

    @POST("v1/history/get_actions")
    Call<JsonObject> getActions(@Body ReqActions action);

    @POST("v1/chain/get_currency_balance")
    Call<List<String>> getCurrency(@Body ReqCurrencyBalance data);

    @GET("v1/chain/get_info")
    Call<ResInfo> getInfo();

    @POST("v1/chain/get_block")
    Call<ResBlock> getBlock(@Body ReqBlock block);

    @POST("v1/chain/abi_json_to_bin")
    Call<ResAbiToBin> abiJsonToBin(@Body ReqAbiToBin abiToBin);

    @POST("v1/chain/push_transaction")
    Call<JsonObject> pushTransaction(@Body ReqPushTxn pushTxn );


    @POST("v1/chain/get_table_rows")
    Call<ResRamPrice> getRamPrice(@Body ReqRamPrice pushTxn );

    @POST("v1/chain/abi_json_to_bin")
    Call<ResAbiToBin> abiJsonToBinBuyRamByte(@Body ReqAbiToBinBuyRamByte abiToBin);

    @POST("v1/chain/abi_json_to_bin")
    Call<ResAbiToBin> abiJsonToBinBuyRam(@Body ReqAbiToBinBuyRam abiToBin);

    @POST("v1/chain/abi_json_to_bin")
    Call<ResAbiToBin> abiJsonToBinSellRam(@Body ReqAbiToBinSellRam abiToBin);

    @POST("v1/chain/abi_json_to_bin")
    Call<ResAbiToBin> abiJsonToBinDelegate(@Body ReqAbiToBinDelegate abiToBin);

    @POST("v1/chain/abi_json_to_bin")
    Call<ResAbiToBin> abiJsonToBinUndelegate(@Body ReqAbiToBinUndelegate abiToBin);
}
