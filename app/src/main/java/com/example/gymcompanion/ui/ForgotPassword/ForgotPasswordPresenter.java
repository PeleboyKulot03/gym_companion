package com.example.gymcompanion.ui.ForgotPassword;

import com.example.gymcompanion.utils.ForgotPasswordModel;

public class ForgotPasswordPresenter {
    private final IForgotPassword forgotPasswordInterface;

    public ForgotPasswordPresenter(IForgotPassword forgotPasswordInterface) {
        this.forgotPasswordInterface = forgotPasswordInterface;
    }

    public void hasUser(String email) {
        ForgotPasswordModel model = new ForgotPasswordModel();
        model.hasUser(new ForgotPasswordModel.onRegister() {
            @Override
            public void hasUser(boolean verdict) {
                forgotPasswordInterface.hasUser(verdict);
            }

            @Override
            public void isSend(boolean verdict, String errorMessage) {

            }
        }, email);
    }

    public void sentEmail(String email) {
        ForgotPasswordModel model = new ForgotPasswordModel();
        model.forgotPassword(new ForgotPasswordModel.onRegister() {
            @Override
            public void hasUser(boolean verdict) {

            }

            @Override
            public void isSend(boolean verdict, String errorMessage) {
                forgotPasswordInterface.isSend(verdict, errorMessage);
            }
        }, email);
    }
}
