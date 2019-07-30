package tech.nilu.wallet.ui.common;

import tech.nilu.wallet.db.entity.Network;

/**
 * Created by root on 1/15/18.
 */

public interface NetworkListener {
    void onNetworkChanged(Network selectedNetwork);
}
