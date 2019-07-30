package tech.nilu.wallet.api.model.faucet;

import java.math.BigInteger;

import tech.nilu.wallet.api.model.BasicResponse;

public class FaucetResponse extends BasicResponse {
    private final String hash;
    private final BigInteger value;

    public FaucetResponse(String hash, BigInteger value) {
        this.hash = hash;
        this.value = value;
    }

    public String getHash() {
        return hash;
    }

    public BigInteger getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FaucetResponse{" +
                "hash='" + hash + '\'' +
                ", value=" + value +
                '}';
    }
}
