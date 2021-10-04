package me.opens.password_manager.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProfileDao {

    @Insert
    void insert(Profile profile);

    @Query("SELECT * FROM Profile WHERE username=:username")
    List<Profile> getProfile(String username);

    @Query("SELECT * FROM Profile")
    List<Profile> getProfiles();

    @Query("UPDATE Profile SET name=:name, updated_at=:updatedTime WHERE username=:username")
    void update(String username, String name, Long updatedTime);
}
