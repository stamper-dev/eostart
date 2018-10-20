package wannabit.io.eoswallet.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WannbitService {

    @Headers("X-Auth-Token: tester@wannabit.io=30335081442472=d3a275d23d96fba61af0e6529a495214")
    @GET("/app/version/android")
    Call<JsonObject> getVersion();

    @GET("/eos/getTokenInfo")
    Call<JsonObject> getTokenInfo();
}
