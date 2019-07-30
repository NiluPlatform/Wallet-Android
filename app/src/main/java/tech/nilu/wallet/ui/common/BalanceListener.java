package tech.nilu.wallet.ui.common;

import java.math.BigInteger;

/**
 * Created by root on 2/21/18.
 */

public interface BalanceListener {
    void onBalanceChanged(BigInteger newBalance);
}
