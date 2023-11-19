package com.example.gymcompanion.ui.exercise;

import static java.lang.Math.atan2;
import static java.lang.Math.max;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.ReturnCode;
import com.example.gymcompanion.R;
import com.example.gymcompanion.staticValues.DifferentExercise;
import com.example.gymcompanion.ui.SavedVideos.PlayVideoActivity;
import com.example.gymcompanion.ui.exercise.decoders.Frame;
import com.example.gymcompanion.ui.exercise.decoders.FrameExtractor;
import com.example.gymcompanion.ui.exercise.decoders.IVideoFrameExtractor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExercisePageActivityVideoRecording extends AppCompatActivity implements IVideoFrameExtractor {

    ExecutorService service;
    Recording recording = null;
    VideoCapture<Recorder> videoCapture = null;
    private Button finishSet;
    private PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_FRONT;
    private PoseDetector poseDetector;
    private File outputFile, outputDir;
    private LinearLayout customLoading;
    private TextView progressText;
    private ProgressBar progressBar;

    private AnimationDrawable anim;
    private File dir;
    private final String filePrefix = "picture";
    private final String fileExtn = ".jpeg";
    int count = 1;
    private String direction = "";
    private int directionFrom = 0;
    private boolean didPass = false;
    private boolean onHold = true;
    private ArrayList<ArrayList<Integer>> bottomToMiddle = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> middleToTop = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> topToMiddle = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> middleToBottom = new ArrayList<>();
    private ArrayList<Integer> tempHolder = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    boolean isDone = true;
    boolean isFileSaving = false;
    int i = 0;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ActivityCompat.checkSelfPermission(ExercisePageActivityVideoRecording.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera(cameraFacing);
        }
    });
    private File src;
    private int counter = 0;
    private int testCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page_video_recording);

        Intent intent = getIntent();
        String exercise = intent.getStringExtra("exercise");

        dir = getApplicationContext().getFilesDir();

        ImageView actionImage = findViewById(R.id.action_image);
        previewView = findViewById(R.id.viewFinder);
        finishSet = findViewById(R.id.finishSet);
        progressText = findViewById(R.id.progress_text);
        customLoading = findViewById(R.id.custom_loading);
        progressBar = findViewById(R.id.progressBar);

        actionImage.setBackgroundResource(R.drawable.tutorial_animation);

        DifferentExercise differentExercise = new DifferentExercise(getApplicationContext());
        Map<String, ArrayList<Drawable>> DRAWABLES = new HashMap<>(differentExercise.getDRAWABLES());

        finishSet.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(ExercisePageActivityVideoRecording.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.CAMERA);
            } else if (ActivityCompat.checkSelfPermission(ExercisePageActivityVideoRecording.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.RECORD_AUDIO);
            } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && ActivityCompat.checkSelfPermission(ExercisePageActivityVideoRecording.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                try {
                    captureVideo();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(ExercisePageActivityVideoRecording.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }

        PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build();

        poseDetector = PoseDetection.getClient(options);

        service = Executors.newSingleThreadExecutor();
        anim = (AnimationDrawable) actionImage.getBackground();
        for (Drawable frame: Objects.requireNonNull(DRAWABLES.get(exercise))){
            anim.addFrame(frame, 800);
        }

    }

    public void captureVideo() throws IOException {
        finishSet.setText(getString(R.string.finish_set));
        Recording recording1 = recording;
        if (recording1 != null) {
            recording1.stop();
            recording = null;
            return;
        }

        outputDir = getApplicationContext().getCacheDir();
        outputFile = File.createTempFile("temp", ".mp4", outputDir);

        FileOutputOptions option = new FileOutputOptions.Builder(outputFile).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        recording = videoCapture
                .getOutput()
                .prepareRecording(ExercisePageActivityVideoRecording.this, option)
                .withAudioEnabled()
                .start(ContextCompat.getMainExecutor(ExercisePageActivityVideoRecording.this),
                        videoRecordEvent -> {
                            if (videoRecordEvent instanceof VideoRecordEvent.Start) {
                                finishSet.setEnabled(true);
                            }
                            else if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                                VideoRecordEvent.Finalize finalizeEvent = (VideoRecordEvent.Finalize) videoRecordEvent;
                                if (!finalizeEvent.hasError()) {
                                    service.execute(() -> {
                                        FrameExtractor frameExtractor = new FrameExtractor(ExercisePageActivityVideoRecording.this);
                                        frameExtractor.extractFrames(finalizeEvent.getOutputResults().getOutputUri().getPath());
                                    });

                                    customLoading.setVisibility(View.VISIBLE);
                                    runOnUiThread(() -> {
                                        anim.start();
                                        progressText.setText(getString(R.string.extraction));
                                    });
                                }
                                else {
                                    recording.close();
                                    recording = null;
                                    String msg = "Error: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getError();
                                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    public void startCamera(int cameraFacing) {
        ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(ExercisePageActivityVideoRecording.this);

        processCameraProvider.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = processCameraProvider.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HD))
                        .build();

                videoCapture = VideoCapture.withOutput(recorder);

                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(ExercisePageActivityVideoRecording.this));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        service.shutdown();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        service.shutdown();
    }

    @Override
    public void onCurrentFrameExtracted(@NonNull Frame currentFrame) {
        Bitmap result = Bitmap.createBitmap(currentFrame.getWidth(), currentFrame.getHeight(), Bitmap.Config.ARGB_8888);
        Buffer buffer = currentFrame.getByteBuffer();
        buffer.rewind();
        result.copyPixelsFromBuffer(buffer);
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        Bitmap outputBitmap = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
        outputBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        bitmaps.add(outputBitmap);
        Log.i("tagerista", "onCurrentFrameExtracted: " + testCount++);

    }

    @Override
    public void onAllFrameExtracted(int processedFrameCount, Long processedTimeMs) {
        Log.i("tagerista", "onAllFrameExtracted: " + testCount);
        Log.i("tagerista", "before pose detection");
        addPoseDetections();
        Log.i("tagerista", "after pose detection");

        DifferentExercise differentExercise = new DifferentExercise(getApplicationContext());
        int max = differentExercise.getShoulderPressBottomToMiddleMax();
        int min = differentExercise.getShoulderPressBottomToMiddleMin();
        ArrayList<Boolean> accuracy = new ArrayList<>();
        for (ArrayList<Integer> angles: bottomToMiddle) {
            for (Integer angle: angles){
                if (angle >= min && angle <= max){
                    accuracy.add(true);
                    continue;
                }
                accuracy.add(false);
            }
        }
        Log.i("testing", "bottom to middles: " + bottomToMiddle);

        Log.i("testing", "bottom to middle: " + accuracy);
        runOnUiThread(() -> {
            progressBar.setProgress(100);
            progressText.setText(getString(R.string.second_phase));
            if (processedFrameCount != 0) {
                int videoLength = MediaPlayer.create(getApplicationContext(), Uri.fromFile(outputFile)).getDuration();
                if (outputFile.delete()){
                    try {
                        outputFile = File.createTempFile("temp", ".mp4", outputDir);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    try {
                        outputFile = File.createTempFile("temp1", ".mp4", outputDir);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                String[] complexCommand = new String[]{"-i", src.getParent() + "/picture%07d.jpeg", "-s", "1920x1080", "-y", outputFile.getAbsolutePath()};


                FFmpegKitConfig.enableStatisticsCallback(statistics -> {
                    float progress = Float.parseFloat(String.valueOf(statistics.getTime())) / videoLength;
                    float progressFinal = progress * 100;
                    progressBar.setProgress((int) progressFinal);
                });

                FFmpegKit.executeWithArgumentsAsync(complexCommand, session -> {
                    if (ReturnCode.isSuccess(session.getReturnCode())) {
                        Intent intent = new Intent(getApplicationContext(), PreviewVideoActivity.class);
                        Uri uri = Uri.fromFile(outputFile);
                        intent.putExtra("uri", uri.toString());
                        startActivity(intent);
                        finish();
                    } else if (ReturnCode.isCancel(session.getReturnCode())) {
                        Toast.makeText(this, "Extraction is cancelled! Please try again later!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("teggg", String.format("Command failed with state %s and rc %s.%s", session.getState(), session.getReturnCode(), session.getFailStackTrace()));
                    }
                    String path = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "TempPictures";
                    File s = new File(path);
                    File[] test = s.listFiles();
                    assert test != null;
                    for (File file : test) {
                        if (!file.delete()){
                            Log.i("TAG", "deletion failed");
                        }
                    }
                });
            }
        });


    }

    private void addPoseDetections() {
        while (i < bitmaps.size()){
            if (isDone) {
                isDone = false;
                Bitmap outputBitmap = bitmaps.get(i);

                InputImage inputImage = InputImage.fromBitmap(outputBitmap, 0);
                poseDetector.process(inputImage).addOnCompleteListener(pose -> {
                    if (pose.isComplete()){
                        GraphicOverlay overlay = new GraphicOverlay(getApplicationContext());
                        PoseGraphic poseGraphic = new PoseGraphic(overlay, pose.getResult(), true, false, true);
                        Canvas canvas = new Canvas(outputBitmap);
                        PoseLandmark firstPoint = pose.getResult().getPoseLandmark(PoseLandmark.LEFT_WRIST);
                        PoseLandmark midPoint = pose.getResult().getPoseLandmark(PoseLandmark.LEFT_ELBOW);
                        PoseLandmark lastPoint = pose.getResult().getPoseLandmark(PoseLandmark.LEFT_SHOULDER);

                        if ((firstPoint != null) && (midPoint != null) && (lastPoint != null) && (firstPoint.getPosition().y < midPoint.getPosition().y)) {
                            int angleResult = (int) Math.toDegrees(
                                    atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                            lastPoint.getPosition().x - midPoint.getPosition().x)
                                            - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                            firstPoint.getPosition().x - midPoint.getPosition().x));

                            if (angleResult > 180) {
                                angleResult = (360 - angleResult);
                            }

                            angleResult = Math.abs(angleResult);
                            Log.i("Tagerista", "Angle Result: " + angleResult);

                            countReps(angleResult);
                            checkForm(angleResult);
                        }
                        poseGraphic.draw(canvas);
                        String childName = filePrefix + String.format(Locale.getDefault(), "%07d", count) + fileExtn;
                        String path = dir.getAbsolutePath() + File.separator + "TempPictures";
                        src = new File(path, childName);
                        try (FileOutputStream out = new FileOutputStream(src)) {
                            outputBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            count++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            Log.i("tagerista", "detection complete at frame: " + i);
                            i++;
                            isDone = true;
                        }
                        runOnUiThread(() -> {
                            if (progressBar.getProgress() < 90) {
                                progressBar.setProgress(progressBar.getProgress() + 1);
                            }
                        });
                    }

                    if (pose.isCanceled()) {
                        isDone = true;
                    }
                }).addOnFailureListener(e -> {
                    isDone = true;
                    Log.i("tagerista", "addPoseDetections: " + e.getLocalizedMessage());
                });
            }
        }
    }

    private void countReps(int angleResult) {
        // reached the down state
        if (angleResult < 70) {
            onHold = false;
        }

        // reach the upstate
        if (angleResult > 150 && !onHold) {
            String tempText = getString(R.string.count_0) + (counter+1);
            onHold = true;
            Log.i("ggggg", tempText);
        }
    }
    private void checkForm(int angleResult) {
        // the down state is reached update the direction
        if (angleResult < 70 && didPass){
            middleToBottom.add(tempHolder);
            tempHolder = new ArrayList<>();
            directionFrom = 0;
            didPass = false;
            direction = "hit the down state";
            Log.i("testing", direction);
        }

        // middle point is reached
        if (angleResult >= 90 && angleResult <= 110 && !didPass){
            if (directionFrom == 0){
                bottomToMiddle.add(tempHolder);
                direction = "passed the middle while going up";
            }
            if (directionFrom == 1){
                topToMiddle.add(tempHolder);
                direction = "passed the middle while going down";
            }
            tempHolder = new ArrayList<>();
            didPass = true;
            Log.i("testing", direction);
        }

        // upstate reached
        if (angleResult > 150 && didPass) {
            middleToTop.add(tempHolder);
            tempHolder = new ArrayList<>();
            directionFrom = 1;
            didPass = false;
            direction = "hit the up state";
            Log.i("testing", direction);
        }
        tempHolder.add(angleResult);
        Log.i("ggggg", "Angle Result: " + angleResult);
    }
}