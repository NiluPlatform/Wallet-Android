package tech.nilu.wallet;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class FIFSRegistrar extends Contract {
    protected static final HashMap<String, String> _addresses;
    private static final String BINARY = "0x608060405234801561001057600080fd5b506040516040806103ba8339810180604052810190808051906020019092919080519060200190929190505050816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600181600019169055505050610321806100996000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063d22057a914610046575b600080fd5b34801561005257600080fd5b506100956004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610097565b005b8160008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be36001548460405180836000191660001916815260200182600019166000191681526020019250505060405180910390206040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561016157600080fd5b505af1158015610175573d6000803e3d6000fd5b505050506040513d602081101561018b57600080fd5b8101908080519060200190929190505050905060008173ffffffffffffffffffffffffffffffffffffffff1614806101ee57503373ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16145b15156101f957600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166306ab592360015486866040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180846000191660001916815260200183600019166000191681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050600060405180830381600087803b1580156102d757600080fd5b505af11580156102eb573d6000803e3d6000fd5b50505050505050505600a165627a7a72305820f80d80a51e6f07cee346c736fe5d42a7ddd8cb3a9b3de19cf91c13c305b609190029";

    static {
        _addresses = new HashMap<>();
    }

    protected FIFSRegistrar(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected FIFSRegistrar(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getBinary() {
        return BINARY;
    }

    public static RemoteCall<FIFSRegistrar> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr, byte[] node) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(nnsAddr),
                new org.web3j.abi.datatypes.generated.Bytes32(node)));
        return deployRemoteCall(FIFSRegistrar.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<FIFSRegistrar> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr, byte[] node) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(nnsAddr),
                new org.web3j.abi.datatypes.generated.Bytes32(node)));
        return deployRemoteCall(FIFSRegistrar.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static FIFSRegistrar load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new FIFSRegistrar(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static FIFSRegistrar load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new FIFSRegistrar(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public RemoteCall<TransactionReceipt> register(byte[] subnode, String owner) {
        final Function function = new Function(
                "register",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(subnode),
                        new org.web3j.abi.datatypes.Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
