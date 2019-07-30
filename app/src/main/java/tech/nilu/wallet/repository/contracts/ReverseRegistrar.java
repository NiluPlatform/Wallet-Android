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
import org.web3j.abi.datatypes.generated.Bytes32;
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
public class ReverseRegistrar extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50604051604080610e4683398101806040528101908080519060200190929190805190602001909291905050506000826000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be37f91d1777781884d03a6757a803996e38de2a42967fb37eeaca72729271025a9e26001026040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561017b57600080fd5b505af115801561018f573d6000803e3d6000fd5b505050506040513d60208110156101a557600080fd5b8101908080519060200190929190505050905060008173ffffffffffffffffffffffffffffffffffffffff161415156102b0578073ffffffffffffffffffffffffffffffffffffffff16631e83409a336040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b15801561027357600080fd5b505af1158015610287573d6000803e3d6000fd5b505050506040513d602081101561029d57600080fd5b8101908080519060200190929190505050505b505050610b84806102c26000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630f5a54661461007d5780631e83409a146100fc578063828eab0e1461015b578063bffbe61c146101b2578063c358133b14610211578063c47f002714610268575b600080fd5b34801561008957600080fd5b506100de600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506102ed565b60405180826000191660001916815260200191505060405180910390f35b34801561010857600080fd5b5061013d600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506108de565b60405180826000191660001916815260200191505060405180910390f35b34801561016757600080fd5b506101706108f2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156101be57600080fd5b506101f3600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610918565b60405180826000191660001916815260200191505060405180910390f35b34801561021d57600080fd5b5061022661097a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561027457600080fd5b506102cf600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061099f565b60405180826000191660001916815260200191505060405180910390f35b6000806000806102fc33610af7565b92507f91d1777781884d03a6757a803996e38de2a42967fb37eeaca72729271025a9e260010283604051808360001916600019168152602001826000191660001916815260200192505050604051809103902091506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b1580156103e957600080fd5b505af11580156103fd573d6000803e3d6000fd5b505050506040513d602081101561041357600080fd5b8101908080519060200190929190505050905060008573ffffffffffffffffffffffffffffffffffffffff161415801561054b57506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16630178b8bf836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b1580156104e057600080fd5b505af11580156104f4573d6000803e3d6000fd5b505050506040513d602081101561050a57600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff1614155b15610785573073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415156106a0576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166306ab59237f91d1777781884d03a6757a803996e38de2a42967fb37eeaca72729271025a9e260010285306040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180846000191660001916815260200183600019166000191681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050600060405180830381600087803b15801561068457600080fd5b505af1158015610698573d6000803e3d6000fd5b505050503090505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631896f70a83876040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018083600019166000191681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200192505050600060405180830381600087803b15801561076c57600080fd5b505af1158015610780573d6000803e3d6000fd5b505050505b8573ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415156108d2576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166306ab59237f91d1777781884d03a6757a803996e38de2a42967fb37eeaca72729271025a9e260010285896040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180846000191660001916815260200183600019166000191681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050600060405180830381600087803b1580156108b957600080fd5b505af11580156108cd573d6000803e3d6000fd5b505050505b81935050505092915050565b60006108eb8260006102ed565b9050919050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60007f91d1777781884d03a6757a803996e38de2a42967fb37eeaca72729271025a9e260010261094783610af7565b60405180836000191660001916815260200182600019166000191681526020019250505060405180910390209050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000806109ce30600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166102ed565b9050600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637737221382856040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180836000191660001916815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610a89578082015181840152602081019050610a6e565b50505050905090810190601f168015610ab65780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610ad657600080fd5b505af1158015610aea573d6000803e3d6000fd5b5050505080915050919050565b60007f303132333435363738396162636465660000000000000000000000000000000060285b60018103905081600f85161a815360108404935060018103905081600f85161a815360108404935080610b1d576028600020925050509190505600a165627a7a723058206ec02a6b34c77172e93c9b312112a7b979fc17adf74572cd13bb3b4764f7f4ad0029";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<>();
    }

    protected ReverseRegistrar(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ReverseRegistrar(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> defaultResolver() {
        final Function function = new Function("defaultResolver", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> nns() {
        final Function function = new Function("nns", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<ReverseRegistrar> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr, String resolverAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(nnsAddr),
                new Address(resolverAddr)));
        return deployRemoteCall(ReverseRegistrar.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<ReverseRegistrar> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr, String resolverAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(nnsAddr),
                new Address(resolverAddr)));
        return deployRemoteCall(ReverseRegistrar.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public RemoteCall<TransactionReceipt> claim(String owner) {
        final Function function = new Function(
                "claim", 
                Arrays.<Type>asList(new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> claimWithResolver(String owner, String resolver) {
        final Function function = new Function(
                "claimWithResolver", 
                Arrays.<Type>asList(new Address(owner),
                new Address(resolver)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setName(String name) {
        final Function function = new Function(
                "setName", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<byte[]> node(String addr) {
        final Function function = new Function("node", 
                Arrays.<Type>asList(new Address(addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public static ReverseRegistrar load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ReverseRegistrar(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ReverseRegistrar load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ReverseRegistrar(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
