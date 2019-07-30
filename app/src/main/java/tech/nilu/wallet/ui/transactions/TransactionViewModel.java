package tech.nilu.wallet.ui.transactions;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import tech.nilu.wallet.api.model.transaction.Transaction;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.TransactionDetails;
import tech.nilu.wallet.repository.NetworkRepository;
import tech.nilu.wallet.repository.TransactionRepository;

/**
 * Created by root on 2/3/18.
 */

public class TransactionViewModel extends ViewModel {
    private final TransactionRepository transactionRepository;
    private final NetworkRepository networkRepository;

    @Inject
    public TransactionViewModel(TransactionRepository transactionRepository, NetworkRepository networkRepository) {
        this.transactionRepository = transactionRepository;
        this.networkRepository = networkRepository;
    }

    public LiveData<LiveResponse<TransactionDetails>> getTransactionDetails(String transactionHash) {
        return transactionRepository.getTransactionDetails(transactionHash);
    }

    public LiveData<LiveResponse<Transaction[]>> fetchTransactions(String address) {
        return transactionRepository.fetchTransactions(address);
    }

    public Network getActiveNetwork() {
        return networkRepository.getActiveNetwork();
    }
}
