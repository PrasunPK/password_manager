package me.opens.password_manager.service;

import javax.inject.Inject;

import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.repository.CredentialDataSource;

public class LoginService {

    @Inject
    CredentialDataSource dataSource;

    @Inject
    public LoginService() {
    }

    public boolean validate(String givenKey, String originalKey) {
        return givenKey.equals(originalKey);
    }

    public boolean validateKey(String mEmail, String mPassword) {
        for (Credential credential : dataSource.getLoginCredentials()) {
            if (credential.getUsername().equals(mEmail)) {
                return credential.getPassword().equals(mPassword);
            }
        }
        return false;
    }
}
