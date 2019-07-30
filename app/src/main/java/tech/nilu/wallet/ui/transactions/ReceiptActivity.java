package tech.nilu.wallet.ui.transactions;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.model.TransactionDetails;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.util.BitmapUtils;

/**
 * Created by root on 2/4/18.
 */

public class ReceiptActivity extends BaseActivity implements HasSupportFragmentInjector {
    public static final String HASH = "Hash";
    public static final String TIMESTAMP = "Timestamp";
    public static final String NETWORK_SYMBOL = "NetworkSymbol";

    @Inject
    TransactionViewModel viewModel;
    @BindView(R.id.background)
    CardView background;
    @BindView(android.R.id.progress)
    ProgressBar progress;
    @BindView(R.id.contentLayout)
    View contentLayout;
    @BindView(android.R.id.icon)
    ImageView icon;
    @BindView(R.id.tvStatus)
    TextView statusText;
    @BindView(R.id.tvHash)
    TextView hashText;
    @BindView(R.id.tvBlockNumber)
    TextView blockNumberText;
    @BindView(R.id.tvTime)
    TextView timeText;
    @BindView(R.id.tvFromAddress)
    TextView fromText;
    @BindView(R.id.tvToAddress)
    TextView toText;
    @BindView(R.id.tvValue)
    TextView valueText;
    @BindView(R.id.tvTxFee)
    TextView txFeeText;
    @BindView(R.id.tvGasUsed)
    TextView gasUsedText;
    @BindView(R.id.tvNonce)
    TextView nonceText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        ButterKnife.bind(this);

        initUI();
        fetchTransactionDetails(getIntent().getStringExtra(HASH));
    }

    private void initUI() {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
    }

    private void fetchTransactionDetails(String transactionHash) {
        progress.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.INVISIBLE);
        viewModel.getTransactionDetails(transactionHash).observe(this, response -> {
            if (response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.setVisibility(View.GONE);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    TransactionDetails details = response.getData();
                    Transaction transaction = details.getTransaction();
                    TransactionReceipt receipt = details.getReceipt();
                    //NiluTransaction niluTransaction = details.getNiluTransaction();
                    BigInteger txFee = transaction.getGasPrice().multiply(transaction.getGas());
                    long timestamp = getIntent().getLongExtra(TIMESTAMP, 0L);

                    contentLayout.setVisibility(View.VISIBLE);

                    hashText.setText(transactionHash);
                    hashText.setPaintFlags(hashText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    hashText.setOnClickListener(v -> {
                        String prefix = "";
                        long chainId = viewModel.getActiveNetwork().getChainId();
                        if (chainId == 1)
                            prefix = "https://etherscan.io/tx/";
                        else if (chainId == 3)
                            prefix = "https://ropsten.etherscan.io/tx/";
                        else if (chainId == 512)
                            prefix = "http://explorer.nilu.tech/#/transaction/";
                        else if (chainId == 3125659152L)
                            prefix = "https://poseidon.pirl.io/explorer/transaction/";
                        else if (chainId == 1313114)
                            prefix = "https://explorer.ether1.org/tx/";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(prefix + transactionHash)));
                    });

                    timeText.setText(timestamp > 0 ? new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(timestamp * 1000)) : "-");
                    fromText.setText(transaction.getFrom());
                    toText.setText(transaction.getTo());
                    valueText.setText(String.format("%s %s", Convert.fromWei(transaction.getValue().toString(), Convert.Unit.ETHER), getIntent().getStringExtra(NETWORK_SYMBOL)));
                    txFeeText.setText(String.format("%s %s", Convert.fromWei(txFee.toString(), Convert.Unit.ETHER).toPlainString(), getIntent().getStringExtra(NETWORK_SYMBOL)));
                    nonceText.setText(transaction.getNonce().toString());
                    if (receipt != null) {
                        if (receipt.getBlockNumber() != null) {
                            icon.setImageResource(R.drawable.ic_succeed_green_24dp);
                            statusText.setText(getString(R.string.successful_transaction));
                            blockNumberText.setText(receipt.getBlockNumber().toString());
                            gasUsedText.setText(String.format("%s wei", transaction.getGas().toString()));
                        } else {
                            icon.setImageResource(R.drawable.ic_failed_red_24dp);
                            statusText.setText(getString(R.string.unsuccessful_transaction));
                        }
                    } else {
                        icon.setImageResource(R.drawable.ic_pending_yellow_24dp);
                        statusText.setText(getString(R.string.pending_transaction));
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_receipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent shareIntent = BitmapUtils.getBitmapIntent(this, background);
                if (shareIntent != null)
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_receipt)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}
