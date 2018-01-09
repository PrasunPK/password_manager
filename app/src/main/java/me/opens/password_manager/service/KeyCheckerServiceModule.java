package me.opens.password_manager.service;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class KeyCheckerServiceModule {

    @Provides
    @Singleton
    KeyCheckerService provideKeyCheckerService() {
        return new KeyCheckerService();
    }
}
