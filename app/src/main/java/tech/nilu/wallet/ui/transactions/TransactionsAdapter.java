package tech.nilu.wallet.ui.transactions;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import tech.nilu.wallet.api.model.transaction.Transaction;

/**
 * Created by root on 2/3/18.
 */

public class TransactionsAdapter extends RecyclerView.Adapter {
    private final String networkSymbol;
    private ArrayList<Transaction> items;

    public TransactionsAdapter(String networkSymbol) {
        this.networkSymbol = networkSymbol;
        this.items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TransactionViewHolder)
            ((TransactionViewHolder) holder).bind(items.get(position), networkSymbol);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(int pos, Transaction item) {
        items.add(pos, item);
        notifyItemInserted(pos);
    }

    public void addAll(Transaction[] transactions) {
        items.addAll(Arrays.asList(transactions));
        notifyDataSetChanged();
    }

    public void clear() {
        items = new ArrayList<>();
        notifyDataSetChanged();
    }
}
