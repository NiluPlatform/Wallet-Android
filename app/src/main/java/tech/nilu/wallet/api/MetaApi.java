package tech.nilu.wallet.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tech.nilu.wallet.api.model.meta.AppIdResponse;

public interface MetaApi {
    @FormUrlEncoded
    @POST("get-referral")
    Call<AppIdResponse> getAppId(@Field("text") String code, @Field("encoded") String encrypted);
}
