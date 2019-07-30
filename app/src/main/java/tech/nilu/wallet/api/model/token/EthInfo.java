package tech.nilu.wallet.api.model.token;

import java.math.BigDecimal;

public class EthInfo {
    private final BigDecimal balance;
    private final BigDecimal totalIn;
    private final BigDecimal totalOut;

    public EthInfo(BigDecimal balance, BigDecimal totalIn, BigDecimal totalOut) {
        this.balance = balance;
        this.totalIn = totalIn;
        this.totalOut = totalOut;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getTotalIn() {
        return totalIn;
    }

    public BigDecimal getTotalOut() {
        return totalOut;
    }

    @Override
    public String toString() {
        return "EthInfo{" +
                "balance=" + balance +
                ", totalIn=" + totalIn +
                ", totalOut=" + totalOut +
                '}';
    }
}
