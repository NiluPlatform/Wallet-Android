package tech.nilu.wallet.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by mnemati on 1/4/18.
 */


@Entity(foreignKeys = @ForeignKey(entity = Network.class, parentColumns = "id", childColumns = "networkId"), indices = @Index("networkId"))
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String path;
    private String mnemonic;
    private long networkId;

    public Wallet(String name, String path, String mnemonic, long networkId) {
        this.name = name;
        this.path = path;
        this.mnemonic = mnemonic;
        this.networkId = networkId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(long networkId) {
        this.networkId = networkId;
    }

    @Override
    public String toString() {
        return name;
    }
}
