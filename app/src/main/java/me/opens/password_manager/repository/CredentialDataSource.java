package me.opens.password_manager.repository;

import javax.inject.Inject;

import me.opens.password_manager.dao.CredentialDao;

public class CredentialDataSource implements CredentialRepository {
    private CredentialDao credentialDao;

    @Inject
    public CredentialDataSource(CredentialDao credentialDao) {
        this.credentialDao = credentialDao;
    }

}
