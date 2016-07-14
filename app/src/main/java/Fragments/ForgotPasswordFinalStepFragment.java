package Fragments;


<<<<<<< HEAD
=======
import android.content.SharedPreferences;
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import allblacks.com.Activities.R;

public class ForgotPasswordFinalStepFragment extends Fragment {

<<<<<<< HEAD

=======
    private SharedPreferences appSharedPreferences;
>>>>>>> 3526f070e03d4131bb2ccb8c0d6c2d3a854e04ff
    public ForgotPasswordFinalStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_final_step, container, false);
    }

}
