package com.example.gymcompanion.ui.homepage.fragments;

import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;

public class HomeFragmentPresenter {
    private IHomeFragment homeFragmentInterface;

    public HomeFragmentPresenter(IHomeFragment homeFragmentInterface) {
        this.homeFragmentInterface = homeFragmentInterface;
    }

    public void getExercise() {
        HomeFragmentModel model = new HomeFragmentModel();
        model.getExercise((verdict, models, finish) -> homeFragmentInterface.getExercise(verdict, models, finish));
    }
}
