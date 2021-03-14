package com.example.museaapplication.ui.login;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.MainActivity;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.signup.SignupFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;

public class LoginFragment extends Fragment {

    View root;
    FragmentManager fm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_login, container, false);
        fm = getParentFragmentManager();
        //setContentView(R.layout.activity_login);

        // Imagen de portada
        SingletonDataHolder.getInstance().setCodedImage(imageToString(R.drawable.image_holder));
        String image = SingletonDataHolder.getInstance().getCodedImage();
        ImageView imageView = root.findViewById(R.id.image_holder);
        imageView.setImageBitmap(stringToImage(image));

        // Get text del username y de la password
        final EditText username = root.findViewById(R.id.enter_username);
        final EditText password = root.findViewById(R.id.enter_password);

        final TextView textWarnings = root.findViewById(R.id.text_warnigs);


        // Implementación del botton 'login'
        final Button loginButton = root.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validUsernamePassword(username.getText().toString(),password.getText().toString())) {
                    textWarnings.setText(getContext().getResources().getString(R.string.warning_login));
                }
                else{
                    // change frag
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Implementación del comportamiento del texto 'sign up'
        final TextView signupText = root.findViewById(R.id.text_signup);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change frag
                Bundle bundle = new Bundle();
                bundle.putString("USERNAME", username.getText().toString());
                Fragment fragment = new SignupFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.InitialActivity,fragment).addToBackStack("tag").commit();
                //Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                //intent.putExtra("USERNAME", username.getText().toString());
                //startActivity(intent);
            }
        });
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        final EditText password = root.findViewById(R.id.enter_password);
        final CheckBox checkBoxRememberMe = root.findViewById(R.id.checkbox_remember_me);
        if (!checkBoxRememberMe.isChecked())
            password.setText("");

    }

    private boolean validUsernamePassword(String username, String password) {
        // TODO: Implementar logica de validacion de usuario y contraseña
        if (username.isEmpty() || password.isEmpty())
            return false;
        return true;
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
