package me.opens.password_manager.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.opens.password_manager.service.KeyCheckerService;
import me.opens.password_manager.service.LoginService;

@Module
public class LoginServiceModule {

    @Provides
    @Singleton
    LoginService provideKeyCheckerService() {
        return new LoginService();
    }
}
