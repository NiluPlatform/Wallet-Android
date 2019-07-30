package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import tech.nilu.wallet.db.dao.DestinationDao;
import tech.nilu.wallet.db.entity.Destination;

/**
 * Created by root on 1/30/18.
 */

@Singleton
public class DestinationRepository {
    private final DestinationDao destinationDao;

    @Inject
    public DestinationRepository(DestinationDao destinationDao) {
        this.destinationDao = destinationDao;
    }

    public long insertDestination(Destination destination) {
        return destinationDao.insertDestination(destination);
    }

    public Destination getDestination(String address) {
        return destinationDao.getDestination(address);
    }

    public List<Destination> getDestinations() {
        return destinationDao.getDestinations();
    }

    public LiveData<List<Destination>> getDestinationsLive() {
        return destinationDao.getDestinationsLive();
    }

    public int countDestinations() {
        return destinationDao.countDestinations();
    }

    public void deleteAll() {
        destinationDao.deleteAll();
    }
}
