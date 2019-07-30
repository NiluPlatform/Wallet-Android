package tech.nilu.wallet.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import tech.nilu.wallet.db.dao.ContractInfoDao;
import tech.nilu.wallet.db.entity.ContractInfo;

/**
 * Created by root on 1/10/18.
 */

@Singleton
public class ContractInfoRepository {
    private final ContractInfoDao contractInfoDao;

    @Inject
    public ContractInfoRepository(ContractInfoDao contractInfoDao) {
        this.contractInfoDao = contractInfoDao;
    }

    public long insertContractInfo(ContractInfo contractInfo) {
        return contractInfoDao.insertContractInfo(contractInfo);
    }

    public void insertContractInfos(List<ContractInfo> contractInfos) {
        contractInfoDao.insertContractInfos(contractInfos);
    }

    public ContractInfo getContractInfo(long id) {
        return contractInfoDao.getContractInfo(id);
    }

    public ContractInfo getContractInfo(long walletId, String address) {
        return contractInfoDao.getContractInfo(walletId, address);
    }

    public List<ContractInfo> getContractInfos(long walletId) {
        return contractInfoDao.getContractInfos(walletId);
    }

    public LiveData<List<ContractInfo>> getContractInfosLive(long walletId) {
        return contractInfoDao.getContractInfosLive(walletId);
    }

    public void deleteContractInfo(ContractInfo ci) {
        contractInfoDao.deleteContractInfo(ci);
    }

    public void deleteContractInfos(long walletId) {
        contractInfoDao.deleteContractInfos(walletId);
    }
}
