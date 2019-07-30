package tech.nilu.wallet.injection;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import tech.nilu.wallet.MainActivity;
import tech.nilu.wallet.ui.faucet.GetAppIdActivity;
import tech.nilu.wallet.ui.faucet.LuckyNiluActivity;
import tech.nilu.wallet.ui.password.SetPasswordActivity;
import tech.nilu.wallet.ui.send.SendMoneyActivity;
import tech.nilu.wallet.ui.send.transfer.TransferActivity;
import tech.nilu.wallet.ui.tokens.TokenDetailsActivity;
import tech.nilu.wallet.ui.tokens.creation.CreateTokenActivity;
import tech.nilu.wallet.ui.tokens.creation.DeployActivity;
import tech.nilu.wallet.ui.transactions.ReceiptActivity;
import tech.nilu.wallet.ui.wallets.backup.BackupWalletActivity;
import tech.nilu.wallet.ui.wallets.contracts.ContractsListActivity;
import tech.nilu.wallet.ui.wallets.creation.CreateWalletActivity;
import tech.nilu.wallet.ui.wallets.details.WalletDetailsActivity;
import tech.nilu.wallet.ui.wallets.importing.ImportWalletActivity;
import tech.nilu.wallet.ui.wallets.nns.RegisterDomainActivity;
import tech.nilu.wallet.ui.wallets.nns.ReleaseDomainActivity;
import tech.nilu.wallet.ui.wallets.nns.SearchDomainsActivity;

/**
 * Created by Navid on 12/28/17.
 */

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract CreateWalletActivity contributeCreateWalletActivity();

    @ContributesAndroidInjector
    abstract CreateTokenActivity contributeCreateTokenActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract DeployActivity contributeDeployActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract ContractsListActivity contributeContractsListActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract WalletDetailsActivity contributeWalletDetailsActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract TokenDetailsActivity contributeTokenDetailsActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract TransferActivity contributeTransferActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract SendMoneyActivity contributeSendMoneyActivity();

    @ContributesAndroidInjector
    abstract ReceiptActivity contributeReceiptActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract BackupWalletActivity contributeBackupWalletActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract ImportWalletActivity contributeImportWalletActivity();

    @ContributesAndroidInjector
    abstract SetPasswordActivity contributeSetPasswordActivity();

    @ContributesAndroidInjector
    abstract LuckyNiluActivity contributeLuckyNiluActivity();

    @ContributesAndroidInjector
    abstract SearchDomainsActivity contributeSearchDomainsActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract RegisterDomainActivity contributeRegisterDomainActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract ReleaseDomainActivity contributeReleaseDomainActivity();

    @ContributesAndroidInjector
    abstract GetAppIdActivity contributeGetAppIdActivity();
}
