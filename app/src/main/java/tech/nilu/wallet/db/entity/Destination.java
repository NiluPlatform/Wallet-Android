package tech.nilu.wallet.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by root on 1/30/18.
 */

@Entity
public class Destination {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String address;

    public Destination(String address) {
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return address;
    }
}
