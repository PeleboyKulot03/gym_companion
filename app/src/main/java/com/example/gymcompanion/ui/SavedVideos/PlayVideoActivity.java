package com.example.gymcompanion.ui.SavedVideos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import com.example.gymcompanion.R;

public class PlayVideoActivity extends AppCompatActivity {
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        mediaController = new MediaController(this);
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
    }

    @Override
    public void onBackPressed() {
        if (mediaController.isShowing()) {
            mediaController.hide();
            return;
        }
        super.onBackPressed();

    }
}