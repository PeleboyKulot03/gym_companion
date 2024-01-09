package com.example.gymcompanion.ui.DetailedProfile;

import com.example.gymcompanion.utils.DetailedProfileModel;

import java.util.ArrayList;
import java.util.Map;

public class DetailedProfilePresenter {
    private final IDetailedProfile iDetailedProfile;

    public DetailedProfilePresenter(IDetailedProfile iDetailedProfile) {
        this.iDetailedProfile = iDetailedProfile;
    }

    public void getData(String user) {
        DetailedProfileModel model = new DetailedProfileModel();
        model.getData(iDetailedProfile::onGetProgram, user);
    }

}
