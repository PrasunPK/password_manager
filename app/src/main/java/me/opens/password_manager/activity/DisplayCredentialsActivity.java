package me.opens.password_manager.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.R;
import me.opens.password_manager.adapter.CredentialAdapter;
import me.opens.password_manager.config.DaggerAppComponent;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;
import me.opens.password_manager.service.AuthenticationService;
import me.opens.password_manager.service.CredentialService;

import static android.text.TextUtils.isEmpty;
import static me.opens.password_manager.activity.LoginActivity.EXTRA_MESSAGE;
import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.USERNAME;
import static me.opens.password_manager.util.Constants.USER_NAME;

public class DisplayCredentialsActivity extends AppCompatActivity {

    private static final String TAG = DisplayCredentialsActivity.class.getCanonicalName();
    private RecyclerView recycleView;
    final Context context = this;
    private Intent intent;

    @Inject
    CredentialService credentialService;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_credentials);
        injectModules();
        intent = new Intent(this, RevealCredentialActivity.class);

        recycleView = findViewById(R.id.recycler_view);

        populateCredentialsForUser();

        setFavAction();
    }

    private void populateCredentialsForUser() {
        String username = sharedPreferenceUtils.getUserName(USER_NAME);
        new Thread(() -> {
            List<Credential> credentials = credentialService
                    .getAllCredentialsFor(username);
            if (!credentials.isEmpty()) {
                populateCredentials(credentials);
            }
        }).start();
    }

    private void injectModules() {
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build()
                .inject(this);
    }

    private void setFavAction() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_enter_new_credential);
            dialog.setTitle("Save credential here");

            String username = sharedPreferenceUtils.getUserName(USER_NAME);
            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
            dialogButton.setOnClickListener(v1 -> {
                EditText mDomain = (EditText) dialog.findViewById(R.id.text_domain);
                EditText mIdentifier = (EditText) dialog.findViewById(R.id.text_identifier);
                EditText mPassword = (EditText) dialog.findViewById(R.id.text_credential);

                showError(mDomain, mIdentifier, mPassword);

                final Credential credential =
                        prepareCredential(username,
                                mDomain.getText().toString(),
                                mIdentifier.getText().toString(),
                                mPassword.getText().toString());

                saveCredentialAndRepopulate(username, credential);

                if (!isEmpty(mDomain.getText().toString())
                        && !isEmpty(mIdentifier.getText().toString())
                        && !isEmpty(mPassword.getText().toString())) {
                    dialog.dismiss();
                }

            });
            dialog.show();
        });
    }

    private void showError(EditText mDomain, EditText mIdentifier, EditText mPassword) {
        if (isEmpty(mDomain.getText().toString())) {
            mDomain.setError("Field can not be empty");
        }
        if (isEmpty(mIdentifier.getText().toString())) {
            mIdentifier.setError("Field can not be empty");
        }
        if (isEmpty(mPassword.getText().toString())) {
            mPassword.setError("Field can not be empty");
        }
    }

    private void saveCredentialAndRepopulate(String username, Credential credential) {
        final boolean[] credentialAdded = {false};
        new Thread(() -> {
            credentialAdded[0] = credentialService.addCredential(credential);
            List<Credential> credentials = credentialService
                    .getAllCredentialsFor(username);
            if (!credentials.isEmpty()) {
                populateCredentials(credentials);
            }
        }).start();
        if (!credentialAdded[0]) {
            Toast.makeText(context, "Can not save credential", Toast.LENGTH_LONG).show();
        }
    }

    private Credential prepareCredential(String username, String domain, String identifier, String password) {
        Credential credential = new Credential();
        credential.setDomain(domain);
        credential.setUsername(identifier);
        credential.setPassword(password);
        credential.setBelongsTo(username);
        return credential;
    }

    private void populateCredentials(final List<Credential> list) {
        runOnUiThread(() -> recycleView.setAdapter(new CredentialAdapter(list, item -> {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_reveal_with_key);

            Button dialogButton = dialog.findViewById(R.id.dialogButtonReveal);
            dialogButton.setOnClickListener(view -> {
                String passedInKey = ((EditText) dialog.findViewById(R.id.text_key)).getText().toString();
                if (credentialService.isKeyMatched(passedInKey)) {
                    setIntent(item);
                    Log.i(TAG, "starting reveal credential activity");
                    startActivity(intent);
                }
                dialog.dismiss();
            });
            dialog.show();
        }
        )));
    }

    private void setIntent(Credential item) {
        intent.putExtra(EXTRA_MESSAGE, "Display Credential Activity");
        intent.putExtra(DOMAIN, item.getDomain());
        intent.putExtra(USERNAME, item.getUsername());
        intent.putExtra(PASSWORD, item.getPassword());
    }
}