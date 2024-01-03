package com.example.gymcompanion.ui.homepage.fragments.dashboard;

import com.example.gymcompanion.utils.DashBoardFragmentModel;

import java.util.Map;

public class DashBoardFragmentPresenter {
    private final IDashBoardFragment iDashBoardFragment;

    public DashBoardFragmentPresenter(IDashBoardFragment iDashBoardFragment) {
        this.iDashBoardFragment = iDashBoardFragment;
    }

    public void getData(String filter, String year, String month) {
        DashBoardFragmentModel model = new DashBoardFragmentModel();
        model.getData(iDashBoardFragment::onGetData, filter, year, month);
    }
}
