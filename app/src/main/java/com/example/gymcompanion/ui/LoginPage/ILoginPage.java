package com.example.gymcompanion.ui.LoginPage;

public interface ILoginPage {
    void createNewUser(boolean verdict);
    void signResult(boolean verdict, String errorMessage, String displayName);
}
