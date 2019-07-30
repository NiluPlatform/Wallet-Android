package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.nilu.wallet.api.TokenApi;
import tech.nilu.wallet.api.model.token.AddressInfoResponse;
import tech.nilu.wallet.db.dao.ContractInfoDao;
import tech.nilu.wallet.db.dao.NetworkDao;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.model.CustomException;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.contracts.templates.ERC20Basic;
import tech.nilu.wallet.repository.contracts.templates.ERC20BasicImpl;

@Singleton
public class EthplorerRepository {
    private final Web3jProvider web3jProvider;
    private final WalletBaseRepository walletBaseRepository;
    private final TokenApi api;
    private final ContractInfoDao contractInfoDao;
    private final NetworkDao networkDao;

    @Inject
    public EthplorerRepository(Web3jProvider web3jProvider, WalletBaseRepository walletBaseRepository, TokenApi api, ContractInfoDao contractInfoDao, NetworkDao networkDao) {
        this.web3jProvider = web3jProvider;
        this.walletBaseRepository = walletBaseRepository;
        this.api = api;
        this.contractInfoDao = contractInfoDao;
        this.networkDao = networkDao;
    }

    public LiveData<LiveResponse<ContractInfo>> fetchTokenInfo(long walletId, ContractInfo contractInfo) {
        long chainId = networkDao.getActiveNetwork().getChainId();
        if (chainId == 1)
            return fetchInfoFromService(contractInfo);
        else if (chainId == 3 || chainId == 3125659152L)
            return fetchInfoFromNetwork(walletId, contractInfo);
        return null;
    }

    @NonNull
    private LiveData<LiveResponse<ContractInfo>> fetchInfoFromService(ContractInfo contractInfo) {
        MutableLiveData<LiveResponse<ContractInfo>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        api.fetchAddressInfo(contractInfo.getAddress(), contractInfo.getAddress()).enqueue(new Callback<AddressInfoResponse>() {
            @Override
            public void onResponse(Call<AddressInfoResponse> call, Response<AddressInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getError() == null) {
                        String name = "-";
                        String symbol = "-";
                        AddressInfoResponse info = response.body();
                        if (info.getTokenInfo() != null) {
                            name = info.getTokenInfo().getName();
                            symbol = info.getTokenInfo().getSymbol();
                        } else if (info.getTokens() != null && info.getTokens().length > 0 && info.getTokens()[0].getTokenInfo() != null) {
                            name = info.getTokens()[0].getTokenInfo().getName();
                            symbol = info.getTokens()[0].getTokenInfo().getSymbol();
                        }
                        ContractInfo ci = new ContractInfo(contractInfo.getWalletId(),
                                contractInfo.getAddress(),
                                name,
                                String.format("https://raw.githubusercontent.com/TrustWallet/tokens/master/images/%s.png", contractInfo.getAddress()),
                                contractInfo.getTypes());
                        ci.setSymbol(symbol);
                        contractInfoDao.insertContractInfo(ci);
                        result.setValue(LiveResponse.of(ci));
                    } else
                        result.setValue(LiveResponse.of(new CustomException(response.body().getError().getMessage())));
                } else
                    result.setValue(LiveResponse.of(new CustomException("Operation failed")));
            }

            @Override
            public void onFailure(Call<AddressInfoResponse> call, Throwable t) {
                t.printStackTrace();
                result.setValue(LiveResponse.of(t));
            }
        });

        return result;
    }

    private LiveData<LiveResponse<ContractInfo>> fetchInfoFromNetwork(long walletId, ContractInfo contractInfo) {
        Web3j web3j = web3jProvider.create();
        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);

        MutableLiveData<LiveResponse<ContractInfo>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));
        EnsResolver ensResolver = new EnsResolver(web3j);
        try {
            String ens = ensResolver.resolve(contractInfo.getAddress());
        } catch (Exception e) {
            result.setValue(LiveResponse.of(new CustomException(e.getMessage())));
            return result;
        }
        ERC20Basic contract = ERC20BasicImpl.load(contractInfo.getAddress(),
                web3j,
                extensionData.getCredentials(),
                BigInteger.ZERO,
                BigInteger.ZERO);
        Single<String> nameSingle = Single.fromFuture(contract.name().sendAsync()).onErrorReturn(t -> "-");
        Single<String> symbolSingle = Single.fromFuture(contract.symbol().sendAsync()).onErrorReturn(t -> "-");
        Single.zip(nameSingle, symbolSingle, (name, symbol) -> {
            ContractInfo ci = new ContractInfo(contractInfo.getWalletId(),
                    contractInfo.getAddress(),
                    name,
                    String.format("https://raw.githubusercontent.com/TrustWallet/tokens/master/images/%s.png", contractInfo.getAddress()),
                    contractInfo.getTypes());
            ci.setSymbol(symbol);
            return ci;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ci -> {
                    contractInfoDao.insertContractInfo(ci);
                    result.setValue(LiveResponse.of(ci));
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(t));
                });
        return result;
    }
}
