package me.opens.password_manager.repository;

import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.dao.CredentialDao;
import me.opens.password_manager.entity.Credential;

public class CredentialDataSource implements CredentialRepository {
    private CredentialDao credentialDao;

    @Inject
    public CredentialDataSource(CredentialDao credentialDao) {
        this.credentialDao = credentialDao;
    }

    @Override
    public List<Credential> getLoginCredentials() {
        return credentialDao.getLoginCredentials();
    }
}
