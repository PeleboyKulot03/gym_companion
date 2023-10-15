package com.example.gymcompanion.ui.SavedVideos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import com.example.gymcompanion.R;
import com.warnyul.android.widget.FastVideoView;

public class PlayVideoActivity extends AppCompatActivity {
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        mediaController = new MediaController(this);
        FastVideoView videoView = findViewById(R.id.videoView);
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