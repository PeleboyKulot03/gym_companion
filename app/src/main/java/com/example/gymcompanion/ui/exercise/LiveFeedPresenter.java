package com.example.gymcompanion.ui.exercise;

import com.example.gymcompanion.utils.LiveFeedExerciseModel;

public class LiveFeedPresenter {
    private ILiveFeed liveFeedInterface;

    public LiveFeedPresenter(ILiveFeed liveFeedInterface) {
        this.liveFeedInterface = liveFeedInterface;
    }

    public void addData(String program, LiveFeedExerciseModel model, String setNumber) {
        LiveFeedExerciseModel liveFeedExerciseModel = new LiveFeedExerciseModel();
        liveFeedExerciseModel.addData((verdict, message) -> liveFeedInterface.onAddData(verdict, message), program, model, setNumber);
    }
}
