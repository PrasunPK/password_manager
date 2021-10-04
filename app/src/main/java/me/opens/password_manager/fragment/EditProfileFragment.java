package me.opens.password_manager.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import me.opens.password_manager.R;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.data.Profile;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.ProfileService;

import static java.lang.Thread.sleep;
import static me.opens.password_manager.util.Constants.USER_NAME;

public class EditProfileFragment extends Fragment {
    private EditProfileFragment.OnFragmentInteractionListener mListener;


    private static SharedPreferenceUtils sharedPreferenceUtils;
    private static CredentialService credentialService;
    private static ProfileService profileService;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(SharedPreferenceUtils sharedPreferenceUtils, CredentialService credentialService, ProfileService profileService) {
        EditProfileFragment.sharedPreferenceUtils = sharedPreferenceUtils;
        EditProfileFragment.credentialService = credentialService;
        EditProfileFragment.profileService = profileService;
        return new EditProfileFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSaveProfileOption();
        setSaveSecurityOption();
    }

    private void setSaveSecurityOption() {
        Button saveSecurityBtn = (Button) getView().findViewById(R.id.save_security_btn);
        saveSecurityBtn.setOnClickListener(v -> {
            EditText oldPass = (EditText) getView().findViewById(R.id.old_pass_code_text_identifier);
            EditText newPass = (EditText) getView().findViewById(R.id.new_pass_code_text_identifier);
            new Thread(() -> {
                try {
                    boolean updated = credentialService.updateKey(oldPass.getText().toString(), newPass.getText().toString());
                    TextView warning = (TextView) getView().findViewById(R.id.warning_old_password_mismatch);
                    warning.setVisibility(updated ? View.INVISIBLE : View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private void setSaveProfileOption() {
        Button saveProfileBtn = (Button) getView().findViewById(R.id.save_profile_btn);
        saveProfileBtn.setOnClickListener(v -> {
            EditText newName = (EditText) getView().findViewById(R.id.name_text_identifier);
            new Thread(() -> {
                if (!profileService.doesExist()) {
                    long timeNowInMillis = new Date().getTime();
                    Profile profile = new Profile();
                    profile.setName(newName.getText().toString());
                    profile.setUsername(sharedPreferenceUtils.getUserName(USER_NAME));
                    profile.setCreatedAt(timeNowInMillis);
                    profile.setUpdatedAt(timeNowInMillis);
                    profileService.addProfile(profile);

                    refreshPreviousFragment();
                } else {
                    profileService.updateProfile(sharedPreferenceUtils.getUserName(USER_NAME), newName.getText().toString());

                    refreshPreviousFragment();
                }
            }).start();
        });
    }

    private void refreshPreviousFragment() {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.remove(this).commit();
        Objects.requireNonNull(getSettingsFragment()).onResume();
    }

    private Fragment getSettingsFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof SettingsFragment) {
                return fragment;
            }
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditProfileFragment.OnFragmentInteractionListener) {
            mListener = (EditProfileFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
