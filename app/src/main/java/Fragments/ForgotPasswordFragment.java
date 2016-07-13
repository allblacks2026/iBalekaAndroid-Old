package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import Listeners.LoginButtonListener;
import allblacks.com.Activities.R;

public class ForgotPasswordFragment extends Fragment {

    private EditText emailAddressEditText;
    private EditText usernameEditText;
    private Button nextStepButton;
    private LoginButtonListener buttonListener;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        emailAddressEditText = (EditText) currentView.findViewById(R.id
                .ForgotPasswordEmailEditText);
        nextStepButton = (Button) currentView.findViewById(R.id.ForgotPasswordNextStepButton);
        buttonListener = new LoginButtonListener(this.getActivity());
        nextStepButton.setOnClickListener(buttonListener);
    }

}
