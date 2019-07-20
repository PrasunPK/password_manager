package me.opens.password_manager.service;


import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.data.Credential;
import me.opens.password_manager.data.CredentialDataSource;

import static me.opens.password_manager.util.Constants.USER_KEY;
import static me.opens.password_manager.util.Constants.USER_NAME;

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
        Log.i(CredentialService.class.getName(),"Deleting credential for: " + domain + username + password);
        dataSource.deleteFor(
                sharedPreferenceUtils.getUserKey(USER_NAME),
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

    public void updateCredential(String domain, String identifier, String password, Long updatedTime) {
        dataSource.update(sharedPreferenceUtils.getUserKey(USER_NAME),
                domain, identifier, password, updatedTime);
    }
}
