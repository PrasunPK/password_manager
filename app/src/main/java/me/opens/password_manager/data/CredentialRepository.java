package me.opens.password_manager.data;

import java.util.List;

public interface CredentialRepository {
    List<Credential> getLoginCredentials();

    void registerAccount(String username, String password);

    List<Credential> getAllFor(String username);

    void addNew(Credential credential);

    void deleteFor(String userKey, String domain, String username, String password);

    void update(String userKey, String domain, String username, String password, Long updatedTime);
}
