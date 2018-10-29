package wannabit.io.eoswallet.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EosParkService {

    @POST("interface_main")
    Call<JsonObject> parkActions(@Body ReqParkAction actionRq);


    @POST("interface_main")
    Call<JsonObject> parkTxCheck(@Body ReqParkTxCheck txRq);


    @GET("api/")
    Call<JsonObject> parkApiActions(@Query("module") String module, @Query("action") String action,
                                    @Query("apikey") String apikey, @Query("account") String account,
                                    @Query("page") int page, @Query("size") int size,
                                    @Query("symbol") String symbol, @Query("code") String code);
}
