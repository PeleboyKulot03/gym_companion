package com.example.gymcompanion.ui.Homepage.fragments.home;

import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;

public interface IHomeFragment {
    void getProgram(String currentDay, String date);
    void getExercise(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date);
    void setNewExercise(boolean verdict);
}