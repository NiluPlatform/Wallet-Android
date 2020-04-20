package tech.nilu.wallet.ui.tokens;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.web3j.crypto.Credentials;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import tech.nilu.wallet.api.model.transaction.Transaction;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Destination;
import tech.nilu.wallet.model.GasParams;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.repository.ContractInfoRepository;
import tech.nilu.wallet.repository.DestinationRepository;
import tech.nilu.wallet.repository.ERC20ContractRepository;
import tech.nilu.wallet.repository.NNSRepository;
import tech.nilu.wallet.repository.TransactionRepository;
import tech.nilu.wallet.repository.contracts.NiluToken;
import tech.nilu.wallet.repository.contracts.NotaryTokens;

/**
 * Created by root on 3/10/18.
 */

public class TokenViewModel extends ViewModel {
    private final ContractInfoRepository contractInfoRepository;
    private final ERC20ContractRepository erc20ContractRepository;
    private final DestinationRepository destinationRepository;
    private final TransactionRepository transactionRepository;
    private final NNSRepository nnsRepository;

    @Inject
    public TokenViewModel(ContractInfoRepository contractInfoRepository, ERC20ContractRepository erc20ContractRepository, DestinationRepository destinationRepository, TransactionRepository transactionRepository, NNSRepository nnsRepository) {
        this.contractInfoRepository = contractInfoRepository;
        this.erc20ContractRepository = erc20ContractRepository;
        this.destinationRepository = destinationRepository;
        this.transactionRepository = transactionRepository;
        this.nnsRepository = nnsRepository;
    }

    public ContractInfo getContractInfo(long id) {
        return contractInfoRepository.getContractInfo(id);
    }

    public LiveData<LiveResponse<ContractInfo>> fetchERC20Contract(long walletId, ContractInfo contractInfo) {
        return erc20ContractRepository.fetchERC20Contract(walletId, contractInfo);
    }

    public List<Destination> getDestinations() {
        return destinationRepository.getDestinations();
    }

    public LiveData<LiveResponse<Transaction[]>> fetchTokenTransfers(String tokenAddress, String walletAddress) {
        return transactionRepository.fetchTokenTransfers(tokenAddress, walletAddress);
    }

    public LiveData<LiveResponse<GasParams>> getDeploymentFee(Credentials credentials, String name, String symbol, BigInteger decimals, BigInteger totalSupply) {
        return erc20ContractRepository.getDeploymentFee(credentials, name, symbol, decimals, totalSupply);
    }

    public LiveData<LiveResponse<GasParams>> getDeploymentFee(Credentials credentials, String binary, String encodedConstructor) {
        return erc20ContractRepository.getDeploymentFee(credentials, binary, encodedConstructor);
    }

    public LiveData<LiveResponse<NiluToken>> deployContract(Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String name, String symbol, BigInteger decimals, BigInteger totalSupply) {
        return erc20ContractRepository.deployContract(credentials, gasPrice, gasLimit, name, symbol, decimals, totalSupply);
    }

    public LiveData<LiveResponse<NotaryTokens>> deployContract(Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return erc20ContractRepository.deployContract(credentials, gasPrice, gasLimit);
    }

    public LiveData<LiveResponse<String>> resolve(long walletId, String domain) {
        return nnsRepository.resolve(walletId, domain);
    }
}
