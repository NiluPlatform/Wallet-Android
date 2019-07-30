package tech.nilu.wallet.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tech.nilu.wallet.db.entity.Wallet;

/**
 * Created by Navid on 11/17/17.
 */

@Dao
public interface WalletDao {
    //region CREATE
    @Insert
    long insertWallet(Wallet wallet);

    @Insert
    void insertWallets(List<Wallet> wallets);
    //endregion

    //region READ
    @Query("SELECT * FROM Wallet WHERE id = :id")
    Wallet getWallet(long id);

    @Query("SELECT * FROM Wallet WHERE id = :id")
    LiveData<Wallet> getWalletLive(long id);

    @Query("SELECT * FROM Wallet ORDER BY id")
    List<Wallet> getWallets();

    @Query("SELECT * FROM Wallet ORDER BY id")
    LiveData<List<Wallet>> getWalletsLive();

    @Query("SELECT * FROM Wallet WHERE networkId = :networkId ORDER BY id")
    List<Wallet> getWallets(long networkId);

    @Query("SELECT * FROM Wallet WHERE networkId = :networkId ORDER BY id")
    LiveData<List<Wallet>> getWalletsLive(long networkId);

    @Query("SELECT COUNT(*) FROM Wallet")
    int countWallets();

    @Query("SELECT MAX(id) FROM Wallet")
    long getNextId();
    //endregion

    //region UPDATE
    @Update
    void updateWallet(Wallet wallet);
    //endregion

    //region DELETE
    @Query("DELETE FROM Wallet")
    void deleteAll();

    @Delete
    void deleteWallet(Wallet wallet);

    @Delete
    void deleteWallets(List<Wallet> wallets);
    //endregion
}
