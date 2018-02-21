package me.opens.password_manager.service;


import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.data.Credential;
import me.opens.password_manager.data.CredentialDataSource;

import static me.opens.password_manager.util.Constants.USER_KEY;

public class CredentialService {

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    @Inject
    CredentialDataSource dataSource;

    @Inject
    public CredentialService() {
    }

    public boolean isKeyMatched(String passedInKey) {
        return sharedPreferenceUtils.getUserKey(USER_KEY).equals(passedInKey);
    }

    public List<Credential> getAllCredentialsFor(String username) {
        return dataSource.getAllFor(username);
    }

    public void deleteCredential(String domain, String username, String password) {
        dataSource.deleteFor(
                sharedPreferenceUtils.getUserKey(USER_KEY),
                domain,
                username,
                password
        );
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
                || TextUtils.isEmpty(credential.getUsername())
                || TextUtils.isEmpty(credential.getPassword());
    }

    public void updateCredential(String domain, String identifier, String password) {
        dataSource.update(sharedPreferenceUtils.getUserKey(USER_KEY),
                domain, identifier, password);
    }
}
