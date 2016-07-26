package Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import Listeners.LoginButtonListener;
import allblacks.com.iBaleka.R;

public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccountButton;
    private Button forgotPasswordButton;
    private LoginButtonListener buttonListener;
    private List<String> signInList;
    private TextView toolbarTextView;

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
        signInList = new ArrayList<>();
        signInList.add("Sign-up with Facebook");
        signInList.add("Sign-up with Google");
        toolbarTextView = (TextView) getActivity().findViewById(R.id.LoginActivityToolbarTextView);
        toolbarTextView.setText("Login to Continue");
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

    @Override
    public void onResume() {
        super.onResume();
        toolbarTextView.setText("Login to Continue");
    }
}
