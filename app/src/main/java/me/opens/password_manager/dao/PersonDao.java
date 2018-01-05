package me.opens.password_manager.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import me.opens.password_manager.entity.Person;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM person")
    List<Person> getAll();

    @Insert
    void insertAll(List<Person> people);
}
