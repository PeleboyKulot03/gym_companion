package com.example.gymcompanion.utils;

import com.google.firebase.auth.FirebaseAuth;

public class AccountModel {
    private final FirebaseAuth auth;

    public AccountModel() {
        auth = FirebaseAuth.getInstance();
    }


    public void getUserInfo() {
        auth.getCurrentUser().getDisplayName();
    }
    public void signOut() {
        auth.signOut();
    }
}
