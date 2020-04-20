package tech.nilu.wallet.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by root on 1/10/18.
 */

@Entity(foreignKeys = @ForeignKey(entity = Wallet.class, parentColumns = "id", childColumns = "walletId"), indices = @Index("walletId"))
public class ContractInfo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long walletId;
    private String address;
    private String name;
    private String image;
    private String types;
    private String symbol;

    @Ignore
    private BigInteger balance;

    @Ignore
    private String tokenName;
    @Ignore
    private String tokenSymbol;
    @Ignore
    private BigInteger tokenTotalSupply;
    @Ignore
    private BigInteger tokenDecimals;
    @Ignore
    private BigInteger tokenRate;
    @Ignore
    private Boolean tokenIsPayable;

    public ContractInfo(long walletId, String address, String name, String image, String types) {
        this.walletId = walletId;
        this.address = address;
        this.name = name;
        this.image = image;
        this.types = types;
        this.symbol = null;
        this.balance = BigInteger.ZERO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWalletId() {
        return walletId;
    }

    public void setWalletId(long walletId) {
        this.walletId = walletId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public BigInteger getTokenTotalSupply() {
        return tokenTotalSupply;
    }

    public void setTokenTotalSupply(BigInteger tokenTotalSupply) {
        this.tokenTotalSupply = tokenTotalSupply;
    }

    public BigInteger getTokenDecimals() {
        return tokenDecimals;
    }

    public void setTokenDecimals(BigInteger tokenDecimals) {
        this.tokenDecimals = tokenDecimals;
    }

    public BigInteger getTokenRate() {
        return tokenRate;
    }

    public void setTokenRate(BigInteger tokenRate) {
        this.tokenRate = tokenRate;
    }

    public Boolean getTokenIsPayable() {
        return tokenIsPayable;
    }

    public void setTokenIsPayable(Boolean tokenIsPayable) {
        this.tokenIsPayable = tokenIsPayable;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ContractInfo && ((ContractInfo) obj).getAddress().equals(address);
    }
}
