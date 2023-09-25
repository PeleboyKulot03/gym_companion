package com.example.gymcompanion.ui.registration;

import com.example.gymcompanion.utils.RegistrationPageModel;

public class RegistrationPagePresenter {
    private final IRegistrationPage registrationPageInterface;

    public RegistrationPagePresenter(IRegistrationPage registrationPageInterface) {
        this.registrationPageInterface = registrationPageInterface;
    }

    public void hasUser(String email) {
        RegistrationPageModel model = new RegistrationPageModel();
        model.hasUser(new RegistrationPageModel.onRegister() {
            @Override
            public void isSuccess(boolean verdict, String errorMessage) {

            }

            @Override
            public void hasUser(boolean verdict) {
                registrationPageInterface.hasUser(verdict);
            }
        }, email);
    }

    public void createNewUser(String email, String password, RegistrationPageModel model) {
        RegistrationPageModel registrationPageModel = new RegistrationPageModel();
        registrationPageModel.createNewUser(new RegistrationPageModel.onRegister() {
            @Override
            public void isSuccess(boolean verdict, String errorMessage) {
                registrationPageInterface.createNewAccount(verdict, errorMessage);
            }

            @Override
            public void hasUser(boolean verdict) {

            }
        }, email, password, model);
    }
}
