package me.opens.password_manager.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import me.opens.password_manager.R;
import me.opens.password_manager.config.DaggerAppComponent;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;

public class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "me.opens.password_manager.MESSAGE";
    private static final String TAG = BaseActivity.class.getCanonicalName();
    private static final String USER_NAME = "USER_NAME";
    private static final int TIME_OUT = 1000;

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    AvailabilityCheckTask availabilityCheckTask;
    Intent intent;

    private Activity activity = BaseActivity.this;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String FALLBACK_ACCOUNT = "fallback-account@email.com";
    ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, RegistrationActivity.class);

        setContentView(R.layout.activity_base);
        injectModules();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                String wantPermission = Manifest.permission.READ_CONTACTS;
                String email = checkAndGetEmail(wantPermission);
                finish();
                availabilityCheckTask = new AvailabilityCheckTask(email);
                availabilityCheckTask.execute((Void) null);
            }
        }, TIME_OUT);

    }

    private String checkAndGetEmail(String wantPermission) {
        if (!checkPermission(wantPermission)) {
            requestPermission(wantPermission);
        } else {
            return getEmail();
        }
        return getEmail();
    }

    private void injectModules() {
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build()
                .inject(this);
    }

    public class AvailabilityCheckTask extends AsyncTask<Void, Void, Boolean> {

        private String email;

        AvailabilityCheckTask(String email) {
            this.email = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String userName = sharedPreferenceUtils.getUserName(USER_NAME);
            return userName != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finish();
                intent = new Intent(BaseActivity.this, PassCodeActivity.class);
                startActivity(intent);
            } else {
                finish();
                intent = new Intent(BaseActivity.this, RegistrationActivity.class);
                intent.putExtra(USER_NAME, email);
                startActivity(intent);
            }
        }
    }


    private String getEmail() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccountsByType("com.google");
        String email = FALLBACK_ACCOUNT;
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                email = account.name;
            }
        }
        return email;
    }

    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(activity, permission);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void requestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            Toast.makeText(activity, "Get account permission allows us to get your email",
                    Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @Nonnull String[] permissions, @Nonnull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getEmail();
            } else {
                Toast.makeText(activity, "Permission Denied.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
