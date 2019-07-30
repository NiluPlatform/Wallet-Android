package tech.nilu.wallet.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Navid on 2/15/18.
 */

@Entity
public class Network implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String address;
    private int active;
    private String symbol;
    private long chainId;
    private String explorerAddress;

    public Network(String name, String address, int active, String symbol, long chainId, String explorerAddress) {
        this.name = name;
        this.address = address;
        this.active = active;
        this.symbol = symbol;
        this.chainId = chainId;
        this.explorerAddress = explorerAddress;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getChainId() {
        return chainId;
    }

    public void setChainId(long chainId) {
        this.chainId = chainId;
    }

    public String getExplorerAddress() {
        return explorerAddress;
    }

    public void setExplorerAddress(String explorerAddress) {
        this.explorerAddress = explorerAddress;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Network) obj).getAddress().equals(address);
    }
}
