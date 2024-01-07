package com.example.gymcompanion.ui.Exercise.decoders;

public interface IVideoFrameExtractor {
    void onCurrentFrameExtracted(Frame Frame);
    void onAllFrameExtracted(int processedFrameCount, Long processedTimeMs);
}
