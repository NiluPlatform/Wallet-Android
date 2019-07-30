package tech.nilu.wallet.repository.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 * <p>
 * <p>Generated with web3j version 3.0.2.
 */
public final class SelectedContracts extends Contract {

    private SelectedContracts(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(null, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private SelectedContracts(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(null, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<Tuple4<String, String, String, String>> infoOf(String c) {
        final Function function = new Function("infoOf",
                Arrays.<Type>asList(new Address(c)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }));
        return new RemoteCall<>(
                () -> {
                    List<Type> results = executeCallMultipleValueReturn(function);
                    return new Tuple4<>(
                            (String) results.get(0).getValue(),
                            (String) results.get(1).getValue(),
                            (String) results.get(2).getValue(),
                            (String) results.get(3).getValue());

                });
    }


    public RemoteCall<String> listContracts() {
        Function function = new Function("listContracts",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }


    public static SelectedContracts load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SelectedContracts(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SelectedContracts load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SelectedContracts(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

}
