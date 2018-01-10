package me.opens.password_manager.service;

import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.storage.CredentialDatabase;

public class LoginService {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "admin:1234"
    };

    @Inject
    public LoginService() {
    }

    public boolean validate(String givenKey, String originalKey) {
        return givenKey.equals(originalKey);
    }

    public boolean validateKey(String mEmail, String mPassword) {
        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mEmail)) {
                return pieces[1].equals(mPassword);
            }
        }
        return false;
    }
}
