package com.example.gymcompanion.ui.exercise.decoders;

public interface IVideoFrameExtractor {
    void onCurrentFrameExtracted(Frame Frame);
    void onAllFrameExtracted(int processedFrameCount, Long processedTimeMs);
}
