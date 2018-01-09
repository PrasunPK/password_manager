package me.opens.password_manager.activities;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import me.opens.password_manager.App;
import me.opens.password_manager.service.KeyCheckerService;

@Component(modules = {AndroidInjectionModule.class, MyApplicationModule.class})
public interface MyApplicationComponent extends AndroidInjector<App> {
    KeyCheckerService provideKeyCheckerService();

    void inject(App app);
}