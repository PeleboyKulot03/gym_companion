package com.example.gymcompanion.utils;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AccountModel {
    private final FirebaseAuth auth;

    public AccountModel() {
        auth = FirebaseAuth.getInstance();
    }


    public void getUserInfo() {
        Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
    }
    public void signOut() {
        auth.signOut();
    }
}
