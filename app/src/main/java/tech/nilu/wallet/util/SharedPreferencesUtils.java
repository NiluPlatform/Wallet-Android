package tech.nilu.wallet.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Navid on 11/29/17.
 */

@Singleton
public class SharedPreferencesUtils {
    private final SharedPreferences preferences;

    @Inject
    public SharedPreferencesUtils(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void removePreference(String key) {
        preferences.edit().remove(key).commit();
    }

    public String getPreferenceString(String key) {
        return preferences.getString(key, null);
    }

    public void putPreferenceString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public boolean getPreferenceBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean getPreferenceBoolean(String key, boolean value) {
        return preferences.getBoolean(key, value);
    }

    public void putPreferenceBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public long getPreferenceLong(String key) {
        return preferences.getLong(key, -1);
    }

    public void putPreferenceLong(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    public int getPreferenceInt(String key) {
        return preferences.getInt(key, -1);
    }

    public void putPreferenceInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    public Set<String> getPreferenceStrings(String key) {
        Set<String> strings = preferences.getStringSet(key, null);
        if (strings == null)
            return new HashSet<>();
        return new HashSet<>(strings);
    }

    public void putPreferenceStrings(String key, Set<String> values) {
        preferences.edit().putStringSet(key, values).commit();
    }
}
