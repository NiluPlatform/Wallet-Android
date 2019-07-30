package tech.nilu.wallet.model;

import java.util.List;

import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.repository.WalletBaseRepository;

/**
 * Created by root on 2/19/18.
 */

public class WalletContract {
    private final WalletBaseRepository.WalletExtensionData data;
    private final List<ContractInfo> contractInfos;

    public WalletContract(WalletBaseRepository.WalletExtensionData data, List<ContractInfo> contractInfos) {
        this.data = data;
        this.contractInfos = contractInfos;
    }

    public WalletBaseRepository.WalletExtensionData getData() {
        return data;
    }

    public List<ContractInfo> getContractInfos() {
        return contractInfos;
    }
}
