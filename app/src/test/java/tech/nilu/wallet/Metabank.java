package tech.nilu.wallet;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class Metabank extends Contract {
    private static final String BINARY = "0x6080604052600060015560006002556019600460006101000a81548161ffff021916908361ffff160217905550600060055560006006553480156200004357600080fd5b5060405160a0806200366d833981018060405281019080805190602001909291908051906020019092919080519060200190929190805190602001909291908051906020019092919050505033600c60006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555084601160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550620001348143620001706401000000000262001d8b179091906401000000009004565b600181905550836000819055508260038190555081600460006101000a81548161ffff021916908361ffff16021790555050505050506200018f565b60008082840190508381101515156200018557fe5b8091505092915050565b6134ce806200019f6000396000f30060806040526004361061015f576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806302233cc5146101695780630fb5a6b4146101c057806311da60b4146101eb578063205b81691461020257806327431faa1461022d5780632baf57d3146102585780632e1a7d4d1461028357806348cd4cb1146102b057806351602052146102db57806354b67498146102f25780635bb7168b1461031f5780635d4df5741461034a5780636f3763fb1461035457806370a0823114610387578063775a25e3146103de5780637bac9c7c14610409578063826c758b1461043457806388f185121461048f5780638a19c8bc146104e65780638a48ac03146105115780638da5cb5b1461057d578063945b1f4b146105d4578063e90e9ba514610637578063ea35df251461068e578063ee5493eb146106e5578063f1c2111c1461073c578063faf7ba6a14610753575b610167610782565b005b34801561017557600080fd5b506101aa600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610b37565b6040518082815260200191505060405180910390f35b3480156101cc57600080fd5b506101d5610b80565b6040518082815260200191505060405180910390f35b3480156101f757600080fd5b50610200610b86565b005b34801561020e57600080fd5b50610217610d2d565b6040518082815260200191505060405180910390f35b34801561023957600080fd5b50610242610e0b565b6040518082815260200191505060405180910390f35b34801561026457600080fd5b5061026d610f04565b6040518082815260200191505060405180910390f35b34801561028f57600080fd5b506102ae60048036038101908080359060200190929190505050610f0e565b005b3480156102bc57600080fd5b506102c56115fa565b6040518082815260200191505060405180910390f35b3480156102e757600080fd5b506102f0611600565b005b3480156102fe57600080fd5b5061031d6004803603810190808035906020019092919050505061165a565b005b34801561032b57600080fd5b506103346118be565b6040518082815260200191505060405180910390f35b610352610782565b005b34801561036057600080fd5b506103696118c4565b604051808261ffff1661ffff16815260200191505060405180910390f35b34801561039357600080fd5b506103c8600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506118d8565b6040518082815260200191505060405180910390f35b3480156103ea57600080fd5b506103f3611921565b6040518082815260200191505060405180910390f35b34801561041557600080fd5b5061041e61192b565b6040518082815260200191505060405180910390f35b34801561044057600080fd5b50610475600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611949565b604051808215151515815260200191505060405180910390f35b34801561049b57600080fd5b506104d0600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061199f565b6040518082815260200191505060405180910390f35b3480156104f257600080fd5b506104fb6119e8565b6040518082815260200191505060405180910390f35b34801561051d57600080fd5b506105266119ee565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561056957808201518184015260208101905061054e565b505050509050019250505060405180910390f35b34801561058957600080fd5b50610592611ad8565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156105e057600080fd5b50610621600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803515159060200190929190505050611afe565b6040518082815260200191505060405180910390f35b34801561064357600080fd5b50610678600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611c48565b6040518082815260200191505060405180910390f35b34801561069a57600080fd5b506106cf600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611c91565b6040518082815260200191505060405180910390f35b3480156106f157600080fd5b506106fa611cda565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561074857600080fd5b50610751611d04565b005b34801561075f57600080fd5b50610768611d5e565b604051808215151515815260200191505060405180910390f35b601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610b355760035461087f34610871600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b611d8b90919063ffffffff16565b1015151561088c57600080fd5b6108de34600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061093634600554611d8b90919063ffffffff16565b600581905550600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515610acb576109ae3373ffffffffffffffffffffffffffffffffffffffff16611da9565b15610a0c576001600f60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505b600e3390806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506001600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505b601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc349081150290604051600060405180830381858888f19350505050158015610b33573d6000803e3d6000fd5b505b565b6000600960008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b60005481565b600080600054610ba160015443611dbc90919063ffffffff16565b101580610bb057506000600254145b1515610bbb57600080fd5b6000600254141515610cd657600554601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163111610c10576000610c5f565b610c5e600554601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1631611dbc90919063ffffffff16565b5b9150610c9c612710610c8e600460009054906101000a900461ffff1661ffff1685611dd590919063ffffffff16565b611e0890919063ffffffff16565b9050808211610cac576000610cc0565b610cbf8183611dbc90919063ffffffff16565b5b9150610ccb82611e23565b610cd5338261249e565b5b610cde612890565b610ce66131bf565b600054610cfe60015443611dbc90919063ffffffff16565b101515610d2957610d1b6001600254611d8b90919063ffffffff16565b600281905550436001819055505b5050565b600080600554601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163111610d79576000610dc8565b610dc7600554601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1631611dbc90919063ffffffff16565b5b9050610e05612710610df7600460009054906101000a900461ffff1661ffff1684611dd590919063ffffffff16565b611e0890919063ffffffff16565b91505090565b6000806000600554601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163111610e59576000610ea8565b610ea7600554601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1631611dbc90919063ffffffff16565b5b9150610ee5612710610ed7600460009054906101000a900461ffff1661ffff1685611dd590919063ffffffff16565b611e0890919063ffffffff16565b9050610efa8183611dbc90919063ffffffff16565b9150819250505090565b6000600654905090565b600080611044600a60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611036601060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611028600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b611d8b90919063ffffffff16565b611dbc90919063ffffffff16565b831115151561105257600080fd5b600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054831161109e57826110df565b600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020545b915060008211156112785761113c82600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611dbc90919063ffffffff16565b600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061119482600554611dbc90919063ffffffff16565b600581905550601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e1a00aa33846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050600060405180830381600087803b15801561125f57600080fd5b505af1158015611273573d6000803e3d6000fd5b505050505b601060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546112cb8385611dbc90919063ffffffff16565b116112e8576112e38284611dbc90919063ffffffff16565b611329565b601060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020545b9050600081111561153c5761138681601060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611dbc90919063ffffffff16565b601060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061141b81600860003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b600860003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e1a00aa33836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050600060405180830381600087803b15801561152357600080fd5b505af1158015611537573d6000803e3d6000fd5b505050505b6115b2611564826115568587611dbc90919063ffffffff16565b611dbc90919063ffffffff16565b600a60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b600a60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550505050565b60015481565b6000600b60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550565b6000811180156116a95750601060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548111155b15156116b457600080fd5b61170681601060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611dbc90919063ffffffff16565b601060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061179b81600860003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b600860003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e1a00aa33836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050600060405180830381600087803b1580156118a357600080fd5b505af11580156118b7573d6000803e3d6000fd5b5050505050565b60035481565b600460009054906101000a900461ffff1681565b6000600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6000600554905090565b6000611944600054600154611d8b90919063ffffffff16565b905090565b6000600b60008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff169050919050565b6000600860008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b60025481565b6060600c60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611a4c57600080fd5b600e805480602002602001604051908101604052809291908181526020018280548015611ace57602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311611a84575b5050505050905090565b600c60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000611c4082611b9e57611b99600a60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600760008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611dbc90919063ffffffff16565b611ba1565b60005b611c32601060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600960008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b611d8b90919063ffffffff16565b905092915050565b6000600a60008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6000601060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6000601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6001600b60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550565b60008054611d7760015443611dbc90919063ffffffff16565b101580611d8657506000600254145b905090565b6000808284019050838110151515611d9f57fe5b8091505092915050565b600080823b905060008111915050919050565b6000828211151515611dca57fe5b818303905092915050565b60008082840290506000841480611df65750828482811515611df357fe5b04145b1515611dfe57fe5b8091505092915050565b6000808284811515611e1657fe5b0490508091505092915050565b6000806000808411156124985760009250600091505b600e8054905082101561246157611ee5600654611ed78660076000600e88815481101515611e6357fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611dd590919063ffffffff16565b611e0890919063ffffffff16565b9050600081111561245457600b6000600e84815481101515611f0357fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161561209c576120068160076000600e86815481101515611f9257fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b60076000600e8581548110151561201957fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506120958184611d8b90919063ffffffff16565b9250612453565b600f6000600e848154811015156120af57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16151561234a576121b38160086000600e8681548110151561213f57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b60086000600e858154811015156121c657fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e1a00aa600e8481548110151561227c57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050600060405180830381600087803b15801561232d57600080fd5b505af1158015612341573d6000803e3d6000fd5b50505050612452565b6123d58160106000600e8681548110151561236157fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b60106000600e858154811015156123e857fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055505b5b5b8180600101925050611e39565b61247683600554611d8b90919063ffffffff16565b60058190555061249183600654611d8b90919063ffffffff16565b6006819055505b50505050565b600d60008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16151561262d57600e8290806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506001600d60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055506125ce8273ffffffffffffffffffffffffffffffffffffffff16611da9565b1561262c576001600f60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055505b5b600f60008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff1615612719576126d181601060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b601060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061288c565b601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e1a00aa83836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050600060405180830381600087803b1580156127de57600080fd5b505af11580156127f2573d6000803e3d6000fd5b5050505061284881600860008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b600860008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055505b5050565b600080600091505b600e805490508210156131bb57600a6000600e848154811015156128b857fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905060008111156131ae576129b48160076000600e8681548110151561294057fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611dbc90919063ffffffff16565b60076000600e858154811015156129c757fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550612a4581600654611dbc90919063ffffffff16565b60068190555060035460076000600e85815481101515612a6157fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020541015612d5e57612bd060076000600e85815481101515612ae357fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205460096000600e86815481101515612b5c57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b60096000600e85815481101515612be357fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550612cd960076000600e85815481101515612c6257fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600654611dbc90919063ffffffff16565b600681905550600060076000600e85815481101515612cf457fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055505b6000600a6000600e85815481101515612d7357fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600f6000600e84815481101515612def57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16151561308a57612ef38160086000600e86815481101515612e7f57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b60086000600e85815481101515612f0657fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550601160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639e1a00aa600e84815481101515612fbc57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050600060405180830381600087803b15801561306d57600080fd5b505af1158015613081573d6000803e3d6000fd5b50505050613192565b6131158160106000600e868154811015156130a157fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b60106000600e8581548110151561312857fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055505b6131a781600554611dbc90919063ffffffff16565b6005819055505b8180600101925050612898565b5050565b600080600091505b600e8054905082101561349e5760096000600e848154811015156131e757fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205490506000811180156132eb57506003546132e88260076000600e8781548110151561327457fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b10155b156134915761337b8160076000600e8681548110151561330757fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611d8b90919063ffffffff16565b60076000600e8581548110151561338e57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600060096000600e8581548110151561340c57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061348a81600654611d8b90919063ffffffff16565b6006819055505b81806001019250506131c7565b50505600a165627a7a7230582053fff2d13f812b55057961b5b2cd0ac5f85d40df8b3c0acfa630aa73764b2ee20029";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<>();
    }

    protected Metabank(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Metabank(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<BigInteger> duration() {
        final Function function = new Function("duration", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> startBlock() {
        final Function function = new Function("startBlock", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> minimumAllowedBalance() {
        final Function function = new Function("minimumAllowedBalance", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> heraldFee() {
        final Function function = new Function("heraldFee", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> currentRound() {
        final Function function = new Function("currentRound", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<Metabank> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String s, BigInteger d, BigInteger m, BigInteger h, BigInteger w) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(s),
                new Uint256(d),
                new Uint256(m),
                new Uint16(h),
                new Uint256(w)));
        return deployRemoteCall(Metabank.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Metabank> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String s, BigInteger d, BigInteger m, BigInteger h, BigInteger w) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(s),
                new Uint256(d),
                new Uint256(m),
                new Uint16(h),
                new Uint256(w)));
        return deployRemoteCall(Metabank.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public RemoteCall<TransactionReceipt> handlePayment(BigInteger weiValue) {
        final Function function = new Function(
                "handlePayment", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> allowAccumulate() {
        final Function function = new Function(
                "allowAccumulate", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> disallowAccumulate() {
        final Function function = new Function(
                "disallowAccumulate", 
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

    public RemoteCall<TransactionReceipt> cashoutBenefit(BigInteger value) {
        final Function function = new Function(
                "cashoutBenefit", 
                Arrays.<Type>asList(new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> nextSettlementBlock() {
        final Function function = new Function("nextSettlementBlock", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> canSettle() {
        final Function function = new Function("canSettle", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> calculateRoundTotalBenefit() {
        final Function function = new Function("calculateRoundTotalBenefit", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> calculateRoundHeraldBenefit() {
        final Function function = new Function("calculateRoundHeraldBenefit", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> settle() {
        final Function function = new Function(
                "settle", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> getAccounts() {
        final Function function = new Function("getAccounts", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<BigInteger> allowedWithdrawAmount(String a, Boolean immediate) {
        final Function function = new Function("allowedWithdrawAmount", 
                Arrays.<Type>asList(new Address(a),
                new Bool(immediate)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String a) {
        final Function function = new Function("balanceOf", 
                Arrays.<Type>asList(new Address(a)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> paymentHistoryOf(String a) {
        final Function function = new Function("paymentHistoryOf", 
                Arrays.<Type>asList(new Address(a)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> depositRequestsOf(String a) {
        final Function function = new Function("depositRequestsOf", 
                Arrays.<Type>asList(new Address(a)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> withdrawRequestsOf(String a) {
        final Function function = new Function("withdrawRequestsOf", 
                Arrays.<Type>asList(new Address(a)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isAccumulative(String a) {
        final Function function = new Function("isAccumulative", 
                Arrays.<Type>asList(new Address(a)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> readyCashoutOf(String a) {
        final Function function = new Function("readyCashoutOf", 
                Arrays.<Type>asList(new Address(a)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getTotal() {
        final Function function = new Function("getTotal", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getTotalBalances() {
        final Function function = new Function("getTotalBalances", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getSafebox() {
        final Function function = new Function("getSafebox", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static Metabank load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Metabank(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Metabank load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Metabank(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
