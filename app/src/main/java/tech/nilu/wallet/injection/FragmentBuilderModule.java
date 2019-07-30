package tech.nilu.wallet.injection;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import tech.nilu.wallet.ui.common.AddTokenDialogFragment;
import tech.nilu.wallet.ui.common.NetworksListDialogFragment;
import tech.nilu.wallet.ui.password.WalletPasswordDialogFragment;
import tech.nilu.wallet.ui.receive.ReceiveMoneyFragment;
import tech.nilu.wallet.ui.send.SendMoneyFragment;
import tech.nilu.wallet.ui.tokens.TokenInformationFragment;
import tech.nilu.wallet.ui.tokens.TokenTransactionsFragment;
import tech.nilu.wallet.ui.tokens.TokenTransferFragment;
import tech.nilu.wallet.ui.wallets.WalletsFragment;
import tech.nilu.wallet.ui.wallets.contracts.SelectedTokensFragment;
import tech.nilu.wallet.ui.wallets.contracts.VerifiedTokensFragment;
import tech.nilu.wallet.ui.wallets.details.WalletReceiveFragment;
import tech.nilu.wallet.ui.wallets.details.WalletSendFragment;
import tech.nilu.wallet.ui.wallets.details.WalletTokensFragment;
import tech.nilu.wallet.ui.wallets.details.WalletTransactionsFragment;
import tech.nilu.wallet.ui.wallets.importing.KeystoreImportFragment;
import tech.nilu.wallet.ui.wallets.importing.MnemonicsImportFragment;
import tech.nilu.wallet.ui.wallets.importing.PrivateKeyImportFragment;

/**
 * Created by root on 1/8/18.
 */

@Module
public abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract WalletsFragment contributeWalletsFragment();

    @ContributesAndroidInjector
    abstract SendMoneyFragment contributeSendMoneyFragment();

    @ContributesAndroidInjector
    abstract ReceiveMoneyFragment contributeReceiveMoneyFragment();

    @ContributesAndroidInjector
    abstract WalletSendFragment contributeWalletSendFragment();

    @ContributesAndroidInjector
    abstract WalletReceiveFragment contributeWalletReceiveFragment();

    @ContributesAndroidInjector
    abstract WalletTokensFragment contributeWalletTokensFragment();

    @ContributesAndroidInjector
    abstract WalletTransactionsFragment contributeWalletTransactionsFragment();

    @ContributesAndroidInjector
    abstract WalletPasswordDialogFragment contributeWalletPasswordDialogFragment();

    @ContributesAndroidInjector
    abstract NetworksListDialogFragment contributeNetworksListDialogFragment();

    @ContributesAndroidInjector
    abstract AddTokenDialogFragment contributeAddTokenDialogFragment();

    @ContributesAndroidInjector
    abstract TokenInformationFragment contributeTokenInformationFragment();

    @ContributesAndroidInjector
    abstract TokenTransactionsFragment contributeTokenTransactionsFragment();

    @ContributesAndroidInjector
    abstract TokenTransferFragment contributeTokenTransferFragment();

    @ContributesAndroidInjector
    abstract MnemonicsImportFragment contributeMnemonicsImportFragment();

    @ContributesAndroidInjector
    abstract KeystoreImportFragment contributeKeystoreImportFragment();

    @ContributesAndroidInjector
    abstract PrivateKeyImportFragment contributePrivateKeyImportFragment();

    @ContributesAndroidInjector
    abstract SelectedTokensFragment contributeSelectedTokensFragment();

    @ContributesAndroidInjector
    abstract VerifiedTokensFragment contributeVerifiedTokensFragment();
}
