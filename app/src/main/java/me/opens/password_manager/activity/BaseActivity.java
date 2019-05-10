package me.opens.password_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import me.opens.password_manager.R;

public class BaseActivity extends AppCompatActivity {


    public static final String EXTRA_MESSAGE = "me.opens.password_manager.MESSAGE";
    private static final String TAG = BaseActivity.class.getCanonicalName();
    private static int TIME_OUT = 2500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Base Activity");
                Log.i(TAG, "starting display login activity");
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);
    }
}
