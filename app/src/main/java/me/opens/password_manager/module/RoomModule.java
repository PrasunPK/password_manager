package me.opens.password_manager.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.opens.password_manager.dao.CredentialDao;
import me.opens.password_manager.repository.CredentialDataSource;
import me.opens.password_manager.repository.CredentialRepository;
import me.opens.password_manager.storage.CredentialDatabase;

import static me.opens.password_manager.util.Constants.DATABASE_NAME;

@Module
public class RoomModule {

    private CredentialDatabase credentialDatabase;

    public RoomModule(Application mApplication) {
        credentialDatabase = Room.databaseBuilder(mApplication, CredentialDatabase.class, DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    CredentialDatabase providesCredentialDatabase() {
        return credentialDatabase;
    }

    @Singleton
    @Provides
    CredentialDao providesCredentialDao(CredentialDatabase demoDatabase) {
        return demoDatabase.credentialDao();
    }

    @Singleton
    @Provides
    CredentialRepository credentialRepository(CredentialDao credentialDao) {
        return new CredentialDataSource(credentialDao);
    }

}