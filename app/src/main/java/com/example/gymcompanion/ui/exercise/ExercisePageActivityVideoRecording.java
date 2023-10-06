package com.example.gymcompanion.ui.exercise;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FallbackStrategy;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.MediaStoreOutputOptions;
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
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.testActivity;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExercisePageActivityVideoRecording extends AppCompatActivity {

    ExecutorService service;
    Recording recording = null;
    VideoCapture<Recorder> videoCapture = null;
    private Button finishSet;
    private PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_FRONT;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ActivityCompat.checkSelfPermission(ExercisePageActivityVideoRecording.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera(cameraFacing);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page_video_recording);
        previewView = findViewById(R.id.viewFinder);
        finishSet = findViewById(R.id.finishSet);

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


        service = Executors.newSingleThreadExecutor();
    }

    public void captureVideo() throws IOException {
        finishSet.setText(getString(R.string.finish_set));
        Recording recording1 = recording;
        if (recording1 != null) {
            recording1.stop();
            recording = null;
            return;
        }

        File outputDir = getApplicationContext().getCacheDir();
        File outputFile = File.createTempFile("temp", ".mp4", outputDir);

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

                                    String path = ((VideoRecordEvent.Finalize) videoRecordEvent).getOutputResults().getOutputUri().getLastPathSegment();
                                    Intent intent = new Intent(getApplicationContext(), testActivity.class);
                                    intent.putExtra("path", path);
                                    startActivity(intent);
                                    finish();

                                } else {
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
}