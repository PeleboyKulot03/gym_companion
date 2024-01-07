package com.example.gymcompanion.ui.Homepage.fragments.dashboard;

import java.util.Map;

public interface IDetailedDashBoard {
    void onGetData(boolean verdict, Map<String, Map<Double, Long>> stats);
    void onGetDataLineChart(boolean verdict, Map<String, Map<Double, Long>> stats);

    void onChangeFilter(String mode);
}
