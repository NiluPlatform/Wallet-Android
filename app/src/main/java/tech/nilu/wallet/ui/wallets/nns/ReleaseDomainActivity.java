package tech.nilu.wallet.ui.wallets.nns;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.web3j.utils.Convert;

import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.WalletBaseRepository;
import tech.nilu.wallet.ui.common.AVLIVDialogFragment;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.password.WalletPasswordDialogFragment;
import tech.nilu.wallet.ui.wallets.WalletViewModel;

public class ReleaseDomainActivity extends BaseActivity implements HasSupportFragmentInjector, WalletPasswordDialogFragment.WalletPasswordClickListener {
    public static final String WALLET_ID = "WalletId";
    public static final String OWNED = "OwnedDomain";

    @Inject
    NNSViewModel nnsViewModel;
    @Inject
    WalletViewModel walletViewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @BindView(android.R.id.progress)
    ProgressBar progress;
    @BindView(R.id.liberatorInfoLayout)
    View liberatorInfoLayout;
    @BindView(R.id.feeText)
    TextView feeText;
    @BindView(R.id.domainText)
    TextView domainText;
    @BindView(R.id.liberatorText)
    TextView liberatorText;

    private AVLIVDialogFragment progressDialog;
    private WalletBaseRepository.WalletExtensionData wallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_domain);

        wallet = walletViewModel.getWalletData(getIntent().getLongExtra(WALLET_ID, 0L));

        initUI();
        fetchGasPrice();
        nnsViewModel.releaseDomain.observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                if (progressDialog != null && progressDialog.getDialog() != null && progressDialog.getDialog().isShowing())
                    progressDialog.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    Toast.makeText(this, getString(R.string.successful_release_domain, getIntent().getStringExtra(OWNED)), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else
                    Toast.makeText(this, response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.releaseButton)
    public void onReleaseClick() {
        WalletPasswordDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    private void initUI() {
        progressDialog = AVLIVDialogFragment.newInstance(getString(R.string.releasing, getIntent().getStringExtra(OWNED)));
        progressDialog.setCancelable(false);
    }

    private void fetchGasPrice() {
        progress.setVisibility(View.VISIBLE);
        liberatorInfoLayout.setVisibility(View.INVISIBLE);
        nnsViewModel.getGasPrice().observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.setVisibility(View.GONE);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    liberatorInfoLayout.setVisibility(View.VISIBLE);

                    BigInteger fee = response.getData().multiply(new BigInteger("225000")); //sum of estimation for each step (40K + 35K + 35K + 45K + 35K + 35K)
                    domainText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.domain), getIntent().getStringExtra(OWNED))));
                    liberatorText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.liberator), wallet.getCredentials().getAddress())));
                    feeText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s %s</font>", getString(R.string.fee), Convert.fromWei(fee.toString(), Convert.Unit.ETHER).toString(), walletViewModel.getNetwork(wallet.getWallet().getNetworkId()).getSymbol())));
                }
            }
        });
    }

    @Override
    public void onAccept(WalletPasswordDialogFragment dialog, String password) {
        dialog.dismiss();

        if (progressDialog != null && (progressDialog.getDialog() == null || !progressDialog.getDialog().isShowing()))
            progressDialog.show(getSupportFragmentManager(), null);
        nnsViewModel.setReleaseDomainData(wallet.getWallet().getId(), getIntent().getStringExtra(OWNED));
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
