package tech.nilu.wallet.repository.contracts;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public class SubnodeRegistrar extends Contract {
    protected static final HashMap<String, String> _addresses;
    private static final String BINARY = "0x608060405234801561001057600080fd5b506040516040806108668339810180604052810190808051906020019092919080519060200190929190505050816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555033600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050610756806101106000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306ab59231461005c57806369aea8aa146100bb578063a2eb615e14610112575b600080fd5b34801561006857600080fd5b506100b960048036038101908080356000191690602001909291908035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610143565b005b3480156100c757600080fd5b506100d061042b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561011e57600080fd5b506101416004803603810190808035600019169060200190929190505050610451565b005b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3858560405180836000191660001916815260200182600019166000191681526020019250505060405180910390206040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561020a57600080fd5b505af115801561021e573d6000803e3d6000fd5b505050506040513d602081101561023457600080fd5b8101908080519060200190929190505050905060008173ffffffffffffffffffffffffffffffffffffffff16148061029757503373ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16145b1515610331576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b8152602001807f4f6e6c79207375626e6f6465206f776e65722063616e206368616e676520746881526020017f65206f776e65727368697000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166306ab59238585856040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180846000191660001916815260200183600019166000191681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050600060405180830381600087803b15801561040d57600080fd5b505af1158015610421573d6000803e3d6000fd5b5050505050505050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561053c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f4f6e6c79206f776e6572206861732061636365737320746f20646f207468697381526020017f2066756e6374696f6e000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d5fa2b0082306040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018083600019166000191681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200192505050600060405180830381600087803b15801561060957600080fd5b505af115801561061d573d6000803e3d6000fd5b505050506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631896f70a82600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018083600019166000191681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200192505050600060405180830381600087803b15801561070f57600080fd5b505af1158015610723573d6000803e3d6000fd5b50505050505600a165627a7a72305820be9a0666150da058202f0877115366d64038794730cae19de48fe5ff00259c310029";

    static {
        _addresses = new HashMap<>();
    }

    protected SubnodeRegistrar(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SubnodeRegistrar(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getBinary() {
        return BINARY;
    }

    public static RemoteCall<SubnodeRegistrar> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr, String resolverAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(nnsAddr),
                new Address(resolverAddr)));
        return deployRemoteCall(SubnodeRegistrar.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<SubnodeRegistrar> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr, String resolverAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(nnsAddr),
                new Address(resolverAddr)));
        return deployRemoteCall(SubnodeRegistrar.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static SubnodeRegistrar load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SubnodeRegistrar(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SubnodeRegistrar load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SubnodeRegistrar(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public RemoteCall<String> subnodeOwner() {
        final Function function = new Function("subnodeOwner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setResolver(byte[] node) {
        final Function function = new Function(
                "setResolver",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(node)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setSubnodeOwner(byte[] node, byte[] label, String owner) {
        final Function function = new Function(
                "setSubnodeOwner",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.generated.Bytes32(label),
                        new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
