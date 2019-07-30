package tech.nilu.wallet.ui.wallets.creation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.ui.wallets.importing.ImportWalletActivity;
import tech.nilu.wallet.util.AnalyticsUtil;
import tech.nilu.wallet.util.MyKeyStore;
import tech.nilu.wallet.util.UIUtils;

/**
 * Created by Navid on 12/28/17.
 */

public class CreateWalletActivity extends BaseActivity implements HasSupportFragmentInjector {
    public static final String NETWORK = "Network";
    public static final String WALLET_ID = "WalletId";
    public static final String BACKUP = "Backup";

    private static final int RC_IMPORT_WALLET = 1001;

    @Inject
    WalletViewModel viewModel;
    @Inject
    BaseViewModel baseViewModel;
    @Inject
    AnalyticsUtil firebase;

    @BindView(R.id.nameText)
    TextInputEditText nameText;
    @BindView(R.id.nameTextLayout)
    TextInputLayout nameTextLayout;
    @BindView(R.id.passwordText)
    TextInputEditText passwordText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        ButterKnife.bind(this);

        passwordText.setText(baseViewModel.retrieveString(MyKeyStore.PASSWORD));
    }

    @OnClick(R.id.createButton)
    public void onCreateClick() {
        nameTextLayout.setError(null);

        String name = nameText.getText().toString();
        String password = baseViewModel.retrieveString(MyKeyStore.PASSWORD);

        if (TextUtils.isEmpty(name)) {
            nameTextLayout.setError("Invalid name");
            return;
        }

        createWallet(name, password);
    }

    @OnClick(R.id.importWalletButton)
    public void onImportWalletClick() {
        startActivityForResult(new Intent(this, ImportWalletActivity.class)
                .putExtra(ImportWalletActivity.NETWORK, getIntent().getLongExtra(NETWORK, 0L)), RC_IMPORT_WALLET);
    }

    private void createWallet(String name, String password) {
        ProgressDialog progress = UIUtils.getProgressDialog(this, false, getString(R.string.please_wait));
        progress.show();

        long networkId = getIntent().getLongExtra(NETWORK, 0L);
        viewModel.createWallet(name, password, getFilesDir(), networkId).observe(this, response -> {
            if (response != null && response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                firebase.logCreateWalletEvent(networkId);

                progress.dismiss();
                setResult(RESULT_OK, new Intent()
                        .putExtra(CreateWalletActivity.WALLET_ID, response.getData())
                        .putExtra(CreateWalletActivity.BACKUP, true));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMPORT_WALLET && resultCode == RESULT_OK && data != null) {
            firebase.logImportWalletEvent(getIntent().getLongExtra(NETWORK, 0L));

            setResult(RESULT_OK, new Intent()
                    .putExtra(CreateWalletActivity.WALLET_ID, data.getLongExtra(CreateWalletActivity.WALLET_ID, 0L))
                    .putExtra(CreateWalletActivity.BACKUP, false));
            finish();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}
