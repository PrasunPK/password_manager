package me.opens.password_manager.data;

import java.util.List;

public interface ProfileRepository {
    void addNew(Profile profile);

    void update(String username, String name, Long updatedTime);

    List<Profile> get(String username);
}
