package Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import Listeners.LoginButtonListener;
import allblacks.com.Activities.MainActivity;
import allblacks.com.Activities.R;

public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccountButton;
    private Button forgotPasswordButton;
    private LoginButtonListener buttonListener;
    private SharedPreferences preferencesObject;

    public LoginFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_login, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        usernameEditText = (EditText) currentView.findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) currentView.findViewById(R.id.passwordEditText);
        loginButton = (Button) currentView.findViewById(R.id.loginButton);
        forgotPasswordButton = (Button) currentView.findViewById(R.id.forgotPasswordButton);
        createAccountButton = (Button) currentView.findViewById(R.id.registerAccountbtn);
        buttonListener = new LoginButtonListener(this.getActivity());
        loginButton.setOnClickListener(buttonListener);
        forgotPasswordButton.setOnClickListener(buttonListener);
        createAccountButton.setOnClickListener(buttonListener);

    }

}
