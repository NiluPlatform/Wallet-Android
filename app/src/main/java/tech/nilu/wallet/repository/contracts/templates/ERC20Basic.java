package tech.nilu.wallet.repository.contracts.templates;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

import rx.Observable;

public interface ERC20Basic {

    RemoteCall<String> name();

    RemoteCall<BigInteger> totalSupply();

    RemoteCall<BigInteger> rate();

    RemoteCall<BigInteger> decimals();

    RemoteCall<String> symbol();

    RemoteCall<Boolean> isPayable();

    RemoteCall<BigInteger> balanceOf(String me);

    RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value);

    List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt);

    Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock);

    RemoteCall<TransactionReceipt> createTokens(BigInteger weiValue);

    String prepareTransferData(String _to, BigInteger _value);
}
