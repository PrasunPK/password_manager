package me.opens.password_manager.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.opens.password_manager.activitiy.LoginActivity;

@Module
public abstract class LoginModule {
    @ContributesAndroidInjector
    abstract LoginActivity contributeActivityInjector();
}
