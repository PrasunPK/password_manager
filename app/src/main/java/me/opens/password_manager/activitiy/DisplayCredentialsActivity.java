package me.opens.password_manager.activitiy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.PasswordManagerApplication;
import me.opens.password_manager.R;
import me.opens.password_manager.adapter.CredentialAdapter;
import me.opens.password_manager.config.DaggerAppComponent;
import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.module.AppModule;
import me.opens.password_manager.module.RoomModule;
import me.opens.password_manager.module.SharedPreferencesModule;
import me.opens.password_manager.service.KeyCheckerService;

import static me.opens.password_manager.activitiy.LoginActivity.EXTRA_MESSAGE;
import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.UNLOCK_KEY;
import static me.opens.password_manager.util.Constants.USERNAME;

public class DisplayCredentialsActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    final Context context = this;
    private Intent intent;

    @Inject
    KeyCheckerService keyCheckerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_credentials);
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
                .build()
                .inject(this);
        intent = new Intent(this, RevealCredentialActivity.class);

        recycleView = findViewById(R.id.recycler_view);

        new Thread(() -> {
            List<Credential> all = PasswordManagerApplication.get().getDB().credentialDao().getAll();
            if (!all.isEmpty()) {
                populateAll(all);
            }
        }).start();

        setFavAction();
    }

    private void setFavAction() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Clicked to add an item", Toast.LENGTH_LONG).show();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_layout);
            dialog.setTitle("Save credential here");

            Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
            dialogButton.setOnClickListener(v1 -> {
                String domain = ((EditText) dialog.findViewById(R.id.text_domain)).getText().toString();
                String identifier = ((EditText) dialog.findViewById(R.id.text_identifier)).getText().toString();
                String password = ((EditText) dialog.findViewById(R.id.text_credential)).getText().toString();
                final Credential credential = new Credential();
                credential.setDomain(domain);
                credential.setUsername(identifier);
                credential.setPassword(password);

                new Thread(() -> {
                    PasswordManagerApplication.get().getDB().credentialDao().insert(credential);
                    List<Credential> all = PasswordManagerApplication.get().getDB().credentialDao().getAll();
                    if (!all.isEmpty()) {
                        populateAll(all);
                    }
                }).start();

                dialog.dismiss();

            });
            dialog.show();
        });
    }

    private void populateAll(final List<Credential> list) {
        runOnUiThread(() -> recycleView.setAdapter(new CredentialAdapter(list, item -> {
            Toast.makeText(getBaseContext(), "Item Clicked", Toast.LENGTH_LONG).show();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_reveal_with_key);

            Button dialogButton = dialog.findViewById(R.id.dialogButtonReveal);
            dialogButton.setOnClickListener(view -> {
                String passedInKey = ((EditText) dialog.findViewById(R.id.text_key)).getText().toString();
                if (keyCheckerService.isKeyMatched(UNLOCK_KEY, passedInKey)) {
                    intent.putExtra(EXTRA_MESSAGE, "Display Credential Activity");
                    intent.putExtra(DOMAIN, item.getDomain());
                    intent.putExtra(USERNAME, item.getUsername());
                    intent.putExtra(PASSWORD, item.getPassword());
                    startActivity(intent);
                }
                dialog.dismiss();
            });
            dialog.show();
        }
        )));
    }
}
