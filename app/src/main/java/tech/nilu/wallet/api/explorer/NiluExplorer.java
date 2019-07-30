package tech.nilu.wallet.api.explorer;

import javax.inject.Inject;

import retrofit2.Call;
import tech.nilu.wallet.api.NiluApi;
import tech.nilu.wallet.api.model.transaction.Transaction;

public class NiluExplorer implements Explorer {
    private final NiluApi api;

    @Inject
    public NiluExplorer(NiluApi api) {
        this.api = api;
    }

    @Override
    public Call<Transaction[]> fetchTransactions(String address) {
        return api.fetchTransactions(address, 0, 50);
    }

    @Override
    public Call<Transaction[]> fetchTokenTransfers(String tokenAddress, String walletAddress) {
        return api.fetchTokenTransfers(tokenAddress, walletAddress, 0, 50);
    }
}
