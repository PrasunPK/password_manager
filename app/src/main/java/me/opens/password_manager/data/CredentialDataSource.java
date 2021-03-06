package me.opens.password_manager.data;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

public class CredentialDataSource implements CredentialRepository {
    private CredentialDao credentialDao;

    private static final String TAG = CredentialDao.class.getCanonicalName();

    @Inject
    public CredentialDataSource(CredentialDao credentialDao) {
        this.credentialDao = credentialDao;
    }

    @Override
    public List<Credential> getLoginCredentials() {
        return credentialDao.getLoginCredentials();
    }

    @Override
    public void registerAccount(String username, String password) {
        Credential credential = new Credential();
        credential.setDomain("LOGIN");
        credential.setUsername(username);
        credential.setPassword(password);
        addNew(credential);
    }

    @Override
    public List<Credential> getAllFor(String username) {
        return credentialDao.getAllFor(username);
    }

    @Override
    public void addNew(Credential credential) {
        credentialDao.insert(credential);
    }

    @Override
    public void deleteFor(String userKey, String domain, String username, String password) {
        credentialDao.delete(userKey, domain, username, password);
    }

    @Override
    public void update(String userKey, String domain, String username, String password, Long updatedTime) {
        credentialDao.update(userKey, domain, username, password, updatedTime);
    }

    @Override
    public void updateBulk(String accountName, String domain, String identifier, String password, Long updatedTime) {
        credentialDao.updateBulk(accountName, domain, identifier, password, updatedTime);
    }
}
