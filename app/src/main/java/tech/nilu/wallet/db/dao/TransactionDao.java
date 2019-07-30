package tech.nilu.wallet.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import tech.nilu.wallet.db.entity.NiluTransaction;

/**
 * Created by root on 2/3/18.
 */

@Dao
public interface TransactionDao {
    //region CREATE
    @Insert
    long insertTransaction(NiluTransaction transaction);
    //endregion

    //region READ
    @Query("SELECT * FROM NiluTransaction WHERE hash = :hash")
    NiluTransaction getTransaction(String hash);

    @Query("SELECT * FROM NiluTransaction WHERE fromAddress = :address ORDER BY time DESC")
    List<NiluTransaction> getTransactions(String address);

    @Query("SELECT * FROM NiluTransaction WHERE fromAddress = :address ORDER BY time DESC")
    LiveData<List<NiluTransaction>> getTransactionsLive(String address);

    @Query("SELECT * FROM NiluTransaction ORDER BY time DESC")
    List<NiluTransaction> getTransactions();

    @Query("SELECT * FROM NiluTransaction ORDER BY time DESC")
    LiveData<List<NiluTransaction>> getTransactionsLive();
    //endregion

    //region UPDATE
    //endregion

    //region DELETE
    @Query("DELETE FROM NiluTransaction")
    void deleteAll();

    @Query("DELETE FROM NiluTransaction WHERE fromAddress = :address")
    void deleteTransactions(String address);
    //endregion
}
