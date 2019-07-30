package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.WalletUtils;
import org.web3j.ens.NameHash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tech.nilu.wallet.model.CustomException;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.contracts.DefaultReverseResolver;
import tech.nilu.wallet.repository.contracts.NNSRegistry;
import tech.nilu.wallet.repository.contracts.PublicResolver;
import tech.nilu.wallet.repository.contracts.ReverseRegistrar;
import tech.nilu.wallet.repository.contracts.SubnodeRegistrar;

@Singleton
public class NNSRepository {
    private final String nnsRegistryAddress;
    private final String publicResolverAddress;
    private final String subnodeRegistrarAddress;
    private final String reverseResolverAddress;
    private final Web3jProvider web3jProvider;
    private final WalletBaseRepository walletBaseRepository;

    @Inject
    public NNSRepository(Web3jProvider web3jProvider, WalletBaseRepository walletBaseRepository) {
        this.nnsRegistryAddress = "0xd878ff289b0033cb4ae35c18f19e901f461f9997";
        this.publicResolverAddress = "0x821fac8be5c2b44b23616bf0608ecae47e4532cf";
        this.subnodeRegistrarAddress = "0x1c8a2cb13b22164e74b2b76712b81b4671eb80e0";
        this.reverseResolverAddress = "0x25f049ef55c693bc6f0cfb983f329d49479ec43c";
        this.web3jProvider = web3jProvider;
        this.walletBaseRepository = walletBaseRepository;
    }

    public LiveData<LiveResponse<BigInteger>> getGasPrice() {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<BigInteger>> result = new MutableLiveData<>();

        Single.fromFuture(web3j.ethGasPrice().sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(price -> result.setValue(LiveResponse.of(price.getGasPrice())), t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }

    public LiveData<LiveResponse<String>> resolve(long walletId, String domain) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<String>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);

        Single.fromCallable(() -> resolveDomain(domain, web3j, extensionData))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(address -> {
                    if (!WalletUtils.isValidAddress(address))
                        throw new CustomException("Unable to resolve address for name: " + domain);
                    result.setValue(LiveResponse.of(address));
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }

    public LiveData<LiveResponse<String>> reverseResolution(long walletId) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<String>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);

        NNSRegistry registry = NNSRegistry.load(nnsRegistryAddress,
                web3j,
                extensionData.getCredentials(),
                null,
                null
        );
        byte[] node = NameHash.nameHashAsBytes(extensionData.getCredentials().getAddress().substring(2).toLowerCase() + ".addr.reverse");
        Single.fromFuture(registry.resolver(node).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resolver -> {
                    DefaultReverseResolver reverseResolver = DefaultReverseResolver.load(resolver,
                            web3j,
                            extensionData.getCredentials(),
                            null,
                            null
                    );
                    Single.fromFuture(reverseResolver.name(node).sendAsync())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(name -> result.setValue(LiveResponse.of(name)), t -> {
                                t.printStackTrace();
                                result.setValue(LiveResponse.of(t));
                            });
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }

    public LiveData<LiveResponse<Tuple2<String, String>>> queryDomains(long walletId, String node) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<Tuple2<String, String>>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);
        NNSRegistry registry = NNSRegistry.load(nnsRegistryAddress,
                web3j,
                extensionData.getCredentials(),
                null,
                null
        );
        Single<String> ownerSingle = Single.fromFuture(registry.owner(NameHash.nameHashAsBytes(node)).sendAsync());
        Single<String> resolverSingle = Single.fromFuture(registry.resolver(NameHash.nameHashAsBytes(node)).sendAsync());
        Single.zip(ownerSingle, resolverSingle, (owner, resolver) -> new Tuple2(owner, resolver))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x)), t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }

    public LiveData<LiveResponse<Boolean>> registerDomain(long walletId, String subnode, String rootNode) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<Boolean>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);
        Single.fromFuture(web3j.ethGasPrice().sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(price -> {
                    Single.fromCallable(() -> registerDomain(subnode, rootNode, web3j, extensionData.getCredentials(), price.getGasPrice()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(receipts -> result.setValue(LiveResponse.of(true)), t -> {
                                t.printStackTrace();
                                result.setValue(LiveResponse.of(t));
                            });
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }

    public LiveData<LiveResponse<Boolean>> releaseDomain(long walletId, String domainToRelease) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<Boolean>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);
        Single.fromFuture(web3j.ethGasPrice().sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(price -> {
                    Single.fromCallable(() -> releaseDomain(domainToRelease, web3j, extensionData.getCredentials(), price.getGasPrice()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(receipts -> result.setValue(LiveResponse.of(true)), t -> {
                                t.printStackTrace();
                                result.setValue(LiveResponse.of(t));
                            });
                });

        return result;
    }

    private String resolveDomain(String domain, Web3j web3j, WalletBaseRepository.WalletExtensionData extensionData) throws Exception {
        byte[] node = NameHash.nameHashAsBytes(domain);

        NNSRegistry registry = NNSRegistry.load(nnsRegistryAddress,
                web3j,
                extensionData.getCredentials(),
                null,
                null
        );
        String resolverAddress = registry.resolver(node).send();

        PublicResolver resolver = PublicResolver.load(resolverAddress,
                web3j,
                extensionData.getCredentials(),
                null,
                null
        );
        return resolver.addr(node).send();
    }

    @NonNull
    private Tuple6 releaseDomain(String domainToRelease, Web3j web3j, Credentials credentials, BigInteger gasPrice) throws Exception {
        PublicResolver resolver = PublicResolver.load(publicResolverAddress,
                web3j,
                credentials,
                gasPrice,
                BigInteger.valueOf(40000)
        );
        NNSRegistry registry = NNSRegistry.load(nnsRegistryAddress,
                web3j,
                credentials,
                gasPrice,
                BigInteger.valueOf(35000)
        );
        DefaultReverseResolver reverseResolver = DefaultReverseResolver.load(reverseResolverAddress,
                web3j,
                credentials,
                gasPrice,
                BigInteger.valueOf(45000)
        );
        String zeroAddress = Address.DEFAULT.toString();
        byte[] node = NameHash.nameHashAsBytes(domainToRelease);
        byte[] reverseNode = NameHash.nameHashAsBytes(credentials.getAddress().substring(2).toLowerCase() + ".addr.reverse");
        TransactionReceipt receipt1 = resolver.setAddr(node, zeroAddress).send();
        TransactionReceipt receipt2 = registry.setResolver(node, zeroAddress).send();
        TransactionReceipt receipt3 = registry.setOwner(node, zeroAddress).send();
        TransactionReceipt receipt4 = reverseResolver.setName(reverseNode, "").send();
        TransactionReceipt receipt5 = registry.setResolver(reverseNode, zeroAddress).send();
        TransactionReceipt receipt6 = registry.setOwner(reverseNode, zeroAddress).send();
        return new Tuple6(receipt1, receipt2, receipt3, receipt4, receipt5, receipt6);
    }

    @NonNull
    private Tuple6 registerDomain(String subnode, String rootNode, Web3j web3j, Credentials credentials, BigInteger gasPrice) throws Exception {
        SubnodeRegistrar registrar = SubnodeRegistrar.load(subnodeRegistrarAddress,
                web3j,
                credentials,
                gasPrice,
                BigInteger.valueOf(60000)
        );
        PublicResolver resolver = PublicResolver.load(publicResolverAddress,
                web3j,
                credentials,
                gasPrice,
                BigInteger.valueOf(55000)
        );
        NNSRegistry registry = NNSRegistry.load(nnsRegistryAddress,
                web3j,
                credentials,
                gasPrice,
                BigInteger.valueOf(55000)
        );
        DefaultReverseResolver reverseResolver = DefaultReverseResolver.load(reverseResolverAddress,
                web3j,
                credentials,
                gasPrice,
                BigInteger.valueOf(55000)
        );
        ReverseRegistrar reverseRegistrar = ReverseRegistrar.load(registry.owner(NameHash.nameHashAsBytes("addr.reverse")).send(),
                web3j,
                credentials,
                gasPrice,
                BigInteger.valueOf(60000)
        );
        byte[] node = NameHash.nameHashAsBytes(subnode + "." + rootNode);
        byte[] reverseNode = NameHash.nameHashAsBytes(credentials.getAddress().substring(2).toLowerCase() + ".addr.reverse");
        TransactionReceipt receipt1 = registrar.setSubnodeOwner(NameHash.nameHashAsBytes(rootNode), Hash.sha3(subnode.getBytes()), credentials.getAddress().toLowerCase()).send();
        TransactionReceipt receipt2 = resolver.setAddr(node, credentials.getAddress().toLowerCase()).send();
        TransactionReceipt receipt3 = registry.setResolver(node, resolver.getContractAddress()).send();
        TransactionReceipt receipt4 = reverseRegistrar.claim(credentials.getAddress().toLowerCase()).send();
        TransactionReceipt receipt5 = registry.setResolver(reverseNode, reverseResolver.getContractAddress()).send();
        TransactionReceipt receipt6 = reverseResolver.setName(reverseNode, subnode + "." + rootNode).send();
        return new Tuple6(receipt1, receipt2, receipt3, receipt4, receipt5, receipt6);
    }
}
