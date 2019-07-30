package tech.nilu.wallet.ui.faucet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import tech.nilu.wallet.api.model.faucet.FaucetResponse;
import tech.nilu.wallet.api.model.meta.AppIdResponse;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.model.LiveResponse;
import tech.nilu.wallet.repository.FaucetRepository;
import tech.nilu.wallet.repository.NetworkRepository;

public class FaucetViewModel extends ViewModel {
    private final FaucetRepository faucetRepository;
    private final NetworkRepository networkRepository;

    @Inject
    public FaucetViewModel(FaucetRepository faucetRepository, NetworkRepository networkRepository) {
        this.faucetRepository = faucetRepository;
        this.networkRepository = networkRepository;
    }

    public LiveData<LiveResponse<FaucetResponse>> getFaucet(String type, String address) {
        return faucetRepository.getFaucet(type, address);
    }

    public LiveData<LiveResponse<AppIdResponse>> getAppId() {
        return faucetRepository.getAppId();
    }

    public Network getNetwork(String address) {
        return networkRepository.getNetwork(address);
    }
}
