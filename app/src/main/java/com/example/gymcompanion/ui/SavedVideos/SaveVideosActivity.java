package com.example.gymcompanion.ui.SavedVideos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.gymcompanion.R;

import java.io.File;
import java.util.ArrayList;

public class SaveVideosActivity extends AppCompatActivity {
    private ArrayList<Uri> uris;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_videos);
        uris = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        getSavedVideos();
        SaveVideosAdapter saveVideosAdapter = new SaveVideosAdapter(uris, getApplicationContext(), SaveVideosActivity.this);
        recyclerView.setAdapter(saveVideosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void getSavedVideos() {
        String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/SavedVideos";
        File directory = new File(path);
        File[] files = directory.listFiles();

        assert files != null;
        for (File file: files){
            Uri uri = Uri.fromFile(file);
            Log.i("ttttt", "getSavedVideos: " + file.getName());
            uris.add(uri);
        }

        Log.i("TAG", "getSavedVideos: " + uris.size());
    }
}