package me.opens.password_manager.service;


import android.text.TextUtils;
import android.util.Log;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.data.Profile;
import me.opens.password_manager.data.ProfileDataSources;

import static me.opens.password_manager.util.Constants.USER_KEY;
import static me.opens.password_manager.util.Constants.USER_NAME;

public class ProfileService {

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    @Inject
    ProfileDataSources dataSource;

    private static final String TAG = ProfileService.class.getCanonicalName();

    @Inject
    public ProfileService() {
    }

    public boolean isKeyMatched(String passedInKey) {
        return sharedPreferenceUtils.getUserKey(USER_KEY).equals(passedInKey);
    }

    public boolean doesExist() {
        return dataSource.get(sharedPreferenceUtils.getUserName(USER_NAME)).size() > 0;
    }
    public Profile getProfile() {
        List<Profile> profiles = dataSource.get(sharedPreferenceUtils.getUserName(USER_NAME));
        return profiles.get(0);
    }


    public boolean addProfile(Profile profile) {
        if (!isEmpty(profile)) {
            dataSource.addNew(profile);
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmpty(Profile profile) {
        return TextUtils.isEmpty(profile.getName());
    }

    public void updateProfile(String username, String name) {

        dataSource.update(sharedPreferenceUtils.getUserKey(USER_NAME),
                name, new Date().getTime());
    }
}
