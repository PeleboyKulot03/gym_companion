package com.example.gymcompanion.ui.homepage.fragments.home;

import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;

public interface IHomeFragment {
    void getExercise(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date);
}