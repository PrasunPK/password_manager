package me.opens.password_manager.listener;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.List;

import me.opens.password_manager.R;
import me.opens.password_manager.activity.DisplayCredentialsActivity;
import me.opens.password_manager.data.Credential;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.CryptService;

import static android.text.TextUtils.isEmpty;
import static me.opens.password_manager.activity.DisplayCredentialsActivity.TAG;

public class FabClickListener implements View.OnClickListener {

    private DisplayCredentialsActivity activity;
    private Context context;
    private CredentialService credentialService;
    private CryptService cryptService;
    private String username;

    public FabClickListener(DisplayCredentialsActivity activity, Context context,
                            CredentialService credentialService, CryptService cryptService,
                            String username) {
        this.activity = activity;
        this.context = context;
        this.credentialService = credentialService;
        this.cryptService = cryptService;
        this.username = username;
    }

    @Override
    public void onClick(View view) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_enter_new_credential);
        dialog.setTitle("Save credential here");

        Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new DialogButtonListener(dialog));
        dialog.show();
    }

    private void saveCredentialAndRepopulate(String username, Credential credential) {
        final boolean[] credentialAdded = {false};
        new Thread(() -> {
            credentialAdded[0] = credentialService.addCredential(credential);
            List<Credential> credentials = credentialService
                    .getAllCredentialsFor(username);
            if (!credentials.isEmpty()) {
                activity.populateCredentials(credentials);
            }
        }).start();
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

    private Credential prepareCredential(String username, String domain, String identifier, String password) {
        Date date = new Date();

        Credential credential = new Credential();
        credential.setDomain(domain);
        credential.setUsername(identifier);
        credential.setPassword(password);
        credential.setBelongsTo(username);
        credential.setCreatedAt(date.getTime());
        credential.setUpdatedAt(date.getTime());
        return credential;
    }

    private class DialogButtonListener implements View.OnClickListener {

        private Dialog dialog;

        DialogButtonListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {
            EditText mDomain = (EditText) dialog.findViewById(R.id.text_domain);
            EditText mIdentifier = (EditText) dialog.findViewById(R.id.text_identifier);
            EditText mPassword = (EditText) dialog.findViewById(R.id.text_credential);

            showError(mDomain, mIdentifier, mPassword);
            String encryptedIdentifier = cryptService
                    .encrypt(mIdentifier.getText().toString());
            String encryptedPassword = cryptService
                    .encrypt(mPassword.getText().toString());

            Log.i(TAG, "Encrypted [" + encryptedPassword + "]");

            final Credential credential =
                    prepareCredential(username,
                            mDomain.getText().toString(),
                            encryptedIdentifier,
                            encryptedPassword);
            saveCredentialAndRepopulate(username, credential);

            if (!isEmpty(mDomain.getText().toString())
                    && !isEmpty(mIdentifier.getText().toString())
                    && !isEmpty(mPassword.getText().toString())) {
                dialog.dismiss();
            }
        }
    }
}