package tech.nilu.wallet.api.model.token;

import java.math.BigDecimal;

public class BalanceInfo {
    private final TokenInfo tokenInfo;
    private final BigDecimal balance;
    private final BigDecimal totalIn;
    private final BigDecimal totalOut;

    public BalanceInfo(TokenInfo tokenInfo, BigDecimal balance, BigDecimal totalIn, BigDecimal totalOut) {
        this.tokenInfo = tokenInfo;
        this.balance = balance;
        this.totalIn = totalIn;
        this.totalOut = totalOut;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
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
        return "BalanceInfo{" +
                "tokenInfo=" + tokenInfo +
                ", balance=" + balance +
                ", totalIn=" + totalIn +
                ", totalOut=" + totalOut +
                '}';
    }
}
