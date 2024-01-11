package com.example.gymcompanion.ui.Exercise.Logics;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.max;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.gymcompanion.ui.Exercise.LiveFeedPresenter;
import com.example.gymcompanion.ui.Exercise.PoseGraphic;
import com.example.gymcompanion.utils.LiveFeedExerciseModel;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Deadlift {
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
    private final ArrayList<Double> finalAccuracy;
    private ArrayList<ArrayList<Double>> accuracies;
    private double leftBodyAccuracy = 0.0, rightBodyAccuracy = 0.0, leftArmAccuracy = 0.0, rightArmAccuracy = 0.0, leftLowerBodyAccuracy = 0.0, rightLowerBodyAccuracy = 0.0;
    private boolean isFirsTime = true;
    private boolean isFirstArm = false;


    public Deadlift(TextView counter, TextView timer, Handler timerHandler, LiveFeedPresenter presenter, String exercise, int setNumber) {
        this.counter = counter;
        this.timer = timer;
        this.timerHandler = timerHandler;
        this.presenter = presenter;
        this.exercise = exercise;
        this.setNumber = setNumber;
        finalAccuracy = new ArrayList<>();
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

        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);

        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);

        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);


        if (((leftWrist != null) && (leftElbow != null) && (leftShoulder != null))
                && ((rightWrist != null) && (rightElbow != null) && (rightShoulder != null))
                && ((leftHip != null) && (leftKnee != null) && (leftAnkle != null))
                && ((rightHip != null) && (rightKnee != null) && (rightAnkle != null))) {

            if (isFirsTime){
                if ((leftWrist.getPosition().y >= leftKnee.getPosition().y) && (leftWrist.getPosition().y >= leftKnee.getPosition().y)) {
                    startCounter();
                    isFirsTime = false;
                }
            }
            int leftArmAngle = (int) Math.toDegrees(
                    atan2(leftShoulder.getPosition().y - leftElbow.getPosition().y,
                            leftShoulder.getPosition().x - leftElbow.getPosition().x)
                            - atan2(leftWrist.getPosition().y - leftElbow.getPosition().y,
                            leftWrist.getPosition().x - leftElbow.getPosition().x));

            int rightArmAngle = (int) Math.toDegrees(
                    atan2(rightShoulder.getPosition().y - rightElbow.getPosition().y,
                            rightShoulder.getPosition().x - rightElbow.getPosition().x)
                            - atan2(rightWrist.getPosition().y - rightElbow.getPosition().y,
                            rightWrist.getPosition().x - rightElbow.getPosition().x));

            int leftBodyAngle = (int) Math.toDegrees(
                    atan2(leftShoulder.getPosition().y - leftHip.getPosition().y,
                            leftShoulder.getPosition().x - leftHip.getPosition().x)
                            - atan2(leftKnee.getPosition().y - leftHip.getPosition().y,
                            leftKnee.getPosition().x - leftHip.getPosition().x));

            int rightBodyAngle = (int) Math.toDegrees(
                    atan2(rightShoulder.getPosition().y - rightHip.getPosition().y,
                            rightShoulder.getPosition().x - rightHip.getPosition().x)
                            - atan2(rightKnee.getPosition().y - rightHip.getPosition().y,
                            rightKnee.getPosition().x - rightHip.getPosition().x));

            int leftLowerBodyAngle = (int) Math.toDegrees(
                    atan2(leftHip.getPosition().y - leftKnee.getPosition().y,
                            leftHip.getPosition().x - leftKnee.getPosition().x)
                            - atan2(leftAnkle.getPosition().y - leftKnee.getPosition().y,
                            leftAnkle.getPosition().x - leftKnee.getPosition().x));

            int rightLowerBodyAngle = (int) Math.toDegrees(
                    atan2(rightHip.getPosition().y - rightKnee.getPosition().y,
                            rightHip.getPosition().x - rightKnee.getPosition().x)
                            - atan2(rightAnkle.getPosition().y - rightKnee.getPosition().y,
                            rightAnkle.getPosition().x - rightKnee.getPosition().x));

            leftArmAngle = abs(leftArmAngle);
            rightArmAngle = abs(rightArmAngle);
            leftBodyAngle = abs(leftBodyAngle);
            rightBodyAngle = abs(rightBodyAngle);
            leftLowerBodyAngle = abs(leftLowerBodyAngle);
            rightLowerBodyAngle = abs(rightLowerBodyAngle);

            if (leftArmAngle > 180) {
                leftArmAngle = (360 - leftArmAngle);
            }
            if (rightArmAngle > 180) {
                rightArmAngle = (360 - rightArmAngle);
            }
            if (leftBodyAngle > 180) {
                leftBodyAngle = (360 - leftBodyAngle);
            }
            if (rightBodyAngle > 180) {
                rightBodyAngle = (360 - rightBodyAngle);
            }
            if (leftLowerBodyAngle > 180) {
                leftLowerBodyAngle = (360 - leftLowerBodyAngle);
            }
            if (rightLowerBodyAngle > 180) {
                rightLowerBodyAngle = (360 - rightLowerBodyAngle);
            }

            checkForm(leftBodyAngle);
            checkForm(rightBodyAngle);

            Log.i("testtt", "left: " + leftBodyAngle);
            Log.i("testtt", "right: " + rightBodyAngle);


            leftBodyAccuracy = calculateAccuracy(leftBodyAngle);
            rightBodyAccuracy = calculateAccuracy(rightBodyAngle);

            leftArmAccuracy = calculateAccuracyArm(leftArmAngle);
            rightArmAccuracy = calculateAccuracyArm(rightArmAngle);

            leftLowerBodyAccuracy = calculateAccuracy(leftLowerBodyAngle);
            rightLowerBodyAccuracy = calculateAccuracy(rightLowerBodyAngle);

            ArrayList<Double> tempAcc = new ArrayList<>();
            tempAcc.add(leftArmAccuracy);
            tempAcc.add(rightArmAccuracy);

            ArrayList<Double> tempAcc1 = new ArrayList<>();
            tempAcc1.add(leftBodyAccuracy);
            tempAcc1.add(rightBodyAccuracy);

            ArrayList<Double> tempAcc2 = new ArrayList<>();
            tempAcc2.add(leftLowerBodyAccuracy);
            tempAcc2.add(rightLowerBodyAccuracy);

            accuracies.add(tempAcc);
            accuracies.add(tempAcc1);
            accuracies.add(tempAcc2);
        }


        poseGraphic.draw(canvas, leftBodyAccuracy, rightBodyAccuracy, exercise, accuracies);
        accuracies.clear();
        leftBodyAccuracy = 0.0;
        rightBodyAccuracy = 0.0;
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
                isFirstArm = true;
                return;
            }

            // direction is from top to middle
            if (directionFrom == 1){
                didPass = true;
                curLoc = "MB";
                isFirst = true;
                isFirstArm = true;
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
                for (Double accuracy: finalAccuracy){
                    averageAccuracy += accuracy;
                }
                averageAccuracy /= finalAccuracy.size();

                LiveFeedExerciseModel model = new LiveFeedExerciseModel(millis, Double.parseDouble(decimalFormat.format(averageAccuracy)));
                presenter.addData(exercise, model, setNumber);
            }
        }
    }

    public double calculateAccuracy(double angleResult) {
        if (!curLoc.equals("") && (curLoc.equals("MB") || curLoc.equals("BM"))) {
            double accuracy = (100.0 - ((70.0 - angleResult) / 70.0) * 100.0);
            if (accuracy > 100.0) {
                double difference = accuracy - 100.00;
                accuracy = 100. - difference;
            }
            if (isFirst) {
                if (accuracy < 0) {
                    return accuracy;
                }
                finalAccuracy.add(accuracy);
                isFirst = false;
            }
            finalAccuracy.add(accuracy);
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
                finalAccuracy.add(accuracy);
                isFirst = false;
            }
            finalAccuracy.add(accuracy);
            return accuracy;
        }

        return 0.0;
    }

    public double calculateAccuracyArm(double angleResult) {
        double accuracy = (100.0 - ((175.0 - angleResult) / 175.0) * 100.0);
        if (accuracy > 100.0) {
            double difference = accuracy - 100.00;
            accuracy = 100. - difference;
        }
        if (isFirstArm) {
            if (accuracy < 0) {
                return accuracy;
            }
            finalAccuracy.add(accuracy);
            isFirstArm = false;
        }
        finalAccuracy.add(accuracy);
        return accuracy;
    }

    public void startCounter() {
        if (!hasStart) {
            hasStart = true;
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }
}
