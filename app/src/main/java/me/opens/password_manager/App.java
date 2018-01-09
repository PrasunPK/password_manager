package me.opens.password_manager;

import android.app.Activity;
import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import me.opens.password_manager.activities.DaggerMyApplicationComponent;
import me.opens.password_manager.storage.CredentialDatabase;

public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public static App INSTANCE;
    private static final String DATABASE_NAME = "CredentialDatabase";

    private CredentialDatabase database;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerMyApplicationComponent.create().inject(this);
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
