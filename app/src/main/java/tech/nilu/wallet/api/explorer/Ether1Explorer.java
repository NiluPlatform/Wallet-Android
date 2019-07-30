package tech.nilu.wallet.api.explorer;

import javax.inject.Inject;

import retrofit2.Call;
import tech.nilu.wallet.api.Ether1Api;
import tech.nilu.wallet.api.model.transaction.Transaction;

public class Ether1Explorer implements Explorer {
    private final Ether1Api api;

    @Inject
    public Ether1Explorer(Ether1Api api) {
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
