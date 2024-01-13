package com.example.gymcompanion.ui.Exercise;

import com.example.gymcompanion.utils.LiveFeedExerciseModel;

public class LiveFeedPresenter {
    private final ILiveFeed liveFeedInterface;

    public LiveFeedPresenter(ILiveFeed liveFeedInterface) {
        this.liveFeedInterface = liveFeedInterface;
    }

    public void addData(String program, LiveFeedExerciseModel model, int setNumber, String acc, String time) {
        LiveFeedExerciseModel liveFeedExerciseModel = new LiveFeedExerciseModel();
        liveFeedExerciseModel.addData((verdict, message) -> liveFeedInterface.onAddData(verdict, message, acc, time), program, model, setNumber);
    }
}
