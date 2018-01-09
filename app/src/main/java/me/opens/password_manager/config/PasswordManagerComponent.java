package me.opens.password_manager.config;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import me.opens.password_manager.PasswordManager;
import me.opens.password_manager.module.PasswordManagerModule;
import me.opens.password_manager.service.KeyCheckerService;

@Component(modules = {AndroidInjectionModule.class, PasswordManagerModule.class})
public interface PasswordManagerComponent extends AndroidInjector<PasswordManager> {
    KeyCheckerService provideKeyCheckerService();

    void inject(PasswordManager passwordManager);
}