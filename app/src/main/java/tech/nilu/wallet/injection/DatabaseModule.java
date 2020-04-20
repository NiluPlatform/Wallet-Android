package tech.nilu.wallet.injection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.nilu.wallet.db.Migrations;
import tech.nilu.wallet.db.MyDatabase;
import tech.nilu.wallet.db.dao.ContractInfoDao;
import tech.nilu.wallet.db.dao.DestinationDao;
import tech.nilu.wallet.db.dao.NetworkDao;
import tech.nilu.wallet.db.dao.TransactionDao;
import tech.nilu.wallet.db.dao.WalletDao;

/**
 * Created by root on 11/28/17.
 */

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    MyDatabase provideDatabase(Application app) {
        return Room.databaseBuilder(app, MyDatabase.class, "nilu.db")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(app.getAssets().open("nilu.sql")))) {
                            for (String line; (line = br.readLine()) != null; )
                                db.execSQL(line);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .allowMainThreadQueries()
                .addMigrations(Migrations.MIGRATION_1_2, Migrations.MIGRATION_2_3, Migrations.MIGRATION_3_4)
                .build();
    }

    @Provides
    @Singleton
    ContractInfoDao provideContractInfoDao(MyDatabase db) {
        return db.contractInfoDao();
    }

    @Provides
    @Singleton
    DestinationDao provideDestinationDao(MyDatabase db) {
        return db.destinationDao();
    }

    @Provides
    @Singleton
    NetworkDao provideNetworkDao(MyDatabase db) {
        return db.networkDao();
    }

    @Provides
    @Singleton
    TransactionDao provideTransactionDao(MyDatabase db) {
        return db.transactionDao();
    }

    @Provides
    @Singleton
    WalletDao provideWalletDao(MyDatabase db) {
        return db.walletDao();
    }
}


