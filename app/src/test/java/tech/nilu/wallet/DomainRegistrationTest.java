package tech.nilu.wallet;

import org.junit.Before;
import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.ens.NameHash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Single;
import tech.nilu.wallet.repository.contracts.DefaultReverseResolver;
import tech.nilu.wallet.repository.contracts.NNSRegistry;
import tech.nilu.wallet.repository.contracts.PublicResolver;
import tech.nilu.wallet.repository.contracts.ResolverInterface;
import tech.nilu.wallet.repository.contracts.ReverseRegistrar;
import tech.nilu.wallet.repository.contracts.SubnodeRegistrar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DomainRegistrationTest {
    private static final String ENS_ADDRESS = "0xd878ff289b0033cb4ae35c18f19e901f461f9997";
    private static final String RESOLVER_ADDRESS = "0x821fac8be5c2b44b23616bf0608ecae47e4532cf";
    private static final String REGISTRAR_ADDRESS = "0xee2e76a02567eb1bf17311ba2d243dbc54c04e38";
    private static final String SUBNODE_REGISTRAR_ADDRESS = "0x1c8a2cb13b22164e74b2b76712b81b4671eb80e0";
    private static final String REVERSE_RESOLVER_ADDRESS = "0x25f049ef55c693bc6f0cfb983f329d49479ec43c";
    private static final String REVERSE_REGISTRAR_ADDRESS = "0x55829fe3be9a3ad4c49f69513c9ea6ccb1d0174f";
    /*private static final String ENS_ADDRESS = "0xad7c0d0e5e8020c88a317187f7b3ed7b5738e762";
    private static final String RESOLVER_ADDRESS = "0x878fa3971ac56440e05398568132eb0eec129aa8";
    private static final String REGISTRAR_ADDRESS = "0x0a2a92d6f68aa2c48b5382104f5c5a5545aab258";
    private static final String SUBNODE_REGISTRAR_ADDRESS = "0x11ef9fb9cbb9a053232a2935f896986479b0ee78";
    private static final String REVERSE_RESOLVER_ADDRESS = "0x6dc787e634e358290f9421a713ef286ae9e0542e";
    private static final String REVERSE_REGISTRAR_ADDRESS = "0x96568eedcfedd99a2507c37c64cf79d8e7c624c9";*/

    private Tuple4<Web3j, Credentials, BigInteger, BigInteger> params;

    private Tuple4<Web3j, Credentials, BigInteger, BigInteger> getParams() {
        return params;
    }

    @Before
    public void initParams() throws Exception {
        Web3j web3j = Web3Util.getWeb3j(true);
        Credentials credentials = Web3Util.getCredential(true);
        BigInteger gasLimit = new BigInteger("2000000");
        BigInteger gasPrice = new BigInteger("20000000000");
        try {
            gasPrice = web3j.ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.params = new Tuple4<>(web3j, credentials, gasPrice, gasLimit);
    }

    @Test
    public void getReverseRegistrar() throws Exception {
        NNSRegistry nns = ens();
        System.out.println(nns.owner(NameHash.nameHashAsBytes("addr.reverse")).send());
    }

    @Test
    public void resolution() throws Exception {
        NNSRegistry nns = ens();
        byte[] node = NameHash.nameHashAsBytes("dev.me.nilu");
        String resolverAddr = nns.resolver(node).send();
        System.out.println(resolver(resolverAddr).addr(node).send());
    }

    @Test
    public void owner() throws Exception {
        NNSRegistry nns = ens();
        PublicResolver resolver = resolver();
        byte[] node = NameHash.nameHashAsBytes("novd.me.nilu");
        System.out.println(resolver.addr(node).send());
    }

    @Test
    public void reverseResolution() throws Exception {
        Web3j web3j = getParams().getValue1();
        Credentials credentials = getParams().getValue2();
        NNSRegistry nns = ens();

        byte[] node = NameHash.nameHashAsBytes(credentials.getAddress().substring(2).toLowerCase() + ".addr.reverse");
        String resolver = nns.resolver(node).send();
        DefaultReverseResolver reverseResolver = DefaultReverseResolver.load(resolver,
                web3j,
                credentials,
                null,
                null
        );
        System.out.println(reverseResolver.name(node).send());
    }

    @Test
    public void registerDomain() throws Exception {
        Web3j web3j = getParams().getValue1();
        Credentials credentials = getParams().getValue2();
        NNSRegistry nns = ens();
        PublicResolver resolver = resolver();
        SubnodeRegistrar registrar = subnodeRegistrar();
        DefaultReverseResolver reverseResolver = reverseResolver();

        byte[] node = NameHash.nameHashAsBytes("dev.me.nilu");
        byte[] reverseNode = NameHash.nameHashAsBytes(credentials.getAddress().substring(2).toLowerCase() + ".addr.reverse");
        Single.fromFuture(web3j.ethGasPrice().sendAsync())
                .subscribe(price -> {
                    Single.fromCallable(() -> {
                        TransactionReceipt receipt1 = registrar.setSubnodeOwner(NameHash.nameHashAsBytes("me.nilu"), Hash.sha3("dev".getBytes()), credentials.getAddress()).send();
                        TransactionReceipt receipt2 = resolver.setAddr(node, credentials.getAddress()).send();
                        TransactionReceipt receipt3 = nns.setResolver(node, resolver.getContractAddress()).send();

                        ReverseRegistrar rr = ReverseRegistrar.load(nns.owner(NameHash.nameHashAsBytes("addr.reverse")).send(),
                                web3j,
                                credentials,
                                getParams().getValue3(),
                                getParams().getValue4()
                        );
                        TransactionReceipt receipt4 = rr.claim(credentials.getAddress().toLowerCase()).send();
                        TransactionReceipt receipt5 = nns.setResolver(reverseNode, reverseResolver.getContractAddress()).send();
                        TransactionReceipt receipt6 = reverseResolver.setName(reverseNode, "dev.me.nilu").send();
                        return true;
                    }).subscribe(receipts -> {
                        String resolverAddress = nns.resolver(node).send();
                        String resolver1 = resolver(resolverAddress).addr(node).send();
                        String owner1 = nns.owner(reverseNode).send();
                        String resolver2 = nns.resolver(reverseNode).send();
                        String name = reverseResolver.name(reverseNode).send();

                        System.out.println(resolverAddress);
                        System.out.println(resolver1);
                        System.out.println(owner1);
                        System.out.println(resolver2);
                        System.out.println(name);
                        assertThat(resolverAddress, is(RESOLVER_ADDRESS));
                        assertThat(resolver1, is(credentials.getAddress()));
                        assertThat(owner1, is(credentials.getAddress()));
                        assertThat(resolver2, is(REVERSE_RESOLVER_ADDRESS));
                        assertThat(name, is("dev.me.nilu"));
                    }, Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }

    @Test
    public void estimateGas() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        Web3j web3j = params.getValue1();
        Credentials credentials = params.getValue2();
        Function function = new Function("setName",
                Arrays.asList(new Bytes32(NameHash.nameHashAsBytes(credentials.getAddress().substring(2).toLowerCase() + ".addr.reverse")),
                        new Utf8String("")),
                Collections.emptyList());
        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
        Transaction tx = new Transaction(credentials.getAddress(),
                nonce,
                null,
                null,
                REVERSE_RESOLVER_ADDRESS,
                BigInteger.ZERO,
                FunctionEncoder.encode(function));
        System.out.println(web3j.ethEstimateGas(tx).send().getAmountUsed().toString());
    }

    @Test
    public void releaseDomain() throws Exception {
        Web3j web3j = getParams().getValue1();
        Credentials credentials = getParams().getValue2();
        NNSRegistry nns = ens();
        PublicResolver resolver = resolver();
        DefaultReverseResolver reverseResolver = reverseResolver();

        String zeroAddress = Address.DEFAULT.toString();
        byte[] node = NameHash.nameHashAsBytes("sia.me.nilu");
        byte[] reverseNode = NameHash.nameHashAsBytes(credentials.getAddress().substring(2).toLowerCase() + ".addr.reverse");
        Single.fromFuture(web3j.ethGasPrice().sendAsync())
                .subscribe(price -> {
                    Single.fromCallable(() -> {
                        TransactionReceipt receipt1 = resolver.setAddr(node, zeroAddress).send();
                        TransactionReceipt receipt2 = nns.setResolver(node, zeroAddress).send();
                        TransactionReceipt receipt3 = nns.setOwner(node, zeroAddress).send();
                        TransactionReceipt receipt4 = reverseResolver.setName(reverseNode, "").send();
                        TransactionReceipt receipt5 = nns.setResolver(reverseNode, zeroAddress).send();
                        TransactionReceipt receipt6 = nns.setOwner(reverseNode, zeroAddress).send();
                        return true;
                    }).subscribe(receipts -> {
                        String resolver1 = resolver.addr(node).send();
                        String resolver2 = nns.resolver(node).send();
                        String owner1 = nns.owner(node).send();
                        String reverseResolver1 = reverseResolver.name(reverseNode).send();
                        String reverseResolver2 = nns.resolver(reverseNode).send();
                        String owner2 = nns.owner(reverseNode).send();

                        System.out.println(resolver1);
                        System.out.println(resolver2);
                        System.out.println(owner1);
                        System.out.println(reverseResolver1);
                        System.out.println(reverseResolver2);
                        System.out.println(owner2);
                        assertThat(resolver1, is(zeroAddress));
                        assertThat(resolver2, is(zeroAddress));
                        assertThat(owner1, is(zeroAddress));
                        assertThat(reverseResolver1, is(""));
                        assertThat(reverseResolver2, is(zeroAddress));
                        assertThat(owner2, is(zeroAddress));
                    }, Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }

    @Test
    public void initDNS() throws Exception {
        NNSRegistry nns = ens();
        FIFSRegistrar registrar = registrar();
        SubnodeRegistrar subnodeRegistrar = subnodeRegistrar();
        ReverseRegistrar reverseRegistrar = reverseRegistrar();
        System.out.println(nns.setSubnodeOwner(NameHash.nameHashAsBytes(""), Hash.sha3("nilu".getBytes()), registrar.getContractAddress()).send());
        System.out.println(registrar.register(Hash.sha3("me".getBytes()), SUBNODE_REGISTRAR_ADDRESS).send());
        System.out.println(subnodeRegistrar.setResolver(NameHash.nameHashAsBytes("me.nilu")).send());
        System.out.println(nns.setSubnodeOwner(NameHash.nameHashAsBytes(""), Hash.sha3("reverse".getBytes()), "0x0551Ba1E8a179459Ae5281B26de5f63a8A248f5e").send());
        System.out.println(nns.setSubnodeOwner(NameHash.nameHashAsBytes("reverse"), Hash.sha3("addr".getBytes()), reverseRegistrar.getContractAddress()).send());
    }

    @Test
    public void deployAll() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        NNSRegistry ens = NNSRegistry.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        ).send();
        PublicResolver resolver = PublicResolver.deploy(
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ens.getContractAddress()
        ).send();
        FIFSRegistrar registrar = FIFSRegistrar.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ens.getContractAddress(),
                NameHash.nameHashAsBytes("nilu")
        ).send();
        SubnodeRegistrar subnodeRegistrar = SubnodeRegistrar.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ens.getContractAddress(),
                resolver.getContractAddress()
        ).send();
        DefaultReverseResolver reverseResolver = DefaultReverseResolver.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ens.getContractAddress()
        ).send();
        ReverseRegistrar reverseRegistrar = ReverseRegistrar.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ens.getContractAddress(),
                reverseResolver.getContractAddress()
        ).send();
        System.out.println("ENS: " + ens.getContractAddress());
        System.out.println("RESOLVER: " + resolver.getContractAddress());
        System.out.println("REGISTRAR: " + registrar.getContractAddress());
        System.out.println("SUBNODE: " + subnodeRegistrar.getContractAddress());
        System.out.println("REVERSE RESOLVER: " + reverseResolver.getContractAddress());
        System.out.println("REVERSE REGISTRAR: " + reverseRegistrar.getContractAddress());
    }

    private NNSRegistry ens() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        return NNSRegistry.load(ENS_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private PublicResolver resolver() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        return PublicResolver.load(RESOLVER_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private ResolverInterface resolver(String address) throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        return ResolverInterface.load(address,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private FIFSRegistrar registrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        return FIFSRegistrar.load(REGISTRAR_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private SubnodeRegistrar subnodeRegistrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        return SubnodeRegistrar.load(SUBNODE_REGISTRAR_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private DefaultReverseResolver reverseResolver() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        return DefaultReverseResolver.load(REVERSE_RESOLVER_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private ReverseRegistrar reverseRegistrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getParams();
        return ReverseRegistrar.load(REVERSE_REGISTRAR_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }
}
