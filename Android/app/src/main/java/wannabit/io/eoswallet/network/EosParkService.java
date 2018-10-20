package wannabit.io.eoswallet.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EosParkService {

    @POST("interface_main")
    Call<JsonObject> parkActions(@Body ReqParkAction actionRq);


    @POST("interface_main")
    Call<JsonObject> parkTxCheck(@Body ReqParkTxCheck txRq);
}
