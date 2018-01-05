package me.opens.password_manager.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import me.opens.password_manager.dao.PersonDao;
import me.opens.password_manager.entity.Person;

@Database(entities = {Person.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract PersonDao productDao();
}