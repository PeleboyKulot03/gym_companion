package com.example.gymcompanion.ui.loginpage;

public interface ILoginPage {
    void createNewUser(boolean verdict);
    void signResult(boolean verdict, String errorMessage, String displayName);
}
