package me.opens.password_manager.service;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.data.Credential;
import me.opens.password_manager.data.CredentialDataSource;

import static me.opens.password_manager.util.Constants.USER_EMAIL;
import static me.opens.password_manager.util.Constants.USER_KEY;
import static me.opens.password_manager.util.Constants.USER_NAME;

public class CredentialService {

    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;

    @Inject
    CredentialDataSource dataSource;

    private static final String TAG = CredentialService.class.getCanonicalName();

    @Inject
    public CredentialService() {
    }

    public boolean isKeyMatched(String passedInKey) {
        return sharedPreferenceUtils.getUserKey(USER_KEY).equals(passedInKey);
    }

    public List<Credential> getAllCredentialsFor(String username) {
        return dataSource.getAllFor(username);
    }

    public void deleteCredential(String domain, String username, String password) {
        Log.i(CredentialService.class.getName(), "Deleting credential for: " + domain + username + password);
        dataSource.deleteFor(
                sharedPreferenceUtils.getUserKey(USER_NAME),
                domain,
                username,
                password
        );
    }

    public boolean addCredential(Credential credential) {
        if (!isEmpty(credential)) {
            dataSource.addNew(credential);
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmpty(Credential credential) {
        return TextUtils.isEmpty(credential.getDomain())
                || TextUtils.isEmpty(credential.getUsername())
                || TextUtils.isEmpty(credential.getPassword());
    }

    public void updateCredential(String domain, String identifier, String password, Long updatedTime) {
        dataSource.update(sharedPreferenceUtils.getUserKey(USER_NAME),
                domain, identifier, password, updatedTime);
    }

    private void bulkUpdateCredential(String domain, String identifier, String password, Long updatedTime) {
        Log.i(TAG, "Updating credential one by one ...");
        dataSource.updateBulk(sharedPreferenceUtils.getAccountName(USER_EMAIL),
                domain, identifier, password, updatedTime);
    }

    public boolean updateKey(String passedInOldKey, String newKey) throws Exception {
        String userName = sharedPreferenceUtils.getUserName(USER_NAME);
        String oldUserKey = sharedPreferenceUtils.getUserName(USER_KEY); // Needs migration
        CryptService cryptServiceWithOldKey = new CryptService(oldUserKey);

        if (oldUserKey.equals(passedInOldKey) && newKey.length() == 4) {
            //set new user key
            sharedPreferenceUtils.setUserKey(USER_KEY, newKey);
            Log.i(TAG,"Conditions satisfied. Going to update ...");
            CryptService cryptServiceWithNewKey = new CryptService(newKey);
            List<Credential> allCreds = dataSource.getAllFor(userName);
            Log.i(TAG,"Length of all credentials " + allCreds.size());
            allCreds.forEach(ocr -> {
                String username = cryptServiceWithNewKey.encrypt(cryptServiceWithOldKey.decrypt(ocr.getUsername()));
                String password = cryptServiceWithNewKey.encrypt(cryptServiceWithOldKey.decrypt(ocr.getPassword()));
                long time = new Date().getTime();
                bulkUpdateCredential(ocr.getDomain(), username, password, time);
            });
            return true;
        }
        return false;
    }

    public void writeToFile(Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("credentials.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Toast.makeText(context, "File has been save to the location", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
