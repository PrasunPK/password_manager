package me.opens.password_manager.service;

import android.text.TextUtils;

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

    public boolean register(String username, String password) {
        return !isValidUser(username, password) &&
                dataSource.registerAccount(username, password);
    }

    public boolean addCredential(Credential credential) {
        if (!isEmpty(credential)) {
            dataSource.addNew(credential);
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmpty(Credential credential) {
        return TextUtils.isEmpty(credential.getDomain())
                && TextUtils.isEmpty(credential.getUsername())
                && TextUtils.isEmpty(credential.getPassword());
    }
}
