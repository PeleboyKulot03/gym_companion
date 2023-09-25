package com.example.gymcompanion.ui.home.fragments;

import com.example.gymcompanion.utils.AccountModel;

public class AccountFragmentPresenter {
    public AccountFragmentPresenter() {

    }
    public void signOut() {
        AccountModel model = new AccountModel();
        model.signOut();
    }
}
