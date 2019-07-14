package me.opens.password_manager.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = getView().findViewById(R.id.fab);
        recycleView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        populateCredentialsForUser();
        setFavAction(fab);
    }

    private void setFavAction(FloatingActionButton fab) {
        String username = sharedPreferenceUtils.getUserName(USER_NAME);
        String userKey = sharedPreferenceUtils.getUserName(USER_KEY);

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
        getActivity().runOnUiThread(() -> {
            try {
                Log.i(TAG, "Trying to run on UI threda");
                recycleView.setAdapter(new CredentialAdapter(list, item -> {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.dialog_reveal_with_key);
                            Log.i(TAG, "Trying to set RecycleView Adapter");

                            Button dialogButton = dialog.findViewById(R.id.dialogButtonReveal);
                            dialogButton.setOnClickListener(view -> {
                                String passedInKey = ((EditText) dialog.findViewById(R.id.text_key)).getText().toString();
                                if (credentialService.isKeyMatched(passedInKey)) {
                                    setIntent(item);
                                    Log.i(TAG, "starting reveal credential activity");
                                    startActivity(intent);
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

    private void setIntent(Credential item) {
        Date date = new Date(item.getUpdatedAt());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

        intent.putExtra("me.opens.password_manager.MESSAGE", "List Credentials Fragment")
                .putExtra(DOMAIN, item.getDomain())
                .putExtra(USERNAME, item.getUsername())
                .putExtra(PASSWORD, item.getPassword())
                .putExtra(LAST_UPDATED, dateFormat.format(date));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_creadentials, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
