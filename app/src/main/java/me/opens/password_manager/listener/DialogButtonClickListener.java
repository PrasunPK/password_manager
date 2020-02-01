package me.opens.password_manager.listener;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.opens.password_manager.R;
import me.opens.password_manager.fragment.RevealCredentialFragment;
import me.opens.password_manager.service.CredentialService;

public class DialogButtonClickListener implements View.OnClickListener {

    private RevealCredentialFragment currentFragment;
    private final Context context;
    private final CredentialService credentialService;
    private final String domain;
    private final String username;
    private final String password;
    private Fragment destinationFragment;

    public DialogButtonClickListener(RevealCredentialFragment currentFragment, Context context, CredentialService credentialService, String domain,
                                     String username, String password, Fragment destinationFragment) {
        this.currentFragment = currentFragment;
        this.context = context;
        this.credentialService = credentialService;
        this.domain = domain;
        this.username = username;
        this.password = password;
        this.destinationFragment = destinationFragment;
    }

    @Override
    public void onClick(View view) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirm_deletion);
        dialog.setTitle("Alert");

        Button dialogButtonOk = dialog.findViewById(R.id.dialog_delete_ok);
        Button dialogButtonCancel = dialog.findViewById(R.id.dialog_delete_cancel);
        dialogButtonOk.setOnClickListener(v1 -> {
            new Thread(() -> {
                credentialService.deleteCredential(domain, username, password);
                Log.i(RevealCredentialFragment.TAG, "DELETING CREDENTIAL");
            }).start();
            dialog.dismiss();
            currentFragment.onBackPressed();
            destinationFragment.onResume();
        });

        dialogButtonCancel.setOnClickListener(v1 -> dialog.dismiss());
        dialog.show();
    }
}
