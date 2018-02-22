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
import me.opens.password_manager.listener.EditButtonClickListener;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.EncryptionService;

import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.LAST_UPDATED;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.USERNAME;
import static me.opens.password_manager.util.Constants.USER_KEY;

public class RevealCredentialActivity extends AppCompatActivity {
    final Context context = this;

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    @Inject
    CredentialService credentialService;
    private static final String TAG = RevealCredentialActivity.class.getCanonicalName();
    private EncryptionService encryptionService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_credential);

        injectModules();
        try {
            encryptionService = new EncryptionService(sharedPreferenceUtils.getUserKey(USER_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String domain = getIntent().getStringExtra(DOMAIN);
        String username = getIntent().getStringExtra(USERNAME);
        String password = getIntent().getStringExtra(PASSWORD);
        String lastUpdated = getIntent().getStringExtra(LAST_UPDATED);

        updateTextViewsWithCredential(domain, username, password, lastUpdated);

        setDeleteAction(domain, username, password);
        try {
            setEditAction(domain, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextViewsWithCredential(String domain, String username, String password, String lastUpdated) {
        TextView domainTextView = findViewById(R.id.revealed_domain);
        TextView userNameTextView = findViewById(R.id.revealed_username);
        TextView passwordTextView = findViewById(R.id.revealed_password);
        TextView lastUpdatedTextView = findViewById(R.id.last_update_time);

        domainTextView.setText(domain);
        userNameTextView.setText(encryptionService.decrypt(username));
        passwordTextView.setText(encryptionService.decrypt(password));
        lastUpdatedTextView.setText(lastUpdated);
    }

    private void setEditAction(String domain, String identifier, String password) throws Exception {
        View editButton = findViewById(R.id.edit_credential);
        editButton.setOnClickListener(
                new EditButtonClickListener(
                        context,
                        RevealCredentialActivity.this,
                        credentialService,
                        encryptionService,
                        domain,
                        identifier,
                        password
                )
        );
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DisplayCredentialsActivity.class));
    }

    private void setDeleteAction(String domain, String username, String password) {
        View deleteButton = findViewById(R.id.delete_credential);
        deleteButton.setOnClickListener(new DialogButtonClickListner(domain, username, password));
    }

    private void injectModules() {
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build()
                .inject(this);
    }

    private class DialogButtonClickListner implements View.OnClickListener {

        private final String domain;
        private final String username;
        private final String password;

        DialogButtonClickListner(String domain, String username, String password) {
            this.domain = domain;
            this.username = username;
            this.password = password;
        }

        @Override
        public void onClick(View view) {
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
        }
    }
}
