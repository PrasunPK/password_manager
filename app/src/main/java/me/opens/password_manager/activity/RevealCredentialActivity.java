package me.opens.password_manager.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import me.opens.password_manager.R;
import me.opens.password_manager.config.DaggerAppComponent;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.fragment.RevealCredentialFragment;
import me.opens.password_manager.listener.DialogButtonClickListener;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.CryptService;

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
    private CryptService cryptService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_credential);

        injectModules();
        try {
            cryptService = new CryptService(sharedPreferenceUtils.getUserKey(USER_KEY));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        String domain = getIntent().getStringExtra(DOMAIN);
        String username = getIntent().getStringExtra(USERNAME);
        String password = getIntent().getStringExtra(PASSWORD);
        String lastUpdated = getIntent().getStringExtra(LAST_UPDATED);

        updateTextViewsWithCredential(domain, username, password, lastUpdated);

        try {
            setDeleteAction(domain, username, password);
            setEditAction(domain, username, password);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void updateTextViewsWithCredential(String domain, String username, String password, String lastUpdated) {
        TextView domainTextView = findViewById(R.id.revealed_domain);
        TextView userNameTextView = findViewById(R.id.revealed_username);
        TextView passwordTextView = findViewById(R.id.revealed_password);
        TextView lastUpdatedTextView = findViewById(R.id.last_update_time);

        domainTextView.setText(domain);
        userNameTextView.setText(cryptService.decrypt(username));
        passwordTextView.setText(cryptService.decrypt(password));
        lastUpdatedTextView.setText(lastUpdated);
    }

    private void setEditAction(String domain, String identifier, String password) throws Exception {
        View editButton = findViewById(R.id.edit_credential);
//        editButton.setOnClickListener(
//                new EditButtonClickListener(
//                        context, new Fragmen,
//                        credentialService,
//                        cryptService,
//                        domain,
//                        identifier,
//                        password)
//        );
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(this, DisplayCredentialsActivity.class));
    }

    private void setDeleteAction(String domain, String username, String password) {
        View deleteButton = findViewById(R.id.delete_credential);
        deleteButton.setOnClickListener(new DialogButtonClickListener(new RevealCredentialFragment(), this.context, credentialService, domain, username, password, null));
    }

    private void injectModules() {
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build();
    }
}
