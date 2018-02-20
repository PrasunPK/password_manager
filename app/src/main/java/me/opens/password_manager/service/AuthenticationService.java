package me.opens.password_manager.service;

import javax.inject.Inject;

import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.repository.CredentialDataSource;

public class AuthenticationService {

    @Inject
    CredentialDataSource dataSource;

    @Inject
    public AuthenticationService() {
    }

    public boolean isValidUser(String mEmail, String mPassword) {
        for (Credential credential : dataSource.getLoginCredentials()) {
            if (credential.getUsername().equals(mEmail)) {
                return credential.getPassword().equals(mPassword);
            }
        }
        return false;
    }

    public void register(String username, String password) {
        if (!isValidUser(username, password)) {
            dataSource.registerAccount(username, password);
        }
    }
}
