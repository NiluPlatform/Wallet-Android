package tech.nilu.wallet.api.explorer;

import retrofit2.Call;
import tech.nilu.wallet.api.model.transaction.Transaction;

public interface Explorer {
    Call<Transaction[]> fetchTransactions(String address);

    Call<Transaction[]> fetchTokenTransfers(String tokenAddress, String walletAddress);
}
