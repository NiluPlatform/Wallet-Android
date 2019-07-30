package tech.nilu.wallet;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.UUID;

import javax.inject.Inject;

import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.repository.NetworkRepository;
import tech.nilu.wallet.repository.SecurityRepository;
import tech.nilu.wallet.util.MyKeyStore;

/**
 * Created by root on 1/8/18.
 */

public class MainViewModel extends AndroidViewModel {
    private final SecurityRepository securityRepository;
    private final NetworkRepository networkRepository;

    @Inject
    public MainViewModel(@NonNull Application application, SecurityRepository securityRepository, NetworkRepository networkRepository) {
        super(application);
        this.securityRepository = securityRepository;
        this.networkRepository = networkRepository;
    }

    public void createKeys() {
        securityRepository.createKeys();
    }

    public void generateIdentifier() {
        if (!securityRepository.hasIdentifier())
            securityRepository.storeString(MyKeyStore.IDENTIFIER, UUID.randomUUID().toString());
    }

    public boolean hasPassword() {
        return securityRepository.hasPassword();
    }

    public void deactivateNetworks() {
        networkRepository.deactivateNetworks();
    }

    public Network activateNetwork(String address) {
        deactivateNetworks();
        Network toActivate = getNetwork(address);
        toActivate.setActive(1);
        updateNetwork(toActivate);
        return toActivate;
    }

    public void updateNetwork(Network network) {
        networkRepository.updateNetwork(network);
    }

    public Network getNetwork(String address) {
        return networkRepository.getNetwork(address);
    }
}
