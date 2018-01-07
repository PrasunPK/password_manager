package me.opens.password_manager.service;


public class KeyCheckerService {

    public boolean isKeyMatched(String unlockKey, String passedInKey) {
        return unlockKey.equals(passedInKey);
    }
}
