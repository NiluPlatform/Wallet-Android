package tech.nilu.wallet.ui.wallets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Destination;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.model.WalletContract;
import tech.nilu.wallet.repository.ContractInfoRepository;
import tech.nilu.wallet.repository.DestinationRepository;
import tech.nilu.wallet.repository.ERC20ContractRepository;
import tech.nilu.wallet.repository.EthplorerRepository;
import tech.nilu.wallet.repository.NNSRepository;
import tech.nilu.wallet.repository.NetworkRepository;
import tech.nilu.wallet.repository.SelectedContractsRepository;
import tech.nilu.wallet.repository.TransactionRepository;
import tech.nilu.wallet.repository.WalletBaseRepository;

/**
 * Created by root on 1/7/18.
 */

public class WalletViewModel extends ViewModel {
    private final WalletBaseRepository walletBaseRepository;
    private final SelectedContractsRepository selectedContractsRepository;
    private final ContractInfoRepository contractInfoRepository;
    private final ERC20ContractRepository erc20ContractRepository;
    private final DestinationRepository destinationRepository;
    private final NetworkRepository networkRepository;
    private final EthplorerRepository ethplorerRepository;
    private final TransactionRepository transactionRepository;
    private final NNSRepository nnsRepository;

    @Inject
    public WalletViewModel(WalletBaseRepository walletBaseRepository,
                           SelectedContractsRepository selectedContractsRepository,
                           ContractInfoRepository contractInfoRepository,
                           ERC20ContractRepository erc20ContractRepository,
                           DestinationRepository destinationRepository,
                           NetworkRepository networkRepository,
                           EthplorerRepository ethplorerRepository,
                           TransactionRepository transactionRepository,
                           NNSRepository nnsRepository) {
        this.walletBaseRepository = walletBaseRepository;
        this.selectedContractsRepository = selectedContractsRepository;
        this.contractInfoRepository = contractInfoRepository;
        this.erc20ContractRepository = erc20ContractRepository;
        this.destinationRepository = destinationRepository;
        this.networkRepository = networkRepository;
        this.ethplorerRepository = ethplorerRepository;
        this.transactionRepository = transactionRepository;
        this.nnsRepository = nnsRepository;
    }

    public long insertWallet(Wallet wallet) {
        return walletBaseRepository.insertWallet(wallet);
    }

    public void updateWallet(Wallet wallet) {
        walletBaseRepository.updateWallet(wallet);
    }

    public void deleteWallet(Wallet wallet) {
        walletBaseRepository.deleteWallet(wallet);
    }

    public Wallet getWallet(long id) {
        return walletBaseRepository.getWallet(id);
    }

    public WalletBaseRepository.WalletExtensionData getWalletData(long id) {
        return walletBaseRepository.getWallets().get(id);
    }

    public LiveData<List<Wallet>> getWalletsLive(long networkId) {
        return walletBaseRepository.getWalletsLive(networkId);
    }

    public List<Wallet> getWallets(long networkId) {
        return walletBaseRepository.getWallets(networkId);
    }

    public void addWalletData(long id, WalletBaseRepository.WalletExtensionData data) {
        walletBaseRepository.addWallet(id, data);
    }

    public WalletBaseRepository.WalletExtensionData getWalletData(Wallet wallet, File parent, String password) throws IOException, CipherException {
        WalletBaseRepository.WalletExtensionData data = walletBaseRepository.getWallets().get(wallet.getId());
        if (data == null || data.getCredentials() == null) {
            Credentials credentials = WalletUtils.loadCredentials(password, new File(parent, wallet.getPath()));
            data = new WalletBaseRepository.WalletExtensionData(wallet, credentials);
            addWalletData(wallet.getId(), data);
        }
        return data;
    }

    public LiveData<LiveResponse<List<WalletContract>>> loadWallets(long selectedNetworkId, File parent, String password) {
        return walletBaseRepository.loadWallets(selectedNetworkId, parent, password);
    }

    public LiveData<LiveResponse<WalletContract>> loadWallet(long walletId, File parent, String password) {
        return walletBaseRepository.loadWallet(walletId, parent, password);
    }

    public LiveData<LiveResponse<WalletBaseRepository.WalletExtensionData>> loadWalletData(long walletId, File parent, String password) {
        return walletBaseRepository.loadWalletData(walletId, parent, password);
    }

    public LiveData<LiveResponse<BigInteger>> getBalance(String address) {
        return walletBaseRepository.getBalance(address);
    }

    public LiveData<LiveResponse<List<ContractInfo>>> fetchSelectedContracts(long walletId) {
        return selectedContractsRepository.fetchSelectedContracts(walletId);
    }

    public LiveData<LiveResponse<List<ContractInfo>>> fetchVerifiedContracts(long walletId, int offset, int length) {
        return selectedContractsRepository.fetchVerifiedContracts(walletId, offset, length);
    }

    public LiveData<LiveResponse<TransactionReceipt>> removeToken(long walletId, String address) {
        return selectedContractsRepository.removeToken(walletId, address);
    }

    public ContractInfo getContractInfo(long id) {
        return contractInfoRepository.getContractInfo(id);
    }

    public LiveData<List<ContractInfo>> getContractInfosLive(long walletId) {
        return contractInfoRepository.getContractInfosLive(walletId);
    }

    public List<ContractInfo> getContractInfos(long walletId) {
        return contractInfoRepository.getContractInfos(walletId);
    }

    public void deleteContractInfo(ContractInfo ci) {
        erc20ContractRepository.deleteContractInfo(ci);
    }

    public void deleteContractInfos(long walletId) {
        contractInfoRepository.deleteContractInfos(walletId);
    }

    public void insertContractInfos(List<ContractInfo> contractInfos) {
        contractInfoRepository.insertContractInfos(contractInfos);
    }

    public LiveData<LiveResponse<BigInteger>> getERC20Balance(long walletId, ContractInfo contractInfo) {
        return erc20ContractRepository.fetchBalance(walletId, contractInfo);
    }

    public LiveData<LiveResponse<String>> getERC20Symbol(long walletId, ContractInfo contractInfo) {
        return erc20ContractRepository.fetchSymbol(walletId, contractInfo);
    }

    public List<Destination> getDestinations() {
        return destinationRepository.getDestinations();
    }

    public LiveData<LiveResponse<Long>> createWallet(String name, String password, File destinationDir, long networkId) {
        return walletBaseRepository.createWallet(name, password, destinationDir, networkId);
    }

    public LiveData<LiveResponse<Long>> importWallet(String mnemonic, String walletName, String password, File destinationDir, long networkId) {
        return walletBaseRepository.importWallet(mnemonic, walletName, password, destinationDir, networkId);
    }

    public LiveData<LiveResponse<Long>> importWalletFromKeystore(String keystoreJson, String keystorePassword, String walletName, String password, File destinationDir, long networkId) {
        return walletBaseRepository.importWalletFromKeystore(keystoreJson, keystorePassword, walletName, password, destinationDir, networkId);
    }

    public LiveData<LiveResponse<Long>> importWalletFromPrivateKey(String privateKey, String walletName, String password, File destinationDir, long networkId) {
        return walletBaseRepository.importWalletFromPrivateKey(privateKey, walletName, password, destinationDir, networkId);
    }

    public LiveData<LiveResponse<Boolean>> saveContracts(long walletId, List<ContractInfo> contractInfos) {
        return erc20ContractRepository.saveContracts(walletId, contractInfos);
    }

    public LiveData<LiveResponse<BigInteger[]>> getBalances(String[] addresses) {
        return walletBaseRepository.getBalances(addresses);
    }

    public LiveData<LiveResponse<BigInteger>> getTotalBalance(List<String> addresses) {
        return walletBaseRepository.getTotalBalance(addresses);
    }

    public LiveData<LiveResponse<ContractInfo>> fetchSmartContract(long walletId, ContractInfo contractInfo) {
        return erc20ContractRepository.fetchSmartContract(walletId, contractInfo);
    }

    public String getKeystoreContent(Wallet wallet, File parent) {
        return walletBaseRepository.getKeystoreContent(wallet, parent);
    }

    public Network getActiveNetwork() {
        return networkRepository.getActiveNetwork();
    }

    public Network getNetwork(long id) {
        return networkRepository.getNetwork(id);
    }

    public LiveData<LiveResponse<ContractInfo>> fetchTokenInfo(long walletId, ContractInfo contractInfo) {
        return ethplorerRepository.fetchTokenInfo(walletId, contractInfo);
    }

    public boolean canFetchTransactions() {
        return transactionRepository.canFetchTransactions();
    }

    public boolean canFetchTokenTransfers() {
        return transactionRepository.canFetchTokenTransfers();
    }

    public void updateWallet(long walletId, String name) {
        Wallet wallet = walletBaseRepository.getWallet(walletId);
        WalletBaseRepository.WalletExtensionData walletData = getWalletData(walletId);
        wallet.setName(name);
        walletData.setWallet(wallet);
        walletBaseRepository.updateWallet(wallet);
    }

    public LiveData<LiveResponse<String>> resolve(long walletId, String domain) {
        return nnsRepository.resolve(walletId, domain);
    }
}
