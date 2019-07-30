package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.model.CustomException;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.contracts.NotaryTokens;
import tech.nilu.wallet.repository.contracts.SelectedContracts;


/**
 * Created by mnemati on 1/4/18.
 */

@Singleton
public class SelectedContractsRepository {
    private final String selectedContractsAddress;
    private final String verifiedContractsAddress;
    private final Web3jProvider web3jProvider;
    private final WalletBaseRepository walletBaseRepository;

    @Inject
    public SelectedContractsRepository(Web3jProvider web3jProvider, WalletBaseRepository walletBaseRepository) {
        this.web3jProvider = web3jProvider;
        this.walletBaseRepository = walletBaseRepository;
        this.selectedContractsAddress = "0xfd51a808815befb1356eae02549f4ef48884f568";
        this.verifiedContractsAddress = "0xe3Ab83082bB4F1ECDFbA755e5723A368CB1C243a";
    }

    public LiveData<LiveResponse<List<ContractInfo>>> fetchSelectedContracts(long walletId) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<List<ContractInfo>>> ret = new MutableLiveData<>();
        ret.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);
        SelectedContracts contract = SelectedContracts.load(selectedContractsAddress,
                web3j,
                extensionData.getCredentials(),
                BigInteger.ZERO,
                BigInteger.ZERO);
        Single.fromFuture(contract.listContracts().sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Gson gson = new Gson();
                    ContractInfo[] contracts = gson.fromJson(s, ContractInfo[].class);
                    for (ContractInfo ci : contracts) {
                        ci.setAddress("0x" + ci.getAddress());
                        ci.setWalletId(walletId);
                    }
                    ret.setValue(LiveResponse.of(Arrays.asList(contracts)));
                }, throwable -> {
                    throwable.printStackTrace();
                    ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return ret;
    }

    public LiveData<LiveResponse<List<ContractInfo>>> fetchVerifiedContracts(long walletId, int offset, int length) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<List<ContractInfo>>> ret = new MutableLiveData<>();
        ret.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);
        NotaryTokens notaryTokens = NotaryTokens.load(verifiedContractsAddress,
                web3j,
                extensionData.getCredentials(),
                BigInteger.ZERO,
                BigInteger.ZERO);
        Single.fromFuture(notaryTokens.getTokens(BigInteger.valueOf(offset), BigInteger.valueOf(length)).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Gson gson = new Gson();
                    ContractInfo[] contracts = gson.fromJson(s, ContractInfo[].class);
                    for (ContractInfo ci : contracts) {
                        ci.setAddress("0x" + ci.getAddress());
                        ci.setWalletId(walletId);
                        ci.setTypes("ERC20Basic");
                        ci.setImage(String.format("https://raw.githubusercontent.com/NiluPlatform/ERC20-Tokens/master/images/%s.png", ci.getAddress()));
                    }
                    ret.setValue(LiveResponse.of(Arrays.asList(contracts)));
                }, throwable -> {
                    throwable.printStackTrace();
                    ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return ret;
    }

    public LiveData<LiveResponse<TransactionReceipt>> removeToken(long walletId, String address) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<TransactionReceipt>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        WalletBaseRepository.WalletExtensionData extensionData = walletBaseRepository.getWallets().get(walletId);
        if (extensionData == null || extensionData.getCredentials() == null)
            throw new CustomException(CustomException.WALLET_NO_LOGIN_ERROR);
        NotaryTokens notaryTokens = NotaryTokens.load(verifiedContractsAddress,
                web3j,
                extensionData.getCredentials(),
                new BigInteger("20000000000"),
                new BigInteger("1500000"));
        Single.fromFuture(notaryTokens.removeToken(address).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x)));

        return result;
    }
}
