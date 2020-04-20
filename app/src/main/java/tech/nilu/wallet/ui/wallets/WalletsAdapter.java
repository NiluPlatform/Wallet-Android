package tech.nilu.wallet.ui.wallets;

import android.util.LongSparseArray;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.repository.WalletBaseRepository;

/**
 * Created by Navid on 12/14/17.
 */

public class WalletsAdapter extends RecyclerView.Adapter {
    private final WalletViewHolder.AddTokenClickListener addTokenListener;

    private Network activeNetwork;
    private ArrayList<WalletBaseRepository.WalletExtensionData> items = new ArrayList<>();
    private LongSparseArray<List<ContractInfo>> itemContractInfos = new LongSparseArray<>();

    public WalletsAdapter(WalletViewHolder.AddTokenClickListener listener) {
        this.addTokenListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WalletViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WalletViewHolder) {
            WalletBaseRepository.WalletExtensionData item = items.get(position);
            ((WalletViewHolder) holder).bind(item, itemContractInfos.get(item.getWallet().getId()), activeNetwork, addTokenListener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(int pos, WalletBaseRepository.WalletExtensionData item, List<ContractInfo> contractInfos) {
        items.add(pos, item);
        itemContractInfos.put(item.getWallet().getId(), contractInfos);
        notifyItemInserted(pos);
    }

    public void removeAll() {
        items = new ArrayList<>();
        itemContractInfos = new LongSparseArray<>();
        notifyDataSetChanged();
    }

    public void updateItem(WalletBaseRepository.WalletExtensionData data, BigInteger balance) {
        int pos = items.indexOf(data);
        if (pos > -1) {
            items.get(pos).setBalance(balance);
            notifyItemChanged(pos);
        }
    }

    public void updateItem(WalletBaseRepository.WalletExtensionData data) {
        int pos = items.indexOf(data);
        if (pos > -1)
            notifyItemChanged(pos);
    }

    public void updateItemContractBalance(WalletBaseRepository.WalletExtensionData data,
                                          ContractInfo contractInfo,
                                          BigInteger balance) {
        List<ContractInfo> contractInfos = itemContractInfos.get(data.getWallet().getId());
        if (contractInfo != null) {
            int pos = contractInfos.indexOf(contractInfo);
            if (pos > -1) {
                contractInfos.get(pos).setBalance(balance);
                notifyItemChanged(items.indexOf(data));
            }
        }
    }

    public void updateItemContracts(WalletBaseRepository.WalletExtensionData data, List<ContractInfo> contractInfos) {
        int pos = items.indexOf(data);
        if (pos > -1) {
            itemContractInfos.put(data.getWallet().getId(), contractInfos);
            notifyItemChanged(pos);
        }
    }

    public void setActiveNetwork(Network network) {
        this.activeNetwork = network;
        notifyDataSetChanged();
    }
}
