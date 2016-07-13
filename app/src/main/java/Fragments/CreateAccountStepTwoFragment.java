package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import BackgroundTasks.UserGatewayBackgroundTask;
import Listeners.LoginButtonListener;
import Listeners.RegistrationButtonListener;
import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountStepTwoFragment extends Fragment {

    private EditText usernameEditText, passwordEditText, securityQuestionEditText,
            securityAnswerEditText;
    private Button createAccountButton;
    private RegistrationButtonListener buttonListener;
    public CreateAccountStepTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_create_account_step_two, container, false);
        usernameEditText = (EditText) myView.findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) myView.findViewById(R.id.passwordEditText);
        securityQuestionEditText = (EditText) myView.findViewById(R.id.SecurityQuestionEditText);
        securityAnswerEditText = (EditText) myView.findViewById(R.id.SecurityAnswerEditText);
        createAccountButton = (Button) myView.findViewById(R.id.CreateAccountButton);
        buttonListener = new RegistrationButtonListener(this.getActivity());
        createAccountButton.setOnClickListener(buttonListener);
        return myView;
    }

    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRetainInstance();
    }
}
