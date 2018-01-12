package me.opens.password_manager.service;

import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.repository.CredentialDataSource;

public class AuthorizationService {

    @Inject
    CredentialDataSource dataSource;

    @Inject
    public AuthorizationService() {
    }

    public boolean validate(String givenKey, String originalKey) {
        return givenKey.equals(originalKey);
    }

    public boolean isValidUser(String mEmail, String mPassword) {
        for (Credential credential : dataSource.getLoginCredentials()) {
            if (credential.getUsername().equals(mEmail)) {
                return credential.getPassword().equals(mPassword);
            }
        }
        return false;
    }

    public boolean register(String username, String password) {
        return !isValidUser(username, password) &&
                dataSource.registerAccount(username, password);
    }

    public List<Credential> getAllCredentialsFor(String username) {
        return dataSource.getAllFor(username);
    }

    public void addCredential(Credential credential) {
        dataSource.addNew(credential);
    }
}
