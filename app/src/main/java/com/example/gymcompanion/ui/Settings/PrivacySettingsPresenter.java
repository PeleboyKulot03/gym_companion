package com.example.gymcompanion.ui.Settings;

import com.example.gymcompanion.utils.PrivacySettingModel;
import com.example.gymcompanion.utils.RegistrationPageModel;

public class PrivacySettingsPresenter {
    private IPrivacySettings iPrivacySettings;

    public PrivacySettingsPresenter(IPrivacySettings iPrivacySettings) {
        this.iPrivacySettings = iPrivacySettings;
    }

    public void onGetInformation() {
        PrivacySettingModel privacySettingModel = new PrivacySettingModel();
        privacySettingModel.getInformation(new PrivacySettingModel.onGetData() {
            @Override
            public void onGetInformation(PrivacySettingModel model) {
                iPrivacySettings.onGetInformation(model);
            }

            @Override
            public void reAuthenticate(boolean verdict) {

            }

            @Override
            public void hasUser(boolean verdict) {

            }

            @Override
            public void onChangeInfo(boolean verdict, String message) {

            }
        });
    }

    public void reAuthenticate(String email, String password) {
        PrivacySettingModel privacySettingModel = new PrivacySettingModel();
        privacySettingModel.reAuthenticate(new PrivacySettingModel.onGetData() {
            @Override
            public void onGetInformation(PrivacySettingModel model) {

            }

            @Override
            public void reAuthenticate(boolean verdict) {
                iPrivacySettings.reAuthenticate(verdict);
            }

            @Override
            public void hasUser(boolean verdict) {

            }

            @Override
            public void onChangeInfo(boolean verdict, String message) {

            }
        }, email, password);
    }

    public void hasUser(String value) {
        PrivacySettingModel privacySettingModel = new PrivacySettingModel();
        privacySettingModel.hasUser(new PrivacySettingModel.onGetData() {
            @Override
            public void onGetInformation(PrivacySettingModel model) {

            }

            @Override
            public void reAuthenticate(boolean verdict) {

            }

            @Override
            public void hasUser(boolean verdict) {
                iPrivacySettings.hasUser(verdict);
            }

            @Override
            public void onChangeInfo(boolean verdict, String message) {

            }
        }, value);
    }

    public void changePassword(String password) {
        PrivacySettingModel privacySettingModel = new PrivacySettingModel();
        privacySettingModel.changePassword(new PrivacySettingModel.onGetData() {
            @Override
            public void onGetInformation(PrivacySettingModel model) {

            }

            @Override
            public void reAuthenticate(boolean verdict) {

            }

            @Override
            public void hasUser(boolean verdict) {

            }

            @Override
            public void onChangeInfo(boolean verdict, String message) {
                iPrivacySettings.onChangeInfo(verdict, message);
            }
        }, password);
    }

    public void changeUsername(String username) {
        PrivacySettingModel privacySettingModel = new PrivacySettingModel();
        privacySettingModel.changeUsername(new PrivacySettingModel.onGetData() {
            @Override
            public void onGetInformation(PrivacySettingModel model) {

            }

            @Override
            public void reAuthenticate(boolean verdict) {

            }

            @Override
            public void hasUser(boolean verdict) {

            }

            @Override
            public void onChangeInfo(boolean verdict, String message) {

            }
        }, username);
    }

    public void changeName(String fName, String mName, String sName) {
        PrivacySettingModel model = new PrivacySettingModel();
        model.changeName(new PrivacySettingModel.onGetData() {
            @Override
            public void onGetInformation(PrivacySettingModel model) {

            }

            @Override
            public void reAuthenticate(boolean verdict) {

            }

            @Override
            public void hasUser(boolean verdict) {

            }

            @Override
            public void onChangeInfo(boolean verdict, String message) {
                iPrivacySettings.onChangeInfo(verdict, message);
            }
        }, fName, mName, sName);
    }

    public void changeHeightAndWeight(String weight, String height) {
        PrivacySettingModel model = new PrivacySettingModel();
        model.changeHeightAndWeight(new PrivacySettingModel.onGetData() {
            @Override
            public void onGetInformation(PrivacySettingModel model) {

            }

            @Override
            public void reAuthenticate(boolean verdict) {

            }

            @Override
            public void hasUser(boolean verdict) {

            }

            @Override
            public void onChangeInfo(boolean verdict, String message) {
                iPrivacySettings.onChangeInfo(verdict, message);
            }
        }, weight, height);
    }

    public void changeAge(String birthday, String finalAge) {
        PrivacySettingModel model = new PrivacySettingModel();
        model.changeAge(new PrivacySettingModel.onGetData() {
            @Override
            public void onGetInformation(PrivacySettingModel model) {

            }

            @Override
            public void reAuthenticate(boolean verdict) {

            }

            @Override
            public void hasUser(boolean verdict) {

            }

            @Override
            public void onChangeInfo(boolean verdict, String message) {
                iPrivacySettings.onChangeInfo(verdict, message);
            }
        }, birthday, finalAge);
    }
}
