package com.example.gymcompanion.ui.Registration;

public interface IRegistrationPage {
    void hasUser(boolean verdict);
    void createNewAccount(boolean verdict, String errorMessage);

}
