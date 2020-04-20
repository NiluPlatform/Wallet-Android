package tech.nilu.wallet.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tech.nilu.wallet.db.entity.Network;

/**
 * Created by Navid on 2/15/18.
 */

@Dao
public interface NetworkDao {
    //region CREATE
    @Insert
    long insertNetwork(Network network);

    @Insert
    void insertNetworks(List<Network> networks);
    //endregion

    //region READ
    @Query("SELECT * FROM Network WHERE id = :id")
    Network getNetwork(long id);

    @Query("SELECT * FROM Network WHERE address = :address")
    Network getNetwork(String address);

    @Query("SELECT * FROM Network WHERE active = 1 LIMIT 1")
    Network getActiveNetwork();

    @Query("SELECT * FROM Network WHERE active = 1 LIMIT 1")
    LiveData<Network> getActiveNetworkLive();

    @Query("SELECT * FROM Network")
    List<Network> getNetworks();

    @Query("SELECT * FROM Network")
    LiveData<List<Network>> getNetworksLive();

    @Query("SELECT COUNT(*) FROM Network")
    int countNetworks();
    //endregion

    //region UPDATE
    @Query("UPDATE Network SET active = 0")
    void deactivateNetworks();

    @Update
    void updateNetwork(Network network);
    //endregion

    //region DELETE
    @Query("DELETE FROM Network")
    void deleteAll();
    //endregion
}
