package tech.nilu.wallet.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.nilu.wallet.api.explorer.Explorer;
import tech.nilu.wallet.api.model.transaction.Transaction;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.model.CustomException;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.model.TransactionDetails;
import tech.nilu.wallet.repository.contracts.templates.ERC20;
import tech.nilu.wallet.repository.contracts.templates.ERC20Basic;
import tech.nilu.wallet.repository.contracts.templates.ERC20BasicImpl;

/**
 * Created by root on 1/20/18.
 */

@Singleton
public class TransactionRepository {
    private final Web3jProvider web3jProvider;
    private final ExplorerProvider explorerProvider;

    @Inject
    public TransactionRepository(Web3jProvider web3jProvider, ExplorerProvider explorerProvider) {
        this.web3jProvider = web3jProvider;
        this.explorerProvider = explorerProvider;
    }

    public LiveData<LiveResponse<TransactionDetails>> getTransactionDetails(String transactionHash) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<TransactionDetails>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single<EthTransaction> transactionSingle = Single.fromFuture(web3j.ethGetTransactionByHash(transactionHash).sendAsync());
        Single<EthGetTransactionReceipt> receiptSingle = Single.fromFuture(web3j.ethGetTransactionReceipt(transactionHash).sendAsync());
        Single<TransactionDetails> observable = Single.zip(transactionSingle, receiptSingle, (transaction, transactionReceipt) -> new TransactionDetails(transaction.getTransaction(), transactionReceipt.getTransactionReceipt(), null));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(details -> result.setValue(LiveResponse.of(details)));

        return result;
    }

    public LiveData<LiveResponse<TransactionReceipt>> getTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        Web3j web3j = web3jProvider.create();
        TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(web3j, TransactionManager.DEFAULT_POLLING_FREQUENCY, TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
        MutableLiveData<LiveResponse<TransactionReceipt>> result = new MutableLiveData<>();
        result.postValue(LiveResponse.of(processor.waitForTransactionReceipt(transactionHash)));
        return result;
    }

    public LiveData<LiveResponse<Transaction[]>> fetchTransactions(String address) {
        Explorer explorer = explorerProvider.create();
        MutableLiveData<LiveResponse<Transaction[]>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        explorer.fetchTransactions(address).enqueue(new Callback<Transaction[]>() {
            @Override
            public void onResponse(Call<Transaction[]> call, Response<Transaction[]> response) {
                if (response.isSuccessful())
                    result.setValue(LiveResponse.of(response.body()));
            }

            @Override
            public void onFailure(Call<Transaction[]> call, Throwable t) {
                result.setValue(LiveResponse.of(t));
            }
        });

        return result;
    }

    public LiveData<LiveResponse<Transaction[]>> fetchTokenTransfers(String tokenAddress, String walletAddress) {
        Explorer explorer = explorerProvider.create();
        MutableLiveData<LiveResponse<Transaction[]>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        explorer.fetchTokenTransfers(tokenAddress, walletAddress).enqueue(new Callback<Transaction[]>() {
            @Override
            public void onResponse(Call<Transaction[]> call, Response<Transaction[]> response) {
                if (response.isSuccessful())
                    result.setValue(LiveResponse.of(response.body()));
            }

            @Override
            public void onFailure(Call<Transaction[]> call, Throwable t) {
                result.setValue(LiveResponse.of(t));
            }
        });

        return result;
    }

    public boolean canFetchTransactions() {
        Explorer explorer = explorerProvider.create();
        return explorer != null && explorer.fetchTransactions(null) != null;
    }

    public boolean canFetchTokenTransfers() {
        Explorer explorer = explorerProvider.create();
        return explorer != null && explorer.fetchTokenTransfers(null, null) != null;
    }

    public LiveData<LiveResponse<TransactionReceipt>> createTokens(WalletBaseRepository.WalletExtensionData data, ContractInfo contractInfo, BigDecimal value, BigInteger gasPrice) throws IOException {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<TransactionReceipt>> result = new MutableLiveData<>();
        result.postValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));
        if (!contractInfo.getTypes().contains(ERC20Basic.class.getSimpleName()) && !contractInfo.getTypes().contains(ERC20.class.getSimpleName())) {
            result.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
            return result;
        }
        if (data == null || data.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);

        if (gasPrice == null) {
            EthGasPrice egp = web3j.ethGasPrice().send();
            gasPrice = egp.getGasPrice();
        }
        ERC20Basic contract = ERC20BasicImpl.load("0x" + contractInfo.getAddress(),
                web3j,
                data.getCredentials(),
                gasPrice,
                Transfer.GAS_LIMIT);
        Single.fromFuture(contract.createTokens(Convert.toWei(value, Convert.Unit.ETHER).toBigInteger()).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(receipt -> result.postValue(LiveResponse.of(receipt)));
        return result;
    }
}
