package me.opens.password_manager;

import android.app.Application;
import android.arch.persistence.room.Room;

import me.opens.password_manager.storage.CredentialDatabase;

public class App extends Application {

    public static App INSTANCE;
    private static final String DATABASE_NAME = "CredentialDatabase";

    private CredentialDatabase database;

    public static App get() {
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
}
