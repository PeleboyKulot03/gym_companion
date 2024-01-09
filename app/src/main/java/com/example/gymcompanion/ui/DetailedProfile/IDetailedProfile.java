package com.example.gymcompanion.ui.DetailedProfile;

import com.example.gymcompanion.utils.DetailedProfileModel;

import java.util.ArrayList;
import java.util.Map;

public interface IDetailedProfile {
    void onGetProgram(Map<String, ArrayList<DetailedProfileModel>> models);
}
