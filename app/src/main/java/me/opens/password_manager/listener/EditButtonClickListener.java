package me.opens.password_manager.listener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.opens.password_manager.R;
import me.opens.password_manager.fragment.RevealCredentialFragment;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.CryptService;

import static android.support.constraint.Constraints.TAG;
import static android.text.TextUtils.isEmpty;
import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.LAST_UPDATED;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.USERNAME;

public class EditButtonClickListener implements View.OnClickListener {

    private CryptService cryptService;
    private final String domain;
    private final String identifier;
    private final String password;
    private RevealCredentialFragment revealCredentialFragment;
    private final CredentialService credentialService;
    private Context context;
//    private RevealCredentialActivity activity;

    public EditButtonClickListener(Context context, RevealCredentialFragment revealCredentialFragment, CredentialService credentialService, CryptService cryptService,
                                   String domain, String identifier, String password) {
        this.context = context;
        this.revealCredentialFragment = revealCredentialFragment;
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

            Log.i(TAG, "Trying to save credential [ "+ mDomain.getText().toString() + mPassword.getText().toString() + mIdentifier.getText().toString() +" ]");
            new Thread(() -> {
                credentialService.updateCredential(
                        mDomain.getText().toString(),
                        encryptedIdentifier,
                        encryptedPassword,
                        date.getTime()
                );
            }).start();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

            Bundle args = new Bundle();
            args.putString(DOMAIN, domain);
            args.putString(USERNAME, encryptedIdentifier);
            args.putString(PASSWORD, encryptedPassword);
            args.putString(LAST_UPDATED, dateFormat.format(date.getTime()));



            if (!isEmpty(mPassword.getText().toString())) {
                dialog.dismiss();
                FragmentTransaction ft = revealCredentialFragment.getFragmentManager().beginTransaction();
                ft.detach(revealCredentialFragment);
                revealCredentialFragment.setArguments(args);
                ft.attach(revealCredentialFragment).commit();
            }
        }
    }
}
