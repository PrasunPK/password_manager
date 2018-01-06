package me.opens.password_manager.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import me.opens.password_manager.dao.CredentialDao;
import me.opens.password_manager.entity.Credential;

@Database(entities = {Credential.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract CredentialDao credentialDao();
}