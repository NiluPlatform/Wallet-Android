package tech.nilu.wallet.injection;

import tech.nilu.wallet.ui.send.transfer.TransferService;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Navid on 12/28/17.
 */

@Module
abstract class ServiceBuilderModule {
    @ContributesAndroidInjector
    abstract TransferService contributeTransferService();
}
