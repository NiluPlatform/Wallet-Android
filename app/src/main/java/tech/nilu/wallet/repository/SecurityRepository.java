package tech.nilu.wallet.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import tech.nilu.wallet.util.MyKeyStore;
import tech.nilu.wallet.util.SharedPreferencesUtils;

/**
 * Created by root on 12/13/17.
 */

@Singleton
public class SecurityRepository {
    private final MyKeyStore keyStore;
    private final SharedPreferencesUtils sharedPreferencesUtils;

    @Inject
    public SecurityRepository(MyKeyStore keyStore, SharedPreferencesUtils sharedPreferencesUtils) {
        this.keyStore = keyStore;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
    }

    public void createKeys() {
        keyStore.createNewKeys(MyKeyStore.NILU_ALIAS);
    }

    public void storeString(String key, String value) {
        if (value == null)
            sharedPreferencesUtils.removePreference(key);
        else {
            String encryptedValue = keyStore.encryptString(MyKeyStore.NILU_ALIAS, value);
            sharedPreferencesUtils.putPreferenceString(key, encryptedValue);
        }
    }

    public String retrieveString(String key) {
        String encryptedValue = sharedPreferencesUtils.getPreferenceString(key);
        return encryptedValue == null ? null : keyStore.decryptString(MyKeyStore.NILU_ALIAS, encryptedValue);
    }

    public boolean hasIdentifier() {
        return sharedPreferencesUtils.getPreferenceString(MyKeyStore.IDENTIFIER) != null;
    }

    public boolean hasPassword() {
        return sharedPreferencesUtils.getPreferenceString(MyKeyStore.PASSWORD) != null;
    }
}
