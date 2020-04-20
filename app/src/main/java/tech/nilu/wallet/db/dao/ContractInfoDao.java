package tech.nilu.wallet.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tech.nilu.wallet.db.entity.ContractInfo;

/**
 * Created by root on 1/10/18.
 */

@Dao
public interface ContractInfoDao {
    //region CREATE
    @Insert
    long insertContractInfo(ContractInfo contractInfo);

    @Insert
    void insertContractInfos(List<ContractInfo> contractInfos);
    //endregion

    //region READ
    @Query("SELECT * FROM ContractInfo WHERE id = :id")
    ContractInfo getContractInfo(long id);

    @Query("SELECT * FROM ContractInfo WHERE walletId = :walletId AND address = :address")
    ContractInfo getContractInfo(long walletId, String address);

    @Query("SELECT * FROM ContractInfo WHERE id = :id")
    LiveData<ContractInfo> getContractInfoLive(long id);

    @Query("SELECT * FROM ContractInfo WHERE walletId = :walletId")
    List<ContractInfo> getContractInfos(long walletId);

    @Query("SELECT * FROM ContractInfo WHERE walletId = :walletId")
    LiveData<List<ContractInfo>> getContractInfosLive(long walletId);

    @Query("SELECT COUNT(*) FROM ContractInfo")
    int countContractInfos();
    //endregion

    //region UPDATE
    @Update
    void updateContractInfo(ContractInfo contractInfo);
    //endregion

    //region DELETE
    @Query("DELETE FROM ContractInfo")
    void deleteAll();

    @Query("DELETE FROM ContractInfo WHERE id = :id")
    void deleteContractInfo(long id);

    @Query("DELETE FROM ContractInfo WHERE walletId = :walletId")
    void deleteContractInfos(long walletId);

    @Delete
    void deleteContractInfo(ContractInfo contractInfo);

    @Delete
    void deleteContractInfos(List<ContractInfo> contractInfos);
    //endregion
}
