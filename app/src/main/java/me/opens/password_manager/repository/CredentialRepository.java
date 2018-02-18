package me.opens.password_manager.repository;

import java.util.List;

import me.opens.password_manager.entity.Credential;

public interface CredentialRepository {
    List<Credential> getLoginCredentials();

    boolean registerAccount(String username, String password);

    List<Credential> getAllFor(String username);

    void addNew(Credential credential);

    void deleteFor(String userKey, String domain, String username, String password);
}
