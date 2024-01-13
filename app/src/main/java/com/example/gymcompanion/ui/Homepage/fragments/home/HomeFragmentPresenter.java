package com.example.gymcompanion.ui.Homepage.fragments.home;

import android.util.Log;

import com.example.gymcompanion.utils.HomeFragmentModel;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragmentPresenter {
    private final IHomeFragment homeFragmentInterface;

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
            public void isDone(boolean verdict) {

            }

            @Override
            public void onGetProgram(String day, String date) {

            }

            @Override
            public void onGetUpdate(boolean verdict, Map<String, Boolean> isReadyForNewProgram) {

            }

            @Override
            public void onUpdateProgram(boolean verdict) {

            }
        });
    }

    public void getProgram() {
        HomeFragmentModel model = new HomeFragmentModel();
        model.getCurrentProgram(new HomeFragmentModel.onGetExercise() {
            @Override
            public void isSuccess(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date) {

            }

            @Override
            public void isDone(boolean verdict) {

            }

            @Override
            public void onGetProgram(String date, String day) {
                homeFragmentInterface.getProgram(day, date);
            }

            @Override
            public void onGetUpdate(boolean verdict, Map<String, Boolean> isReadyForNewProgram) {

            }

            @Override
            public void onUpdateProgram(boolean verdict) {

            }
        });

    }
    public void setNewExercise(String day, String date) {
        HomeFragmentModel model = new HomeFragmentModel();
        model.setNewExercise(new HomeFragmentModel.onGetExercise() {
            @Override
            public void isSuccess(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date) {

            }

            @Override
            public void isDone(boolean verdict) {
                homeFragmentInterface.setNewExercise(verdict);
            }

            @Override
            public void onGetProgram(String date, String day) {

            }

            @Override
            public void onGetUpdate(boolean verdict, Map<String, Boolean> isReadyForNewProgram) {

            }

            @Override
            public void onUpdateProgram(boolean verdict) {

            }
        }, day, date);
    }

    public void checkForNewProgram(int year, int month, int day) {
        HomeFragmentModel model = new HomeFragmentModel();
        model.checkForNewProgram(new HomeFragmentModel.onGetExercise() {
            @Override
            public void isSuccess(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date) {

            }

            @Override
            public void isDone(boolean verdict) {

            }

            @Override
            public void onGetProgram(String date, String day) {

            }

            @Override
            public void onGetUpdate(boolean verdict, Map<String, Boolean> isReadyForNewProgram) {
                homeFragmentInterface.onGetUpdate(verdict, isReadyForNewProgram);
            }

            @Override
            public void onUpdateProgram(boolean verdict) {

            }
        }, year, month, day);
    }

    public void updateCurrentProgram(Map<String, Boolean> isReadyForUpdate) {
        HomeFragmentModel model = new HomeFragmentModel();
        model.updateCurrentProgram(new HomeFragmentModel.onGetExercise() {
            @Override
            public void isSuccess(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date) {

            }

            @Override
            public void isDone(boolean verdict) {

            }

            @Override
            public void onGetProgram(String date, String day) {

            }

            @Override
            public void onGetUpdate(boolean verdict, Map<String, Boolean> isReadyForNewProgram) {

            }

            @Override
            public void onUpdateProgram(boolean verdict) {

            }
        }, isReadyForUpdate);
    }
}