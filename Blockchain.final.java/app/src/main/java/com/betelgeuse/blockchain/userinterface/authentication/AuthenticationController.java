package com.betelgeuse.blockchain.userinterface.authentication;

import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationController extends ViewModel {
    FirebaseUser currentUser;
     private IAuthentication auth = new FirebaseAuthentication();
     AuthenticationController(){
         currentUser = auth.getCurrentUser();
     }

    public FirebaseUser  getCurrentUser(){
         return currentUser;
    }
    protected void signUpWithEmailAndPassword(String email, String password, IAuthentication.AuthListener listener){

         auth.signUpWithEmailAndPassword(email,password,listener);
    }
    protected void signInWithEmailAndPassword(String email, String password, IAuthentication.AuthListener listener){
       auth.signInWithEmailAndPassword(email,password,listener);
    }
    protected  void  signIn_signUp_WithEmailAndPassword(String email, String password, IAuthentication.AuthListener listener){
       auth.signIn_signUp_WithEmailAndPassword(email,password,listener);
    }
    protected boolean isUserNameValid (String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }
    protected boolean isPasswordValid (String password) {
        return password != null && password.trim().length() > 5;
    }
}
