package me.opens.password_manager.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.R;
import me.opens.password_manager.config.DaggerAppComponent;
import me.opens.password_manager.adapter.CredentialAdapter;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.data.Credential;
import me.opens.password_manager.service.AuthenticationService;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.CryptService;

import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.LAST_UPDATED;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.USERNAME;
import static me.opens.password_manager.util.Constants.USER_NAME;

public class DisplayCredentialsActivity extends AppCompatActivity {

//    public static final String TAG = DisplayCredentialsActivity.class.getCanonicalName();
    private RecyclerView recycleView;
    final Context context = this;
    private Intent intent;

    private FloatingActionButton fab;

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

        // Navigation view header
        fab = findViewById(R.id.fab);

//        injectModules();
        intent = new Intent(this, RevealCredentialActivity.class);

        recycleView = findViewById(R.id.recycler_view);

        populateCredentialsForUser();
//        setFavAction(fab);
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

//    private void setFavAction(FloatingActionButton fab) {
//        String username = sharedPreferenceUtils.getUserName(USER_NAME);
//        String userKey = sharedPreferenceUtils.getUserName(USER_KEY);
//
//        try {
//            fab.setOnClickListener(new FabClickListener(DisplayCredentialsActivity.this, context,
//                    credentialService, new CryptService(userKey), username));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void populateCredentials(final List<Credential> list) {
        runOnUiThread(() -> {
            try {
                recycleView.setAdapter(new CredentialAdapter(list, item -> {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_reveal_with_key);

                    Button dialogButton = dialog.findViewById(R.id.t9_key_reveal);
                    dialogButton.setOnClickListener(view -> {
                        String passedInKey = ((TextView) dialog.findViewById(R.id.text_key)).getText().toString();
                        if (credentialService.isKeyMatched(passedInKey)) {
                            setIntent(item);
//                            Log.i(TAG, "starting reveal credential activity");
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    });
                    dialog.show();
                }, new CryptService(sharedPreferenceUtils.getUserName(USER_NAME))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setIntent(Credential item) {
        Date date = new Date(item.getUpdatedAt());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

        intent.putExtra("me.opens.password_manager.MESSAGE", "Display Credential Activity")
                .putExtra(DOMAIN, item.getDomain())
                .putExtra(USERNAME, item.getUsername())
                .putExtra(PASSWORD, item.getPassword())
                .putExtra(LAST_UPDATED, dateFormat.format(date));
    }

//    private void injectModules() {
//        DaggerAppComponent.builder()
//                .appModule(new AppModule(getApplication()))
//                .roomModule(new RoomModule(getApplication()))
//                .sharedPreferencesModule(new SharedPreferencesModule(getApplicationContext()))
//                .build()
//                .inject(this);
//    }
}