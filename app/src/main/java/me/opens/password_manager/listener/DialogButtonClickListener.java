package me.opens.password_manager.listener;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.opens.password_manager.R;
import me.opens.password_manager.activity.RevealCredentialActivity;
import me.opens.password_manager.fragment.RevealCredentialFragment;
import me.opens.password_manager.service.CredentialService;

public class DialogButtonClickListener implements View.OnClickListener {

    private final Context context;
    private final CredentialService credentialService;
    private RevealCredentialActivity revealCredentialActivity;
    private final String domain;
    private final String username;
    private final String password;

    public DialogButtonClickListener(Context context, CredentialService credentialService, String domain, String username, String password) {
        this.context = context;
        this.credentialService = credentialService;
        this.domain = domain;
        this.username = username;
        this.password = password;
    }

    @Override
    public void onClick(View view) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirm_deletion);
        dialog.setTitle("Alert");

        Button dialogButton = dialog.findViewById(R.id.dialog_delete_ok);
        dialogButton.setOnClickListener(v1 -> {
            new Thread(() -> {
                credentialService.deleteCredential(domain, username, password);
                Log.i(RevealCredentialFragment.TAG, "DELETING CREDENTIAL");
            }).start();
            dialog.dismiss();
//            revealCredentialActivity.onBackPressed();
        });
        dialog.show();
    }
}
