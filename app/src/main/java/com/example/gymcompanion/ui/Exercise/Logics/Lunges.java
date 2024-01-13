package com.example.gymcompanion.ui.Exercise.Logics;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymcompanion.ui.Exercise.LiveFeedPresenter;
import com.example.gymcompanion.ui.Exercise.PoseGraphic;
import com.example.gymcompanion.utils.LiveFeedExerciseModel;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Lunges {
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
    private boolean isFirstTime = true;
    private int rightAngleResult = 0, leftAngleResult = 0;
    private DecimalFormat decimalFormat;

    public Lunges(TextView counter, TextView timer, Handler timerHandler, LiveFeedPresenter presenter, String exercise, int setNumber, Button finishSet, Activity activity) {
        this.counter = counter;
        this.timer = timer;
        this.timerHandler = timerHandler;
        this.presenter = presenter;
        this.exercise = exercise;
        this.setNumber = setNumber;
        accuracies = new ArrayList<>();
        decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.UP);
        finishSet.setOnClickListener(view -> {
            if (count > 12) {
                double averageAccuracy = 0.0;
                for (Double accuracy: accuracies){
                    averageAccuracy += accuracy;
                }
                averageAccuracy /= accuracies.size();
                LiveFeedExerciseModel model = new LiveFeedExerciseModel(millis, Double.parseDouble(decimalFormat.format(averageAccuracy)));
                presenter.addData(exercise, model, setNumber, String.valueOf(averageAccuracy), String.valueOf(seconds));
                return;
            }
            Toast.makeText(activity, "There is still " + (12 - count) + " left, You got this!", Toast.LENGTH_SHORT).show();
        });
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
        // left legs
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);

        // right legs
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);


        if (((leftAnkle != null) && (leftKnee != null) && (leftHip != null)) && ((rightAnkle != null) && (rightKnee != null) && (rightHip != null))) {

            if (isFirstTime) {
                if (leftHip.getPosition().y < leftKnee.getPosition().y && rightHip.getPosition().y < rightKnee.getPosition().y) {
                    startCounter();
                    isFirstTime = false;
                }
            }
            leftAngleResult = (int) Math.toDegrees(
                    atan2(leftHip.getPosition().y - leftKnee.getPosition().y,
                            leftHip.getPosition().x - leftKnee.getPosition().x)
                            - atan2(leftAnkle.getPosition().y - leftKnee.getPosition().y,
                            leftAnkle.getPosition().x - leftKnee.getPosition().x));

            rightAngleResult = (int) Math.toDegrees(
                    atan2(rightHip.getPosition().y - rightKnee.getPosition().y,
                            rightHip.getPosition().x - rightKnee.getPosition().x)
                            - atan2(rightAnkle.getPosition().y - rightKnee.getPosition().y,
                            rightAnkle.getPosition().x - rightKnee.getPosition().x));

            leftAngleResult = abs(leftAngleResult);
            rightAngleResult = abs(rightAngleResult);

            if (leftAngleResult > 180) {
                leftAngleResult = (360 - leftAngleResult);
            }
            if (rightAngleResult > 180) {
                rightAngleResult = (360 - rightAngleResult);
            }

            checkForm(leftAngleResult);
            if (leftAngleResult < 90) {
                leftAccuracy = calculateAccuracy(leftAngleResult);
                rightAccuracy = calculateAlternateAccuracy(rightAngleResult);
            }
            else {
                leftAccuracy = calculateAlternateAccuracy(leftAngleResult);
                rightAccuracy = calculateAccuracy(rightAngleResult);
            }
        }

        poseGraphic.draw(canvas, leftAccuracy, rightAccuracy, exercise, null);
        leftAccuracy = 0.0;
        rightAccuracy = 0.0;
    }

    public void checkForm(double angleResult) {
        // the down state is reached update the direction
        // direction is from middle to bottom
        if (angleResult < 90.0 && didPass){
            directionFrom = 0;
            didPass = false;
            curLoc = "BM";
            return;
        }

        // middle point is reached
        if (angleResult >= 130.0 && angleResult <= 140.0 && !didPass){
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
                isFirst = true;
                return;
            }
            didPass = true;
        }

        // upstate reached and the direction is from middle to top
        if (angleResult > 170.0 && didPass) {
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
                presenter.addData(exercise, model, setNumber, String.valueOf(averageAccuracy), String.valueOf(seconds));
            }
        }
    }

    public double calculateAccuracy(double angleResult) {
        if (!curLoc.equals("") && (curLoc.equals("MB") || curLoc.equals("BM"))) {
            double accuracy = (100.0 - ((90.0 - angleResult) / 90.0) * 100.0);
            if (accuracy > 100.0) {
                double difference = accuracy - 100.00;
                accuracy = 100.0 - difference;
            }
            if (isFirst) {
                if (accuracy < 0) {
                    return accuracy;
                }
                accuracies.add(accuracy);
                isFirst = false;
            }
            return accuracy;
        }

        if (!curLoc.equals("") && (curLoc.equals("MT") || curLoc.equals("TM"))) {
            double accuracy = (100.0 - ((170.0 - angleResult) / 170.0) * 100.0);
            if (accuracy > 100.0) {
                double difference = accuracy - 100.00;
                accuracy = 100.0 - difference;
            }
            if (isFirst) {
                if (accuracy < 0) {
                    return accuracy;
                }
                accuracies.add(accuracy);
                isFirst = false;
            }
            accuracies.add(accuracy);
            return accuracy;
        }

        return 0.0;
    }

    public double calculateAlternateAccuracy(double angleResult) {
        if (!curLoc.equals("") && (curLoc.equals("MB") || curLoc.equals("BM"))) {
            double accuracy = (100.0 - ((170.0 - angleResult) / 170.0) * 100.0);
            if (accuracy > 100.0) {
                double difference = accuracy - 100.00;
                accuracy = 100.0 - difference;
            }
            if (isFirst) {
                if (accuracy < 0) {
                    return accuracy;
                }
                accuracies.add(accuracy);
                isFirst = false;
            }
            return accuracy;
        }

        if (!curLoc.equals("") && (curLoc.equals("MT") || curLoc.equals("TM"))) {
            double accuracy = (100.0 - ((90.0 - angleResult) / 90.0) * 100.0);
            if (accuracy > 100.0) {
                double difference = accuracy - 100.00;
                accuracy = 100.0 - difference;
            }
            if (isFirst) {
                if (accuracy < 0) {
                    return accuracy;
                }
                accuracies.add(accuracy);
                isFirst = false;
            }
            accuracies.add(accuracy);
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
