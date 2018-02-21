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
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import javax.inject.Inject;

import me.opens.password_manager.R;
import me.opens.password_manager.config.DaggerAppComponent;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;
import me.opens.password_manager.service.CredentialService;

import static android.text.TextUtils.isEmpty;
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
        setEditAction(domain, username, password);
    }

    private void setEditAction(String domain, String identifier, String password) {
        View editButton = findViewById(R.id.edit_credential);
        editButton.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_enter_new_credential);
            dialog.setTitle("Save credential here");

            EditText mDomain = (EditText) dialog.findViewById(R.id.text_domain);
            EditText mIdentifier = (EditText) dialog.findViewById(R.id.text_identifier);
            EditText mPassword = (EditText) dialog.findViewById(R.id.text_credential);

            mDomain.setText(domain);
            mIdentifier.setText(identifier);
            mPassword.setText(password);

            mDomain.setEnabled(false);
            mIdentifier.setEnabled(false);

            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
            dialogButton.setOnClickListener(v1 -> {
                showError(mPassword);
                Date date = new Date();

                new Thread(() -> {
                    credentialService.updateCredential(
                            mDomain.getText().toString(),
                            mIdentifier.getText().toString(),
                            mPassword.getText().toString(),
                            date.getTime()
                    );
                }).start();
                getIntent().putExtra(PASSWORD, mPassword.getText().toString());

                if (!isEmpty(mPassword.getText().toString())) {
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }
            });
            dialog.show();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DisplayCredentialsActivity.class));
    }

    private void showError(EditText mPassword) {
        if (isEmpty(mPassword.getText().toString())) {
            mPassword.setError("Field can not be empty");
        }
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
