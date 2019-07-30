package tech.nilu.wallet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public class Safebox extends Contract {
    private static final String BINARY = "0x6080604052336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610b89806100536000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631707e1ea146100905780631b8fc2f0146100e75780632e1a7d4d1461012a578063317f9fe6146101575780635044c4381461019a57806365894a39146101f15780639e1a00aa14610234578063f2a75fe414610281575b005b34801561009c57600080fd5b506100a5610298565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156100f357600080fd5b50610128600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506102c2565b005b34801561013657600080fd5b5061015560048036038101908080359060200190929190505050610448565b005b34801561016357600080fd5b50610198600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610572565b005b3480156101a657600080fd5b506101af6106f7565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156101fd57600080fd5b50610232600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610721565b005b34801561024057600080fd5b5061027f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061084d565b005b34801561028d57600080fd5b50610296610a1d565b005b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561031d57600080fd5b61033c8173ffffffffffffffffffffffffffffffffffffffff16610b4a565b80156103965750600073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16145b15156103a157600080fd5b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507f4fc104032de2e94c507c9cae8d55a131ce4d9a8d3dd809d74a0925b1ce89256581604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a150565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480156104bc57503073ffffffffffffffffffffffffffffffffffffffff16318111155b15156104c757600080fd5b3373ffffffffffffffffffffffffffffffffffffffff163462030d4090604051600060405180830381858888f19350505050151561050457600080fd5b7f884edad9ce6fa2440d8a54cc123490eb96d2768479d49ff9c7366125a94243643382604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a150565b600073ffffffffffffffffffffffffffffffffffffffff16600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415801561061e5750600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b801561064557506106448173ffffffffffffffffffffffffffffffffffffffff16610b4a565b5b151561065057600080fd5b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507fa49ca843bd44510267e722efd459ea856b6a41781ca3e1f14c38892d087e38fc81604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a150565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561077c57600080fd5b61079b8173ffffffffffffffffffffffffffffffffffffffff16610b4a565b15156107a657600080fd5b80600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507fa503c006e2d9fc2ca6f3934d935f4d3663756759e0b0780621dfbd294d49e3b281604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a150565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480156108c157503073ffffffffffffffffffffffffffffffffffffffff16318111155b15156108cc57600080fd5b6108eb8273ffffffffffffffffffffffffffffffffffffffff16610b4a565b15610932578173ffffffffffffffffffffffffffffffffffffffff168162030d4090604051600060405180830381858888f19350505050151561092d57600080fd5b61097a565b8173ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f19350505050158015610978573d6000803e3d6000fd5b505b7fdb67368402d9286d4bdeba421437a86ceab887e6a086634adec427e91c8a5f6b338383604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a15050565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610a7b57600080fd5b3073ffffffffffffffffffffffffffffffffffffffff163190503373ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f19350505050158015610adb573d6000803e3d6000fd5b507fc705a8718ef7adedf48ef80068dc4a78c40f1475a3990d9018ffb51bf9ded2a53382604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a150565b600080823b9050600081119150509190505600a165627a7a723058209262429f071e992d50beaf65ee4129533596db4cc9a802fd8b93634eccbcad560029";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<>();
    }

    protected Safebox(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Safebox(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<SetVoteHandlerEventResponse> getSetVoteHandlerEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("SetVoteHandler", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<SetVoteHandlerEventResponse> responses = new ArrayList<SetVoteHandlerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SetVoteHandlerEventResponse typedResponse = new SetVoteHandlerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.voteHandler = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SetVoteHandlerEventResponse> setVoteHandlerEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("SetVoteHandler", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, SetVoteHandlerEventResponse>() {
            @Override
            public SetVoteHandlerEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                SetVoteHandlerEventResponse typedResponse = new SetVoteHandlerEventResponse();
                typedResponse.log = log;
                typedResponse.voteHandler = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ChangeAllowedAddressEventResponse> getChangeAllowedAddressEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("ChangeAllowedAddress", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ChangeAllowedAddressEventResponse> responses = new ArrayList<ChangeAllowedAddressEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ChangeAllowedAddressEventResponse typedResponse = new ChangeAllowedAddressEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ChangeAllowedAddressEventResponse> changeAllowedAddressEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("ChangeAllowedAddress", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ChangeAllowedAddressEventResponse>() {
            @Override
            public ChangeAllowedAddressEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ChangeAllowedAddressEventResponse typedResponse = new ChangeAllowedAddressEventResponse();
                typedResponse.log = log;
                typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<SetAllowedAddressEventResponse> getSetAllowedAddressEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("SetAllowedAddress", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<SetAllowedAddressEventResponse> responses = new ArrayList<SetAllowedAddressEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SetAllowedAddressEventResponse typedResponse = new SetAllowedAddressEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SetAllowedAddressEventResponse> setAllowedAddressEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("SetAllowedAddress", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, SetAllowedAddressEventResponse>() {
            @Override
            public SetAllowedAddressEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                SetAllowedAddressEventResponse typedResponse = new SetAllowedAddressEventResponse();
                typedResponse.log = log;
                typedResponse.allowedAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<EmptyEventResponse> getEmptyEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Empty", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<EmptyEventResponse> responses = new ArrayList<EmptyEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            EmptyEventResponse typedResponse = new EmptyEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<EmptyEventResponse> emptyEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Empty", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, EmptyEventResponse>() {
            @Override
            public EmptyEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                EmptyEventResponse typedResponse = new EmptyEventResponse();
                typedResponse.log = log;
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<WithdrawEventResponse> getWithdrawEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Withdraw", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<WithdrawEventResponse> responses = new ArrayList<WithdrawEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            WithdrawEventResponse typedResponse = new WithdrawEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<WithdrawEventResponse> withdrawEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Withdraw", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, WithdrawEventResponse>() {
            @Override
            public WithdrawEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                WithdrawEventResponse typedResponse = new WithdrawEventResponse();
                typedResponse.log = log;
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<SendToEventResponse> getSendToEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("SendTo", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<SendToEventResponse> responses = new ArrayList<SendToEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SendToEventResponse typedResponse = new SendToEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SendToEventResponse> sendToEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("SendTo", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, SendToEventResponse>() {
            @Override
            public SendToEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                SendToEventResponse typedResponse = new SendToEventResponse();
                typedResponse.log = log;
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> setAllowedAddress(String a) {
        final Function function = new Function(
                "setAllowedAddress", 
                Arrays.<Type>asList(new Address(a)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> changeAllowedAddress(String a) {
        final Function function = new Function(
                "changeAllowedAddress", 
                Arrays.<Type>asList(new Address(a)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setVoteHandler(String a) {
        final Function function = new Function(
                "setVoteHandler", 
                Arrays.<Type>asList(new Address(a)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> empty() {
        final Function function = new Function(
                "empty", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdraw(BigInteger value) {
        final Function function = new Function(
                "withdraw", 
                Arrays.<Type>asList(new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> sendTo(String a, BigInteger value) {
        final Function function = new Function(
                "sendTo", 
                Arrays.<Type>asList(new Address(a),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getAllowedAddress() {
        final Function function = new Function("getAllowedAddress", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getVoteHandlerAddress() {
        final Function function = new Function("getVoteHandlerAddress", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<Safebox> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Safebox.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Safebox> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Safebox.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Safebox load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Safebox(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Safebox load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Safebox(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class SetVoteHandlerEventResponse {
        public Log log;

        public String voteHandler;
    }

    public static class ChangeAllowedAddressEventResponse {
        public Log log;

        public String allowedAddress;
    }

    public static class SetAllowedAddressEventResponse {
        public Log log;

        public String allowedAddress;
    }

    public static class EmptyEventResponse {
        public Log log;

        public String a;

        public BigInteger value;
    }

    public static class WithdrawEventResponse {
        public Log log;

        public String a;

        public BigInteger value;
    }

    public static class SendToEventResponse {
        public Log log;

        public String a;

        public String to;

        public BigInteger value;
    }
}
