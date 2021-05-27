package com.example.museaapplication.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.Json.UserInfoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MainActivity;
import com.example.museaapplication.ui.signup.SignupFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private View root;
    private FragmentManager fm;
    private LoginViewModel loginViewModel;
    private Integer r;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        root = inflater.inflate(R.layout.fragment_login, container, false);
        fm = getParentFragmentManager();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Carga de preferencias compartidas
        SharedPreferences sharedPref = getActivity().getSharedPreferences("infoUser", Context.MODE_PRIVATE);

        // Imagen de portada
        ImageView imageView = root.findViewById(R.id.image_holder);
        String url = getContext().getResources().getString(R.string.url_logo);
        Picasso.get().load(url).fit().into(imageView);

        // Get text del username y de la password
        final EditText username = root.findViewById(R.id.enter_username);
        final EditText password = root.findViewById(R.id.enter_password);

        final CheckBox checkBoxRememberMe = root.findViewById(R.id.checkbox_remember_me);

        final TextView textWarnings = root.findViewById(R.id.text_warnigs);

        final View loadingPanel = root.findViewById(R.id.loadingPanel);

        loginViewModel.getRes().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                r = integer;
                loadingPanel.setVisibility(View.GONE);
                if (r.equals(1)) {
                    Log.d("Response state","Succesfuly Login");
                    Call<UserInfoValue> call = RetrofitClient.getInstance().getMyApi().getUserInfo(username.getText().toString());
                    call.enqueue(new Callback<UserInfoValue>() {
                        @Override
                        public void onResponse(Call<UserInfoValue> call, Response<UserInfoValue> response) {
                            if (response.code() == 200){
                                UserInfoValue myuserinfo = response.body();
                                UserInfo loggedUser = myuserinfo.getUserinfo();
                                SingletonDataHolder.getInstance().setLoggedUser(loggedUser);
                                Toast.makeText(getContext(), "Logged", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else if (response.code() == 404)
                            {
                                Toast.makeText(getContext(), "There is no user for such id", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<UserInfoValue> call, Throwable t) {
                            Log.e("TAG1", t.getLocalizedMessage());
                            Log.e("TAG2", t.getMessage());
                            t.printStackTrace();
                        }
                    });

                    SharedPreferences.Editor editor = sharedPref.edit();
                    if (checkBoxRememberMe.isChecked()) {
                        String stringKey = username.getText().toString().concat("#").concat(password.getText().toString());
                        editor.putString(getString(R.string.auto_signin_key), stringKey);
                    }
                    else {
                        editor.putString(getString(R.string.auto_signin_key), "");
                    }
                    editor.apply();
                }
                else if (r.equals(2)) {
                    Log.d("Response state","Wrong username or password");
                    textWarnings.setText(getContext().getResources().getString(R.string.warning_login));
                }
                else if (r.equals(3)) {
                    Log.d("Response state","Service unavailable");
                    Toast.makeText(getContext(), "Service currently unavailable", Toast.LENGTH_SHORT).show();
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

        // Implementación del botton 'login'
        final Button loginButton = root.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginViewModel.validUsernamePassword(username.getText().toString(),password.getText().toString())) {
                    if (textWarnings.getText().toString().isEmpty()) textWarnings.setText(getContext().getResources().getString(R.string.warning_login2));
                    SingletonDataHolder.getInstance().setLoggedUser(null);
                }
            }
        });

        // Inicio de sesión automatico
        String defaultValue = "";
        String sharedValue = sharedPref.getString(getString(R.string.auto_signin_key), defaultValue);
        if (!sharedValue.isEmpty()) {
            int index = sharedValue.lastIndexOf('#');
            Log.d("SharedPreferences","Login automatico: " + sharedValue);
            Log.d("SharedPreferences",sharedValue.substring(0,index));
            Log.d("SharedPreferences",sharedValue.substring(index + 1));
            if (index != -1) {
                username.setText(sharedValue.substring(0,index));
                password.setText(sharedValue.substring(index + 1));
                loginButton.performClick();
            }
        }
        else {
            Log.d("SharedPreferences","Logeate tu mismo crack " + sharedValue);
            loadingPanel.setVisibility(View.GONE);
        }


        //Implementación del button 'Sign in with Google'
        final SignInButton loginGoogle = root.findViewById(R.id.sign_in_button);
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        // Implementación de los intentos de inicio de sesión
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

        final EditText username = root.findViewById(R.id.enter_username);
        final EditText password = root.findViewById(R.id.enter_password);
        final CheckBox checkBoxRememberMe = root.findViewById(R.id.checkbox_remember_me);
        final TextView textWarnings = root.findViewById(R.id.text_warnigs);
        textWarnings.setText("");
        //password.setText("");
        //username.setText("");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("infoUser", Context.MODE_PRIVATE);
        String defaultValue = "";
        String sharedValue = sharedPref.getString(getString(R.string.auto_signin_key), defaultValue);
        if (checkBoxRememberMe.isChecked() || !sharedValue.isEmpty()) {
            checkBoxRememberMe.setChecked(true);
            int index = sharedValue.lastIndexOf('#');
            Log.d("SharedPreferences2","Login automatico: " + sharedValue);
            Log.d("SharedPreferences2",sharedValue.substring(0,index));
            Log.d("SharedPreferences2",sharedValue.substring(index + 1));
            username.setText(sharedValue.substring(0,index));
            password.setText(sharedValue.substring(index+1));
        }
        else {
            password.setText("");
            username.setText("");
        }
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


    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("Response state","Succesfuly Login");
            // Signed in successfully, show authenticated UI.

            //Get google information account
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                Log.d("Valeeeee",String.valueOf(personName));
                Log.d("Valeeeee", String.valueOf(personEmail.split("@")[0]));
                Log.d("Valeeeee",String.valueOf(personGivenName));
                loginViewModel.createUserInfo(personEmail.split("@")[0],personEmail);
            }


            Toast.makeText(getContext(), "Logged", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

}