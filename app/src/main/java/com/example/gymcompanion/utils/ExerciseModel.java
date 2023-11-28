package com.example.gymcompanion.utils;

public class ExerciseModel {
    private boolean done;
    private int reps;
    private int set;
    private int weight;

    public ExerciseModel(boolean done, int reps, int set, int weight) {
        this.done = done;
        this.reps = reps;
        this.set = set;
        this.weight = weight;
    }

    public boolean done() {
        return done;
    }

    public int getReps() {
        return reps;
    }

    public int getSet() {
        return set;
    }

    public int getWeight() {
        return weight;
    }
}