package me.opens.password_manager.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import me.opens.password_manager.R;
import me.opens.password_manager.adapter.CredentialAdapter;
import me.opens.password_manager.config.SharedPreferenceUtils;
import me.opens.password_manager.data.Credential;
import me.opens.password_manager.listener.FabClickListener;
import me.opens.password_manager.service.CredentialService;
import me.opens.password_manager.service.CryptService;

import static me.opens.password_manager.util.Constants.DOMAIN;
import static me.opens.password_manager.util.Constants.LAST_UPDATED;
import static me.opens.password_manager.util.Constants.PASSWORD;
import static me.opens.password_manager.util.Constants.USERNAME;
import static me.opens.password_manager.util.Constants.USER_KEY;
import static me.opens.password_manager.util.Constants.USER_NAME;

public class ListCreadentialsFragment extends Fragment {

    public static final String TAG = ListCreadentialsFragment.class.getCanonicalName();

    private SharedPreferenceUtils sharedPreferenceUtils;
    private CredentialService credentialService;

    private OnFragmentInteractionListener mListener;


    private RecyclerView recycleView;
    SearchView searchView;
    private Intent intent;

    private FloatingActionButton fab;


    public ListCreadentialsFragment() {
        // Required empty public constructor
    }

    public static ListCreadentialsFragment newInstance(SharedPreferenceUtils sharedPreferenceUtils, CredentialService credentialService) {
        ListCreadentialsFragment fragment = new ListCreadentialsFragment();
        fragment.sharedPreferenceUtils = sharedPreferenceUtils;
        fragment.credentialService = credentialService;
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = getView().findViewById(R.id.fab);
        recycleView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        populateCredentialsForUser();
        enableSearchOption();
        setFavAction(fab);
    }

    private void enableSearchOption() {
        searchView = (SearchView) requireView().findViewById(R.id.credential_search_box);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return searchAndApply(query);
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange" + newText);
                return searchAndApply(newText);
            }

            private boolean searchAndApply(String query) {
                String username = sharedPreferenceUtils.getUserName(USER_NAME);
                new Thread(() -> {
                    List<Credential> credentials = credentialService
                            .getAllCredentialsFor(username);

                    List<Credential> filteredCredentials = credentials.stream().filter(cred -> cred.getDomain().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
                            || cred.getUsername().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))).collect(Collectors.toList());

                    if (!filteredCredentials.isEmpty()) {
                        populateCredentials(filteredCredentials);
                    }
                }).start();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        populateCredentialsForUser();
    }

    private void setFavAction(FloatingActionButton fab) {
        String username = sharedPreferenceUtils.getUserName(USER_NAME);
        String userKey = sharedPreferenceUtils.getUserName(USER_KEY); // Needs migration

        try {
            fab.setOnClickListener(new FabClickListener(this, getContext(),
                    credentialService, new CryptService(userKey), username));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateCredentialsForUser() {
        String username = sharedPreferenceUtils.getUserName(USER_NAME);
        new Thread(() -> {
            List<Credential> credentials = credentialService
                    .getAllCredentialsFor(username);
            if (!credentials.isEmpty()) {
                populateCredentials(credentials);
            }
        }).start();
    }

    public void populateCredentials(final List<Credential> list) {
        List<Credential> sortedList = list.stream().sorted(Comparator.comparing(Credential::getDomain)).collect(Collectors.toList());
        getActivity().runOnUiThread(() -> {
            try {
                Log.i(TAG, "Trying to run on UI thread");
                recycleView.setAdapter(new CredentialAdapter(sortedList, item -> {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.dialog_reveal_with_key);
                            Log.i(TAG, "Trying to set RecycleView Adapter");
                            TextView textView = dialog.findViewById(R.id.t9_key_reveal);
                            textView.setOnClickListener(view -> {
                                String passedInKey = ((EditText) dialog.findViewById(R.id.password_field)).getText().toString();
                                if (credentialService.isKeyMatched(passedInKey)) {
                                    Log.i(TAG, "Starting reveal credential activity with key [" + passedInKey + "]");

                                    Date date = new Date(item.getUpdatedAt());
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                                    Bundle args = new Bundle();
                                    args.putString(DOMAIN, item.getDomain());
                                    args.putString(USERNAME, item.getUsername());
                                    args.putString(PASSWORD, item.getPassword());
                                    args.putString(LAST_UPDATED, dateFormat.format(date));

                                    Fragment fragment = RevealCredentialFragment.newInstance(sharedPreferenceUtils, credentialService);
                                    fragment.setArguments(args);

                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_list_credentials, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                                dialog.dismiss();
                            });
                            dialog.show();
                        }, new CryptService(sharedPreferenceUtils.getUserName(USER_NAME)))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_creadentials, container, false);
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
