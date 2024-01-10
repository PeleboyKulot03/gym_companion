package com.example.gymcompanion.ui.Exercise.Logics;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;

import android.graphics.Canvas;
import android.os.Handler;
import android.widget.TextView;

import com.example.gymcompanion.ui.Exercise.LiveFeedPresenter;
import com.example.gymcompanion.ui.Exercise.PoseGraphic;
import com.example.gymcompanion.utils.LiveFeedExerciseModel;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FlatBenchPress {
    private boolean didPass = false, isFirst = false, hasStart = false;
    private int directionFrom = 0, count = 0;
    private String curLoc = "BM";
    private final TextView counter;
    private final TextView timer;
    long startTime = 0;
    long millis, seconds, minutes;
    private final Handler timerHandler;
    private final LiveFeedPresenter presenter;
    private final String exercise;
    private final int setNumber;
    private final ArrayList<Double> accuracies;
    private double leftAccuracy = 0.0, rightAccuracy = 0.0;


    public FlatBenchPress(TextView counter, TextView timer, Handler timerHandler, LiveFeedPresenter presenter, String exercise, int setNumber) {
        this.counter = counter;
        this.timer = timer;
        this.timerHandler = timerHandler;
        this.presenter = presenter;
        this.exercise = exercise;
        this.setNumber = setNumber;
        accuracies = new ArrayList<>();
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            timer.setText(String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    public void getTheLandmarks(Pose pose, PoseGraphic poseGraphic, Canvas canvas) {
        // left arm
        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);

        // right arm
        PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);


        if (((leftWrist != null) && (leftElbow != null) && (leftShoulder != null) && (leftWrist.getPosition().y < leftElbow.getPosition().y))
                && ((rightWrist != null) && (rightElbow != null) && (rightShoulder != null) && (rightWrist.getPosition().y < rightElbow.getPosition().y))) {

            startCounter();
            int leftAngleResult = (int) Math.toDegrees(
                    atan2(leftShoulder.getPosition().y - leftElbow.getPosition().y,
                            leftShoulder.getPosition().x - leftElbow.getPosition().x)
                            - atan2(leftWrist.getPosition().y - leftElbow.getPosition().y,
                            leftWrist.getPosition().x - leftElbow.getPosition().x));

            int rightAngleResult = (int) Math.toDegrees(
                    atan2(rightShoulder.getPosition().y - rightElbow.getPosition().y,
                            rightShoulder.getPosition().x - rightElbow.getPosition().x)
                            - atan2(rightWrist.getPosition().y - rightElbow.getPosition().y,
                            rightWrist.getPosition().x - rightElbow.getPosition().x));

            leftAngleResult = abs(leftAngleResult);
            rightAngleResult = abs(rightAngleResult);

            if (leftAngleResult > 180) {
                leftAngleResult = (360 - leftAngleResult);
            }
            if (rightAngleResult > 180) {
                rightAngleResult = (360 - rightAngleResult);
            }

            checkForm(leftAngleResult);
            checkForm(rightAngleResult);
            leftAccuracy = calculateAccuracy(leftAngleResult);
            rightAccuracy = calculateAccuracy(rightAngleResult);
        }

        poseGraphic.draw(canvas, leftAccuracy, rightAccuracy, exercise, null);
        leftAccuracy = 0.0;
        rightAccuracy = 0.0;
    }

    public void checkForm(double angleResult) {
        // the down state is reached update the direction
        // direction is from middle to bottom
        if (angleResult < 70.0 && didPass){
            directionFrom = 0;
            didPass = false;
            curLoc = "BM";
            return;
        }

        // middle point is reached
        if (angleResult >= 90.0 && angleResult <= 110.0 && !didPass){
            // middle point is hit and the direction is from bottom to middle
            if (directionFrom == 0){
                didPass = true;
                curLoc = "MT";
                isFirst = true;
                return;
            }

            // direction is from top to middle
            if (directionFrom == 1){
                didPass = true;
                curLoc = "MB";
                return;
            }
            didPass = true;
        }

        // upstate reached and the direction is from middle to top
        if (angleResult > 150.0 && didPass) {
            directionFrom = 1;
            didPass = false;
            curLoc = "TM";
            count += 1;
            String temp = "Count: " + count;
            counter.setText(temp);
            if (count == 12) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                decimalFormat.setRoundingMode(RoundingMode.UP);
                timerHandler.removeCallbacks(timerRunnable);
                double averageAccuracy = 0.0;
                for (Double accuracy: accuracies){
                    averageAccuracy += accuracy;
                }
                averageAccuracy /= accuracies.size();

                LiveFeedExerciseModel model = new LiveFeedExerciseModel(millis, Double.parseDouble(decimalFormat.format(averageAccuracy)));
                presenter.addData(exercise, model, setNumber);
            }
        }
    }

    public double calculateAccuracy(double angleResult) {
        if (!curLoc.equals("") && curLoc.equals("MB")) {
            double accuracy = (100.0 - ((90.0 - angleResult) / 90.0) * 100.0);
            if (accuracy > 100.0) {
                double difference = accuracy - 100.00;
                accuracy = 100. - difference;
            }
            accuracies.add(accuracy);
            isFirst = false;
            return accuracy;
        }
        if (!curLoc.equals("") && curLoc.equals("BM")) {
            double accuracy = (100.0 - ((70.0 - angleResult) / 70.0) * 100.0);
            if (accuracy > 100.0) {
                double difference = accuracy - 100.00;
                accuracy = 100. - difference;
            }
            accuracies.add(accuracy);
            isFirst = false;
            return accuracy;
        }
        if (!curLoc.equals("") && (curLoc.equals("MT") || curLoc.equals("TM"))) {
            double accuracy = (100.0 - ((170.0 - angleResult) / 170.0) * 100.0);
            if (accuracy > 100.0) {
                double difference = accuracy - 100.00;
                accuracy = 100.0 - difference;
            }
            return accuracy;
        }

        return 0.0;
    }

    public void startCounter() {
        if (!hasStart) {
            hasStart = true;
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
        }

    }
}
