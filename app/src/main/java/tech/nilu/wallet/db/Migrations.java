package tech.nilu.wallet.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

public class Migrations {
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE Network ADD COLUMN chainId INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE Network ADD COLUMN explorerAddress TEXT");
            db.execSQL("UPDATE Network SET address='https://walletapi.nilu.tech', chainId=512, explorerAddress='https://walletapi.nilu.tech/' WHERE id=1");
            db.execSQL("UPDATE Network SET chainId=1, explorerAddress='https://api.ethplorer.io/' WHERE id=2");
            db.execSQL("UPDATE Network SET chainId=3, explorerAddress='' WHERE id=3");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("INSERT INTO Network (id,name,address,active,symbol,chainId,explorerAddress) VALUES (4,'Pirl','https://rpc.pirl.minerpool.net',0,'PIRL',3125659152,'http://devpool.nilu.tech/pirl/')");
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("INSERT INTO Network (id,name,address,active,symbol,chainId,explorerAddress) VALUES (5,'Ether-1','https://rpc.ether1.org',0,'ETHO',1313114,'https://walletapi.ether1.org/')");
        }
    };
}
