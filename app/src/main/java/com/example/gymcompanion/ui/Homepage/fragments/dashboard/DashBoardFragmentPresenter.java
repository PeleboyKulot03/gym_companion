package com.example.gymcompanion.ui.Homepage.fragments.dashboard;

import com.example.gymcompanion.utils.DashBoardFragmentModel;

import java.util.Map;

public class DashBoardFragmentPresenter {
    private final IDashBoardFragment iDashBoardFragment;

    public DashBoardFragmentPresenter(IDashBoardFragment iDashBoardFragment) {
        this.iDashBoardFragment = iDashBoardFragment;
    }

    public void getData(String filter, String year, String month) {
        DashBoardFragmentModel model = new DashBoardFragmentModel();
        model.getData(new DashBoardFragmentModel.onGetData() {
            @Override
            public void isSuccess(boolean verdict, Map<String, Map<Double, Long>> data, Map<String, Integer> occurrence) {
                iDashBoardFragment.onGetData(verdict, data, occurrence);
            }

            @Override
            public void onGetWeight(boolean verdict, String currentWeight, String lostWeight) {

            }
        }, filter, year, month);
    }

    public void getWeight() {
        DashBoardFragmentModel model = new DashBoardFragmentModel();
        model.getWeight(new DashBoardFragmentModel.onGetData() {
            @Override
            public void isSuccess(boolean verdict, Map<String, Map<Double, Long>> data, Map<String, Integer> occurrence) {

            }

            @Override
            public void onGetWeight(boolean verdict, String currentWeight, String lostWeight) {
                iDashBoardFragment.onGetWeight(verdict, currentWeight, lostWeight);
            }
        });
    }
}
