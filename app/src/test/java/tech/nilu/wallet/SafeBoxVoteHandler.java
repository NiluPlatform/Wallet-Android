package tech.nilu.wallet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class SafeBoxVoteHandler extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50604051604080610f028339810180604052810190808051906020019092919080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806001819055505050610e2c806100d66000396000f3006080604052600436106100a4576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632495c0ce146100ae5780632b04a862146100c5578063403208331461010857806361cac4181461015f578063752187151461017657806379ec5d3a14610180578063ad2e8c9b146101db578063b0cb2f6f14610206578063cda7b96614610231578063ff1dae2314610274575b6100ac61029f565b005b3480156100ba57600080fd5b506100c361041a565b005b3480156100d157600080fd5b506100f2600480360381019080803515159060200190929190505050610596565b6040518082815260200191505060405180910390f35b34801561011457600080fd5b5061011d6106ca565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561016b57600080fd5b506101746106f4565b005b61017e61029f565b005b34801561018c57600080fd5b506101c1600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610961565b604051808215151515815260200191505060405180910390f35b3480156101e757600080fd5b506101f06109cb565b6040518082815260200191505060405180910390f35b34801561021257600080fd5b5061021b6109d5565b6040518082815260200191505060405180910390f35b34801561023d57600080fd5b50610272600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506109df565b005b34801561028057600080fd5b50610289610bf0565b6040518082815260200191505060405180910390f35b6000600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff1660ff16141561035e5760063390806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505b6000341161036d576002610370565b60015b600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908360ff1602179055506000341115610418573373ffffffffffffffffffffffffffffffffffffffff166108fc349081150290604051600060405180830381858888f19350505050158015610416573d6000803e3d6000fd5b505b565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561047557600080fd5b600073ffffffffffffffffffffffffffffffffffffffff16600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515156104d357600080fd5b7fa433b1e229314f0523a30dda3bcde27adb193b2c9e05cf2bd6c389f7d41e62fc33600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a1610594610bfa565b565b60008060008090505b6006805490508110156106c057836105b85760026105bb565b60015b60ff16600560006006848154811015156105d157fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff1660ff1614156106b3576106b060068281548110151561065f57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163183610d4390919063ffffffff16565b91505b808060010191505061059f565b8192505050919050565b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000806000600454431015801561075a5750600073ffffffffffffffffffffffffffffffffffffffff16600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614155b151561076557600080fd5b61076f6001610596565b925061077b6000610596565b915081831015801561078f57506002548310155b9050801561088f57600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663317f9fe6600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b15801561087657600080fd5b505af115801561088a573d6000803e3d6000fd5b505050505b7f7651b64cf5d1ad2c7e8f0f49f212d2245bb0618629d709fee4363bdb28a7196833600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1683604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182151515158152602001935050505060405180910390a161095c610bfa565b505050565b60006001600560008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff1660ff16146109c15760006109c4565b60015b9050919050565b6000600154905090565b6000600254905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610a3a57600080fd5b600073ffffffffffffffffffffffffffffffffffffffff16600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610a9757600080fd5b80600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610aed60015443610d4390919063ffffffff16565b600481905550610b1a600a610b0c600843610d6190919063ffffffff16565b610d9490919063ffffffff16565b6002819055507f8bf611ee471cd3f7882121a2ffefdfdb208917e9d9cc249a6761f3c6ab2822e233600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600454600254604051808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200182815260200194505050505060405180910390a150565b6000600454905090565b600080600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060006004819055506000600281905550600090505b600680549050811015610d305760056000600683815481101515610c7257fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81549060ff0219169055600681815481101515610cf657fe5b9060005260206000200160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690558080600101915050610c52565b6000600681610d3f9190610daf565b5050565b6000808284019050838110151515610d5757fe5b8091505092915050565b60008082840290506000841480610d825750828482811515610d7f57fe5b04145b1515610d8a57fe5b8091505092915050565b6000808284811515610da257fe5b0490508091505092915050565b815481835581811115610dd657818360005260206000209182019101610dd59190610ddb565b5b505050565b610dfd91905b80821115610df9576000816000905550600101610de1565b5090565b905600a165627a7a72305820f8b5314dc8cb5ef7ce6132a87375d909f17281a6099e0e40afa2a354a12d253e0029";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<>();
    }

    protected SafeBoxVoteHandler(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SafeBoxVoteHandler(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<StartVoteEventResponse> getStartVoteEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("StartVote", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<StartVoteEventResponse> responses = new ArrayList<StartVoteEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            StartVoteEventResponse typedResponse = new StartVoteEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.allowedBlock = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.minimumAccepted = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<StartVoteEventResponse> startVoteEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("StartVote", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, StartVoteEventResponse>() {
            @Override
            public StartVoteEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                StartVoteEventResponse typedResponse = new StartVoteEventResponse();
                typedResponse.log = log;
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.allowedBlock = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.minimumAccepted = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public List<StopVoteEventResponse> getStopVoteEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("StopVote", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<StopVoteEventResponse> responses = new ArrayList<StopVoteEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            StopVoteEventResponse typedResponse = new StopVoteEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<StopVoteEventResponse> stopVoteEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("StopVote", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, StopVoteEventResponse>() {
            @Override
            public StopVoteEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                StopVoteEventResponse typedResponse = new StopVoteEventResponse();
                typedResponse.log = log;
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<FinalizeVoteEventResponse> getFinalizeVoteEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("FinalizeVote", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<FinalizeVoteEventResponse> responses = new ArrayList<FinalizeVoteEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            FinalizeVoteEventResponse typedResponse = new FinalizeVoteEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.result = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<FinalizeVoteEventResponse> finalizeVoteEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("FinalizeVote", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, FinalizeVoteEventResponse>() {
            @Override
            public FinalizeVoteEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                FinalizeVoteEventResponse typedResponse = new FinalizeVoteEventResponse();
                typedResponse.log = log;
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.result = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public static RemoteCall<SafeBoxVoteHandler> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String s, BigInteger d) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(s),
                new Uint256(d)));
        return deployRemoteCall(SafeBoxVoteHandler.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<SafeBoxVoteHandler> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String s, BigInteger d) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(s),
                new Uint256(d)));
        return deployRemoteCall(SafeBoxVoteHandler.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public RemoteCall<TransactionReceipt> startVote(String suggest) {
        final Function function = new Function(
                "startVote", 
                Arrays.<Type>asList(new Address(suggest)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> stopVote() {
        final Function function = new Function(
                "stopVote", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> handleVote(BigInteger weiValue) {
        final Function function = new Function(
                "handleVote", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> calculateResult(Boolean accept) {
        final Function function = new Function("calculateResult", 
                Arrays.<Type>asList(new Bool(accept)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> finalizeResult() {
        final Function function = new Function(
                "finalizeResult", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> votesOf(String a) {
        final Function function = new Function("votesOf", 
                Arrays.<Type>asList(new Address(a)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> getMinimumAccepted() {
        final Function function = new Function("getMinimumAccepted", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getDuration() {
        final Function function = new Function("getDuration", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getSuggestedAllowedAddress() {
        final Function function = new Function("getSuggestedAllowedAddress", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getAllowedBlockHeight() {
        final Function function = new Function("getAllowedBlockHeight", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static SafeBoxVoteHandler load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeBoxVoteHandler(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SafeBoxVoteHandler load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeBoxVoteHandler(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class StartVoteEventResponse {
        public Log log;

        public String a;

        public String allowedAddress;

        public BigInteger allowedBlock;

        public BigInteger minimumAccepted;
    }

    public static class StopVoteEventResponse {
        public Log log;

        public String a;

        public String allowedAddress;
    }

    public static class FinalizeVoteEventResponse {
        public Log log;

        public String a;

        public String allowedAddress;

        public Boolean result;
    }
}
