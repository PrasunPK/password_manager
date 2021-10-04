package me.opens.password_manager.data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Credential.class, Profile.class}, version = 2, exportSchema = false)
public abstract class CredentialDatabase extends RoomDatabase {
    public abstract CredentialDao credentialDao();
    public abstract ProfileDao profileDao();
}