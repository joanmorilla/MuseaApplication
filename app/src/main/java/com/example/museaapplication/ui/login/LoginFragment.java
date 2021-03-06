package com.example.museaapplication.ui.login;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.ui.MainActivity;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.signup.SignupFragment;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;

public class LoginFragment extends Fragment {

    private View root;
    private FragmentManager fm;
    private LoginViewModel loginViewModel;
    private Integer r;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        root = inflater.inflate(R.layout.fragment_login, container, false);
        fm = getParentFragmentManager();

        // Imagen de portada
        ImageView imageView = root.findViewById(R.id.image_holder);
        String url = getContext().getResources().getString(R.string.url_logo);
        Picasso.get().load(url).fit().into(imageView);

        // Get text del username y de la password
        final EditText username = root.findViewById(R.id.enter_username);
        final EditText password = root.findViewById(R.id.enter_password);

        final TextView textWarnings = root.findViewById(R.id.text_warnigs);

        final View loadingPanel = root.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);

        loginViewModel.getRes().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                r = integer;
                loadingPanel.setVisibility(View.GONE);
                if (r.equals(1)) {
                    Log.d("Response state","Succesfuly Login");
                    Toast.makeText(getContext(), "Logged", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
                else if (r.equals(2)) {
                    Log.d("Response state","Wrong username or password");
                    textWarnings.setText(getContext().getResources().getString(R.string.warning_login));
                }
                else if (r.equals(-1)){
                    Log.d("Response state","algo ocurrio");
                    Toast.makeText(getContext(), "Something went wrong try again later", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("Response state","esperando respuesta...");
                    loadingPanel.setVisibility(View.VISIBLE);
                }
            }
        });

        // Implementaci??n del botton 'login'
        final Button loginButton = root.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginViewModel.validUsernamePassword(username.getText().toString(),password.getText().toString())) {
                    if (textWarnings.getText().toString().isEmpty()) textWarnings.setText(getContext().getResources().getString(R.string.warning_login2));
                }
            }
        });

        // Implementaci??n de los intentos de inicio de sesi??n
        loginViewModel.getnAttempts().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer n) {
                System.out.println(loginViewModel.getRemainingTime());
                if (n > 10) {
                    loginViewModel.disableLogin();
                    textWarnings.setText(getContext().getResources().getString(R.string.warning_attempts_exceeded));
                    textWarnings.setText(textWarnings.getText() + "\n" + loginViewModel.getRemainingTime() + " seconds left to try again");
                }
                if (n == 0)
                    textWarnings.setText("");
            }
        });

        // Implementaci??n del comportamiento del texto 'sign up'
        final TextView signupText = root.findViewById(R.id.text_signup);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change frag
                Bundle bundle = new Bundle();
                bundle.putString("USERNAME", username.getText().toString());
                Fragment fragment = new SignupFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.InitialActivity,fragment)
                        .addToBackStack("tag")
                        .commit();
            }
        });
        // inflate the layout using the cloned inflater, not default inflater
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        final EditText password = root.findViewById(R.id.enter_password);
        final CheckBox checkBoxRememberMe = root.findViewById(R.id.checkbox_remember_me);
        final TextView textWarnings = root.findViewById(R.id.text_warnigs);
        textWarnings.setText("");
        if (!checkBoxRememberMe.isChecked())
            password.setText("");

        final View loadingPanel = root.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
    }

    String imageToString(int id){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    Bitmap stringToImage(String codeImage){
        byte[] imageBytes = Base64.decode(codeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
