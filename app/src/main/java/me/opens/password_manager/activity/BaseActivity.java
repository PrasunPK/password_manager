package me.opens.password_manager.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
    private static final int TIME_OUT = 2500;

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    AvailabilityCheckTask availabilityCheckTask;
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, LoginActivity.class);

        setContentView(R.layout.activity_base);
        injectModules();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();

                availabilityCheckTask = new AvailabilityCheckTask(sharedPreferenceUtils);
                availabilityCheckTask.execute((Void) null);
            }
        }, TIME_OUT);
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

        private SharedPreferenceUtils sharedPreferenceUtils;

        public AvailabilityCheckTask(SharedPreferenceUtils sharedPreferenceUtils) {
            this.sharedPreferenceUtils = sharedPreferenceUtils;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String userName = sharedPreferenceUtils.getUserName(USER_NAME);
            if (userName != null) {
//                Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_LONG);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finish();
                intent = new Intent(BaseActivity.this, PassCodeActivity.class);
                System.out.println("USER NAME present so starting the latest activity");
                startActivity(intent);
            }
        }
    }

}
