package tech.nilu.wallet.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tech.nilu.wallet.api.model.faucet.FaucetRequest;
import tech.nilu.wallet.api.model.faucet.FaucetResponse;

public interface FaucetApi {
    @POST("faucet/{type}/{address}")
    Call<FaucetResponse> getFaucet(@Path("type") String faucetType, @Path("address") String walletAddress, @Body FaucetRequest req);
}
