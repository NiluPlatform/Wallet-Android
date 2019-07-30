package tech.nilu.wallet.repository.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class DefaultReverseResolver extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50604051602080610732833981018060405281019080805190602001909291905050506000816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be37f91d1777781884d03a6757a803996e38de2a42967fb37eeaca72729271025a9e26001026040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561013057600080fd5b505af1158015610144573d6000803e3d6000fd5b505050506040513d602081101561015a57600080fd5b8101908080519060200190929190505050905060008173ffffffffffffffffffffffffffffffffffffffff16141515610265578073ffffffffffffffffffffffffffffffffffffffff16631e83409a336040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b15801561022857600080fd5b505af115801561023c573d6000803e3d6000fd5b505050506040513d602081101561025257600080fd5b8101908080519060200190929190505050505b50506104bc806102766000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063691f34311461005c5780637737221314610106578063c358133b1461017d575b600080fd5b34801561006857600080fd5b5061008b60048036038101908080356000191690602001909291905050506101d4565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100cb5780820151818401526020810190506100b0565b50505050905090810190601f1680156100f85780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011257600080fd5b5061017b6004803603810190808035600019169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610284565b005b34801561018957600080fd5b506101926103c6565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60016020528060005260406000206000915090508054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561027c5780601f106102515761010080835404028352916020019161027c565b820191906000526020600020905b81548152906001019060200180831161025f57829003601f168201915b505050505081565b816000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3826040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561031d57600080fd5b505af1158015610331573d6000803e3d6000fd5b505050506040513d602081101561034757600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561039157600080fd5b8160016000856000191660001916815260200190815260200160002090805190602001906103c09291906103eb565b50505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061042c57805160ff191683800117855561045a565b8280016001018555821561045a579182015b8281111561045957825182559160200191906001019061043e565b5b509050610467919061046b565b5090565b61048d91905b80821115610489576000816000905550600101610471565b5090565b905600a165627a7a72305820b5deb9df1f13b7958cdbbc2276757b13050ed4c7724f58d5f036019a8f7e96e60029";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<>();
    }

    protected DefaultReverseResolver(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DefaultReverseResolver(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> name(byte[] param0) {
        final Function function = new Function("name", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> nns() {
        final Function function = new Function("nns", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<DefaultReverseResolver> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(nnsAddr)));
        return deployRemoteCall(DefaultReverseResolver.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<DefaultReverseResolver> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(nnsAddr)));
        return deployRemoteCall(DefaultReverseResolver.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public RemoteCall<TransactionReceipt> setName(byte[] node, String _name) {
        final Function function = new Function(
                "setName", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(node), 
                new Utf8String(_name)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static DefaultReverseResolver load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DefaultReverseResolver(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static DefaultReverseResolver load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DefaultReverseResolver(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
