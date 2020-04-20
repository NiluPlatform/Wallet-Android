package tech.nilu.wallet.ui.wallets;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Network;

/**
 * Created by root on 3/17/18.
 */

public class TokensAdapter extends RecyclerView.Adapter {
    private int holderIconSize;
    private Resources resources;
    private Network activeNetwork;
    private List<ContractInfo> items;
    private View.OnClickListener clickListener;

    public TokensAdapter(Context context, Network activeNetwork, List<ContractInfo> contracts, View.OnClickListener clickListener) {
        this.resources = context.getResources();
        this.holderIconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, resources.getDisplayMetrics());
        this.activeNetwork = activeNetwork;
        this.clickListener = clickListener;
        this.items = new ArrayList<>(contracts);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TokenViewHolder(parent, resources, holderIconSize, clickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TokenViewHolder)
            ((TokenViewHolder) holder).bind(items.get(position), activeNetwork);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
