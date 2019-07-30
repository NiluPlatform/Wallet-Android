package tech.nilu.wallet.repository;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import tech.nilu.wallet.util.SharedPreferencesUtils;

/**
 * Created by root on 12/10/17.
 */

@Singleton
public class SharedPreferencesRepository {
    private final SharedPreferencesUtils utils;

    @Inject
    public SharedPreferencesRepository(SharedPreferencesUtils utils) {
        this.utils = utils;
    }

    public String getPreferenceString(String key) {
        return utils.getPreferenceString(key);
    }

    public Set<String> getPreferenceStrings(String key) {
        return utils.getPreferenceStrings(key);
    }

    public void putPreferenceStrings(String key, Set<String> values) {
        utils.putPreferenceStrings(key, values);
    }

    public void putPreferenceInt(String key, int value) {
        utils.putPreferenceInt(key, value);
    }

    public int getPreferenceInt(String key) {
        return utils.getPreferenceInt(key);
    }
}
