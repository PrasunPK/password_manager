package me.opens.password_manager.activities;

import android.app.Activity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MyApplicationModule {
    @ContributesAndroidInjector
    abstract DisplayCredentialsActivity contributeActivityInjector();
}
