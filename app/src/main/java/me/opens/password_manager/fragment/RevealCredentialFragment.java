package me.opens.password_manager.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import me.opens.password_manager.R;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.listener.DialogButtonClickListener;
import me.opens.password_manager.listener.EditButtonClickListener;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.CryptService;

import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.LAST_UPDATED;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.USERNAME;
import static me.opens.password_manager.util.Constants.USER_KEY;

public class RevealCredentialFragment extends Fragment {

    public static final String TAG = RevealCredentialFragment.class.getCanonicalName();
    private static CryptService cryptService;

    private SharedPreferenceUtils sharedPreferenceUtils;
    private CredentialService credentialService;

    private OnFragmentInteractionListener mListener;

    public RevealCredentialFragment() {
        // Required empty public constructor
    }

    public static RevealCredentialFragment newInstance(SharedPreferenceUtils sharedPreferenceUtils, CredentialService credentialService) {
        RevealCredentialFragment fragment = new RevealCredentialFragment();
        fragment.sharedPreferenceUtils = sharedPreferenceUtils;
        fragment.credentialService = credentialService;
        try {
            cryptService = new CryptService(sharedPreferenceUtils.getUserKey(USER_KEY));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String domain = this.getArguments().getString(DOMAIN);
        String username = this.getArguments().getString(USERNAME);
        String password = this.getArguments().getString(PASSWORD);
        String lastUpdated = this.getArguments().getString(LAST_UPDATED);

        updateTextViewsWithCredential(domain, username, password, lastUpdated);

        try {
            setDeleteAction(domain, username, password, getListCredentialFragment());
            setEditAction(domain, username, password);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void setDeleteAction(String domain, String username, String password, Fragment destinationFragment) {
        View deleteButton = getView().findViewById(R.id.delete_credential);
        deleteButton.setOnClickListener(new DialogButtonClickListener(this, getContext(), credentialService, domain, username, password, destinationFragment));
    }

    private void setEditAction(String domain, String identifier, String password) throws Exception {
        View editButton = getView().findViewById(R.id.edit_credential);
        editButton.setOnClickListener(
                new EditButtonClickListener(
                        getContext(), this,
                        credentialService,
                        cryptService,
                        domain,
                        identifier,
                        password)
        );
    }

    private void updateTextViewsWithCredential(String domain, String username, String password, String lastUpdated) {
        TextView domainTextView = getView().findViewById(R.id.revealed_domain);
        TextView userNameTextView = getView().findViewById(R.id.revealed_username);
        TextView passwordTextView = getView().findViewById(R.id.revealed_password);
        TextView lastUpdatedTextView = getView().findViewById(R.id.last_update_time);

        domainTextView.setText(domain);
        userNameTextView.setText(cryptService.decrypt(username));
        passwordTextView.setText(cryptService.decrypt(password));
        lastUpdatedTextView.setText(lastUpdated);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reveal_credential, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onBackPressed() {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.remove(this).commit();
        Objects.requireNonNull(getListCredentialFragment()).onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getListCredentialFragment()).onResume();
    }

    private Fragment getListCredentialFragment() {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment :fragments) {
            if (fragment instanceof ListCreadentialsFragment) {
                return fragment;
            }
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
