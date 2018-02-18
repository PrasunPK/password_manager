package me.opens.password_manager.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import me.opens.password_manager.R;
import me.opens.password_manager.config.DaggerAppComponent;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;
import me.opens.password_manager.service.CredentialService;

import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.USERNAME;

public class RevealCredentialActivity extends AppCompatActivity {
    final Context context = this;

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    @Inject
    CredentialService credentialService;
    private static final String TAG = RevealCredentialActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_credential);

        injectModules();

        TextView domainTextView = findViewById(R.id.revealed_domain);
        TextView userNameTextView = findViewById(R.id.revealed_username);
        TextView passwordTextView = findViewById(R.id.revealed_password);

        String domain = getIntent().getStringExtra(DOMAIN);
        String username = getIntent().getStringExtra(USERNAME);
        String password = getIntent().getStringExtra(PASSWORD);

        domainTextView.setText(domain);
        userNameTextView.setText(username);
        passwordTextView.setText(password);

        setDeleteAction(domain, username, password);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DisplayCredentialsActivity.class));
    }

    private void setDeleteAction(String domain, String username, String password) {
        View deleteButton = findViewById(R.id.delete_credential);
        deleteButton.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_confirm_deletion);
            dialog.setTitle("Alert");

            Button dialogButton = dialog.findViewById(R.id.dialog_delete_ok);
            dialogButton.setOnClickListener(v1 -> {
                new Thread(() -> {
                    credentialService.deleteCredential(domain, username, password);
                    Log.i(TAG, "DELETING CREDENTIAL");
                }).start();
                dialog.dismiss();
                onBackPressed();
            });
            dialog.show();
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
