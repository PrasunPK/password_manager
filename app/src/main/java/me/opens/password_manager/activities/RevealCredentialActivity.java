package me.opens.password_manager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.opens.password_manager.R;

import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.USERNAME;

public class RevealCredentialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_credential);

        TextView domainTextView = findViewById(R.id.revealed_domain);
        TextView userNameTextView = findViewById(R.id.revealed_username);
        TextView passwordTextView = findViewById(R.id.revealed_password);

        domainTextView.setText(getIntent().getStringExtra(DOMAIN));
        userNameTextView.setText(getIntent().getStringExtra(USERNAME));
        passwordTextView.setText(getIntent().getStringExtra(PASSWORD));
    }
}
