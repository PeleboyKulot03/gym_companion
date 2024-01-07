package com.example.gymcompanion.ui.Homepage.fragments.dashboard;

import java.util.Map;

public interface IDashBoardFragment {
    void onGetData(boolean verdict, Map<String, Map<Double, Long>> stats, Map<String, Integer> occurrence);
    void onChangeFilter(String mode);

    void onGetWeight(boolean verdict, String currentWeight, String lostWeight);
}
