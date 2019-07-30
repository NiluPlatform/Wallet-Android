package tech.nilu.wallet.repository.contracts;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint64;
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

import java.math.BigInteger;
import java.util.*;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class NNSRegistry extends Contract {
    protected static final HashMap<String, String> _addresses;
    private static final String BINARY = "0x608060405234801561001057600080fd5b50336000808060010260001916815260200190815260200160002060000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061090b8061007b6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630178b8bf1461008857806302571be3146100f957806306ab59231461016a57806314ab9038146101c957806316a25cbd1461020e5780631896f70a146102675780635b0fc9c3146102b8575b600080fd5b34801561009457600080fd5b506100b76004803603810190808035600019169060200190929190505050610309565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561010557600080fd5b506101286004803603810190808035600019169060200190929190505050610350565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561017657600080fd5b506101c760048036038101908080356000191690602001909291908035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610397565b005b3480156101d557600080fd5b5061020c6004803603810190808035600019169060200190929190803567ffffffffffffffff169060200190929190505050610511565b005b34801561021a57600080fd5b5061023d6004803603810190808035600019169060200190929190505050610622565b604051808267ffffffffffffffff1667ffffffffffffffff16815260200191505060405180910390f35b34801561027357600080fd5b506102b66004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061065d565b005b3480156102c457600080fd5b506103076004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061079e565b005b6000806000836000191660001916815260200190815260200160002060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b6000806000836000191660001916815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b6000833373ffffffffffffffffffffffffffffffffffffffff16600080836000191660001916815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561041157600080fd5b848460405180836000191660001916815260200182600019166000191681526020019250505060405180910390209150836000191685600019167fce0457fe73731f824cc272376169235128c118b49d344817417c6d108d155e8285604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a382600080846000191660001916815260200190815260200160002060000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050505050565b813373ffffffffffffffffffffffffffffffffffffffff16600080836000191660001916815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561058957600080fd5b82600019167f1d4f9bbfc9cab89d66e1a1562f2233ccbf1308cb4f63de2ead5787adddb8fa6883604051808267ffffffffffffffff1667ffffffffffffffff16815260200191505060405180910390a281600080856000191660001916815260200190815260200160002060010160146101000a81548167ffffffffffffffff021916908367ffffffffffffffff160217905550505050565b6000806000836000191660001916815260200190815260200160002060010160149054906101000a900467ffffffffffffffff169050919050565b813373ffffffffffffffffffffffffffffffffffffffff16600080836000191660001916815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415156106d557600080fd5b82600019167f335721b01866dc23fbee8b6b2c7b1e14d6f05c28cd35a2c934239f94095602a083604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a281600080856000191660001916815260200190815260200160002060010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050565b813373ffffffffffffffffffffffffffffffffffffffff16600080836000191660001916815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561081657600080fd5b82600019167fd4735d920b0f87494915f556dd9b54c8f309026070caea5c737245152564d26683604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a281600080856000191660001916815260200190815260200160002060000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050505600a165627a7a7230582035a19abba94ad30abf7a7c2108387072de0fe916cf3b722e2b0840212d0b14990029";

    static {
        _addresses = new HashMap<>();
    }

    protected NNSRegistry(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NNSRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getBinary() {
        return BINARY;
    }

    public static RemoteCall<NNSRegistry> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(NNSRegistry.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<NNSRegistry> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(NNSRegistry.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static NNSRegistry load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NNSRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NNSRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NNSRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public List<NewOwnerEventResponse> getNewOwnerEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("NewOwner",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<NewOwnerEventResponse> responses = new ArrayList<NewOwnerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewOwnerEventResponse typedResponse = new NewOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.label = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewOwnerEventResponse> newOwnerEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("NewOwner",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewOwnerEventResponse>() {
            @Override
            public NewOwnerEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                NewOwnerEventResponse typedResponse = new NewOwnerEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.label = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<NewResolverEventResponse> getNewResolverEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("NewResolver",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<NewResolverEventResponse> responses = new ArrayList<NewResolverEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewResolverEventResponse typedResponse = new NewResolverEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.resolver = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewResolverEventResponse> newResolverEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("NewResolver",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewResolverEventResponse>() {
            @Override
            public NewResolverEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                NewResolverEventResponse typedResponse = new NewResolverEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.resolver = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<NewTTLEventResponse> getNewTTLEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("NewTTL",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<NewTTLEventResponse> responses = new ArrayList<NewTTLEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewTTLEventResponse typedResponse = new NewTTLEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ttl = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewTTLEventResponse> newTTLEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("NewTTL",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewTTLEventResponse>() {
            @Override
            public NewTTLEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                NewTTLEventResponse typedResponse = new NewTTLEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ttl = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> setOwner(byte[] node, String owner) {
        final Function function = new Function(
                "setOwner",
                Arrays.<Type>asList(new Bytes32(node),
                        new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setSubnodeOwner(byte[] node, byte[] label, String owner) {
        final Function function = new Function(
                "setSubnodeOwner",
                Arrays.<Type>asList(new Bytes32(node),
                        new Bytes32(label),
                        new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setResolver(byte[] node, String resolver) {
        final Function function = new Function(
                "setResolver",
                Arrays.<Type>asList(new Bytes32(node),
                        new Address(resolver)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setTTL(byte[] node, BigInteger ttl) {
        final Function function = new Function(
                "setTTL",
                Arrays.<Type>asList(new Bytes32(node),
                        new Uint64(ttl)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner(byte[] node) {
        final Function function = new Function("owner",
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> resolver(byte[] node) {
        final Function function = new Function("resolver",
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> ttl(byte[] node) {
        final Function function = new Function("ttl",
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class NewOwnerEventResponse {
        public Log log;

        public byte[] node;

        public byte[] label;

        public String owner;
    }

    public static class TransferEventResponse {
        public Log log;

        public byte[] node;

        public String owner;
    }

    public static class NewResolverEventResponse {
        public Log log;

        public byte[] node;

        public String resolver;
    }

    public static class NewTTLEventResponse {
        public Log log;

        public byte[] node;

        public BigInteger ttl;
    }
}
