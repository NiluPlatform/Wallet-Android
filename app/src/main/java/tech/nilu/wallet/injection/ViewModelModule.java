package tech.nilu.wallet.injection;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import tech.nilu.wallet.MainViewModel;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.faucet.FaucetViewModel;
import tech.nilu.wallet.ui.password.PasswordViewModel;
import tech.nilu.wallet.ui.send.transfer.TransferViewModel;
import tech.nilu.wallet.ui.tokens.TokenViewModel;
import tech.nilu.wallet.ui.transactions.TransactionViewModel;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.ui.wallets.nns.NNSViewModel;
import tech.nilu.wallet.viewmodel.NiluViewModelFactory;

/**
 * Created by root on 1/7/18.
 */

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(BaseViewModel.class)
    abstract ViewModel bindBaseViewModel(BaseViewModel baseViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WalletViewModel.class)
    abstract ViewModel bindWalletViewModel(WalletViewModel walletViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransferViewModel.class)
    abstract ViewModel bindTransferViewModel(TransferViewModel transferViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransactionViewModel.class)
    abstract ViewModel bindTransactionViewModel(TransactionViewModel transactionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PasswordViewModel.class)
    abstract ViewModel bindPasswordViewModel(PasswordViewModel passwordViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TokenViewModel.class)
    abstract ViewModel bindTokenViewModel(TokenViewModel tokenViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FaucetViewModel.class)
    abstract ViewModel bindFaucetViewModel(FaucetViewModel faucetViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NNSViewModel.class)
    abstract ViewModel bindNNSViewModel(NNSViewModel nnsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(NiluViewModelFactory factory);
}
