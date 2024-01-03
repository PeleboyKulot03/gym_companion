package com.example.gymcompanion.ui.homepage.fragments.dashboard;

import android.util.Log;

import com.example.gymcompanion.utils.DetailedDashboardModel;

import java.util.Map;

public class DetailedDashBoardPresenter {
    private final IDetailedDashBoard iDetailedDashBoard;

    public DetailedDashBoardPresenter(IDetailedDashBoard iDetailedDashBoard) {
        this.iDetailedDashBoard = iDetailedDashBoard;
    }

    public void getData(String exercise, String filter, int year, int month) {
        DetailedDashboardModel model = new DetailedDashboardModel();
        model.getData(iDetailedDashBoard::onGetData, exercise, filter, year, month);
    }

    public void getDataLineChart(String exercise, String filter, int year, int month) {
        DetailedDashboardModel model = new DetailedDashboardModel();
        model.getData(iDetailedDashBoard::onGetDataLineChart, exercise, filter, year, month);
    }
}
