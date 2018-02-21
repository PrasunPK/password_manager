package me.opens.password_manager.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CredentialDao {

    @Query("SELECT * FROM Credential")
    List<Credential> getAll();

    @Insert
    void insertAll(List<Credential> credentials);

    @Insert
    void insert(Credential credential);

    @Query("SELECT * FROM Credential WHERE domain='LOGIN'")
    List<Credential> getLoginCredentials();

    @Query("SELECT * FROM Credential WHERE belongs_to=:username")
    List<Credential> getAllFor(String username);

    @Query("DELETE FROM Credential WHERE belongs_to=:userKey AND domain=:domain " +
            "AND username=:username AND password=:password")
    void delete(String userKey, String domain, String username, String password);

    @Query("UPDATE Credential SET password=:password " +
            "WHERE domain=:domain AND username=:username " +
            "AND belongs_to=:userKey AND updated_at=:updatedTime")
    void update(String userKey, String domain, String username,
                String password, Long updatedTime);
}
