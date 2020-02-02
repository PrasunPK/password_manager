package me.opens.password_manager;

import android.app.Activity;
import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import me.opens.password_manager.data.CredentialDatabase;

import static me.opens.password_manager.util.Constants.DATABASE_NAME;

public class PasswordManagerApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public static PasswordManagerApplication INSTANCE;

    private CredentialDatabase database;

    public static PasswordManagerApplication get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                CredentialDatabase.class, DATABASE_NAME)
                .build();

        INSTANCE = this;
    }

    public CredentialDatabase getDB() {
        return database;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
