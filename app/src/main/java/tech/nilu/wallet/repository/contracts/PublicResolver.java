package tech.nilu.wallet.repository.contracts;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class PublicResolver extends Contract {
    protected static final HashMap<String, String> _addresses;
    private static final String BINARY = "0x608060405234801561001057600080fd5b50604051602080611f8083398101806040528101908080519060200190929190505050806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050611efd806100836000396000f3006080604052600436106100db576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806301ffc9a7146100e057806310f13a8c146101445780632203ab561461020157806329cd62ea146102bc5780632dff6941146103095780633b3b57de1461035657806359d1d43c146103c7578063623195b0146104b7578063691f34311461053857806377372213146105e2578063aa4cb54714610659578063c3d014d6146106d0578063c86902331461070f578063d5fa2b001461076b578063e89401a1146107bc575b600080fd5b3480156100ec57600080fd5b5061012a60048036038101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19169060200190929190505050610866565b604051808215151515815260200191505060405180910390f35b34801561015057600080fd5b506101ff6004803603810190808035600019169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610bc0565b005b34801561020d57600080fd5b5061023a600480360381019080803560001916906020019092919080359060200190929190505050610e7d565b6040518083815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610280578082015181840152602081019050610265565b50505050905090810190601f1680156102ad5780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b3480156102c857600080fd5b50610307600480360381019080803560001916906020019092919080356000191690602001909291908035600019169060200190929190505050610fc0565b005b34801561031557600080fd5b506103386004803603810190808035600019169060200190929190505050611185565b60405180826000191660001916815260200191505060405180910390f35b34801561036257600080fd5b5061038560048036038101908080356000191690602001909291905050506111ad565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156103d357600080fd5b5061043c6004803603810190808035600019169060200190929190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506111f5565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561047c578082015181840152602081019050610461565b50505050905090810190601f1680156104a95780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156104c357600080fd5b50610536600480360381019080803560001916906020019092919080359060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061131f565b005b34801561054457600080fd5b5061056760048036038101908080356000191690602001909291905050506114bc565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105a757808201518184015260208101905061058c565b50505050905090810190601f1680156105d45780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156105ee57600080fd5b506106576004803603810190808035600019169060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061157c565b005b34801561066557600080fd5b506106ce6004803603810190808035600019169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050611762565b005b3480156106dc57600080fd5b5061070d60048036038101908080356000191690602001909291908035600019169060200190929190505050611948565b005b34801561071b57600080fd5b5061073e6004803603810190808035600019169060200190929190505050611ac5565b60405180836000191660001916815260200182600019166000191681526020019250505060405180910390f35b34801561077757600080fd5b506107ba6004803603810190808035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611b15565b005b3480156107c857600080fd5b506107eb6004803603810190808035600019169060200190929190505050611cec565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561082b578082015181840152602081019050610810565b50505050905090810190601f1680156108585780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6000633b3b57de7c0100000000000000000000000000000000000000000000000000000000027bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161480610937575063d8389dc57c0100000000000000000000000000000000000000000000000000000000027bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b806109a2575063691f34317c0100000000000000000000000000000000000000000000000000000000027bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b80610a0d5750632203ab567c0100000000000000000000000000000000000000000000000000000000027bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b80610a78575063c86902337c0100000000000000000000000000000000000000000000000000000000027bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b80610ae357506359d1d43c7c0100000000000000000000000000000000000000000000000000000000027bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b80610b4e575063e89401a17c0100000000000000000000000000000000000000000000000000000000027bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b80610bb957506301ffc9a77c0100000000000000000000000000000000000000000000000000000000027bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b9050919050565b823373ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b158015610c7057600080fd5b505af1158015610c84573d6000803e3d6000fd5b505050506040513d6020811015610c9a57600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff16141515610ccd57600080fd5b81600160008660001916600019168152602001908152602001600020600501846040518082805190602001908083835b602083101515610d225780518252602082019150602081019050602083039250610cfd565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390209080519060200190610d68929190611dac565b5083600019167fd8c9334b1a9c2f9da342a0a2b32629c1a229b6445dad78947f674b44444a75508485604051808060200180602001838103835285818151815260200191508051906020019080838360005b83811015610dd5578082015181840152602081019050610dba565b50505050905090810190601f168015610e025780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610e3b578082015181840152602081019050610e20565b50505050905090810190601f168015610e685780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a250505050565b6000606060006001600086600019166000191681526020019081526020016000209050600192505b8383111515610fb357600084841614158015610eea57506000816006016000858152602001908152602001600020805460018160011615610100020316600290049050115b15610fa4578060060160008481526020019081526020016000208054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610f985780601f10610f6d57610100808354040283529160200191610f98565b820191906000526020600020905b815481529060010190602001808311610f7b57829003601f168201915b50505050509150610fb8565b6001839060020a029250610ea5565b600092505b509250929050565b823373ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561107057600080fd5b505af1158015611084573d6000803e3d6000fd5b505050506040513d602081101561109a57600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff161415156110cd57600080fd5b6040805190810160405280846000191681526020018360001916815250600160008660001916600019168152602001908152602001600020600301600082015181600001906000191690556020820151816001019060001916905590505083600019167f1d6f5e03d3f63eb58751986629a5439baee5079ff04f345becb66e23eb154e46848460405180836000191660001916815260200182600019166000191681526020019250505060405180910390a250505050565b6000600160008360001916600019168152602001908152602001600020600101549050919050565b600060016000836000191660001916815260200190815260200160002060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b6060600160008460001916600019168152602001908152602001600020600501826040518082805190602001908083835b60208310151561124b5780518252602082019150602081019050602083039250611226565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156113125780601f106112e757610100808354040283529160200191611312565b820191906000526020600020905b8154815290600101906020018083116112f557829003601f168201915b5050505050905092915050565b823373ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b1580156113cf57600080fd5b505af11580156113e3573d6000803e3d6000fd5b505050506040513d60208110156113f957600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff1614151561142c57600080fd5b600083600185031614151561144057600080fd5b8160016000866000191660001916815260200190815260200160002060060160008581526020019081526020016000209080519060200190611483929190611e2c565b508284600019167faa121bbeef5f32f5961a2a28966e769023910fc9479059ee3495d4c1a696efe360405160405180910390a350505050565b60606001600083600019166000191681526020019081526020016000206002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156115705780601f1061154557610100808354040283529160200191611570565b820191906000526020600020905b81548152906001019060200180831161155357829003601f168201915b50505050509050919050565b813373ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561162c57600080fd5b505af1158015611640573d6000803e3d6000fd5b505050506040513d602081101561165657600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff1614151561168957600080fd5b8160016000856000191660001916815260200190815260200160002060020190805190602001906116bb929190611dac565b5082600019167fb7d29e911041e8d9b843369e890bcb72c9388692ba48b65ac54e7214c4c348f7836040518080602001828103825283818151815260200191508051906020019080838360005b83811015611723578082015181840152602081019050611708565b50505050905090810190601f1680156117505780820380516001836020036101000a031916815260200191505b509250505060405180910390a2505050565b813373ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561181257600080fd5b505af1158015611826573d6000803e3d6000fd5b505050506040513d602081101561183c57600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff1614151561186f57600080fd5b8160016000856000191660001916815260200190815260200160002060070190805190602001906118a1929190611e2c565b5082600019167fc0b0fc07269fc2749adada3221c095a1d2187b2d075b51c915857b520f3a5021836040518080602001828103825283818151815260200191508051906020019080838360005b838110156119095780820151818401526020810190506118ee565b50505050905090810190601f1680156119365780820380516001836020036101000a031916815260200191505b509250505060405180910390a2505050565b813373ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b1580156119f857600080fd5b505af1158015611a0c573d6000803e3d6000fd5b505050506040513d6020811015611a2257600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff16141515611a5557600080fd5b81600160008560001916600019168152602001908152602001600020600101816000191690555082600019167f0424b6fe0d9c3bdbece0e7879dc241bb0c22e900be8b6c168b4ee08bd9bf83bc8360405180826000191660001916815260200191505060405180910390a2505050565b600080600160008460001916600019168152602001908152602001600020600301600001546001600085600019166000191681526020019081526020016000206003016001015491509150915091565b813373ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166302571be3836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b158015611bc557600080fd5b505af1158015611bd9573d6000803e3d6000fd5b505050506040513d6020811015611bef57600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff16141515611c2257600080fd5b8160016000856000191660001916815260200190815260200160002060000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600019167f52d7d861f09ab3d26239d492e8968629f95e9e318cf0b73bfddc441522a15fd283604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a2505050565b60606001600083600019166000191681526020019081526020016000206007018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611da05780601f10611d7557610100808354040283529160200191611da0565b820191906000526020600020905b815481529060010190602001808311611d8357829003601f168201915b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611ded57805160ff1916838001178555611e1b565b82800160010185558215611e1b579182015b82811115611e1a578251825591602001919060010190611dff565b5b509050611e289190611eac565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611e6d57805160ff1916838001178555611e9b565b82800160010185558215611e9b579182015b82811115611e9a578251825591602001919060010190611e7f565b5b509050611ea89190611eac565b5090565b611ece91905b80821115611eca576000816000905550600101611eb2565b5090565b905600a165627a7a72305820193c5a0aadec393ecf4ad17b7a00fb8bc100e528c721773972f62ec9f3773fb10029";

    static {
        _addresses = new HashMap<>();
    }

    protected PublicResolver(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PublicResolver(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getBinary() {
        return BINARY;
    }

    public static RemoteCall<PublicResolver> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(nnsAddr)));
        return deployRemoteCall(PublicResolver.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<PublicResolver> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String nnsAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(nnsAddr)));
        return deployRemoteCall(PublicResolver.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static PublicResolver load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PublicResolver(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static PublicResolver load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PublicResolver(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public List<AddrChangedEventResponse> getAddrChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("AddrChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<AddrChangedEventResponse> responses = new ArrayList<AddrChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddrChangedEventResponse typedResponse = new AddrChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddrChangedEventResponse> addrChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("AddrChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddrChangedEventResponse>() {
            @Override
            public AddrChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                AddrChangedEventResponse typedResponse = new AddrChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ContentChangedEventResponse> getContentChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("ContentChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ContentChangedEventResponse> responses = new ArrayList<ContentChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ContentChangedEventResponse typedResponse = new ContentChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ContentChangedEventResponse> contentChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("ContentChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ContentChangedEventResponse>() {
            @Override
            public ContentChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ContentChangedEventResponse typedResponse = new ContentChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<NameChangedEventResponse> getNameChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("NameChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<NameChangedEventResponse> responses = new ArrayList<NameChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NameChangedEventResponse typedResponse = new NameChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NameChangedEventResponse> nameChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("NameChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, NameChangedEventResponse>() {
            @Override
            public NameChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                NameChangedEventResponse typedResponse = new NameChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ABIChangedEventResponse> getABIChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("ABIChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Uint256>() {
                }),
                Arrays.<TypeReference<?>>asList());
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ABIChangedEventResponse> responses = new ArrayList<ABIChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ABIChangedEventResponse typedResponse = new ABIChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contentType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ABIChangedEventResponse> aBIChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("ABIChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Uint256>() {
                }),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ABIChangedEventResponse>() {
            @Override
            public ABIChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ABIChangedEventResponse typedResponse = new ABIChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.contentType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<PubkeyChangedEventResponse> getPubkeyChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("PubkeyChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Bytes32>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<PubkeyChangedEventResponse> responses = new ArrayList<PubkeyChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PubkeyChangedEventResponse typedResponse = new PubkeyChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.x = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.y = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<PubkeyChangedEventResponse> pubkeyChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("PubkeyChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Bytes32>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, PubkeyChangedEventResponse>() {
            @Override
            public PubkeyChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                PubkeyChangedEventResponse typedResponse = new PubkeyChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.x = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.y = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<TextChangedEventResponse> getTextChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TextChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TextChangedEventResponse> responses = new ArrayList<TextChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TextChangedEventResponse typedResponse = new TextChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.indexedKey = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.key = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TextChangedEventResponse> textChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TextChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TextChangedEventResponse>() {
            @Override
            public TextChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TextChangedEventResponse typedResponse = new TextChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.indexedKey = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.key = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<MultihashChangedEventResponse> getMultihashChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("MultihashChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<MultihashChangedEventResponse> responses = new ArrayList<MultihashChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MultihashChangedEventResponse typedResponse = new MultihashChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MultihashChangedEventResponse> multihashChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("MultihashChanged",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MultihashChangedEventResponse>() {
            @Override
            public MultihashChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                MultihashChangedEventResponse typedResponse = new MultihashChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> setAddr(byte[] node, String addr) {
        final Function function = new Function(
                "setAddr",
                Arrays.<Type>asList(new Bytes32(node),
                        new Address(addr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setContent(byte[] node, byte[] hash) {
        final Function function = new Function(
                "setContent",
                Arrays.<Type>asList(new Bytes32(node),
                        new Bytes32(hash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setMultihash(byte[] node, byte[] hash) {
        final Function function = new Function(
                "setMultihash",
                Arrays.<Type>asList(new Bytes32(node),
                        new DynamicBytes(hash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setName(byte[] node, String name) {
        final Function function = new Function(
                "setName",
                Arrays.<Type>asList(new Bytes32(node),
                        new Utf8String(name)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setABI(byte[] node, BigInteger contentType, byte[] data) {
        final Function function = new Function(
                "setABI",
                Arrays.<Type>asList(new Bytes32(node),
                        new Uint256(contentType),
                        new DynamicBytes(data)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setPubkey(byte[] node, byte[] x, byte[] y) {
        final Function function = new Function(
                "setPubkey",
                Arrays.<Type>asList(new Bytes32(node),
                        new Bytes32(x),
                        new Bytes32(y)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setText(byte[] node, String key, String value) {
        final Function function = new Function(
                "setText",
                Arrays.<Type>asList(new Bytes32(node),
                        new Utf8String(key),
                        new Utf8String(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> text(byte[] node, String key) {
        final Function function = new Function("text",
                Arrays.<Type>asList(new Bytes32(node),
                        new Utf8String(key)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple2<byte[], byte[]>> pubkey(byte[] node) {
        final Function function = new Function("pubkey",
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Bytes32>() {
                }));
        return new RemoteCall<Tuple2<byte[], byte[]>>(
                new Callable<Tuple2<byte[], byte[]>>() {
                    @Override
                    public Tuple2<byte[], byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<byte[], byte[]>(
                                (byte[]) results.get(0).getValue(),
                                (byte[]) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<Tuple2<BigInteger, byte[]>> ABI(byte[] node, BigInteger contentTypes) {
        final Function function = new Function("ABI",
                Arrays.<Type>asList(new Bytes32(node),
                        new Uint256(contentTypes)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }, new TypeReference<DynamicBytes>() {
                }));
        return new RemoteCall<Tuple2<BigInteger, byte[]>>(
                new Callable<Tuple2<BigInteger, byte[]>>() {
                    @Override
                    public Tuple2<BigInteger, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, byte[]>(
                                (BigInteger) results.get(0).getValue(),
                                (byte[]) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<String> name(byte[] node) {
        final Function function = new Function("name",
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<byte[]> content(byte[] node) {
        final Function function = new Function("content",
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<byte[]> multihash(byte[] node) {
        final Function function = new Function("multihash",
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {
                }));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<String> addr(byte[] node) {
        final Function function = new Function("addr",
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] interfaceID) {
        final Function function = new Function("supportsInterface",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class AddrChangedEventResponse {
        public Log log;

        public byte[] node;

        public String a;
    }

    public static class ContentChangedEventResponse {
        public Log log;

        public byte[] node;

        public byte[] hash;
    }

    public static class NameChangedEventResponse {
        public Log log;

        public byte[] node;

        public String name;
    }

    public static class ABIChangedEventResponse {
        public Log log;

        public byte[] node;

        public BigInteger contentType;
    }

    public static class PubkeyChangedEventResponse {
        public Log log;

        public byte[] node;

        public byte[] x;

        public byte[] y;
    }

    public static class TextChangedEventResponse {
        public Log log;

        public byte[] node;

        public String indexedKey;

        public String key;
    }

    public static class MultihashChangedEventResponse {
        public Log log;

        public byte[] node;

        public byte[] hash;
    }
}
