package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import tech.nilu.wallet.db.dao.NetworkDao;
import tech.nilu.wallet.db.entity.Network;

/**
 * Created by Navid on 2/15/18.
 */

@Singleton
public class NetworkRepository {
    private final NetworkDao networkDao;

    @Inject
    public NetworkRepository(NetworkDao networkDao) {
        this.networkDao = networkDao;
    }

    public long insertNetwork(Network network) {
        return networkDao.insertNetwork(network);
    }

    public void insertNetworks(List<Network> networks) {
        networkDao.insertNetworks(networks);
    }

    public Network getNetwork(long id) {
        return networkDao.getNetwork(id);
    }

    public Network getNetwork(String address) {
        return networkDao.getNetwork(address);
    }

    public Network getActiveNetwork() {
        return networkDao.getActiveNetwork();
    }

    public LiveData<Network> getActiveNetworkLive() {
        return networkDao.getActiveNetworkLive();
    }

    public List<Network> getNetworks() {
        return networkDao.getNetworks();
    }

    public LiveData<List<Network>> getNetworksLive() {
        return networkDao.getNetworksLive();
    }

    public boolean hasNetwork() {
        return networkDao.countNetworks() > 0;
    }

    public void deactivateNetworks() {
        networkDao.deactivateNetworks();
    }

    public void updateNetwork(Network network) {
        networkDao.updateNetwork(network);
    }

    public void deleteAll() {
        networkDao.deleteAll();
    }
}
