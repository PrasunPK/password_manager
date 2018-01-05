package me.opens.password_manager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import me.opens.password_manager.R;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "me.opens.password_manager.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText key = findViewById(R.id.key);
        EditText value = findViewById(R.id.value);

        String messageKey = key.getText().toString();
        String messageValue = value.getText().toString();
        String message = String.format("%s - %s", messageKey, messageValue);

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
