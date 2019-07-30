package tech.nilu.wallet.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tech.nilu.wallet.api.model.token.AddressInfoResponse;

public interface TokenApi {
    @GET("getAddressInfo/{address}?apiKey=freekey")
    Call<AddressInfoResponse> fetchAddressInfo(@Path("address") String address, @Query("token") String token);
}
