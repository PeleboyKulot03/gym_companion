package com.example.gymcompanion.ui.Exercise.Decoders;

import java.nio.ByteBuffer;

public class Frame {
    private ByteBuffer byteBuffer;
    private int width;
    private int height;
    private int position;
    private long timestamp;
    private int rotation;
    private boolean isFlipX;
    private boolean isFlipY;

    public Frame(ByteBuffer byteBuffer, int width, int height, int position, long timestamp, int rotation, boolean isFlipX, boolean isFlipY) {
        this.byteBuffer = byteBuffer;
        this.width = width;
        this.height = height;
        this.position = position;
        this.timestamp = timestamp;
        this.rotation = rotation;
        this.isFlipX = isFlipX;
        this.isFlipY = isFlipY;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPosition() {
        return position;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getRotation() {
        return rotation;
    }

    public boolean isFlipX() {
        return isFlipX;
    }

    public boolean isFlipY() {
        return isFlipY;
    }
}
