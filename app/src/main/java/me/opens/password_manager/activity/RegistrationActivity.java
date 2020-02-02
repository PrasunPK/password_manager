package me.opens.password_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hanks.passcodeview.PasscodeView;

import javax.inject.Inject;

import me.opens.password_manager.R;
import me.opens.password_manager.config.DaggerAppComponent;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;

import static me.opens.password_manager.util.Constants.USER_KEY;
import static me.opens.password_manager.util.Constants.USER_NAME;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getCanonicalName();
    public static final String EXTRA_MESSAGE = "me.opens.password_manager.MESSAGE";

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pass_code);

        setActionBar(null);
        injectModules();

        PasscodeView passcodeView = (PasscodeView) findViewById(R.id.passcodeView);
        passcodeView
                .setPasscodeLength(4)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        Toast.makeText(getApplication(), "Wrong!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String number) {
                        Intent intent = getIntent();
                        String email = intent.getStringExtra(USER_NAME);

                        intent = new Intent(RegistrationActivity.this, PostLoginMainActivity.class);

                        sharedPreferenceUtils.setUserName(USER_NAME, email);
                        sharedPreferenceUtils.setUserKey(USER_KEY, number);

                        intent.putExtra(USER_KEY, number);
                        startActivity(intent);
                    }
                });
    }

    private void injectModules() {
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build()
                .inject(this);
    }
}