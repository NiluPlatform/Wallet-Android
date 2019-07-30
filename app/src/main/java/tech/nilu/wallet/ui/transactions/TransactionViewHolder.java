package tech.nilu.wallet.ui.transactions;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.api.model.transaction.Transaction;

/**
 * Created by root on 2/3/18.
 */

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    @BindView(android.R.id.icon)
    ImageView icon;
    @BindView(R.id.tvHash)
    TextView hashText;
    @BindView(R.id.tvAmount)
    TextView amountText;
    @BindView(R.id.tvAddress)
    TextView addressText;
    @BindView(R.id.tvTime)
    TextView timeText;

    private Context context;
    private Drawable addressDrawable, timeDrawable;
    private SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm");

    public TransactionViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false));

        ButterKnife.bind(this, itemView);
        this.context = parent.getContext();
        this.timeDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_time_gray_16dp);
        this.addressDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_person_gray_16dp);
    }

    public void bind(Transaction item, String networkSymbol) {
        icon.setImageDrawable(AppCompatResources.getDrawable(context, item.isSuccess() ? R.drawable.ic_transaction_out_red_36dp : R.drawable.ic_transaction_in_green_36dp));
        timeText.setCompoundDrawablesWithIntrinsicBounds(timeDrawable, null, null, null);
        addressText.setCompoundDrawablesWithIntrinsicBounds(addressDrawable, null, null, null);

        hashText.setText(item.getHash());
        amountText.setText(String.format("%s %s", item.getValue().stripTrailingZeros().toString(), networkSymbol));
        addressText.setText(item.getTo());
        timeText.setText(formatter.format(new Date(item.getTimestamp() * 1000)));

        itemView.setOnClickListener(view -> context.startActivity(new Intent(context, ReceiptActivity.class)
                .putExtra(ReceiptActivity.NETWORK_SYMBOL, networkSymbol)
                .putExtra(ReceiptActivity.TIMESTAMP, item.getTimestamp())
                .putExtra(ReceiptActivity.HASH, item.getHash())));
    }
}
