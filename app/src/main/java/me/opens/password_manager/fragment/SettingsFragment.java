package me.opens.password_manager.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.opens.password_manager.R;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.service.CredentialService;

public class SettingsFragment extends Fragment {
    private static SharedPreferenceUtils sharedPreferenceUtils;
    private static CredentialService credentialService;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SettingsFragment.OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(SharedPreferenceUtils sharedPreferenceUtils, CredentialService credentialService) {
        SettingsFragment fragment = new SettingsFragment();
        SettingsFragment.sharedPreferenceUtils = sharedPreferenceUtils;
        SettingsFragment.credentialService = credentialService;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsFragment.OnFragmentInteractionListener) {
            mListener = (SettingsFragment.OnFragmentInteractionListener) context;
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
