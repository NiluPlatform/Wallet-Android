package tech.nilu.wallet.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tech.nilu.wallet.api.model.transaction.Transaction;

public interface Ether1Api {
    @GET("transactions/{address}/{offset}/{length}")
    Call<Transaction[]> fetchTransactions(@Path("address") String address, @Path("offset") int offset, @Path("length") int length);

    @GET("transfers/{token}/{wallet}/{offset}/{length}")
    Call<Transaction[]> fetchTokenTransfers(@Path("token") String tokenAddress, @Path("wallet") String walletAddress, @Path("offset") int offset, @Path("length") int length);
}
