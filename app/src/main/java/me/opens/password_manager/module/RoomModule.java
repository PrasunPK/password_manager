package me.opens.password_manager.module;

import android.app.Application;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.opens.password_manager.data.CredentialDao;
import me.opens.password_manager.data.CredentialDataSource;
import me.opens.password_manager.data.CredentialRepository;
import me.opens.password_manager.data.CredentialDatabase;
import me.opens.password_manager.data.ProfileDao;
import me.opens.password_manager.data.ProfileDataSources;
import me.opens.password_manager.data.ProfileRepository;

import static me.opens.password_manager.util.Constants.DATABASE_NAME;

@Module
public class RoomModule {

    private CredentialDatabase credentialDatabase;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `Profile` (`pid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `username` TEXT, `created_at` INTEGER, `updated_at` INTEGER)");
        }
    };

    public RoomModule(Application mApplication) {
        credentialDatabase = Room
                .databaseBuilder(mApplication, CredentialDatabase.class, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
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
    ProfileDao providesProfilelDao(CredentialDatabase demoDatabase) {
        return demoDatabase.profileDao();
    }

    @Singleton
    @Provides
    CredentialRepository credentialRepository(CredentialDao credentialDao) {
        return new CredentialDataSource(credentialDao);
    }

    @Singleton
    @Provides
    ProfileRepository profileRepository(ProfileDao profileDao) {
        return new ProfileDataSources(profileDao);
    }
}