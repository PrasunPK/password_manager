package me.opens.password_manager.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Credential {

    @PrimaryKey(autoGenerate = true)
    private int cid;

    @ColumnInfo(name = "domain")
    private String domain;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "credential")
    private String credential;

    public int getCid() {
        return cid;
    }

    public String getDomain() {
        return domain;
    }

    public String getUsername() {
        return username;
    }


    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
