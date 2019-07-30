package tech.nilu.wallet.api.model.transaction;

import java.math.BigDecimal;

public class Transaction {
    private long timestamp;
    private String from;
    private String to;
    private String hash;
    private BigDecimal value;
    private String input;
    private boolean success;

    public Transaction(long timestamp, String from, String to, String hash, BigDecimal value, String input, boolean success) {
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.hash = hash;
        this.value = value;
        this.input = input;
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "timestamp=" + timestamp +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", hash='" + hash + '\'' +
                ", value=" + value +
                ", input='" + input + '\'' +
                ", success=" + success +
                '}';
    }
}
