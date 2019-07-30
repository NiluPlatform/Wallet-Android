package tech.nilu.wallet.model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Navid on 2/3/18.
 */

public class GasParams implements Serializable {
    private final BigInteger nonce;
    private final BigInteger gasPrice;
    private final BigInteger gasLimit;

    public GasParams(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit) {
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }
}
