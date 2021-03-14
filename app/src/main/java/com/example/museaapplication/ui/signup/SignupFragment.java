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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.museaapplication.R;

public class SignupFragment extends Fragment {

    View root;
    FragmentManager fm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_signup, container, false);
        fm = getParentFragmentManager();
        //setContentView(R.layout.activity_signup);

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
                else if (existsUsername(username.getText().toString())) {
                    warningMessage = getContext().getResources().getString(R.string.warning_used_username);
                    b = false;
                }

                if (!isEmail(email.getText().toString())) {
                    if (!warningMessage.isEmpty())
                        warningMessage += "\n";
                    warningMessage += getContext().getResources().getString(R.string.warning_not_email);
                    b = false;
                }
                else if (existsEmail(email.getText().toString())) {
                    if (!warningMessage.isEmpty())
                        warningMessage += "\n";
                    warningMessage += getContext().getResources().getString(R.string.warning_used_email);
                    b = false;
                }

                if (password.getText().toString().length() < 6) {
                    if (!warningMessage.isEmpty())
                        warningMessage += "\n";
                    warningMessage += getContext().getResources().getString(R.string.warning_short_password);
                    b = false;
                }
                else if (isPassword(password.getText().toString())) {
                    if (!warningMessage.isEmpty())
                        warningMessage += "\n";
                    warningMessage += getContext().getResources().getString(R.string.warning_weak_password);
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
                    // TODO: Mostrar que se ha registrado correctamente el nuevo usuario y volver a LoginActivity
                    // finish(); // termina SignupActivity y regresa a LoginActivity
                    getFragmentManager().popBackStack();

                }

            }
        });
        return root;
    }

    private boolean existsUsername(String username) {
        // TODO: implementar logica para determinar si ya existe un usuario con el mismo username
        return true;
    }

    private boolean isEmail(String email) {
        // TODO: implementar logica para determinar si es un email correcto
        return true;
    }
    private boolean existsEmail(String email) {
        // TODO: implementar logica para determinar si es un email ya existe
        return true;
    }
    private boolean isPassword(String password) {
        // TODO: implementar logica para determinar si el password cumple con las restricciones mínimas
        return true;
    }
}
