package tech.nilu.wallet.api.model.token;

import java.math.BigDecimal;

public class TokenInfo {
    private final String address;
    private final String name;
    private final int decimals;
    private final String symbol;
    private final String totalSupply;
    private final String owner;
    private final int transfersCount;
    private final long lastUpdated;
    private final BigDecimal totalIn;
    private final BigDecimal totalOut;
    private final int issuancesCount;
    private final int holdersCount;
    private final String description;

    public TokenInfo(String address, String name, int decimals, String symbol, String totalSupply, String owner, int transfersCount, long lastUpdated, BigDecimal totalIn, BigDecimal totalOut, int issuancesCount, int holdersCount, String description) {
        this.address = address;
        this.name = name;
        this.decimals = decimals;
        this.symbol = symbol;
        this.totalSupply = totalSupply;
        this.owner = owner;
        this.transfersCount = transfersCount;
        this.lastUpdated = lastUpdated;
        this.totalIn = totalIn;
        this.totalOut = totalOut;
        this.issuancesCount = issuancesCount;
        this.holdersCount = holdersCount;
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getDecimals() {
        return decimals;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public String getOwner() {
        return owner;
    }

    public int getTransfersCount() {
        return transfersCount;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public BigDecimal getTotalIn() {
        return totalIn;
    }

    public BigDecimal getTotalOut() {
        return totalOut;
    }

    public int getIssuancesCount() {
        return issuancesCount;
    }

    public int getHoldersCount() {
        return holdersCount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TokenInfoResponse{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", decimals=" + decimals +
                ", symbol='" + symbol + '\'' +
                ", totalSupply='" + totalSupply + '\'' +
                ", owner='" + owner + '\'' +
                ", transfersCount=" + transfersCount +
                ", lastUpdated=" + lastUpdated +
                ", totalIn=" + totalIn +
                ", totalOut=" + totalOut +
                ", issuancesCount=" + issuancesCount +
                ", holdersCount=" + holdersCount +
                ", description='" + description + '\'' +
                '}';
    }
}
