package tech.nilu.wallet.ui.wallets.contracts;

import java.util.List;

import tech.nilu.wallet.db.entity.ContractInfo;

public interface ItemSelector {
    List<ContractInfo> getSelectedItems();
}
