package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.LongSparseArray;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tech.nilu.wallet.crypto.Bip39Locale;
import tech.nilu.wallet.crypto.Bip44Wallet;
import tech.nilu.wallet.crypto.Bip44WalletUtils;
import tech.nilu.wallet.db.dao.ContractInfoDao;
import tech.nilu.wallet.db.dao.WalletDao;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.model.CustomException;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.model.WalletContract;

/**
 * Created by mnemati on 1/4/18.
 */

@Singleton
public class WalletBaseRepository {
    final LongSparseArray<WalletExtensionData> walletsData = new LongSparseArray<>();

    private final Web3jProvider web3jProvider;
    private final WalletDao walletDao;
    private final ContractInfoDao contractInfoDao;

    @Inject
    public WalletBaseRepository(Web3jProvider web3jProvider, WalletDao walletDao, ContractInfoDao contractInfoDao) {
        this.web3jProvider = web3jProvider;
        this.walletDao = walletDao;
        this.contractInfoDao = contractInfoDao;
    }

    public long insertWallet(Wallet wallet) {
        return walletDao.insertWallet(wallet);
    }

    public Wallet getWallet(long id) {
        return walletDao.getWallet(id);
    }

    public List<Wallet> getWallets(long networkId) {
        return walletDao.getWallets(networkId);
    }

    public LiveData<List<Wallet>> getWalletsLive(long networkId) {
        return walletDao.getWalletsLive(networkId);
    }

    public void updateWallet(Wallet wallet) {
        walletDao.updateWallet(wallet);
    }

    public LongSparseArray<WalletExtensionData> getWallets() {
        return walletsData;
    }

    public void addWallet(long id, WalletExtensionData data) {
        walletsData.put(id, data);
    }

    public void deleteWallet(Wallet wallet) {
        contractInfoDao.deleteContractInfos(wallet.getId());
        walletDao.deleteWallet(wallet);
        walletsData.delete(wallet.getId());
    }

    public LiveData<LiveResponse<BigInteger[]>> getBalances(String[] addresses) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<BigInteger[]>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        List<Single<EthGetBalance>> balancesSingles = new ArrayList<>();
        for (String s : addresses)
            balancesSingles.add(Single.fromFuture(web3j.ethGetBalance(s, DefaultBlockParameterName.LATEST).sendAsync()));
        Single<BigInteger[]> observable = Single.zip(balancesSingles, objects -> {
            BigInteger[] balances = new BigInteger[objects.length];
            for (int i = 0; i < objects.length; i++)
                balances[i] = ((EthGetBalance) objects[i]).getBalance();
            return balances;
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balances -> result.setValue(LiveResponse.of(balances)), throwable -> {
                    throwable.printStackTrace();
                    result.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return result;
    }

    public LiveData<LiveResponse<BigInteger>> getTotalBalance(List<String> addresses) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<BigInteger>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        List<Single<EthGetBalance>> balancesSingles = new ArrayList<>();
        for (String s : addresses)
            balancesSingles.add(Single.fromFuture(web3j.ethGetBalance(s, DefaultBlockParameterName.LATEST).sendAsync()));
        Single<BigInteger> observable = Single.zip(balancesSingles, objects -> {
            BigInteger totalBalance = BigInteger.ZERO;
            for (int i = 0; i < objects.length; i++)
                totalBalance = totalBalance.add(((EthGetBalance) objects[i]).getBalance());
            return totalBalance;
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(totalBalance -> result.setValue(LiveResponse.of(totalBalance)), t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return result;
    }

    public LiveData<LiveResponse<BigInteger>> getBalance(String address) {
        Web3j web3j = web3jProvider.create();
        MutableLiveData<LiveResponse<BigInteger>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromFuture(web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    if (x.hasError())
                        result.setValue(LiveResponse.of(new CustomException(x.getError().getMessage())));
                    else
                        result.setValue(LiveResponse.of(x.getBalance()));
                }, t -> {
                    t.printStackTrace();
                    result.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return result;
    }

    public LiveData<LiveResponse<Long>> createWallet(String name, String password, File destinationDir, long networkId) {
        MutableLiveData<LiveResponse<Long>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromCallable(() -> {
            Bip44Wallet wallet = Bip44WalletUtils.generateBip44Wallet(Bip39Locale.ENGLISH, password, destinationDir);
            return insertWallet(new Wallet(name, wallet.getFilename(), wallet.getMnemonic(), networkId));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x)));
        return result;
    }

    public LiveData<LiveResponse<Long>> importWallet(String mnemonic, String walletName, String password, File destinationDir, long networkId) {
        MutableLiveData<LiveResponse<Long>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromCallable(() -> {
            Credentials credentials = Bip44WalletUtils.loadBip44Credentials(mnemonic);
            String fileName = WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), destinationDir, false);
            return insertWallet(new Wallet(walletName, fileName, null, networkId));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x)), t -> result.setValue(LiveResponse.of(t)));
        return result;
    }

    public LiveData<LiveResponse<Long>> importWalletFromKeystore(String keystoreJson, String keystorePassword, String walletName, String password, File destinationDir, long networkId) {
        MutableLiveData<LiveResponse<Long>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromCallable(() -> {
            File file = File.createTempFile("json-", null);
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(keystoreJson.getBytes());
            } finally {
                stream.close();
            }
            try {
                Credentials credentials = WalletUtils.loadCredentials(keystorePassword, file);
                String fileName = WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), destinationDir, false);
                return insertWallet(new Wallet(walletName, fileName, null, networkId));
            } catch (IOException | CipherException e) {
                e.printStackTrace();
                throw new CustomException("Invalid JSON", e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x)), t -> result.setValue(LiveResponse.of(t)));
        return result;
    }

    public LiveData<LiveResponse<Long>> importWalletFromPrivateKey(String privateKey, String walletName, String password, File destinationDir, long networkId) {
        MutableLiveData<LiveResponse<Long>> result = new MutableLiveData<>();
        result.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromCallable(() -> {
            Credentials credentials = Credentials.create(privateKey);
            String fileName = WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), destinationDir, false);
            return insertWallet(new Wallet(walletName, fileName, null, networkId));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> result.setValue(LiveResponse.of(x)), t -> result.setValue(LiveResponse.of(t)));
        return result;
    }

    public LiveData<LiveResponse<List<WalletContract>>> loadWallets(long selectedNetworkId, File parent, String password) {
        MutableLiveData<LiveResponse<List<WalletContract>>> ret = new MutableLiveData<>();
        ret.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromCallable(() -> {
            List<WalletContract> result = new ArrayList<>();

            List<Wallet> wallets = getWallets(selectedNetworkId);
            for (int i = 0; i < wallets.size(); i++) {
                Wallet wallet = wallets.get(i);
                WalletExtensionData data = getWalletData(wallet, parent, password);
                List<ContractInfo> walletContractInfos = contractInfoDao.getContractInfos(wallet.getId());
                result.add(new WalletContract(data, walletContractInfos));
            }
            return result;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> ret.setValue(LiveResponse.of(x)), t -> {
                    t.printStackTrace();
                    ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return ret;
    }

    public LiveData<LiveResponse<WalletContract>> loadWallet(long walletId, File parent, String password) {
        MutableLiveData<LiveResponse<WalletContract>> ret = new MutableLiveData<>();
        ret.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromCallable(() -> {
            Wallet wallet = getWallet(walletId);
            WalletExtensionData data = getWalletData(wallet, parent, password);
            List<ContractInfo> walletContractInfos = contractInfoDao.getContractInfos(walletId);
            return new WalletContract(data, walletContractInfos);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> ret.setValue(LiveResponse.of(x)), t -> {
                    t.printStackTrace();
                    ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return ret;
    }

    public LiveData<LiveResponse<WalletExtensionData>> loadWalletData(long walletId, File parent, String password) {
        MutableLiveData<LiveResponse<WalletExtensionData>> ret = new MutableLiveData<>();
        ret.setValue(LiveResponse.of(LiveResponseStatus.IN_PROGRESS));

        Single.fromCallable(() -> {
            Wallet wallet = getWallet(walletId);
            return getWalletData(wallet, parent, password);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> ret.setValue(LiveResponse.of(x)), t -> {
                    t.printStackTrace();
                    ret.setValue(LiveResponse.of(LiveResponseStatus.FAILED));
                });
        return ret;
    }

    public WalletExtensionData getWalletData(Wallet wallet, File parent, String password) throws IOException, CipherException {
        WalletExtensionData data = getWallets().get(wallet.getId());
        if (data == null || data.getCredentials() == null) {
            Credentials credentials = WalletUtils.loadCredentials(password, new File(parent, wallet.getPath()));
            data = new WalletExtensionData(wallet, credentials);
            addWallet(wallet.getId(), data);
        }
        return data;
    }

    public String getKeystoreContent(Wallet wallet, File parent) {
        File file = new File(parent, wallet.getPath());
        byte[] content = new byte[(int) file.length()];
        try (FileInputStream stream = new FileInputStream(file)) {
            stream.read(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(content);
    }

    public static class WalletExtensionData {
        private Wallet wallet;
        private Credentials credentials;
        private BigInteger balance = BigInteger.ZERO;

        public WalletExtensionData(Wallet wallet, Credentials credentials) {
            this.wallet = wallet;
            this.credentials = credentials;
        }

        public Wallet getWallet() {
            return wallet;
        }

        public void setWallet(Wallet wallet) {
            this.wallet = wallet;
        }

        public BigInteger getBalance() {
            return balance;
        }

        public void setBalance(BigInteger balance) {
            this.balance = balance;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }


        @Override
        public boolean equals(Object obj) {
            return ((WalletExtensionData) obj).wallet.getId() == wallet.getId();
        }
    }
}
