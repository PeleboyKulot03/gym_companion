package com.example.gymcompanion.ui.exercise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymcompanion.R;
import com.warnyul.android.widget.FastVideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class PreviewVideoActivity extends AppCompatActivity {
    private LinearLayout controls;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private boolean onRepeat = false;
    private File dir;
    private String path;
    private boolean isSave = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        path = uri.getPath();
        dir = new File(path);

        FastVideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(uri);
        controls = findViewById(R.id.controls);
        ImageView play = findViewById(R.id.play);
        ImageView forward = findViewById(R.id.forward);
        ImageView rewind = findViewById(R.id.rewind);
        ImageView download = findViewById(R.id.download);
        ImageView repeat = findViewById(R.id.repeat);
        TextView currentTime = findViewById(R.id.currentTime);
        TextView duration = findViewById(R.id.duration);


        seekBar = findViewById(R.id.seekbar);

        videoView.setOnTouchListener((v, event) -> {
            if (controls.getVisibility() == View.VISIBLE){
                controls.setVisibility(View.GONE);
                return false;
            }
            controls.setVisibility(View.VISIBLE);
            return false;
        });

        play.setOnClickListener(view -> {
            if(videoView.isPlaying()){
                videoView.pause();
                play.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_play_circle_24));
                controls.setVisibility(View.GONE);
                return;
            }
            videoView.start();
            play.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_pause_circle_24));
            controls.setVisibility(View.GONE);
        });

        forward.setOnClickListener(v -> {
            videoView.seekTo(Math.min(videoView.getDuration(), videoView.getCurrentPosition() + 1000));
            int mCurrentPosition = videoView.getCurrentPosition() / 1000;
            runOnUiThread(() -> {
                seekBar.setProgress(mCurrentPosition);
                currentTime.setText(splitTime(videoView.getCurrentPosition()));
            });
        });
        rewind.setOnClickListener(v -> {
            videoView.seekTo(Math.max(0, videoView.getCurrentPosition() - 1000));
            int mCurrentPosition = videoView.getCurrentPosition() / 1000;
            runOnUiThread(() -> {
                seekBar.setProgress(mCurrentPosition);
                currentTime.setText(splitTime(videoView.getCurrentPosition()));
            });
        });

        videoView.setOnPreparedListener(mp -> {
            int maxTime = mp.getDuration() / 1000;
            seekBar.setMax(maxTime);
            duration.setText(splitTime(videoView.getDuration()));
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int mCurrentPosition = videoView.getCurrentPosition() / 1000;
                seekBar.setProgress(mCurrentPosition);
                currentTime.setText(splitTime(videoView.getCurrentPosition()));
                handler.postDelayed(this, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress * 1000);
                    currentTime.setText(splitTime(videoView.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        repeat.setOnClickListener(v -> {
            if (onRepeat) {
                repeat.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                onRepeat = !onRepeat;
                return;
            }
            repeat.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
            onRepeat = !onRepeat;
        });
        videoView.setOnCompletionListener(mp -> {
            if (onRepeat) {
                mp.seekTo(0);
                mp.start();
                return;
            }
            play.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_play_circle_24));
        });

        download.setOnClickListener(view -> {
            if (!isSave) {
                saveVideo();
                return;
            }
            Toast.makeText(this, "This video is already saved!", Toast.LENGTH_SHORT).show();

        });

    }
    public static String splitTime(int ms) {
        int ss = ms / 1000;
        int mm = ss / 60;
        ss = ss - (mm * 60);

        return ((mm < 9) ? "0" + mm: mm) + ":" + ((ss < 9) ? "0" + ss: ss);
    }


    @Override
    public void onBackPressed() {
        if (controls.getVisibility() == View.VISIBLE){
            controls.setVisibility(View.GONE);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(PreviewVideoActivity.this);
        builder.setTitle("Returning Notice");
        builder.setMessage("Are you sure you want to go back?");
        builder.setPositiveButton("Yes", (dialog, which) -> super.onBackPressed());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
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
                isSave = true;
            }
            else {
                Toast.makeText(this, "Video saving failed. Source file are missing!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}