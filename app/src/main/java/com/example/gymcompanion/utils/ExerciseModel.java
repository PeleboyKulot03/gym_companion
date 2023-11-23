package com.example.gymcompanion.utils;

public class ExerciseModel {
    private boolean isDone;
    private int reps;
    private int set;
    private int weight;

    public ExerciseModel(boolean isDone, int reps, int set, int weight) {
        this.isDone = isDone;
        this.reps = reps;
        this.set = set;
        this.weight = weight;
    }

    public boolean isDone() {
        return isDone;
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