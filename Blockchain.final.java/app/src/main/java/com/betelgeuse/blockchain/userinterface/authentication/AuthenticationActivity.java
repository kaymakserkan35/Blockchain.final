package com.betelgeuse.blockchain.userinterface.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betelgeuse.blockchain.R;
import com.betelgeuse.blockchain.Testing;
import com.betelgeuse.blockchain.userinterface.authentication.IAuthentication.AuthListener;
import com.betelgeuse.blockchain.userinterface.information.InformationActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity {

    AuthenticationController auth;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        auth = new AuthenticationController();

        {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser!=null) {
                String userEmail = currentUser.getEmail();
                changeActivity(InformationActivity.class,false,userEmail);
            }
        }




        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button signInOrRegister = findViewById(R.id.signInOrRegister);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        signInOrRegister.setOnClickListener((View v) -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            Testing testing = new Testing(this);
            testing.seedUserOptions(email);
            auth.signIn_signUp_WithEmailAndPassword(email, password, new AuthListener() {
                @Override
                public void onSuccess (AuthResult authResult) {
                    String displayName = authResult.getUser().getDisplayName();
                    String message = ((displayName != null) ? "Welcome " + displayName : "Welcome!!");
                    Toast.makeText(AuthenticationActivity.this, message, Toast.LENGTH_SHORT).show();
                    changeActivity(InformationActivity.class,false,email);
                    new Testing(AuthenticationActivity.this).seedUserOptions(email);
                }
                @Override
                public void onFailure (Exception e) {
                    Toast.makeText(AuthenticationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

   private <Activity>  void changeActivity  (Class<Activity> activity, boolean addBackStack, @Nullable String data){
       Intent intent =
               new Intent(AuthenticationActivity.this, activity);
       if (data!=null) intent.putExtra("email",data);
       if (!addBackStack) {finish();}
       startActivity(intent);
   }


}