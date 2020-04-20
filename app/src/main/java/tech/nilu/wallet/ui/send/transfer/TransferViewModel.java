package tech.nilu.wallet.ui.send.transfer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.inject.Inject;

import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Destination;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.db.entity.NiluTransaction;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.model.GasParams;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.repository.DestinationRepository;
import tech.nilu.wallet.repository.NetworkRepository;
import tech.nilu.wallet.repository.SecurityRepository;
import tech.nilu.wallet.repository.TransferRepository;
import tech.nilu.wallet.repository.WalletBaseRepository;

/**
 * Created by root on 1/27/18.
 */

public class TransferViewModel extends ViewModel {
    private final TransferRepository transferRepository;
    private final WalletBaseRepository walletBaseRepository;
    private final DestinationRepository destinationRepository;
    private final NetworkRepository networkRepository;
    private final SecurityRepository securityRepository;

    @Inject
    public TransferViewModel(TransferRepository transferRepository, WalletBaseRepository walletBaseRepository, DestinationRepository destinationRepository, NetworkRepository networkRepository, SecurityRepository securityRepository) {
        this.transferRepository = transferRepository;
        this.walletBaseRepository = walletBaseRepository;
        this.destinationRepository = destinationRepository;
        this.networkRepository = networkRepository;
        this.securityRepository = securityRepository;
    }

    public WalletBaseRepository.WalletExtensionData getWalletData(long id, File parent, String password) {
        Wallet wallet = walletBaseRepository.getWallet(id);
        try {
            return walletBaseRepository.getWalletData(wallet, parent, password);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WalletBaseRepository.WalletExtensionData getWalletData(long id) {
        return walletBaseRepository.getWallets().get(id);
    }

    public LiveData<LiveResponse<GasParams>> getTransactionFee(Credentials credentials, ContractInfo contractInfo, String toAddress, BigDecimal value, String data) throws IOException {
        if (contractInfo == null)
            return transferRepository.getTransferFee(credentials, toAddress, value, data);
        else
            return transferRepository.getTransferFee(credentials, contractInfo, toAddress, value);
    }

    public LiveData<LiveResponse<String>> sendTransaction(Credentials credentials, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, ContractInfo contractInfo, String toAddress, BigDecimal value, String data) {
        if (contractInfo == null)
            return transferRepository.transfer(credentials, nonce, gasPrice, gasLimit, toAddress, value, data);
        else
            return transferRepository.transfer(credentials, nonce, gasPrice, gasLimit, contractInfo, toAddress, value);
    }

    public LiveData<LiveResponse<BigInteger>> getGasPrice() {
        return transferRepository.getGasPrice();
    }

    public Destination getDestination(String address) {
        return destinationRepository.getDestination(address);
    }

    public long insertDestination(Destination destination) {
        return destinationRepository.insertDestination(destination);
    }

    public long insertTransaction(NiluTransaction transaction) {
        return transferRepository.insertTransaction(transaction);
    }

    public String retrieveString(String key) {
        return securityRepository.retrieveString(key);
    }

    public Network getNetwork(long id) {
        return networkRepository.getNetwork(id);
    }
}
