package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.model.CustomException;
import tech.nilu.wallet.model.GasParams;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.contracts.NiluToken;
import tech.nilu.wallet.repository.contracts.NotaryTokens;
import tech.nilu.wallet.repository.contracts.templates.ERC20;
import tech.nilu.wallet.repository.contracts.templates.ERC20Basic;
import tech.nilu.wallet.repository.contracts.templates.ERC20BasicImpl;


/**
 * Created by mnemati on 1/4/18.
 */

@Singleton
public class ERC20ContractRepository {
    private final Web3jProvider web3jProvider;
    private final WalletBaseRepository walletBaseRepository;
    private final ContractInfoRepository contractInfoRepository;

    @Inject
    public ERC20ContractRepository(Web3jProvider web3jProvider, WalletBaseRepository walletBaseRepository, ContractInfoRepository contractInfoRepository) {
        this.web3jProvider = web3jProvider;
        this.walletBaseRepository = walletBaseRepository;
        this.contractInfoRepository = contractInfoRepository;
    }

    public LiveData<LiveResponse<Boolean>> saveContracts(long walletId, List<ContractInfo> contractInfos) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<Boolean>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));
        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);

        contractInfoRepository.deleteContractInfos(walletId);
        if (contractInfos.isEmpty()) {
            result.setValue(LiveResponse.of(true));
            return result;
        }

        Credentials credentials = extensionData.getCredentials();
        List<Single<String>> contractsSingles = new ArrayList<>();
        for (ContractInfo ci : contractInfos) {
            ERC20Basic contract = ERC20BasicImpl.load(ci.getAddress(),
                    web3j,
                    credentials,
                    BigInteger.ZERO,
                    BigInteger.ZERO);
            contractsSingles.add(Single.fromFuture(contract.symbol().sendAsync()));
        }
        Single<String[]> observable = Single.zip(contractsSingles, objects -> {
            String[] symbols = new String[objects.length];
            for (int i = 0; i < objects.length; i++)
                symbols[i] = (String) objects[i];
            return symbols;
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(symbols -> {
                    for (int i = 0; i < contractInfos.size(); i++) {
                        contractInfos.get(i).setSymbol(symbols[i]);
                        contractInfoRepository.insertContractInfo(contractInfos.get(i));
                    }
                    result.setValue(LiveResponse.of(true));
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });
        return result;
    }

    public LiveData<LiveResponse<BigInteger>> fetchBalance(long walletId, ContractInfo contractInfo) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<BigInteger>> ret = new MutableLiveData<>();
        ret.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));
        if (!contractInfo.getTypes().contains(ERC20Basic.class.getSimpleName()) && !contractInfo.getTypes().contains(ERC20.class.getSimpleName())) {
            ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
            return ret;
        }
        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);
        ERC20Basic contract = ERC20BasicImpl.load(contractInfo.getAddress(),
                web3j,
                extensionData.getCredentials(),
                BigInteger.ZERO,
                BigInteger.ZERO);
        Single.fromFuture(contract.balanceOf(extensionData.getCredentials().getAddress()).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> ret.setValue(LiveResponse.of(i)), t -> {
                    t.printStackTrace();
                    ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return ret;
    }

    public LiveData<LiveResponse<String>> fetchSymbol(long walletId, ContractInfo contractInfo) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<String>> ret = new MutableLiveData<>();
        ret.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));
        if (!contractInfo.getTypes().contains(ERC20Basic.class.getSimpleName()) && !contractInfo.getTypes().contains(ERC20.class.getSimpleName())) {
            ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
            return ret;
        }
        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);
        ERC20Basic contract = ERC20BasicImpl.load(contractInfo.getAddress(),
                web3j,
                extensionData.getCredentials(),
                BigInteger.ZERO,
                BigInteger.ZERO);
        Single.fromFuture(contract.symbol().sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ret.setValue(LiveResponse.of(s)));
        return ret;
    }

    public LiveData<LiveResponse<ContractInfo>> fetchSmartContract(long walletId, ContractInfo contractInfo) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<ContractInfo>> ret = new MutableLiveData<>();
        ret.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));
        if (!contractInfo.getTypes().contains(ERC20Basic.class.getSimpleName()) && !contractInfo.getTypes().contains(ERC20.class.getSimpleName())) {
            ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
            return ret;
        }
        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);

        ERC20Basic contract = ERC20BasicImpl.load(contractInfo.getAddress(),
                web3j,
                extensionData.getCredentials(),
                BigInteger.ZERO,
                BigInteger.ZERO);
        Single<String> nameSingle = Single.fromFuture(contract.name().sendAsync());
        Single<String> symbolSingle = Single.fromFuture(contract.symbol().sendAsync());
        Single<BigInteger> balanceSingle = Single.fromFuture(contract.balanceOf(extensionData.getCredentials().getAddress()).sendAsync());
        Single.zip(nameSingle, symbolSingle, balanceSingle, (name, symbol, balance) -> {
            ContractInfo ci = new ContractInfo(contractInfo.getWalletId(), contractInfo.getAddress(), name, contractInfo.getImage(), contractInfo.getTypes());
            ci.setSymbol(symbol);
            ci.setBalance(balance);
            return ci;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ci -> {
                    contractInfoRepository.insertContractInfo(ci);
                    ret.setValue(LiveResponse.of(ci));
                }, t -> {
                    t.printStackTrace();
                    ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });

        return ret;
    }

    public LiveData<LiveResponse<ContractInfo>> fetchERC20Contract(long walletId, ContractInfo contractInfo) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<ContractInfo>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));
        if (!contractInfo.getTypes().contains(ERC20Basic.class.getSimpleName()) && !contractInfo.getTypes().contains(ERC20.class.getSimpleName())) {
            result.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
            return result;
        }
        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);

        ERC20Basic contract = ERC20BasicImpl.load(contractInfo.getAddress(),
                web3j,
                extensionData.getCredentials(),
                BigInteger.ZERO,
                BigInteger.ZERO);
        Single<String> nameSingle = Single.fromFuture(contract.name().sendAsync()).onErrorReturn(t -> "-");
        Single<String> symbolSingle = Single.fromFuture(contract.symbol().sendAsync()).onErrorReturn(t -> "-");
        Single<BigInteger> totalSupplySingle = Single.fromFuture(contract.totalSupply().sendAsync()).onErrorReturn(t -> BigInteger.ZERO);
        Single<BigInteger> decimalsSingle = Single.fromFuture(contract.decimals().sendAsync()).onErrorReturn(t -> BigInteger.ZERO);
        Single<BigInteger> rateSingle = Single.fromFuture(contract.rate().sendAsync()).onErrorReturn(t -> BigInteger.ZERO);
        Single<Boolean> isPayableSingle = Single.fromFuture(contract.isPayable().sendAsync()).onErrorReturn(t -> true);
        Single.zip(nameSingle, symbolSingle, totalSupplySingle, decimalsSingle, rateSingle, isPayableSingle, (name, symbol, totalSupply, decimals, rate, isPayable) -> {
            ContractInfo ci = new ContractInfo(contractInfo.getWalletId(), contractInfo.getAddress(), contractInfo.getName(), contractInfo.getImage(), contractInfo.getTypes());
            ci.setTokenName(name);
            ci.setTokenSymbol(symbol);
            ci.setTokenTotalSupply(totalSupply);
            ci.setTokenDecimals(decimals);
            ci.setTokenRate(rate);
            ci.setTokenIsPayable(isPayable);
            return ci;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ci -> result.setValue(LiveResponse.of(ci)), t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });

        return result;
    }

    public void deleteContractInfo(ContractInfo ci) {
        contractInfoRepository.deleteContractInfo(ci);
    }

    public LiveData<LiveResponse<NiluToken>> deployContract(Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String name, String symbol, BigInteger decimals, BigInteger totalSupply) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<NiluToken>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromFuture(NiluToken.deploy(web3j, credentials, gasPrice, gasLimit, name, symbol, decimals, totalSupply).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> {
                    if (token != null && !TextUtils.isEmpty(token.getContractAddress()))
                        result.setValue(LiveResponse.of(token));
                    else
                        result.setValue(LiveResponse.of(new CustomException("Deployment failed")));
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }

    public LiveData<LiveResponse<NotaryTokens>> deployContract(Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<NotaryTokens>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromFuture(NotaryTokens.deploy(web3j, credentials, gasPrice, gasLimit).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> {
                    if (token != null && !TextUtils.isEmpty(token.getContractAddress()))
                        result.setValue(LiveResponse.of(token));
                    else
                        result.setValue(LiveResponse.of(new CustomException("Deployment failed")));
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }

    public LiveData<LiveResponse<GasParams>> getDeploymentFee(Credentials credentials, String name, String symbol, BigInteger decimals, BigInteger totalSupply) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<GasParams>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(name),
                new Utf8String(symbol),
                new Uint8(decimals),
                new Uint256(totalSupply)));
        String data = NiluToken.getBinary() + encodedConstructor;
        Transaction tx = new Transaction(credentials.getAddress(),
                null,
                null,
                null,
                null,
                null,
                data);
        Single<EthGasPrice> gasPriceSingle = Single.fromFuture(web3j.ethGasPrice().sendAsync());
        Single<EthEstimateGas> estimateGasSingle = Single.fromFuture(web3j.ethEstimateGas(tx).sendAsync());
        Single.zip(gasPriceSingle, estimateGasSingle, (gasPrice, estimateGas) -> new GasParams(null, gasPrice.getGasPrice(), estimateGas.getAmountUsed()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(params -> {
                    if (params != null)
                        result.setValue(LiveResponse.of(params));
                    else
                        result.setValue(LiveResponse.of(new CustomException("Failed to estimate deployment gas")));
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }

    public LiveData<LiveResponse<GasParams>> getDeploymentFee(Credentials credentials, String binary, String encodedConstructor) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<GasParams>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        String data = binary + encodedConstructor;
        Transaction tx = new Transaction(credentials.getAddress(),
                null,
                null,
                null,
                null,
                null,
                data);
        Single<EthGasPrice> gasPriceSingle = Single.fromFuture(web3j.ethGasPrice().sendAsync());
        Single<EthEstimateGas> estimateGasSingle = Single.fromFuture(web3j.ethEstimateGas(tx).sendAsync());
        Single.zip(gasPriceSingle, estimateGasSingle, (gasPrice, estimateGas) -> new GasParams(null, gasPrice.getGasPrice(), estimateGas.getAmountUsed()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(params -> {
                    if (params != null)
                        result.setValue(LiveResponse.of(params));
                    else
                        result.setValue(LiveResponse.of(new CustomException("Failed to estimate deployment gas")));
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });

        return result;
    }
}
