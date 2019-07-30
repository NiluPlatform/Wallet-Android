package tech.nilu.wallet.api.explorer;

import javax.inject.Inject;

import retrofit2.Call;
import tech.nilu.wallet.api.EthplorerApi;
import tech.nilu.wallet.api.model.transaction.Transaction;

public class EthplorerExplorer implements Explorer {
    private final EthplorerApi api;

    @Inject
    public EthplorerExplorer(EthplorerApi api) {
        this.api = api;
    }

    @Override
    public Call<Transaction[]> fetchTransactions(String address) {
        return api.fetchTransactions(address);
    }

    @Override
    public Call<Transaction[]> fetchTokenTransfers(String tokenAddress, String walletAddress) {
        return null;
    }
}
