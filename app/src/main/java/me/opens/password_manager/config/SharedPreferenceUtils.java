package me.opens.password_manager.config;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class SharedPreferenceUtils {

    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferenceUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void putData(String key, String data) {
        sharedPreferences.edit().putString(key, data).apply();
    }

    public String getData(String key) {
        return sharedPreferences.getString(key, null);
    }
}
