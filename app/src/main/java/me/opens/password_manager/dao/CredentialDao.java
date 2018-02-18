package me.opens.password_manager.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import me.opens.password_manager.entity.Credential;

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
}
