package com.example.gymcompanion.ui.Exercise.Decoders;

public interface IVideoFrameExtractor {
    void onCurrentFrameExtracted(Frame Frame);
    void onAllFrameExtracted(int processedFrameCount, Long processedTimeMs);
}
