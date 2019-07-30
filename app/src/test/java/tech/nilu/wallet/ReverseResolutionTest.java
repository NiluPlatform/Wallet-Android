package tech.nilu.wallet;

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
import org.web3j.tuples.generated.Tuple4;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import tech.nilu.wallet.repository.contracts.DefaultReverseResolver;
import tech.nilu.wallet.repository.contracts.NNSRegistry;
import tech.nilu.wallet.repository.contracts.ReverseRegistrar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReverseResolutionTest {
    private static final String ENS_ADDRESS = "0xd878ff289b0033cb4ae35c18f19e901f461f9997";
    private static final String REVERSE_RESOLVER_ADDRESS = "0x25f049ef55c693bc6f0cfb983f329d49479ec43c";
    private static final String REVERSE_REGISTRAR_ADDRESS = "0x55829fe3be9a3ad4c49f69513c9ea6ccb1d0174f";
    /*private static final String ENS_ADDRESS = "0xad7c0d0e5e8020c88a317187f7b3ed7b5738e762";
    private static final String REVERSE_RESOLVER_ADDRESS = "0x878fa3971ac56440e05398568132eb0eec129aa8";
    private static final String REVERSE_REGISTRAR_ADDRESS = "0x0a2a92d6f68aa2c48b5382104f5c5a5545aab258";*/

    @Test
    public void registerReverseDomain() throws Exception {
        NNSRegistry ens = ens();
        ReverseRegistrar reverseRegistrar = reverseRegistrar();
        System.out.println(ens.setSubnodeOwner(NameHash.nameHashAsBytes(""), Hash.sha3("reverse".getBytes()), "0x585DbC24539a01565a65F56D55c5697248E01Ed2").send());
        System.out.println(ens.setSubnodeOwner(NameHash.nameHashAsBytes("reverse"), Hash.sha3("addr".getBytes()), reverseRegistrar.getContractAddress()).send());
    }

    @Test
    public void reverseRegistration() throws Exception {
        NNSRegistry ens = ens();
        DefaultReverseResolver reverseResolver = reverseResolver();
        String reverseRegistrarAddress = ens.owner(NameHash.nameHashAsBytes("addr.reverse")).send();

        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        Credentials credentials = params.getValue2();
        ReverseRegistrar reverseRegistrar = ReverseRegistrar.load(reverseRegistrarAddress,
                params.getValue1(),
                credentials,
                params.getValue3(),
                params.getValue4()
        );

        byte[] node = NameHash.nameHashAsBytes(credentials.getAddress().substring(2).toLowerCase() + ".addr.reverse");
        System.out.println(reverseRegistrar.claim(credentials.getAddress().toLowerCase()).send());
        System.out.println(ens.setResolver(node, REVERSE_RESOLVER_ADDRESS).send());
        System.out.println(reverseResolver.setName(node, "novd.me.nilu").send());
        assertThat(ens.owner(node).send(), is(credentials.getAddress().toLowerCase()));
        assertThat(ens.resolver(node).send(), is(REVERSE_RESOLVER_ADDRESS));
        assertThat(reverseResolver.name(node).send(), is("novd.me.nilu"));
    }

    @Test
    public void reverseResolution() throws Exception {
        NNSRegistry ens = ens();

        byte[] node = NameHash.nameHashAsBytes(getDeployParams().getValue2().getAddress().substring(2).toLowerCase() + ".addr.reverse");
        String resolver = ens.resolver(node).send();

        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        DefaultReverseResolver reverseResolver = DefaultReverseResolver.load(resolver,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
        String domain = reverseResolver.name(node).send();
        System.out.println(domain);
        assertThat(domain, is("novd.me.nilu"));
    }

    @Test
    public void getOwner() throws Exception {
        NNSRegistry ens = ens();
        System.out.println(ens.owner(NameHash.nameHashAsBytes("c9c2dbec5afa62520f02c46ea2f193525eb2751d.addr.reverse")).send());
    }

    @Test
    public void getResolver() throws Exception {
        NNSRegistry ens = ens();
        System.out.println(ens.resolver(NameHash.nameHashAsBytes("c9c2dbec5afa62520f02c46ea2f193525eb2751d.addr.reverse")).send());
    }

    @Test
    public void getReverseResolver() throws Exception {
        DefaultReverseResolver reverseResolver = reverseResolver();
        System.out.println(reverseResolver.name(NameHash.nameHashAsBytes("c9c2dbec5afa62520f02c46ea2f193525eb2751d.addr.reverse")).send());
    }

    @Test
    public void transferOwner() throws Exception {
        NNSRegistry ens = ens();
        DefaultReverseResolver reverseResolver = reverseResolver();

        byte[] node = NameHash.nameHashAsBytes("c9c2dbec5afa62520f02c46ea2f193525eb2751d.addr.reverse");
        System.out.println(reverseResolver.setName(node, "").send());
        System.out.println(ens.setResolver(node, Address.DEFAULT.toString()).send());
        System.out.println(ens.setOwner(node, Address.DEFAULT.toString()).send());
    }

    @Test
    public void estimateGas() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        Web3j web3j = params.getValue1();
        Credentials credentials = params.getValue2();
        Function function = new Function("setName",
                Arrays.<Type>asList(new Bytes32(NameHash.nameHashAsBytes("c9c2dbec5afa62520f02c46ea2f193525eb2751d.addr.reverse")),
                        new Utf8String("novd.me.nilu")),
                Collections.<TypeReference<?>>emptyList());
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
    public void testRegistrar() throws Exception {
        ReverseRegistrar reverseRegistrar = reverseRegistrar();
        byte[] node = NameHash.nameHashAsBytes("0551Ba1E8a179459Ae5281B26de5f63a8A248f5e".toLowerCase() + ".addr.reverse");
        assertThat(reverseRegistrar.node("0x0551Ba1E8a179459Ae5281B26de5f63a8A248f5e").send(), is(node));
    }

    @Test
    public void testClaim() throws Exception {
        NNSRegistry ens = ens();
        ReverseRegistrar reverseRegistrar = reverseRegistrar();
        byte[] node = NameHash.nameHashAsBytes("0551Ba1E8a179459Ae5281B26de5f63a8A248f5e".toLowerCase() + ".addr.reverse");
        System.out.println(reverseRegistrar.claim("0x3e5660F4F03d19c686A131a4931a9Aa9EaC35214").send());
        assertThat(ens.owner(node).send(), is("0x3e5660F4F03d19c686A131a4931a9Aa9EaC35214"));
    }

    @Test
    public void testClaimWithResolver() throws Exception {
        NNSRegistry ens = ens();
        ReverseRegistrar reverseRegistrar = reverseRegistrar();
        byte[] node = NameHash.nameHashAsBytes("0551Ba1E8a179459Ae5281B26de5f63a8A248f5e".toLowerCase() + ".addr.reverse");
        System.out.println(reverseRegistrar.claimWithResolver("0x3e5660f4f03d19c686a131a4931a9aa9eac35214", "0x211f465ddec92909f569003571cd6725c85821d4").send());
        assertThat(ens.owner(node).send(), is("0x3e5660f4f03d19c686a131a4931a9aa9eac35214"));
        assertThat(ens.resolver(node).send(), is("0x211f465ddec92909f569003571cd6725c85821d4"));
    }

    @Test
    public void testOverwrite() throws Exception {
        NNSRegistry ens = ens();
        ReverseRegistrar reverseRegistrar = reverseRegistrar();
        byte[] node = NameHash.nameHashAsBytes("0551Ba1E8a179459Ae5281B26de5f63a8A248f5e".toLowerCase() + ".addr.reverse");
        System.out.println(reverseRegistrar.claimWithResolver("0x3e5660f4f03d19c686a131a4931a9aa9eac35214", "0x211f465ddec92909f569003571cd6725c85821d4").send());
        System.out.println(reverseRegistrar.claim("0x015146a3c77a534fcdaaa97f90d63125b07032b3").send());
        assertThat(ens.owner(node).send(), is("0x015146a3c77a534fcdaaa97f90d63125b07032b3"));
        assertThat(ens.resolver(node).send(), is("0x211f465ddec92909f569003571cd6725c85821d4"));
    }

    @Test
    public void testRegistration() throws Exception {
        NNSRegistry ens = ens();
        ReverseRegistrar reverseRegistrar = reverseRegistrar();
        DefaultReverseResolver reverseResolver = reverseResolver();

        byte[] node = NameHash.nameHashAsBytes("0551Ba1E8a179459Ae5281B26de5f63a8A248f5e".toLowerCase() + ".addr.reverse");
        System.out.println(reverseRegistrar.setName("testname").send());
        assertThat(ens.resolver(node).send(), is(reverseResolver.getContractAddress()));
        assertThat(reverseResolver.name(node).send(), is("testname"));
    }

    @Test
    public void testNonOwner() throws Exception {
        ReverseRegistrar reverseRegistrar = reverseRegistrar();
        DefaultReverseResolver reverseResolver = reverseResolver();

        byte[] node = NameHash.nameHashAsBytes("0551Ba1E8a179459Ae5281B26de5f63a8A248f5e".toLowerCase() + ".addr.reverse");
        System.out.println(reverseRegistrar.claimWithResolver("0x3e5660f4f03d19c686a131a4931a9aa9eac35214", reverseResolver.getContractAddress()).send());
        System.out.println(reverseResolver.setName(node, "testname").send());
    }

    private Tuple4<Web3j, Credentials, BigInteger, BigInteger> getDeployParams() throws Exception {
        Web3j web3j = Web3Util.getWeb3j(true);
        Credentials credentials = Web3Util.getCredential(true);
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = new BigInteger("2200000");
        return new Tuple4<>(web3j, credentials, gasPrice, gasLimit);
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
    public void deployReverseResolver() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        DefaultReverseResolver reverseResolver = DefaultReverseResolver.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ENS_ADDRESS
        ).send();
        System.out.println(reverseResolver.getContractAddress());
    }

    @Test
    public void deployReverseRegistrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        ReverseRegistrar reverseRegistrar = ReverseRegistrar.deploy(params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4(),
                ENS_ADDRESS,
                REVERSE_RESOLVER_ADDRESS
        ).send();
        System.out.println(reverseRegistrar.getContractAddress());
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

    private DefaultReverseResolver reverseResolver() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        return DefaultReverseResolver.load(REVERSE_RESOLVER_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }

    private ReverseRegistrar reverseRegistrar() throws Exception {
        Tuple4<Web3j, Credentials, BigInteger, BigInteger> params = getDeployParams();
        return ReverseRegistrar.load(REVERSE_REGISTRAR_ADDRESS,
                params.getValue1(),
                params.getValue2(),
                params.getValue3(),
                params.getValue4()
        );
    }
}
