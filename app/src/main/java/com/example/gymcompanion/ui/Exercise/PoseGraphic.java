package com.example.gymcompanion.ui.Exercise;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PoseGraphic extends GraphicOverlay.Graphic {

    private static final float DOT_RADIUS = 8.0f;
    private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f;
    private static final float STROKE_WIDTH = 10.0f;
    private static final float POSE_CLASSIFICATION_TEXT_SIZE = 60.0f;

    private final Pose pose;
    private final boolean showInFrameLikelihood;
    private final boolean visualizeZ;
    private final boolean rescaleZForVisualization;
    private float zMin = Float.MAX_VALUE;
    private float zMax = Float.MIN_VALUE;

    private final Paint classificationTextPaint;
    private final Paint leftPaint;
    private final Paint rightPaint;
    private final Paint whitePaint;

    public PoseGraphic(
            GraphicOverlay overlay,
            Pose pose,
            boolean showInFrameLikelihood,
            boolean visualizeZ,
            boolean rescaleZForVisualization) {
        super(overlay);
        this.pose = pose;
        this.showInFrameLikelihood = showInFrameLikelihood;
        this.visualizeZ = visualizeZ;
        this.rescaleZForVisualization = rescaleZForVisualization;

        classificationTextPaint = new Paint();
        classificationTextPaint.setColor(Color.WHITE);
        classificationTextPaint.setTextSize(POSE_CLASSIFICATION_TEXT_SIZE);
        classificationTextPaint.setShadowLayer(5.0f, 0f, 0f, Color.BLACK);

        whitePaint = new Paint();
        whitePaint.setStrokeWidth(STROKE_WIDTH);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);
        leftPaint = new Paint();
        leftPaint.setStrokeWidth(STROKE_WIDTH);
        leftPaint.setColor(Color.GREEN);
        rightPaint = new Paint();
        rightPaint.setStrokeWidth(STROKE_WIDTH);
        rightPaint.setColor(Color.YELLOW);
    }

    @Override
    public void draw(Canvas canvas, double leftAccuracy, double rightAccuracy, String exercise, ArrayList<ArrayList<Double>> accuracies) {
        List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
        if (landmarks.isEmpty()) {
            return;
        }

        // Draw all the points
        for (PoseLandmark landmark : landmarks) {
            drawPoint(canvas, landmark, whitePaint);
        }

        PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
        PoseLandmark leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER);
        PoseLandmark leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
        PoseLandmark leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER);
        PoseLandmark rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER);
        PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
        PoseLandmark rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER);
        PoseLandmark leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);
        PoseLandmark rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
        PoseLandmark leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH);
        PoseLandmark rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);

        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
        PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

        PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
        PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
        PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
        PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
        PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
        PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
        PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
        PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
        PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
        PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);

        // Face
        drawLine(canvas, nose, leftEyeInner, whitePaint);
        drawLine(canvas, leftEyeInner, leftEye, whitePaint);
        drawLine(canvas, leftEye, leftEyeOuter, whitePaint);
        drawLine(canvas, leftEyeOuter, leftEar, whitePaint);
        drawLine(canvas, nose, rightEyeInner, whitePaint);
        drawLine(canvas, rightEyeInner, rightEye, whitePaint);
        drawLine(canvas, rightEye, rightEyeOuter, whitePaint);
        drawLine(canvas, rightEyeOuter, rightEar, whitePaint);
        drawLine(canvas, leftMouth, rightMouth, whitePaint);

        drawLine(canvas, leftShoulder, rightShoulder, whitePaint);
        drawLine(canvas, leftHip, rightHip, whitePaint);

        if (exercise != null
                && (exercise.equals("Dumbbell Shoulder Press")
                || exercise.equals("Side Lateral Raises")
                || exercise.equals("Flat Bench Press")
                || exercise.equals("Incline Bench Press")
                || exercise.equals("Skull Crushers")
                || exercise.equals("Barbell Curls")
                || exercise.equals("Preacher Curls"))
                && leftAccuracy != 0.0
                && rightAccuracy != 0.0) {

            // Left body
            drawLineWithAccuracy(canvas, leftShoulder, leftElbow, leftPaint,leftAccuracy);
            drawLineWithAccuracy(canvas, leftElbow, leftWrist, leftPaint,leftAccuracy);
            drawLineWithAccuracy(canvas, leftWrist, leftThumb, leftPaint,leftAccuracy);
            drawLineWithAccuracy(canvas, leftWrist, leftPinky, leftPaint,leftAccuracy);
            drawLineWithAccuracy(canvas, leftWrist, leftIndex, leftPaint,leftAccuracy);
            drawLineWithAccuracy(canvas, leftIndex, leftPinky, leftPaint,leftAccuracy);

            drawLine(canvas, leftShoulder, leftHip, leftPaint);
            drawLine(canvas, leftHip, leftKnee, leftPaint);
            drawLine(canvas, leftKnee, leftAnkle, leftPaint);
            drawLine(canvas, leftAnkle, leftHeel, leftPaint);
            drawLine(canvas, leftHeel, leftFootIndex, leftPaint);

            // Right body
            drawLineWithAccuracy(canvas, rightShoulder, rightElbow, rightPaint, rightAccuracy);
            drawLineWithAccuracy(canvas, rightElbow, rightWrist, rightPaint, rightAccuracy);
            drawLineWithAccuracy(canvas, rightWrist, rightThumb, rightPaint, rightAccuracy);
            drawLineWithAccuracy(canvas, rightWrist, rightPinky, rightPaint, rightAccuracy);
            drawLineWithAccuracy(canvas, rightWrist, rightIndex, rightPaint, rightAccuracy);
            drawLineWithAccuracy(canvas, rightIndex, rightPinky, rightPaint, rightAccuracy);

            drawLine(canvas, rightShoulder, rightHip, rightPaint);
            drawLine(canvas, rightHip, rightKnee, rightPaint);
            drawLine(canvas, rightKnee, rightAnkle, rightPaint);
            drawLine(canvas, rightAnkle, rightHeel, rightPaint);
            drawLine(canvas, rightHeel, rightFootIndex, rightPaint);
        }

        else if (exercise != null && (exercise.equals("Deadlift") || exercise.equals("Barbell Rows")) && leftAccuracy != 0.0 && rightAccuracy != 0.0 && accuracies.size() == 3){

            // Left arm
            drawLineWithAccuracy(canvas, leftShoulder, leftElbow, leftPaint,accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, leftElbow, leftWrist, leftPaint,accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, leftWrist, leftThumb, leftPaint,accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, leftWrist, leftPinky, leftPaint,accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, leftWrist, leftIndex, leftPaint,accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, leftIndex, leftPinky, leftPaint,accuracies.get(0).get(1));

            // left body
            drawLineWithAccuracy(canvas, leftShoulder, leftHip, leftPaint, accuracies.get(1).get(0));
            drawLineWithAccuracy(canvas, leftHip, leftKnee, leftPaint, accuracies.get(2).get(0));
            drawLineWithAccuracy(canvas, leftKnee, leftAnkle, leftPaint, accuracies.get(2).get(0));
            drawLine(canvas, leftAnkle, leftHeel, leftPaint);
            drawLine(canvas, leftHeel, leftFootIndex, leftPaint);

            // Right arm
            drawLineWithAccuracy(canvas, rightShoulder, rightElbow, rightPaint, accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, rightElbow, rightWrist, rightPaint, accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, rightWrist, rightThumb, rightPaint, accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, rightWrist, rightPinky, rightPaint, accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, rightWrist, rightIndex, rightPaint, accuracies.get(0).get(1));
            drawLineWithAccuracy(canvas, rightIndex, rightPinky, rightPaint, accuracies.get(0).get(1));

            // right body
            drawLineWithAccuracy(canvas, rightShoulder, rightHip, rightPaint, accuracies.get(1).get(1));
            drawLineWithAccuracy(canvas, rightHip, rightKnee, rightPaint, accuracies.get(2).get(1));
            drawLineWithAccuracy(canvas, rightKnee, rightAnkle, rightPaint, accuracies.get(2).get(1));
            drawLine(canvas, rightAnkle, rightHeel, rightPaint);
            drawLine(canvas, rightHeel, rightFootIndex, rightPaint);
        }
        else if (exercise != null && (exercise.equals("Squats") && leftAccuracy != 0.0 && rightAccuracy != 0.0)) {
            // Left arm
            drawLine(canvas, leftShoulder, leftElbow, leftPaint);
            drawLine(canvas, leftElbow, leftWrist, leftPaint);
            drawLine(canvas, leftWrist, leftThumb, leftPaint);
            drawLine(canvas, leftWrist, leftPinky, leftPaint);
            drawLine(canvas, leftWrist, leftIndex, leftPaint);
            drawLine(canvas, leftIndex, leftPinky, leftPaint);

            // left body
            drawLine(canvas, leftShoulder, leftHip, leftPaint);
            drawLineWithAccuracy(canvas, leftHip, leftKnee, leftPaint, leftAccuracy);
            drawLineWithAccuracy(canvas, leftKnee, leftAnkle, leftPaint, leftAccuracy);
            drawLine(canvas, leftAnkle, leftHeel, leftPaint);
            drawLine(canvas, leftHeel, leftFootIndex, leftPaint);

            // Right arm
            drawLine(canvas, rightShoulder, rightElbow, rightPaint);
            drawLine(canvas, rightElbow, rightWrist, rightPaint);
            drawLine(canvas, rightWrist, rightThumb, rightPaint);
            drawLine(canvas, rightWrist, rightPinky, rightPaint);
            drawLine(canvas, rightWrist, rightIndex, rightPaint);
            drawLine(canvas, rightIndex, rightPinky, rightPaint);

            // right body
            drawLine(canvas, rightShoulder, rightHip, rightPaint);
            drawLineWithAccuracy(canvas, rightHip, rightKnee, rightPaint, rightAccuracy);
            drawLineWithAccuracy(canvas, rightKnee, rightAnkle, rightPaint, rightAccuracy);
            drawLine(canvas, rightAnkle, rightHeel, rightPaint);
            drawLine(canvas, rightHeel, rightFootIndex, rightPaint);
        }
        else {
            // Left body
            drawLine(canvas, leftShoulder, leftElbow, leftPaint);
            drawLine(canvas, leftElbow, leftWrist, leftPaint);
            drawLine(canvas, leftWrist, leftThumb, leftPaint);
            drawLine(canvas, leftWrist, leftPinky, leftPaint);
            drawLine(canvas, leftWrist, leftIndex, leftPaint);
            drawLine(canvas, leftIndex, leftPinky, leftPaint);

            drawLine(canvas, leftShoulder, leftHip, leftPaint);
            drawLine(canvas, leftHip, leftKnee, leftPaint);
            drawLine(canvas, leftKnee, leftAnkle, leftPaint);
            drawLine(canvas, leftAnkle, leftHeel, leftPaint);
            drawLine(canvas, leftHeel, leftFootIndex, leftPaint);

            // Right body
            drawLine(canvas, rightShoulder, rightElbow, rightPaint);
            drawLine(canvas, rightElbow, rightWrist, rightPaint);
            drawLine(canvas, rightWrist, rightThumb, rightPaint);
            drawLine(canvas, rightWrist, rightPinky, rightPaint);
            drawLine(canvas, rightWrist, rightIndex, rightPaint);
            drawLine(canvas, rightIndex, rightPinky, rightPaint);

            drawLine(canvas, rightShoulder, rightHip, rightPaint);
            drawLine(canvas, rightHip, rightKnee, rightPaint);
            drawLine(canvas, rightKnee, rightAnkle, rightPaint);
            drawLine(canvas, rightAnkle, rightHeel, rightPaint);
            drawLine(canvas, rightHeel, rightFootIndex, rightPaint);
        }

        // Draw inFrameLikelihood for all points
        if (showInFrameLikelihood) {
            for (PoseLandmark landmark : landmarks) {
                canvas.drawText(
                        String.format(Locale.US, "%.2f", landmark.getInFrameLikelihood()),
                        translateX(landmark.getPosition().x),
                        translateY(landmark.getPosition().y),
                        whitePaint);
            }
        }
    }

    void drawPoint(Canvas canvas, PoseLandmark landmark, Paint paint) {
        PointF3D point = landmark.getPosition3D();
        updatePaintColorByZValue(
                paint, canvas, visualizeZ, rescaleZForVisualization, -1.0, zMin, zMax);

        canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), DOT_RADIUS, paint);
    }

    void drawLine(Canvas canvas, PoseLandmark startLandmark, PoseLandmark endLandmark, Paint paint) {
        PointF3D start = startLandmark.getPosition3D();
        PointF3D end = endLandmark.getPosition3D();

        // Gets average z for the current body line
        updatePaintColorByZValue(
                paint, canvas, visualizeZ, rescaleZForVisualization, 0, zMin, zMax);

        canvas.drawLine(
                translateX(start.getX()),
                translateY(start.getY()),
                translateX(end.getX()),
                translateY(end.getY()),
                paint);
    }

    void drawLineWithAccuracy(Canvas canvas, PoseLandmark startLandmark, PoseLandmark endLandmark, Paint paint, double accuracy) {
        PointF3D start = startLandmark.getPosition3D();
        PointF3D end = endLandmark.getPosition3D();

        updatePaintColorByZValue(
                paint, canvas, visualizeZ, rescaleZForVisualization, accuracy, zMin, zMax);

        canvas.drawLine(
                translateX(start.getX()),
                translateY(start.getY()),
                translateX(end.getX()),
                translateY(end.getY()),
                paint);
    }





}