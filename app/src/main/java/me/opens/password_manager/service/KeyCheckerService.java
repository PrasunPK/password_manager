package me.opens.password_manager.service;


import javax.inject.Inject;

import dagger.Provides;

public class KeyCheckerService {

    @Inject
    public KeyCheckerService() {
    }

    public boolean isKeyMatched(String unlockKey, String passedInKey) {
        return unlockKey.equals(passedInKey);
    }
}
