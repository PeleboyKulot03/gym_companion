package com.example.gymcompanion.ui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.exercise.GraphicOverlay;
import com.example.gymcompanion.ui.exercise.MoviePlayer;
import com.example.gymcompanion.ui.exercise.PoseGraphic;
import com.example.gymcompanion.ui.exercise.SpeedControlCallback;
import com.example.gymcompanion.ui.registration.RegistrationPageActivity;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class testActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, MoviePlayer.PlayerFeedback {
    private PoseDetector poseDetector;
    private MoviePlayer.PlayTask mPlayTask = null;
    private TextureView mTextureView;
    private ImageView imgView, download, back;
    private File dir;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();

        if (intent != null) {
            path = intent.getStringExtra("path");
        }

        imgView = findViewById(R.id.imgView);
        mTextureView = findViewById(R.id.mTextureView);
        download = findViewById(R.id.download);
        back = findViewById(R.id.back);

        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setRotationY(180);
        PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build();


        dir = new File(getCacheDir(), path);
        poseDetector = PoseDetection.getClient(options);

        download.setOnClickListener(view -> saveVideo());
    }

    private void clickPlayStop(File file) {
        if (mPlayTask != null) {
            return;
        }

        SpeedControlCallback callback = new SpeedControlCallback();

        SurfaceTexture st = mTextureView.getSurfaceTexture();
        Surface surface = new Surface(st);



        if(file != null){
            MoviePlayer player = null;
            try {
                player = new MoviePlayer(file, surface, callback);
            } catch (IOException e) {
                surface.release();
                throw new RuntimeException(e);
            }
//            adjustAspectRatio(player.getVideoWidth(), player.getVideoHeight());
            mPlayTask = new MoviePlayer.PlayTask(player, this);
            mPlayTask.setLoopMode(true);
            mPlayTask.execute();
        }

    }

    private void adjustAspectRatio(int videoWidth, int videoHeight) {
        int viewWidth = mTextureView.getWidth();
        int viewHeight = mTextureView.getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;
        int newWidth;
        int newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            // limited by narrow width; restrict height
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            // limited by short height; restrict width
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xoff = (viewWidth - newWidth) / 2;
        int yoff = (viewHeight - newHeight) / 2;
        Matrix txform = new Matrix();
        mTextureView.getTransform(txform);
        txform.setScale(
                (float) newWidth / viewWidth,
                (float) newHeight / viewHeight
        );
        //txform.postRotate(10);          // just for fun
        txform.postTranslate((float) xoff, (float) yoff);
        mTextureView.setTransform(txform);

    }


    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        Bitmap bm = mTextureView.getBitmap();
        imgView.setRotationY(180);
        imgView.setImageBitmap(bm);
        clickPlayStop(dir);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
        Bitmap bitmapImage = mTextureView.getBitmap();

        if (bitmapImage != null && mTextureView.isAvailable()) {
            InputImage inputImage = InputImage.fromBitmap(bitmapImage, 0);
            poseDetector.process(inputImage)
                    .addOnSuccessListener(pose -> {
                                GraphicOverlay overlay = new GraphicOverlay(getApplicationContext());
                                PoseGraphic poseGraphic = new PoseGraphic(overlay, pose, true, false, true);
                                Paint paint = new Paint();
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setColor(getColor(R.color.black));
                                paint.setStrokeWidth(5);
                                paint.setTextSize(30);

                                Canvas canvas = new Canvas(bitmapImage);

                                poseGraphic.draw(canvas);
                                imgView.setImageBitmap(bitmapImage);

                    }).
                    addOnFailureListener(e -> Toast.makeText(testActivity.this, "Sorry but " + e.getLocalizedMessage() + ". Please Try again later!", Toast.LENGTH_SHORT).show());
        }

    }

    private void saveVideo() {
        File newfile;

        try {

            String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis()) + ".mp4";
            String path = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "SavedVideos";
            File outputDir = new File(path);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            newfile = new File(outputDir, fileName);

            if(dir.exists()) {
                InputStream in = new FileInputStream(dir);
                OutputStream out = new FileOutputStream(newfile);

                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    Log.i("taagg", "saveVideo: " + len);
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();

                Toast.makeText(this, "Saving video successfully!", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Video saving failed. Source file are missing!", Toast.LENGTH_SHORT).show();
            }
    } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void stopPlayback() {
        mPlayTask.requestStop();
    }

    @Override
    public void playbackStopped() {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(testActivity.this);
        builder.setTitle("Warning Notice")
                .setMessage("Are you sure you want to go back?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    dir.delete();
                    super.onBackPressed();
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayTask != null) {
            stopPlayback();
            mPlayTask.waitForStop();
            mPlayTask = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()){
            clickPlayStop(dir);
            Log.i("tegleeee", "otes ");
        }
    }


}