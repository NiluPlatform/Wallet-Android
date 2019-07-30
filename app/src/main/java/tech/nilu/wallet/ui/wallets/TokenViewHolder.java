package tech.nilu.wallet.ui.wallets;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.web3j.utils.Convert;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.util.BitmapUtils;

/**
 * Created by root on 3/17/18.
 */

public class TokenViewHolder extends RecyclerView.ViewHolder {
    @BindView(android.R.id.icon)
    ImageView icon;
    @BindView(R.id.tvName)
    TextView nameText;
    @BindView(R.id.tvAddress)
    TextView addressText;
    @BindView(R.id.tvBalance)
    TextView balanceText;

    private int holderIconSize;
    private Resources resources;
    private View.OnClickListener clickListener;

    public TokenViewHolder(ViewGroup parent, Resources resources, int holderIconSize, View.OnClickListener clickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_contract, parent, false));

        ButterKnife.bind(this, itemView);
        this.resources = resources;
        this.holderIconSize = holderIconSize;
        this.clickListener = clickListener;
    }

    public void bind(ContractInfo item, Network activeNetwork) {
        Drawable placeholder = new BitmapDrawable(resources, BitmapUtils.getAvatar(holderIconSize, String.valueOf(item.getName().charAt(0)).toUpperCase()));
        Glide.with(itemView)
                .load(item.getImage())
                .apply(new RequestOptions()
                        .circleCrop()
                        .error(placeholder)
                        .placeholder(placeholder))
                .into(icon);
        nameText.setText(item.getName());
        balanceText.setText(String.format("%s %s", Convert.fromWei(item.getBalance().toString(), Convert.Unit.ETHER).toString(), item.getSymbol() == null ? "Ξ" : item.getSymbol()));
        itemView.setOnClickListener(clickListener);
    }
}
