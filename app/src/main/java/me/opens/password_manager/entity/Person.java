package me.opens.password_manager.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Person {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "age")
    private String age;

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
