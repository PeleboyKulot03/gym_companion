package com.example.gymcompanion.ui.Settings;

import com.example.gymcompanion.utils.PrivacySettingModel;

public interface IPrivacySettings {
    void onGetInformation(PrivacySettingModel model);
    void reAuthenticate(boolean verdict);
    void hasUser(boolean verdict);
    void onChangeInfo(boolean verdict, String message);
}
