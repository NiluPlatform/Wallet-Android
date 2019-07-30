package tech.nilu.wallet.ui.wallets;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import tech.nilu.wallet.crypto.Eip55;
import org.web3j.utils.Convert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.MainActivity;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.repository.WalletBaseRepository;
import tech.nilu.wallet.ui.wallets.backup.BackupWalletActivity;
import tech.nilu.wallet.ui.wallets.details.WalletDetailsActivity;

/**
 * Created by Navid on 12/14/17.
 */

public class WalletViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.btnAddToken)
    ImageButton addTokenButton;
    @BindView(R.id.tvWallet)
    TextView nameText;
    @BindView(R.id.btnBackup)
    Button backupButton;
    @BindView(R.id.tvAddress)
    TextView addressText;
    @BindView(R.id.tvBalance)
    TextView balanceText;
    @BindView(android.R.id.list)
    RecyclerView contractsList;

    private Context context;

    public WalletViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, parent, false));

        ButterKnife.bind(this, itemView);
        this.context = parent.getContext();
    }

    public void bind(WalletBaseRepository.WalletExtensionData item, List<ContractInfo> contractInfos, Network activeNetwork, AddTokenClickListener listener) {
        String date = new SimpleDateFormat("yy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
        View.OnClickListener clickListener = v -> ((AppCompatActivity) context).startActivityForResult(new Intent(context, WalletDetailsActivity.class)
                .putExtra(WalletDetailsActivity.BALANCE, item.getBalance())
                .putExtra(WalletDetailsActivity.WALLET_ID, item.getWallet().getId()), MainActivity.RC_WALLET_DETAILS);

        Drawable backedupDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_backedup_wallet_white_20dp);
        nameText.setText(item.getWallet().getName());
        nameText.setCompoundDrawablesWithIntrinsicBounds(null, null, TextUtils.isEmpty(item.getWallet().getMnemonic()) ? backedupDrawable : null, null);
        backupButton.setVisibility(TextUtils.isEmpty(item.getWallet().getMnemonic()) ? View.GONE : View.VISIBLE);
        addressText.setText(Eip55.convertToEip55Address(item.getCredentials().getAddress()));
        balanceText.setText(Html.fromHtml(String.format("<b>%s %s</b> (%s)", Convert.fromWei(item.getBalance().toString(), Convert.Unit.ETHER).toString(), activeNetwork.getSymbol(), date)));
        addTokenButton.setOnClickListener(v -> listener.onClick(item.getWallet().getId()));
        backupButton.setOnClickListener(v -> ((AppCompatActivity) context).startActivityForResult(new Intent(context, BackupWalletActivity.class)
                .putExtra(BackupWalletActivity.WALLET_ID, item.getWallet().getId()), MainActivity.RC_BACKUP_WALLET));
        itemView.setOnClickListener(clickListener);

        TokensAdapter adapter = new TokensAdapter(context, activeNetwork, contractInfos, clickListener);
        contractsList.setLayoutManager(new LinearLayoutManager(context));
        contractsList.setAdapter(adapter);
        //contractsList.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
    }

    public interface AddTokenClickListener {
        void onClick(long walletId);
    }
}
