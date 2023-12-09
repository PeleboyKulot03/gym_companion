package com.example.gymcompanion.utils;

public class DashBoardFragmentModel {

    private final String progressName;
    private final int progress;
    private final int value;

    public DashBoardFragmentModel(String progressName, int progress, int value) {
        this.progressName = progressName;
        this.progress = progress;
        this.value = value;
    }

    public String getProgressName() {
        return progressName;
    }

    public int getProgress() {
        return progress;
    }

    public int getValue() {
        return value;
    }
}
