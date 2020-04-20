package tech.nilu.wallet.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import tech.nilu.wallet.db.converter.BigDecimalConverter;
import tech.nilu.wallet.db.converter.DateConverter;
import tech.nilu.wallet.db.dao.ContractInfoDao;
import tech.nilu.wallet.db.dao.DestinationDao;
import tech.nilu.wallet.db.dao.NetworkDao;
import tech.nilu.wallet.db.dao.TransactionDao;
import tech.nilu.wallet.db.dao.WalletDao;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Destination;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.db.entity.NiluTransaction;
import tech.nilu.wallet.db.entity.Wallet;

/**
 * Created by Navid on 11/28/17.
 */

@Database(entities = {
        ContractInfo.class,
        Destination.class,
        Network.class,
        NiluTransaction.class,
        Wallet.class
}, version = 4)
@TypeConverters({BigDecimalConverter.class, DateConverter.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract ContractInfoDao contractInfoDao();

    public abstract DestinationDao destinationDao();

    public abstract NetworkDao networkDao();

    public abstract TransactionDao transactionDao();

    public abstract WalletDao walletDao();
}
