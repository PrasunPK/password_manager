package me.opens.password_manager.listener;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.opens.password_manager.R;
import me.opens.password_manager.activity.RevealCredentialActivity;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.CryptService;

import static android.text.TextUtils.isEmpty;
import static me.opens.password_manager.util.Constants.LAST_UPDATED;
import static me.opens.password_manager.util.Constants.PASSWORD;

public class EditButtonClickListener implements View.OnClickListener {

    private CryptService cryptService;
    private final String domain;
    private final String identifier;
    private final String password;
    private final CredentialService credentialService;
    private Context context;
    private RevealCredentialActivity activity;

    public EditButtonClickListener(Context context, RevealCredentialActivity activity, CredentialService credentialService, CryptService cryptService,
                                   String domain, String identifier, String password) {
        this.context = context;
        this.activity = activity;
        this.credentialService = credentialService;
        this.cryptService = cryptService;
        this.domain = domain;
        this.identifier = identifier;
        this.password = password;
    }

    @Override
    public void onClick(View view) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_enter_new_credential);
        dialog.setTitle("Save credential here");

        EditText mDomain = (EditText) dialog.findViewById(R.id.text_domain);
        EditText mIdentifier = (EditText) dialog.findViewById(R.id.text_identifier);
        EditText mPassword = (EditText) dialog.findViewById(R.id.text_credential);

        populateDialog(mDomain, mIdentifier, mPassword);

        Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new DialogButtonListener(dialog, mDomain, mIdentifier, mPassword));
        dialog.show();
    }

    private void populateDialog(EditText mDomain, EditText mIdentifier, EditText mPassword) {
        mDomain.setText(domain);
        mIdentifier.setText(cryptService.decrypt(identifier));
        mPassword.setText(cryptService.decrypt(password));

        mDomain.setEnabled(false);
        mIdentifier.setEnabled(false);
    }

    private void showError(EditText mPassword) {
        if (isEmpty(mPassword.getText().toString())) {
            mPassword.setError("Field can not be empty");
        }
    }

    public class DialogButtonListener implements View.OnClickListener {
        private final Dialog dialog;
        private final EditText mDomain;
        private EditText mIdentifier;
        private final EditText mPassword;

        DialogButtonListener(Dialog dialog, EditText mDomain, EditText mIdentifier, EditText mPassword) {
            this.dialog = dialog;
            this.mDomain = mDomain;
            this.mIdentifier = mIdentifier;
            this.mPassword = mPassword;
        }

        @Override
        public void onClick(View view) {
            showError(mPassword);
            Date date = new Date();
            String encryptedIdentifier = cryptService
                    .encrypt(mIdentifier.getText().toString());
            String encryptedPassword = cryptService
                    .encrypt(mPassword.getText().toString());

            new Thread(() -> {
                credentialService.updateCredential(
                        mDomain.getText().toString(),
                        encryptedIdentifier,
                        encryptedPassword,
                        date.getTime()
                );
            }).start();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            activity.getIntent().putExtra(PASSWORD, encryptedPassword)
                    .putExtra(LAST_UPDATED, dateFormat.format(date.getTime()));

            if (!isEmpty(mPassword.getText().toString())) {
                dialog.dismiss();
                activity.finish();
                activity.startActivity(activity.getIntent());
            }
        }
    }
}