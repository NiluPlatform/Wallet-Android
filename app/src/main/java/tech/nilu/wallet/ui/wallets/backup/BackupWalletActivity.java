package tech.nilu.wallet.ui.wallets.backup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.password.WalletPasswordDialogFragment;
import tech.nilu.wallet.ui.wallets.WalletViewModel;

/**
 * Created by root on 2/5/18.
 */

public class BackupWalletActivity extends BaseActivity implements HasSupportFragmentInjector, WalletPasswordDialogFragment.WalletPasswordClickListener {
    public static final String WALLET_ID = "WalletId";

    private static final int RC_BACKUP_MNEMONIC = 1001;

    @Inject
    WalletViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private Wallet wallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_wallet);
        ButterKnife.bind(this);

        wallet = viewModel.getWallet(getIntent().getLongExtra(WALLET_ID, 0L));
        initUI();
    }

    private void initUI() {
        getSupportActionBar().setTitle(String.format("%s %s", getString(R.string.backup), wallet.getName()));
    }

    @OnClick(R.id.backupButton)
    public void onBackupClick() {
        WalletPasswordDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    @Override
    public void onAccept(WalletPasswordDialogFragment dialog, String password) {
        startActivityForResult(new Intent(this, BackupMnemonicsActivity.class)
                .putExtra(BackupMnemonicsActivity.MNEMONIC, wallet.getMnemonic()), RC_BACKUP_MNEMONIC);
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_BACKUP_MNEMONIC && resultCode == RESULT_OK) {
            wallet.setMnemonic(null);
            viewModel.updateWallet(wallet);
            viewModel.getWalletData(wallet.getId()).getWallet().setMnemonic(null);

            setResult(RESULT_OK, new Intent().putExtra(BackupWalletActivity.WALLET_ID, wallet.getId()));
            finish();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
