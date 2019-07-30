package tech.nilu.wallet.repository.contracts;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 * <p>
 * <p>Generated with web3j version 3.3.1.
 */
public class NiluToken extends Contract {
    protected static final HashMap<String, String> _addresses;
    private static final String BINARY = "0x60806040523480156200001157600080fd5b5060405162002919380380620029198339810180604052810190808051820192919060200180518201929190602001805190602001909291908051906020019092919050505083838383838383828282336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060008060146101000a81548160ff0219169083151502179055506001600060156101000a81548160ff0219169083151502179055508260019080519060200190620000ee9291906200023e565b508160029080519060200190620001079291906200023e565b5080600360006101000a81548160ff021916908360ff160217905550600160048190555050505050505080600581905550600554600660008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef6005546040518082815260200191505060405180910390a350505050600060088190555050505050620002ed565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200028157805160ff1916838001178555620002b2565b82800160010185558215620002b2579182015b82811115620002b157825182559160200191906001019062000294565b5b509050620002c19190620002c5565b5090565b620002ea91905b80821115620002e6576000816000905550600101620002cc565b5090565b90565b61261c80620002fd6000396000f300608060405260043610610128576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306fdde0314610133578063095ea7b3146101c357806318160ddd1461022857806322f3e2d41461025357806323b872dd146102825780632c4e722e14610307578063313ce5671461033257806334fcf4371461036357806369ee4c9e1461039057806370a08231146103bb5780638da5cb5b1461041257806395d89b4114610469578063a0712d68146104f9578063a9059cbb1461053e578063b69ef8a8146105a3578063b84c8246146105ce578063c290d69114610637578063c47f002714610657578063ce46e046146106c0578063dc39d06d146106ef578063dd62ed3e14610754578063e9ca7907146107cb575b610131346107f6565b005b34801561013f57600080fd5b50610148610ff2565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561018857808201518184015260208101905061016d565b50505050905090810190601f1680156101b55780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101cf57600080fd5b5061020e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050611090565b604051808215151515815260200191505060405180910390f35b34801561023457600080fd5b5061023d61119c565b6040518082815260200191505060405180910390f35b34801561025f57600080fd5b506102686111a6565b604051808215151515815260200191505060405180910390f35b34801561028e57600080fd5b506102ed600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506111b9565b604051808215151515815260200191505060405180910390f35b34801561031357600080fd5b5061031c6116ce565b6040518082815260200191505060405180910390f35b34801561033e57600080fd5b506103476116d4565b604051808260ff1660ff16815260200191505060405180910390f35b34801561036f57600080fd5b5061038e600480360381019080803590602001909291905050506116e7565b005b34801561039c57600080fd5b506103a56117db565b6040518082815260200191505060405180910390f35b3480156103c757600080fd5b506103fc600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506118cf565b6040518082815260200191505060405180910390f35b34801561041e57600080fd5b50610427611918565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561047557600080fd5b5061047e61193d565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104be5780820151818401526020810190506104a3565b50505050905090810190601f1680156104eb5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561050557600080fd5b50610524600480360381019080803590602001909291905050506119db565b604051808215151515815260200191505060405180910390f35b34801561054a57600080fd5b50610589600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050611cac565b604051808215151515815260200191505060405180910390f35b3480156105af57600080fd5b506105b8611fbd565b6040518082815260200191505060405180910390f35b3480156105da57600080fd5b50610635600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050612004565b005b610655600480360381019080803590602001909291905050506107f6565b005b34801561066357600080fd5b506106be600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050612108565b005b3480156106cc57600080fd5b506106d561220c565b604051808215151515815260200191505060405180910390f35b3480156106fb57600080fd5b5061073a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061221f565b604051808215151515815260200191505060405180910390f35b34801561076057600080fd5b506107b5600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061242c565b6040518082815260200191505060405180910390f35b3480156107d757600080fd5b506107e06124b3565b6040518082815260200191505060405180910390f35b60008060149054906101000a900460ff161515610cdd576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156108f7576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252603e8152602001807f546f6b656e206973206e6f74206163746976617465642e20546865206372656181526020017f746f722068617320746f207061792061637469766174696f6e206665652e000081525060400191505060405180910390fd5b68056bc75e2d631000006008541015610cd85761091f826008546124c090919063ffffffff16565b60088190555068056bc75e2d63100000600854101515610cd7576001600060146101000a81548160ff02191690831515021790555073585dbc24539a01565a65f56d55c5697248e01ed273ffffffffffffffffffffffffffffffffffffffff166108fc68056bc75e2d631000009081150290604051600060405180830381858888f193505050501580156109b7573d6000803e3d6000fd5b503073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fc0c8d17fc786abc10783be91dad4d3f26c41a5a86591cb59c74c24fbe4b0bb7e60405160405180910390a373585dbc24539a01565a65f56d55c5697248e01ed273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef68056bc75e2d631000006040518082815260200191505060405180910390a373e3ab83082bb4f1ecdfba755e5723a368cb1c243a73ffffffffffffffffffffffffffffffffffffffff1663b195773f333060016002600360009054906101000a900460ff166004546005546040518863ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001806020018660ff1660ff168152602001858152602001848152602001838103835288818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610c145780601f10610be957610100808354040283529160200191610c14565b820191906000526020600020905b815481529060010190602001808311610bf757829003601f168201915b5050838103825287818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610c975780601f10610c6c57610100808354040283529160200191610c97565b820191906000526020600020905b815481529060010190602001808311610c7a57829003601f168201915b50509950505050505050505050600060405180830381600087803b158015610cbe57600080fd5b505af1158015610cd2573d6000803e3d6000fd5b505050505b5b610fee565b600073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151515610d1957600080fd5b610d22826124dc565b9050600660008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548111151515610d9357600080fd5b610e0681600660008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546124fa90919063ffffffff16565b600660008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550610ebc81600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546124c090919063ffffffff16565b600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc839081150290604051600060405180830381858888f19350505050158015610f66573d6000803e3d6000fd5b503373ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a35b5050565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110885780601f1061105d57610100808354040283529160200191611088565b820191906000526020600020905b81548152906001019060200180831161106b57829003601f168201915b505050505081565b60008060149054906101000a900460ff1615156110ac57600080fd5b81600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040518082815260200191505060405180910390a36001905092915050565b6000600554905090565b600060149054906101000a900460ff1681565b60008060149054906101000a900460ff1615156111d557600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415151561127a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f43616e206e6f74207472616e7366657220746f20626c61636b686f6c6500000081525060200191505060405180910390fd5b600660008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548211151515611331576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f496e73756666696369656e742062616c616e636500000000000000000000000081525060200191505060405180910390fd5b600760008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548211151515611425576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b61147782600660008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546124fa90919063ffffffff16565b600660008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061150c82600660008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546124c090919063ffffffff16565b600660008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506115de82600760008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546124fa90919063ffffffff16565b600760008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a3600190509392505050565b60045481565b600360009054906101000a900460ff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156117d1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f4f6e6c79206f776e6572206861732061636365737320746f20646f207468697381526020017f2066756e6374696f6e000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8060048190555050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156118c7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f4f6e6c79206f776e6572206861732061636365737320746f20646f207468697381526020017f2066756e6374696f6e000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600854905090565b6000600660008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156119d35780601f106119a8576101008083540402835291602001916119d3565b820191906000526020600020905b8154815290600101906020018083116119b657829003601f168201915b505050505081565b60008060149054906101000a900460ff1615156119f757600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611abb576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f4f6e6c79206f776e65722063616e206d696e74206e657720636f696e7300000081525060200191505060405180910390fd5b611ad0826005546124c090919063ffffffff16565b600581905550611b4982600660008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546124c090919063ffffffff16565b600660008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f0f6798a560793a54c3bcfe86a93cde1e73087d944c0ea20544137d4121396885836040518082815260200191505060405180910390a26000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a360019050919050565b60008060149054906101000a900460ff161515611cc857600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1614151515611d6d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f43616e206e6f74207472616e7366657220746f20626c61636b686f6c6500000081525060200191505060405180910390fd5b600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548211151515611e24576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f496e73756666696369656e742062616c616e636500000000000000000000000081525060200191505060405180910390fd5b611e7682600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546124fa90919063ffffffff16565b600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550611f0b82600660008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546124c090919063ffffffff16565b600660008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a36001905092915050565b6000600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156120ee576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f4f6e6c79206f776e6572206861732061636365737320746f20646f207468697381526020017f2066756e6374696f6e000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806002908051906020019061210492919061254b565b5050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156121f2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f4f6e6c79206f776e6572206861732061636365737320746f20646f207468697381526020017f2066756e6374696f6e000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806001908051906020019061220892919061254b565b5050565b600060159054906101000a900460ff1681565b60008060149054906101000a900460ff16151561223b57600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515612325576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f4f6e6c79206f776e6572206861732061636365737320746f20646f207468697381526020017f2066756e6374696f6e000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8273ffffffffffffffffffffffffffffffffffffffff1663a9059cbb6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b1580156123e957600080fd5b505af11580156123fd573d6000803e3d6000fd5b505050506040513d602081101561241357600080fd5b8101908080519060200190929190505050905092915050565b6000600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b68056bc75e2d6310000081565b600081830190508281101515156124d357fe5b80905092915050565b60006124f36004548361251390919063ffffffff16565b9050919050565b600082821115151561250857fe5b818303905092915050565b6000808314156125265760009050612545565b818302905081838281151561253757fe5b0414151561254157fe5b8090505b92915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061258c57805160ff19168380011785556125ba565b828001600101855582156125ba579182015b828111156125b957825182559160200191906001019061259e565b5b5090506125c791906125cb565b5090565b6125ed91905b808211156125e95760008160009055506001016125d1565b5090565b905600a165627a7a72305820d5dc0166311aa42b775936e1520ce91ad79f702ae85efd69c1b476c54997f9aa0029";

    static {
        _addresses = new HashMap<>();
    }

    protected NiluToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NiluToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getBinary() {
        return BINARY;
    }

    public static RemoteCall<NiluToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _name, String _symbol, BigInteger _decimals, BigInteger _initialSupply) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_symbol),
                new Uint8(_decimals),
                new Uint256(_initialSupply)));
        return deployRemoteCall(NiluToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<NiluToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _name, String _symbol, BigInteger _decimals, BigInteger _initialSupply) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_symbol),
                new Uint8(_decimals),
                new Uint256(_initialSupply)));
        return deployRemoteCall(NiluToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static NiluToken load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NiluToken(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NiluToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NiluToken(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public List<MintEventResponse> getMintEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Mint",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<MintEventResponse> responses = new ArrayList<MintEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MintEventResponse typedResponse = new MintEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MintEventResponse> mintEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Mint",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MintEventResponse>() {
            @Override
            public MintEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                MintEventResponse typedResponse = new MintEventResponse();
                typedResponse.log = log;
                typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ActivatedEventResponse> getActivatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Activated",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Arrays.<TypeReference<?>>asList());
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ActivatedEventResponse> responses = new ArrayList<ActivatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ActivatedEventResponse typedResponse = new ActivatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.creator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.tokenAddr = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ActivatedEventResponse> activatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Activated",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ActivatedEventResponse>() {
            @Override
            public ActivatedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ActivatedEventResponse typedResponse = new ActivatedEventResponse();
                typedResponse.log = log;
                typedResponse.creator = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.tokenAddr = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Approval",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Approval",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        final Function function = new Function("name",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String spender, BigInteger value) {
        final Function function = new Function(
                "approve",
                Arrays.<Type>asList(new Address(spender),
                        new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function("totalSupply",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isActive() {
        final Function function = new Function("isActive",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String from, String to, BigInteger value) {
        final Function function = new Function(
                "transferFrom",
                Arrays.<Type>asList(new Address(from),
                        new Address(to),
                        new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> rate() {
        final Function function = new Function("rate",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function("decimals",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setRate(BigInteger _rate) {
        final Function function = new Function(
                "setRate",
                Arrays.<Type>asList(new Uint256(_rate)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String who) {
        final Function function = new Function("balanceOf",
                Arrays.<Type>asList(new Address(who)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function("symbol",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> mint(BigInteger amount) {
        final Function function = new Function(
                "mint",
                Arrays.<Type>asList(new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transfer(String to, BigInteger value) {
        final Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new Address(to),
                        new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balance() {
        final Function function = new Function("balance",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setSymbol(String _symbol) {
        final Function function = new Function(
                "setSymbol",
                Arrays.<Type>asList(new Utf8String(_symbol)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setName(String _name) {
        final Function function = new Function(
                "setName",
                Arrays.<Type>asList(new Utf8String(_name)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isPayable() {
        final Function function = new Function("isPayable",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> transferAnyERC20Token(String tokenAddress, BigInteger value) {
        final Function function = new Function(
                "transferAnyERC20Token",
                Arrays.<Type>asList(new Address(tokenAddress),
                        new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String owner, String spender) {
        final Function function = new Function("allowance",
                Arrays.<Type>asList(new Address(owner),
                        new Address(spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> ACTIVATION_FEE() {
        final Function function = new Function("ACTIVATION_FEE",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getTotalPaidToActivate() {
        final Function function = new Function("getTotalPaidToActivate",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> pay(BigInteger amount, BigInteger weiValue) {
        final Function function = new Function(
                "pay",
                Arrays.<Type>asList(new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class MintEventResponse {
        public Log log;

        public String to;

        public BigInteger amount;
    }

    public static class ActivatedEventResponse {
        public Log log;

        public String creator;

        public String tokenAddr;
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String owner;

        public String spender;

        public BigInteger value;
    }
}
