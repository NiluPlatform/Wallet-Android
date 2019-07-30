package tech.nilu.wallet.ui.send.transfer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.crypto.Eip55;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Destination;
import tech.nilu.wallet.db.entity.NiluTransaction;
import tech.nilu.wallet.model.GasParams;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.WalletBaseRepository;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.password.WalletPasswordDialogFragment;
import tech.nilu.wallet.ui.transactions.ReceiptActivity;
import tech.nilu.wallet.util.MyKeyStore;
import tech.nilu.wallet.util.UIUtils;

/**
 * Created by root on 1/24/18.
 */

public class TransferActivity extends BaseActivity implements HasSupportFragmentInjector, WalletPasswordDialogFragment.WalletPasswordClickListener {
    public static final String TOKEN = "Token";
    public static final String AMOUNT = "Amount";
    public static final String ADDRESS = "Address";
    public static final String WALLET_ID = "WalletId";
    public static final String DATA = "Data";

    @Inject
    TransferViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @BindView(android.R.id.progress)
    ProgressBar progress;
    @BindView(R.id.transferInfoLayout)
    View transferInfoLayout;
    @BindView(R.id.toText)
    TextView toText;
    @BindView(R.id.fromText)
    TextView fromText;
    @BindView(R.id.feeText)
    TextView feeText;
    @BindView(R.id.amountText)
    TextView amountText;
    @BindView(R.id.totalText)
    TextView totalText;
    @BindView(R.id.divider)
    View divider;

    private ContractInfo token;
    private GasParams params = null;
    private WalletBaseRepository.WalletExtensionData walletData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);

        token = (ContractInfo) getIntent().getSerializableExtra(TOKEN);
        walletData = viewModel.getWalletData(getIntent().getLongExtra(WALLET_ID, 0L),
                getFilesDir(),
                viewModel.retrieveString(MyKeyStore.PASSWORD));
        initUI();
        getGasPrice();
    }

    @OnClick(R.id.confirmButton)
    public void onConfirmClick() {
        WalletPasswordDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    private void initUI() {
        String from = walletData.getCredentials().getAddress();
        String to = getIntent().getStringExtra(ADDRESS);
        String amount = getIntent().getStringExtra(AMOUNT);
        toText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.to), to)));
        fromText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.from), Eip55.convertToEip55Address(from))));
        amountText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s %s</font>", getString(R.string.transfer_amount), amount, token == null ? viewModel.getNetwork(walletData.getWallet().getNetworkId()).getSymbol() : token.getSymbol())));
    }

    private void getGasPrice() {
        String to = getIntent().getStringExtra(ADDRESS);
        String amount = getIntent().getStringExtra(AMOUNT);
        String data = getIntent().getStringExtra(DATA);

        progress.setVisibility(View.VISIBLE);
        transferInfoLayout.setVisibility(View.INVISIBLE);
        try {
            viewModel.getTransactionFee(walletData.getCredentials(), token, to, new BigDecimal(amount), data).observe(this, response -> {
                if (response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                    progress.setVisibility(View.GONE);

                    if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                        transferInfoLayout.setVisibility(View.VISIBLE);

                        params = response.getData();
                        BigInteger fee = params.getGasPrice().multiply(params.getGasLimit());
                        feeText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s %s</font>", getString(R.string.fee), Convert.fromWei(fee.toString(), Convert.Unit.ETHER).toString(), viewModel.getNetwork(walletData.getWallet().getNetworkId()).getSymbol())));
                        if (token == null) {
                            BigDecimal bdFee = Convert.fromWei(new BigDecimal(fee), Convert.Unit.ETHER);
                            totalText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s %s</font>", getString(R.string.total_incl_fee), new BigDecimal(amount).add(bdFee).toString(), viewModel.getNetwork(walletData.getWallet().getNetworkId()).getSymbol())));
                        } else {
                            divider.setVisibility(View.GONE);
                            totalText.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(this, response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccept(WalletPasswordDialogFragment dialog, String password) {
        dialog.dismiss();

        Credentials credentials = walletData.getCredentials();
        String toAddress = getIntent().getStringExtra(ADDRESS);
        String fromAddress = credentials.getAddress();
        String amount = getIntent().getStringExtra(AMOUNT);
        String data = getIntent().getStringExtra(DATA);
        String networkSymbol = viewModel.getNetwork(walletData.getWallet().getNetworkId()).getSymbol();

        ProgressDialog progress = UIUtils.getProgressDialog(this, false, getString(R.string.transferring));
        progress.show();

        viewModel.sendTransaction(credentials,
                params.getNonce(),
                params.getGasPrice(),
                params.getGasLimit(),
                token,
                toAddress,
                new BigDecimal(amount),
                data
        ).observe(this, response -> {
            if (response.getLiveStatus() != LiveResponseStatus.FAILED) {
                progress.dismiss();

                if (response.getData() != null && response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    if (viewModel.getDestination(toAddress) == null)
                        viewModel.insertDestination(new Destination(toAddress));
                    viewModel.insertTransaction(new NiluTransaction(fromAddress, toAddress, new BigDecimal(amount), new Date(), response.getData()));
                    startService(new Intent(this, TransferService.class)
                            .putExtra(TransferService.HASH, response.getData())
                            .putExtra(TransferService.AMOUNT, amount)
                            .putExtra(TransferService.NETWORK_SYMBOL, token == null ? networkSymbol : token.getSymbol()));

                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle(getString(R.string.transfer))
                            .setMessage(getString(R.string.transfer_funds_message, response.getData()))
                            .setNegativeButton(getString(R.string.close), (infoDialog, which) -> {
                                infoDialog.dismiss();

                                setResult(RESULT_OK, new Intent()
                                        .putExtra(ReceiptActivity.NETWORK_SYMBOL, viewModel.getNetwork(walletData.getWallet().getNetworkId()).getSymbol())
                                        .putExtra(ReceiptActivity.HASH, response.getData()));
                                finish();
                            }).show();
                }
            } else
                Toast.makeText(this, response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
