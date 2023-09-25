package com.example.gymcompanion.staticValues;

import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;

public class DifferentExercise {
    private final ArrayList<HomeFragmentModel> pushDay;
    private final ArrayList<HomeFragmentModel> pullDay;
    private final ArrayList<HomeFragmentModel> legDay;

    public DifferentExercise() {
        pushDay = new ArrayList<>();
        pushDay.add(new HomeFragmentModel("Flat Dumbbell Bench Press", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Incline Bench Press", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Close Grip Bench Press", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Dips", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Dumbbell Shoulder Press", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Side Lateral Raises", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Triceps Push down", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Skull crushers", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Seated Triceps Extensions", "3 sets", "12 reps"));
        pushDay.add(new HomeFragmentModel("Dumbbell Shoulder Press", "3 sets", "12 reps"));


        pullDay = new ArrayList<>();
        pullDay.add(new HomeFragmentModel("Pull-Ups", "3 sets", "12 reps"));
        pullDay.add(new HomeFragmentModel("Deadlifts", "3 sets", "12 reps"));
        pullDay.add(new HomeFragmentModel("Lat Pulldowns", "3 sets", "12 reps"));
        pullDay.add(new HomeFragmentModel("Barbell Rows", "3 sets", "12 reps"));
        pullDay.add(new HomeFragmentModel("Dumbbell Rows", "3 sets", "12 reps"));
        pullDay.add(new HomeFragmentModel("Dumbbell Shrugs", "3 sets", "12 reps"));
        pullDay.add(new HomeFragmentModel("Face Pulls", "3 sets", "12 reps"));
        pullDay.add(new HomeFragmentModel("Barbell Biceps Curls", "3 sets", "12 reps"));
        pullDay.add(new HomeFragmentModel("Dumbbell Preacher Curls", "3 sets", "12 reps"));

        legDay = new ArrayList<>();
        legDay.add(new HomeFragmentModel("Squats", "3 sets", "12 reps"));
        legDay.add(new HomeFragmentModel("Leg Press", "3 sets", "12 reps"));
        legDay.add(new HomeFragmentModel("Leg Extension", "3 sets", "12 reps"));
        legDay.add(new HomeFragmentModel("Leg Curl", "3 sets", "12 reps"));
        legDay.add(new HomeFragmentModel("Romanian Deadlifts", "3 sets", "12 reps"));
        legDay.add(new HomeFragmentModel("Lunges", "3 sets", "12 reps"));
        legDay.add(new HomeFragmentModel("Standing Calf Raises", "3 sets", "12 reps"));
    }

    public ArrayList<HomeFragmentModel> getPushDay() {
        return pushDay;
    }

    public ArrayList<HomeFragmentModel> getPullDay() {
        return pullDay;
    }

    public ArrayList<HomeFragmentModel> getLegDay() {
        return legDay;
    }
}
