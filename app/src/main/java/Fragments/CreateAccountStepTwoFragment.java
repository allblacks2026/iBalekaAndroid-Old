package Fragments;


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

import Listeners.RegistrationButtonListener;
import allblacks.com.iBaleka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountStepTwoFragment extends Fragment {

    private EditText usernameEditText, passwordEditText,
            securityAnswerEditText;
    private MaterialSpinner securityQuestionSpinner;
    private Button createAccountButton;
    private RegistrationButtonListener buttonListener;
    private TextView toolbarTextView;
    private List<String> questionsList;
    public CreateAccountStepTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        questionsList = new ArrayList<>();
        questionsList.add("What was the name of your first grade teacher?");
        questionsList.add("What was the name of your first pet?");
        questionsList.add("Who was your first high school crush?");
        questionsList.add("What is your maiden name?");
        questionsList.add("What was the first city you lived in?");
        questionsList.add("Who was your Grade 12 mathematics teacher?");
        questionsList.add("Where did you graduate from high school?");
        View myView = inflater.inflate(R.layout.fragment_create_account_step_two, container, false);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.LoginActivityToolbarTextView);
        toolbarTextView.setText("Create Account - Step 2 of 2");
        usernameEditText = (EditText) myView.findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) myView.findViewById(R.id.passwordEditText);
        securityQuestionSpinner = (MaterialSpinner) myView.findViewById(R.id.SecurityQuestionSpinner);
        securityQuestionSpinner.setItems(questionsList);
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
        toolbarTextView.setText("Create Account - Step 2 of 2");
    }
}
