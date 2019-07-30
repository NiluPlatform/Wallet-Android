package tech.nilu.wallet.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tech.nilu.wallet.api.model.transaction.Transaction;

public interface EthplorerApi {
    @GET("getAddressTransactions/{address}?apiKey=freekey")
    Call<Transaction[]> fetchTransactions(@Path("address") String address);
}
