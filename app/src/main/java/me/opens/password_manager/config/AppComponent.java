package me.opens.password_manager.config;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import me.opens.password_manager.activity.BaseActivity;
import me.opens.password_manager.activity.PassCodeActivity;
import me.opens.password_manager.activity.PostLoginMainActivity;
import me.opens.password_manager.activity.RegistrationActivity;
import me.opens.password_manager.activity.RevealCredentialActivity;
import me.opens.password_manager.data.CredentialDao;
import me.opens.password_manager.data.CredentialDatabase;
import me.opens.password_manager.data.CredentialRepository;
import me.opens.password_manager.data.ProfileDao;
import me.opens.password_manager.data.ProfileDataSources;
import me.opens.password_manager.data.ProfileRepository;
import me.opens.password_manager.fragment.EditProfileFragment;
import me.opens.password_manager.fragment.HomeFragment;
import me.opens.password_manager.fragment.ListCreadentialsFragment;
import me.opens.password_manager.fragment.RevealCredentialFragment;
import me.opens.password_manager.fragment.SettingsFragment;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;
import me.opens.password_manager.service.AuthenticationService;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.ProfileService;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class, SharedPreferencesModule.class})
public interface AppComponent {

    // void inject(RevealCredentialActivity revealCredentialActivity);

    void inject(ListCreadentialsFragment listCreadentialsFragment);

    void inject(RevealCredentialFragment revealCredentialFragment);

    void inject(HomeFragment homeFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(EditProfileFragment editProfileFragment);

    void inject(PostLoginMainActivity postLoginMainActivity);

    void inject(BaseActivity revealBaseActivity);

    void inject(PassCodeActivity revealPassCodeActivity);

    void inject(RegistrationActivity revealRegistrationActivity);

    CredentialDao credentialDao();

    CredentialDatabase credentialDatabase();

    CredentialRepository credentialRepository();

    AuthenticationService loginService();

    CredentialService keyCheckerService();

    ProfileDao profileDao();

    ProfileService profileService();

    ProfileRepository profileRepository();

    Application application();
}