package com.example.museaapplication.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.R;

public class SignupFragment extends Fragment {

    private View root;
    private FragmentManager fm;
    private SignupViewModel signupViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        root = inflater.inflate(R.layout.fragment_signup, container, false);
        fm = getParentFragmentManager();


        // Get text del formulario
        final EditText username = root.findViewById(R.id.enter_username);
        final EditText email = root.findViewById(R.id.enter_email);
        final EditText password = root.findViewById(R.id.enter_password);
        final EditText password2 = root.findViewById(R.id.enter_password2);

        final TextView textWarnings = root.findViewById(R.id.text_warnigs);

        // Si el usuario ya ha escrito un username se lo colocamos directamente en username
        // username.setText(getIntent().getStringExtra("USERNAME"));
        Bundle bundle = getArguments();
        if (bundle == null) {
            System.out.println("No Data");
            return root;
        }
        username.setText(bundle.getString("USERNAME"));

        // Implementación del botton 'signup'
        final Button signup = root.findViewById(R.id.button_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = true;
                String warningMessage = new String();

                if (username.getText().toString().length() < 6) {
                    warningMessage = getContext().getResources().getString(R.string.warning_short_username);
                    b = false;
                }

                if (!signupViewModel.isEmail(email.getText().toString())) {
                    if (!warningMessage.isEmpty())
                        warningMessage += "\n";
                    warningMessage += getContext().getResources().getString(R.string.warning_not_email);
                    b = false;
                }

                /*
                if (signupViewModel.existsUser(username.getText().toString(), email.getText().toString())) {
                    warningMessage = getContext().getResources().getString(R.string.warning_used_username);
                    b = false;
                }
                else if (signupViewModel.existsEmail(email.getText().toString())) {
                    if (!warningMessage.isEmpty())
                        warningMessage += "\n";
                    warningMessage += getContext().getResources().getString(R.string.warning_used_email);
                    b = false;
                }
                */

                if (!signupViewModel.isValidPassword(password.getText().toString()) || password.getText().toString().length()<8) {
                    if (!warningMessage.isEmpty())
                        warningMessage += "\n";
                    warningMessage += getContext().getResources().getString(R.string.warning_password);
                    b = false;
                }

                if (!password2.getText().toString().equals(password.getText().toString())) {
                    if (!warningMessage.isEmpty())
                        warningMessage += "\n";
                    warningMessage += getContext().getResources().getString(R.string.warning_not_match_password);
                    b = false;
                }

                // Go back to the last activity
                if (!b)
                    textWarnings.setText(warningMessage);
                else {
                    if (signupViewModel.newSignup(username.getText().toString(),password.getText().toString(),email.getText().toString()))
                        Toast.makeText(getContext(),"User created!",Toast.LENGTH_SHORT);
                    else
                        Toast.makeText(getContext(),"User already exist",Toast.LENGTH_SHORT);
                    fm.popBackStack();
                }

            }
        });
        return root;
    }

}
