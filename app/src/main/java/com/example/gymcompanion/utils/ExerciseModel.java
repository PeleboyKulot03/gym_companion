package com.example.gymcompanion.utils;

public class ExerciseModel {
    private final boolean done;
    private final int reps;
    private final int set;
    private final double weight;

    public ExerciseModel(boolean done, int reps, int set, double weight) {
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

    public double getWeight() {
        return weight;
    }
}