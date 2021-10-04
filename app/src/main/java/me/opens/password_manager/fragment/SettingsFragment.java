package me.opens.password_manager.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import me.opens.password_manager.R;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.ProfileService;

import static me.opens.password_manager.util.Constants.USER_EMAIL;

public class SettingsFragment extends Fragment {
    private static SharedPreferenceUtils sharedPreferenceUtils;
    private static CredentialService credentialService;
    private static ProfileService profileService;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(SharedPreferenceUtils sharedPreferenceUtils, CredentialService credentialService, ProfileService profileService) {
        SettingsFragment.profileService = profileService;
        SettingsFragment fragment = new SettingsFragment();
        SettingsFragment.sharedPreferenceUtils = sharedPreferenceUtils;
        SettingsFragment.credentialService = credentialService;
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView editProfile = (ImageView) getView().findViewById(R.id.img_pencil_edit_bg);
        editProfile.setOnClickListener(view1 -> {
            Fragment fragment = EditProfileFragment.newInstance(sharedPreferenceUtils, credentialService, profileService);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_settings, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
            transaction.addToBackStack(null);  // this will manage backstack
            transaction.commit();
        });

        populateName();
    }

    private void populateName() {
        new Thread(() -> {
            if (profileService.doesExist()) {
                String name = profileService.getProfile().getName();
                getActivity().runOnUiThread(() -> {
                    TextView profileName = getView().findViewById(R.id.profile_name);
                    profileName.setText(name);
                    TextView emailAccount = getView().findViewById(R.id.email_account);
                    emailAccount.setText(sharedPreferenceUtils.getAccountName(USER_EMAIL));
                });
            }
        }).start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateName();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsFragment.OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
