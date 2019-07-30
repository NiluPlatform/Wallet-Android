package tech.nilu.wallet.util;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalyticsUtil {
    private final FirebaseAnalytics firebase;

    @Inject
    public AnalyticsUtil(FirebaseAnalytics firebase) {
        this.firebase = firebase;
    }

    public FirebaseAnalytics getFirebase() {
        return firebase;
    }

    public void logEvent(String name, Bundle bundle) {
        firebase.logEvent(name, bundle);
    }

    public void logCreateWalletEvent(long networkId) {
        Bundle bundle = new Bundle();
        bundle.putLong(Param.NETWORK_ID, networkId);
        logEvent(Event.CREATE_WALLET, bundle);
    }

    public void logImportWalletEvent(long networkId) {
        Bundle bundle = new Bundle();
        bundle.putLong(Param.NETWORK_ID, networkId);
        logEvent(Event.IMPORT_WALLET, bundle);
    }

    public static class Param {
        public static final String NETWORK_ID = "network_id";

        protected Param() {
        }
    }

    public static class Event {
        public static final String CREATE_WALLET = "create_wallet";
        public static final String IMPORT_WALLET = "import_wallet";

        protected Event() {
        }
    }
}
