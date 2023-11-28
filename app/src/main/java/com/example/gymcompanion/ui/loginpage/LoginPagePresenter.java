package com.example.gymcompanion.ui.loginpage;

import com.example.gymcompanion.utils.LoginPageModel;

public class LoginPagePresenter {
    private final ILoginPage loginPageInterface;
    public LoginPagePresenter(ILoginPage loginPageInterface) {
        this.loginPageInterface = loginPageInterface;
    }
    public void createNewUser(LoginPageModel model, String UId){
        LoginPageModel loginPageModel = new LoginPageModel();
        loginPageModel.createNewUser(new LoginPageModel.onCreateUser() {
            @Override
            public void isSuccess(boolean verdict) {
                loginPageInterface.createNewUser(verdict);
            }

            @Override
            public void isCorrectCredentials(boolean verdict, String errorMessage, String displayName) {

            }
        }, model, UId);
    }

    public void signIn(String username, String password) {
        LoginPageModel model = new LoginPageModel();
        model.signIn(new LoginPageModel.onCreateUser() {
            @Override
            public void isSuccess(boolean verdict) {

            }

            @Override
            public void isCorrectCredentials(boolean verdict, String errorMessage, String displayName) {
                loginPageInterface.signResult(verdict, errorMessage, displayName);
            }
        }, username, password);
    }

}
