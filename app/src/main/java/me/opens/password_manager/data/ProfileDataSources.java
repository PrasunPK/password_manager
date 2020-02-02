package me.opens.password_manager.data;

import java.util.List;

import javax.inject.Inject;

public class ProfileDataSources implements ProfileRepository {
    private ProfileDao profileDao;

    @Inject
    public ProfileDataSources(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @Override
    public void addNew(Profile profile) {
        profileDao.insert(profile);
    }

    @Override
    public void update(String username, String name, Long updatedTime) {
        profileDao.update(username, name, updatedTime);
    }

    @Override
    public List<Profile> get(String username) {
        return profileDao.getProfile(username);
    }
}
