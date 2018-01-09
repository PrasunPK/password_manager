package me.opens.password_manager.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.opens.password_manager.service.KeyCheckerService;

@Module
public class KeyCheckerServiceModule {

    @Provides
    @Singleton
    KeyCheckerService provideKeyCheckerService() {
        return new KeyCheckerService();
    }
}
