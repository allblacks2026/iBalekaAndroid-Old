package Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Listeners.ForgotPasswordListener;
import allblacks.com.Activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordStepTwoFragment extends Fragment {

    private EditText securityAnswerEditText;
    private TextView securityQuestionTextView;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private SharedPreferences appSharedPreferences;
    private ForgotPasswordListener buttonListener;
    private Button resetPasswordButton;

    public ForgotPasswordStepTwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_forgot_password_step_two, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        appSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        buttonListener = new ForgotPasswordListener(getActivity());
        securityAnswerEditText = (EditText) currentView.findViewById(R.id.ForgotPasswordSecurityAnswerEditText);
        securityQuestionTextView = (TextView) currentView.findViewById(R.id.ForgotPasswordAnswerTextView);
        newPasswordEditText = (EditText) currentView.findViewById(R.id.ForgotPasswordNewPasswordEditText);
        confirmPasswordEditText = (EditText) currentView.findViewById(R.id.ForgotPasswordConfirmNewPasswordEditText);
        resetPasswordButton = (Button) currentView.findViewById(R.id.RetrievePasswordButton);
        resetPasswordButton.setOnClickListener(buttonListener);
        setSecurityQuestion();
    }

    private void setSecurityQuestion()
    {
        securityQuestionTextView.setText(appSharedPreferences.getString("SecurityQuestion", ""));
    }

}
