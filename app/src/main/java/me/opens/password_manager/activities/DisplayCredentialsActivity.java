package me.opens.password_manager.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.opens.password_manager.App;
import me.opens.password_manager.R;
import me.opens.password_manager.adapters.CredentialAdapter;
import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.listener.OnItemClickListener;

public class DisplayCredentialsActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_credentials);

        recycleView = (RecyclerView) findViewById(R.id.recycler_view);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Credential> all = App.get().getDB().credentialDao().getAll();
                if (!all.isEmpty()) {
                    populateAll(all);
                }
            }
        }).start();

        setFavAction();
    }

    private void setFavAction() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Clicked to add an item", Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("Save credential here");

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String domain = ((EditText) dialog.findViewById(R.id.text_domain)).getText().toString();
                        String identifier = ((EditText) dialog.findViewById(R.id.text_identifier)).getText().toString();
                        String password = ((EditText) dialog.findViewById(R.id.text_credential)).getText().toString();
                        final Credential credential = new Credential();
                        credential.setDomain(domain);
                        credential.setUsername(identifier);
                        credential.setCredential(password);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                App.get().getDB().credentialDao().insert(credential);
                                List<Credential> all = App.get().getDB().credentialDao().getAll();
                                if (!all.isEmpty()) {
                                    populateAll(all);
                                }
                            }
                        }).start();

                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });
    }

    private void populateAll(final List<Credential> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recycleView.setAdapter(new CredentialAdapter(list, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Credential item) {
                        Toast.makeText(getBaseContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                    }
                }
                ));
            }
        });
    }
}
