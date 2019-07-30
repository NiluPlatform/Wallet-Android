package tech.nilu.wallet;

import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.WalletUtils;
import org.web3j.ens.NameHash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import tech.nilu.wallet.crypto.Bip44WalletUtils;
import tech.nilu.wallet.repository.contracts.NNSRegistry;
import tech.nilu.wallet.repository.contracts.PublicResolver;
import tech.nilu.wallet.repository.contracts.SubnodeRegistrar;
import tech.nilu.wallet.util.SecurityUtils;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testEncryption() {
        final char[] CHARS = "0123456789@ABCDEFGHIJKLMNOPQRSTUVWXYZ#abcdefghijklmnopqrstuvwxyz".toCharArray();
        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 32; i++) {
            char c = CHARS[random.nextInt(CHARS.length)];
            result.append(c);
            System.out.print((int) c + ", ");
        }
        System.out.println(result.toString());
    }

    @Test
    public void testSomething() {
        final char[] iv = "kkw3Z/BquRPcb31ohSGdww==".toCharArray();
        for (char anIv : iv) {
            System.out.print((int) anIv + ", ");
        }
    }

    @Test
    public void walletUtils_ImportFromMnemonics_ReturnsTrue() throws CipherException, IOException {
        Credentials credentials = Bip44WalletUtils.loadBip44Credentials("");
        String name = WalletUtils.generateWalletFile("1234567890", credentials.getEcKeyPair(), new File("/tmp"), false);
        assertThat(name, startsWith("UTC"));
    }

    @Test
    public void walletUtils_ImportFromKeystore_ReturnsTrue() throws IOException, CipherException {
        File file = File.createTempFile("json-", null);
        FileOutputStream stream = new FileOutputStream(file);
        stream.write("".getBytes());
        stream.close();
        Credentials credentials = WalletUtils.loadCredentials("", file);
        String name = WalletUtils.generateWalletFile("1234567890", credentials.getEcKeyPair(), new File("/tmp"), true);
        assertThat(name, startsWith("UTC"));
    }

    @Test
    public void walletUtils_ImportFromPrivateKey_ReturnsTrue() throws CipherException, IOException {
        Credentials credentials = Credentials.create("7082ff4995072eae33f8323d00e9afbbb8025213821017926415bdedb63e35ac");
        String name = WalletUtils.generateWalletFile("1234567890", credentials.getEcKeyPair(), new File("/tmp"), false);
        assertThat(name, startsWith("UTC"));
    }

    @Test
    public void registerDomain() throws Exception {
        Credentials credentials = Credentials.create("");
        Web3j web3j = Web3jFactory.build(new HttpService("https://walletapi.nilu.tech", new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build(), true));

        Single.fromFuture(web3j.ethGasPrice().sendAsync())
                .subscribe(x -> {
                    System.out.println(x.getGasPrice());

                    SubnodeRegistrar registrar = SubnodeRegistrar.load("0x1c8a2cb13b22164e74b2b76712b81b4671eb80e0",
                            web3j,
                            credentials,
                            x.getGasPrice(),
                            BigInteger.valueOf(75000)
                    );
                    Single.fromFuture(registrar.setSubnodeOwner(NameHash.nameHashAsBytes("me.nilu"), Hash.sha3("sia".getBytes()), credentials.getAddress()).sendAsync())
                            .subscribe(y -> {
                                System.out.println(y);

                                Single.fromCallable(() -> {
                                    PublicResolver resolver = PublicResolver.load("0x821fac8be5c2b44b23616bf0608ecae47e4532cf",
                                            web3j,
                                            credentials,
                                            x.getGasPrice(),
                                            BigInteger.valueOf(60000)
                                    );
                                    NNSRegistry registry = NNSRegistry.load("0xd878ff289b0033cb4ae35c18f19e901f461f9997",
                                            web3j,
                                            credentials,
                                            x.getGasPrice(),
                                            BigInteger.valueOf(60000)
                                    );
                                    TransactionReceipt receipt1 = resolver.setAddr(NameHash.nameHashAsBytes("sia.me.nilu"), credentials.getAddress()).send();
                                    TransactionReceipt receipt2 = registry.setResolver(NameHash.nameHashAsBytes("sia.me.nilu"), resolver.getContractAddress()).send();
                                    return new Tuple2(receipt1, receipt2);
                                }).subscribe(z -> {

                                }, Throwable::printStackTrace);
                            }, Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }

    @Test
    public void estimateRegistrar() throws Exception {
        Web3j web3j = Web3jFactory.build(new HttpService("https://walletapi.nilu.tech", new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build(), true));
        Credentials credentials = Credentials.create("7082ff4995072eae33f8323d00e9afbbb8025213821017926415bdedb63e35ac");
        SubnodeRegistrar registrar = SubnodeRegistrar.load("0x1c8a2cb13b22164e74b2b76712b81b4671eb80e0",
                web3j,
                credentials,
                null,
                null
        );
        String registrarData = FunctionEncoder.encode(new Function("setSubnodeOwner",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(NameHash.nameHashAsBytes("me.nilu")),
                        new org.web3j.abi.datatypes.generated.Bytes32(Hash.sha3("siavash".getBytes())),
                        new Address(credentials.getAddress())),
                Collections.<TypeReference<?>>emptyList()));
        Transaction tx = new Transaction(null,
                null,
                null,
                null,
                registrar.getContractAddress(),
                null,
                registrarData
        );
        System.out.println(web3j.ethEstimateGas(tx).send().getAmountUsed().toString());
    }

    @Test
    public void estimateResolver() throws Exception {
        Web3j web3j = Web3jFactory.build(new HttpService("https://walletapi.nilu.tech", new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build(), true));
        Credentials credentials = Credentials.create("d8e89aca91120b6258a542ebf815696dccc5f7fe8a25d0e252b4e804e9f61824");
        PublicResolver resolver = PublicResolver.load("0x821fac8be5c2b44b23616bf0608ecae47e4532cf",
                web3j,
                credentials,
                null,
                null
        );
        String resolverData = FunctionEncoder.encode(new Function("setAddr",
                Arrays.<Type>asList(new Bytes32(NameHash.nameHashAsBytes("sia.me.nilu")),
                        new Address(credentials.getAddress())),
                Collections.<TypeReference<?>>emptyList()));
        Transaction tx = new Transaction(credentials.getAddress(),
                null,
                null,
                null,
                resolver.getContractAddress(),
                null,
                resolverData
        );
        System.out.println(web3j.ethEstimateGas(tx).send().getAmountUsed().toString());
    }

    @Test
    public void estimateRegistry() throws Exception {
        Web3j web3j = Web3jFactory.build(new HttpService("https://walletapi.nilu.tech", new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build(), true));
        Credentials credentials = Credentials.create("d8e89aca91120b6258a542ebf815696dccc5f7fe8a25d0e252b4e804e9f61824");
        NNSRegistry registry = NNSRegistry.load("0xd878ff289b0033cb4ae35c18f19e901f461f9997",
                web3j,
                credentials,
                null,
                null
        );
        String registryData = FunctionEncoder.encode(new Function("setResolver",
                Arrays.<Type>asList(new Bytes32(NameHash.nameHashAsBytes("sia.me.nilu")),
                        new Address("0x821fac8be5c2b44b23616bf0608ecae47e4532cf")),
                Collections.<TypeReference<?>>emptyList()));
        Transaction tx = new Transaction(credentials.getAddress(),
                null,
                null,
                null,
                registry.getContractAddress(),
                null,
                registryData
        );
        System.out.println(web3j.ethEstimateGas(tx).send().getAmountUsed().toString());
    }
}