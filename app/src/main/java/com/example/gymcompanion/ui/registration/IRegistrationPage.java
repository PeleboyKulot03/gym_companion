package com.example.gymcompanion.ui.registration;

public interface IRegistrationPage {
    void hasUser(boolean verdict);
    void createNewAccount(boolean verdict, String errorMessage);

}
