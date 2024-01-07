package com.example.gymcompanion.ui.Exercise;

import static java.lang.Math.atan2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.ReturnCode;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.Exercise.decoders.Frame;
import com.example.gymcompanion.ui.Exercise.decoders.FrameExtractor;
import com.example.gymcompanion.ui.Exercise.decoders.IVideoFrameExtractor;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Extractor extends AppCompatActivity implements IVideoFrameExtractor {

    private ExecutorService service;
    private final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    private PoseDetector poseDetector;
    private final String filePrefix = "picture";
    private final String fileExtn = ".jpeg";
    private int count = 1, counter =0;
    private File dir;
    private File src;
    private File outputFile, outputDir;
    private boolean onHold = true;

    private String direction = "";
    private int directionFrom = 0;
    private boolean didPass = false;
    private ArrayList<ArrayList<Integer>> bottomToMiddle = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> middleToTop = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> topToMiddle = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> middleToBottom = new ArrayList<>();
    private ArrayList<Integer> tempHolder = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extractor);
        Button button = findViewById(R.id.button);
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        dir = getApplicationContext().getFilesDir();

        PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build();

        poseDetector = PoseDetection.getClient(options);

        service = Executors.newSingleThreadExecutor();

        button.setOnClickListener(v -> {
            startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);
                if (selectedImagePath != null) {
                    service.execute(() -> {
                        FrameExtractor frameExtractor = new FrameExtractor(Extractor.this);
                        frameExtractor.extractFrames(selectedImagePath);
                    });
                }
            }
        }
    }

    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public void onCurrentFrameExtracted(Frame currentFrame) {
        Bitmap result = Bitmap.createBitmap(currentFrame.getWidth(), currentFrame.getHeight(), Bitmap.Config.ARGB_8888);
        Buffer buffer = currentFrame.getByteBuffer();
        buffer.rewind();
        result.copyPixelsFromBuffer(buffer);
        Bitmap outputBitmap = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight());
        outputBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);

        InputImage inputImage = InputImage.fromBitmap(outputBitmap, 0);
        poseDetector.process(inputImage).addOnSuccessListener(pose -> {
            GraphicOverlay overlay = new GraphicOverlay(getApplicationContext());
            PoseGraphic poseGraphic = new PoseGraphic(overlay, pose, true, false, true);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getColor(R.color.black));
            paint.setStrokeWidth(5);
            paint.setTextSize(30);

            Canvas canvas = new Canvas(outputBitmap);

            PoseLandmark firstPoint = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
            PoseLandmark midPoint = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
            PoseLandmark lastPoint = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);

            if (firstPoint != null && midPoint != null && lastPoint != null && firstPoint.getPosition().y < lastPoint.getPosition().y) {
                int angleResult =
                        (int) Math.toDegrees(
                                atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                        lastPoint.getPosition().x - midPoint.getPosition().x)
                                        - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                        firstPoint.getPosition().x - midPoint.getPosition().x));
                angleResult = Math.abs(angleResult);

                if (angleResult > 180) {
                    angleResult = (360 - angleResult);
                }

                countReps(angleResult);
                checkForm(angleResult);
            }
//            poseGraphic.draw(canvas);
            String childName = filePrefix + String.format(Locale.getDefault(), "%07d", count) + fileExtn;
            String path = dir.getAbsolutePath() + File.separator + "TempPictures";
            src = new File(path, childName);
            try (FileOutputStream out = new FileOutputStream(src)) {
                outputBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

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

    @Override
    public void onAllFrameExtracted(int processedFrameCount, Long processedTimeMs) {
        Log.i("testing", "bottom to middle" + bottomToMiddle);
        Log.i("testing", "middle to top" + middleToTop);
        Log.i("testing", "top to middle" + topToMiddle);
        Log.i("testing", "middle to bottom" +  middleToBottom);

        outputDir = getApplicationContext().getCacheDir();
        try {
            outputFile = File.createTempFile("temp", ".mp4", outputDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (processedFrameCount != 0) {
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
    }
}