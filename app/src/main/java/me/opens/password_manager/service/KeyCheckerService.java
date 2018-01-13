package me.opens.password_manager.service;


import javax.inject.Inject;

import me.opens.password_manager.config.SharedPreferenceUtils;

import static me.opens.password_manager.util.Constants.USER_KEY;

public class KeyCheckerService {

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    @Inject
    public KeyCheckerService() {
    }

    public boolean isKeyMatched(String passedInKey) {
        return sharedPreferenceUtils.getUserKey(USER_KEY).equals(passedInKey);
    }
}
