package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tech.nilu.wallet.db.dao.TransactionDao;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.NiluTransaction;
import tech.nilu.wallet.model.CustomException;
import tech.nilu.wallet.model.GasParams;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.contracts.templates.ERC20Basic;
import tech.nilu.wallet.repository.contracts.templates.ERC20BasicImpl;

@Singleton
public class TransferRepository {
    private final Web3jProvider web3jProvider;
    private final TransactionDao dao;

    @Inject
    public TransferRepository(Web3jProvider web3jProvider, TransactionDao dao) {
        this.web3jProvider = web3jProvider;
        this.dao = dao;
    }

    public LiveData<LiveResponse<BigInteger>> getGasPrice() {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<BigInteger>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromFuture(web3j.ethGasPrice().sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x.getGasPrice())));
        return result;
    }

    public LiveData<LiveResponse<BigInteger>> getTransactionCount(Credentials credentials) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<BigInteger>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromFuture(web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x.getTransactionCount())));
        return result;
    }

    public LiveData<LiveResponse<BigInteger>> estimateGas(Credentials credentials, BigInteger nonce, BigInteger gasPrice, String toAddress, BigDecimal value) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<BigInteger>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromFuture(web3j.ethEstimateGas(Transaction.createEtherTransaction(credentials.getAddress(), nonce, gasPrice, BigInteger.valueOf(4476768), toAddress, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger())).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x.getAmountUsed())));
        return result;
    }

    public LiveData<LiveResponse<GasParams>> getTransferFee(Credentials credentials, ContractInfo contractInfo, String toAddress, BigDecimal value) throws IOException {
        Web3j web3j = web3jProvider.create();
        ERC20Basic contract = ERC20BasicImpl.load(contractInfo.getAddress(),
                web3j,
                credentials,
                null,
                null);
        String data = contract.prepareTransferData(toAddress, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger());

        return getTransferFee(credentials, contractInfo.getAddress(), BigDecimal.ZERO, data);
    }

    public LiveData<LiveResponse<GasParams>> getTransferFee(Credentials credentials, String toAddress, BigDecimal value, String data) throws IOException {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<GasParams>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single<EthGasPrice> gasPriceSingle = Single.fromFuture(web3j.ethGasPrice().sendAsync());
        Single<EthGetTransactionCount> nonceSingle = Single.fromFuture(web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync());
        Single<GasParams> observable = Single.zip(gasPriceSingle, nonceSingle, (gasPrice, transactionCount) -> new GasParams(transactionCount.getTransactionCount(), gasPrice.getGasPrice(), null));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(params -> {
                    Transaction tx = new Transaction(credentials.getAddress(),
                            params.getNonce(),
                            params.getGasPrice(),
                            BigInteger.valueOf(4476768),
                            toAddress,
                            Convert.toWei(value, Convert.Unit.ETHER).toBigInteger(),
                            data);
                    Single.fromFuture(web3j.ethEstimateGas(tx).sendAsync())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(estimatedGas -> {
                                if (estimatedGas.hasError())
                                    result.setValue(LiveResponse.of(new CustomException(estimatedGas.getError().getMessage())));
                                else
                                    result.setValue(LiveResponse.of(new GasParams(params.getNonce(), params.getGasPrice(), estimatedGas.getAmountUsed())));
                            }, t -> {
                                t.printStackTrace();
                                result.setValue(LiveResponse.of(t));
                            });
                });

        return result;
    }

    public LiveData<LiveResponse<String>> transfer(Credentials credentials, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, ContractInfo contractInfo, String toAddress, BigDecimal value) {
        Web3j web3j = web3jProvider.create();
        ERC20Basic contract = ERC20BasicImpl.load(contractInfo.getAddress(),
                web3j,
                credentials,
                null,
                null);
        String data = contract.prepareTransferData(toAddress, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger());

        return transfer(credentials, nonce, gasPrice, gasLimit, contractInfo.getAddress(), BigDecimal.ZERO, data);
    }

    public LiveData<LiveResponse<String>> transfer(Credentials credentials, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String toAddress, BigDecimal value, String data) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<String>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, Convert.toWei(value, Convert.Unit.ETHER).toBigInteger(), data);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        Single.fromFuture(web3j.ethSendRawTransaction(Numeric.toHexString(signedMessage)).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transaction -> {
                    if (transaction.hasError())
                        result.setValue(LiveResponse.of(new CustomException(transaction.getError().getMessage())));
                    else
                        result.setValue(LiveResponse.of(transaction.getTransactionHash()));
                });
        return result;
    }

    public LiveData<LiveResponse<TransactionReceipt>> sendFunds(Credentials credentials, String toAddress, BigDecimal value) throws InterruptedException, TransactionException, IOException {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<TransactionReceipt>> result = new MutableLiveData<>();
        result.postValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromFuture(Transfer.sendFunds(web3j, credentials, toAddress, value, Convert.Unit.ETHER).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(x -> result.postValue(LiveResponse.of(x)));
        return result;
    }

    public long insertTransaction(NiluTransaction transaction) {
        return dao.insertTransaction(transaction);
    }

    public LiveData<List<NiluTransaction>> getTransactionsLive(String address) {
        return dao.getTransactionsLive(address);
    }
}
