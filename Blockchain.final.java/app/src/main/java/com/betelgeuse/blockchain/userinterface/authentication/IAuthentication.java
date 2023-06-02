package com.betelgeuse.blockchain.userinterface.authentication;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public interface IAuthentication {
    interface AuthListener{
        void onSuccess (AuthResult authResult);
        void  onFailure (Exception e);
    }
    FirebaseUser getCurrentUser ( );
    void signInWithEmailAndPassword (String email, String password,AuthListener listener);
    void signIn_signUp_WithEmailAndPassword (String email, String password,AuthListener listener);
    void signUpWithEmailAndPassword (String email, String password,AuthListener listener);
}
