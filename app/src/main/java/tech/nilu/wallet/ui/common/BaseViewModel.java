package tech.nilu.wallet.ui.common;

import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.repository.NetworkRepository;
import tech.nilu.wallet.repository.SecurityRepository;
import tech.nilu.wallet.repository.SharedPreferencesRepository;

/**
 * Created by root on 1/8/18.
 */

public class BaseViewModel extends ViewModel {
    private final SecurityRepository securityRepository;
    private final SharedPreferencesRepository sharedPreferencesRepository;
    private final NetworkRepository networkRepository;

    @Inject
    public BaseViewModel(SecurityRepository securityRepository, SharedPreferencesRepository sharedPreferencesRepository, NetworkRepository networkRepository) {
        this.securityRepository = securityRepository;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.networkRepository = networkRepository;
    }

    public void storeString(String key, String value) {
        securityRepository.storeString(key, value);
    }

    public String retrieveString(String key) {
        return securityRepository.retrieveString(key);
    }

    public String getPreferenceString(String key) {
        return sharedPreferencesRepository.getPreferenceString(key);
    }

    public int getPreferenceInt(String key) {
        return sharedPreferencesRepository.getPreferenceInt(key);
    }

    public Network getNetwork(long id) {
        return networkRepository.getNetwork(id);
    }

    public Network getNetwork(String address) {
        return networkRepository.getNetwork(address);
    }

    public List<Network> getNetworks() {
        return networkRepository.getNetworks();
    }

    public Network getActiveNetwork() {
        return networkRepository.getActiveNetwork();
    }

    public void deactivateNetworks() {
        networkRepository.deactivateNetworks();
    }

    public void activateNetwork(Network toActivate) {
        deactivateNetworks();
        toActivate.setActive(1);
        updateNetwork(toActivate);
    }

    public void updateNetwork(Network network) {
        networkRepository.updateNetwork(network);
    }
}
