package tech.nilu.wallet.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tech.nilu.wallet.db.entity.Destination;

/**
 * Created by root on 1/30/18.
 */

@Dao
public interface DestinationDao {
    //region CREATE
    @Insert
    long insertDestination(Destination destination);
    //endregion

    //region READ
    @Query("SELECT * FROM Destination WHERE address = :address LIMIT 1")
    Destination getDestination(String address);

    @Query("SELECT * FROM Destination")
    List<Destination> getDestinations();

    @Query("SELECT * FROM Destination")
    LiveData<List<Destination>> getDestinationsLive();

    @Query("SELECT COUNT(*) FROM Destination")
    int countDestinations();
    //endregion

    //region UPDATE
    //endregion

    //region DELETE
    @Query("DELETE FROM Destination")
    void deleteAll();
    //endregion
}
