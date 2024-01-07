package com.example.gymcompanion.ui.Homepage.fragments.home;

import android.util.Log;

import com.example.gymcompanion.utils.HomeFragmentModel;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class HomeFragmentPresenter {
    private IHomeFragment homeFragmentInterface;

    public HomeFragmentPresenter(IHomeFragment homeFragmentInterface) {
        this.homeFragmentInterface = homeFragmentInterface;
    }

    public void getExercise() {
        HomeFragmentModel model = new HomeFragmentModel();
        model.getExercise(new HomeFragmentModel.onGetExercise() {
            @Override
            public void isSuccess(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date) {
                homeFragmentInterface.getExercise(verdict, models, finished, hasInfo, currentDay, date);
            }

            @Override
            public void isDone(boolean verdict, DataSnapshot snapshot) {

            }
        });
    }

    public void getNewExercise(String day) {
        HomeFragmentModel model = new HomeFragmentModel();
        model.getNewExercise(new HomeFragmentModel.onGetExercise() {
            @Override
            public void isSuccess(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date) {

            }

            @Override
            public void isDone(boolean verdict, DataSnapshot snapshot) {
                if (verdict) {
                    Log.i("tesaurus", "isDone: " + snapshot.getValue());
                }
            }
        }, day);
    }
}