package me.opens.password_manager.service;


import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.repository.CredentialDataSource;

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
}
