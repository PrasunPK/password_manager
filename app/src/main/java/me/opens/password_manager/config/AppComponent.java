package me.opens.password_manager.config;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import me.opens.password_manager.activitiy.DisplayCredentialsActivity;
import me.opens.password_manager.activitiy.LoginActivity;
import me.opens.password_manager.activitiy.RevealCredentialActivity;
import me.opens.password_manager.dao.CredentialDao;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;
import me.opens.password_manager.repository.CredentialRepository;
import me.opens.password_manager.service.AuthenticationService;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.storage.CredentialDatabase;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class, SharedPreferencesModule.class})
public interface AppComponent {

    void inject(LoginActivity loginActivity);

    void inject(DisplayCredentialsActivity displayCredentialsActivity);

    void inject(RevealCredentialActivity revealCredentialActivity);

    CredentialDao credentialDao();

    CredentialDatabase credentialDatabase();

    CredentialRepository credentialRepository();

    AuthenticationService loginService();

    CredentialService keyCheckerService();

    Application application();

}