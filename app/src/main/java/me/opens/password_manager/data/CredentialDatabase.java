package me.opens.password_manager.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Credential.class, Profile.class}, version = 2, exportSchema = false)
public abstract class CredentialDatabase extends RoomDatabase {
    public abstract CredentialDao credentialDao();
    public abstract ProfileDao profileDao();
}