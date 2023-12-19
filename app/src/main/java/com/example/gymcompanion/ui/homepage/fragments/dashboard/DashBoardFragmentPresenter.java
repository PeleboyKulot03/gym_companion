package com.example.gymcompanion.ui.homepage.fragments.dashboard;

import com.example.gymcompanion.utils.DashBoardFragmentModel;

import java.util.Map;

public class DashBoardFragmentPresenter {
    private IDashBoardFragment iDashBoardFragment;

    public DashBoardFragmentPresenter(IDashBoardFragment iDashBoardFragment) {
        this.iDashBoardFragment = iDashBoardFragment;
    }

    public void getData(String filter, String year, String month) {
        DashBoardFragmentModel model = new DashBoardFragmentModel();
        model.getData((verdict, data) -> iDashBoardFragment.onGetData(verdict, data), filter, year, month);
    }
}
