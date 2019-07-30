package tech.nilu.wallet;

import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.ens.NameHash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Single;
import tech.nilu.wallet.repository.contracts.NNSRegistry;
import tech.nilu.wallet.repository.contracts.PublicResolver;
import tech.nilu.wallet.repository.contracts.ResolverInterface;
import tech.nilu.wallet.repository.contracts.SubnodeRegistrar;

public class NNSTest {
    private static final String ENS_ADDRESS = "0xd878ff289b0033cb4ae35c18f19e901f461f9997";
    private static final String RESOLVER_ADDRESS = "0x821fac8be5c2b44b23616bf0608ecae47e4532cf";
    private static final String REGISTRAR_ADDRESS = "0xee2e76a02567eb1bf17311ba2d243dbc54c04e38";
    private static final String SUBNODE_REGISTRAR_ADDRESS = "0x1c8a2cb13b22164e74b2b76712b81b4671eb80e0";
    /*private static final String ENS_ADDRESS = "0xad7c0d0e5e8020c88a317187f7b3ed7b5738e762";
    private static final String RESOLVER_ADDRESS = "0x878fa3971ac56440e05398568132eb0eec129aa8";
    private static final String REGISTRAR_ADDRESS = "0x0a2a92d6f68aa2c48b5382104f5c5a5545aab258";
    private static final String SUBNODE_REGISTRAR_ADDRESS = "0x11ef9fb9cbb9a053232a2935f896986479b0ee78";*/

    @Test
    public void setRootNode() throws Exception {
        NNSRegistry ens = ens();
        FIFSRegistrar registrar = registrar();
        System.out.println(ens.setSubnodeOwner(NameHash.nameHashAsBytes(""), Hash.sha3("nilu".getBytes()), registrar.getContractAddress()).send());
    }

    @Test
    public void setSubnode() throws Exception {
        SubnodeRegistrar subnodeRegistrar = subnodeRegistrar();
        System.out.println(subnodeRegistrar.setSubnodeOwner(NameHash.nameHashAsBytes("me.nilu"), Hash.sha3("novd".getBytes()), "0x211f465DDEC92909f569003571cd6725c85821D4").send());
    }

    @Test
    public void registerDomain() throws Exception {
        FIFSRegistrar registrar = registrar();
        System.out.println(registrar.register(Hash.sha3("me".getBytes()), SUBNODE_REGISTRAR_ADDRESS).send());
    }

    @Test
    public void setSubnodeRegistrarResolver() throws Exception {
        SubnodeRegistrar subnodeRegistrar = subnodeRegistrar();
        System.out.println(subnodeRegistrar.setResolver(NameHash.nameHashAsBytes("me.nilu")).send());
    }

    @Test
    public void setResolver() throws Exception {
        NNSRegistry ens = ens();
        PublicResolver resolver = resolver();
        System.out.println(resolver.setAddr(NameHash.nameHashAsBytes("novd.me.nilu"), "0x211f465DDEC92909f569003571cd6725c85821D4").send());
        System.out.println(ens.setResolver(NameHash.nameHashAsBytes("novd.me.nilu"), resolver.getContractAddress()).send());
    }

    @Test
    public void getAddress() throws Exception {
        NNSRegistry ens = ens();
        byte[] node = NameHash.nameHashAsBytes("novd.me.nilu");
        String resolverAddr = ens.resolver(node).send();
        System.out.println(resolver(resolverAddr).addr(node).send());
    }

    @Test
    public void transferOwner() throws Exception {
        NNSRegistry ens = ens();
        PublicResolver resolver = resolver();
        System.out.println(resolver.setAddr(NameHash.nameHashAsBytes("astrain.me.nilu"), Address.DEFAULT.toString()).send());
        System.out.println(ens.setResolver(NameHash.nameHashAsBytes("astrain.me.nilu"), Address.DEFAULT.toString()).send());
        System.out.println(ens.setOwner(NameHash.nameHashAsBytes("astrain.me.nilu"), Address.DEFAULT.toString()).send());
    }

    @Test
    public void estimateGas() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        Web3j web3j = params.getValue1();
        Credentials credentials = params.getValue2();
        Function function = new Function("setOwner",
                Arrays.<Type>asList(new Bytes32(NameHash.nameHashAsBytes("novd.me.nilu")),
                        new Address(Address.DEFAULT.toString())),
                Collections.<TypeReference<?>>emptyList());
        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
        Transaction tx = new Transaction(credentials.getAddress(),
                nonce,
                null,
                null,
                ENS_ADDRESS,
                BigInteger.ZERO,
                FunctionEncoder.encode(function));
        System.out.println(web3j.ethEstimateGas(tx).send().getAmountUsed().toString());
    }

    @Test
    public void getOwner() throws Exception {
        NNSRegistry ens = ens();
        System.out.println(ens.owner(NameHash.nameHashAsBytes("novd.me.nilu")).send());
    }

    @Test
    public void getResolver() throws Exception {
        NNSRegistry ens = ens();
        System.out.println(ens.resolver(NameHash.nameHashAsBytes("novd.me.nilu")).send());
    }

    @Test
    public void addr() throws Exception {
        PublicResolver resolver = resolver();
        System.out.println(resolver.addr(NameHash.nameHashAsBytes("mariam.me.nilu")).send());
    }

    @Test
    public void deployENS() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        NNSRegistry ens = NNSRegistry.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        ).send();
        System.out.println(ens.getContractAddress());
    }

    @Test
    public void deployResolver() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        PublicResolver resolver = PublicResolver.deploy(
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ENS_ADDRESS
        ).send();
        System.out.println(resolver.getContractAddress());
    }

    @Test
    public void deployRegistrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        FIFSRegistrar registrar = FIFSRegistrar.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ENS_ADDRESS,
                NameHash.nameHashAsBytes("nilu")
        ).send();
        System.out.println(registrar.getContractAddress());
    }

    @Test
    public void deploySubnodeRegistrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        SubnodeRegistrar subnodeRegistrar = SubnodeRegistrar.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ENS_ADDRESS,
                RESOLVER_ADDRESS
        ).send();
        System.out.println(subnodeRegistrar.getContractAddress());
    }

    private Tuple4<Web3j, Credentials, BigInteger, BigInteger> getDeployParams() throws Exception {
        Web3j web3j = Web3Util.getWeb3j(true);
        Credentials credentials = Web3Util.getCredential(true);
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = new BigInteger("2200000");
        return new Tuple4<>(web3j, credentials, gasPrice, gasLimit);
    }

    private NNSRegistry ens() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        return NNSRegistry.load(ENS_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private PublicResolver resolver() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        return PublicResolver.load(RESOLVER_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private ResolverInterface resolver(String address) throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        return ResolverInterface.load(address,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private FIFSRegistrar registrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        return FIFSRegistrar.load(REGISTRAR_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private SubnodeRegistrar subnodeRegistrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        return SubnodeRegistrar.load(SUBNODE_REGISTRAR_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    @Test
    public void register() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        Single.fromFuture(params.getValue1().ethGasPrice().sendAsync())
                .subscribe(x -> {
                    System.out.println(x.getGasPrice());

                    SubnodeRegistrar registrar = SubnodeRegistrar.load(SUBNODE_REGISTRAR_ADDRESS,
                            params.getValue1(),
                            params.getValue2(),
                            x.getGasPrice(),
                            BigInteger.valueOf(75000)
                    );
                    Single.fromFuture(registrar.setSubnodeOwner(NameHash.nameHashAsBytes("me.nilu"), Hash.sha3("sia".getBytes()), "0x015146A3c77a534FCdaAA97f90D63125B07032B3").sendAsync())
                            .subscribe(y -> {
                                System.out.println(y);

                                Single.fromCallable(() -> {
                                    PublicResolver resolver = PublicResolver.load(RESOLVER_ADDRESS,
                                            params.getValue1(),
                                            params.getValue2(),
                                            x.getGasPrice(),
                                            BigInteger.valueOf(60000)
                                    );
                                    NNSRegistry registry = NNSRegistry.load(ENS_ADDRESS,
                                            params.getValue1(),
                                            params.getValue2(),
                                            x.getGasPrice(),
                                            BigInteger.valueOf(60000)
                                    );

                                    TransactionReceipt receipt1 = resolver.setAddr(NameHash.nameHashAsBytes("sia.me.nilu"), "0x015146A3c77a534FCdaAA97f90D63125B07032B3").send();
                                    TransactionReceipt receipt2 = registry.setResolver(NameHash.nameHashAsBytes("sia.me.nilu"), resolver.getContractAddress()).send();
                                    return new Tuple2(receipt1, receipt2);
                                }).subscribe(z -> {
                                    System.out.println(z.getValue1());
                                    System.out.println(z.getValue2());
                                });
                            });
                }, Throwable::printStackTrace);
    }
}
