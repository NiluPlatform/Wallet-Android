package tech.nilu.wallet.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by root on 1/30/18.
 */

@Entity
public class NiluTransaction {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String fromAddress;
    private String toAddress;
    private BigDecimal amount;
    private Date time;
    private String hash;

    public NiluTransaction(String fromAddress, String toAddress, BigDecimal amount, Date time, String hash) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.time = time;
        this.hash = hash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
