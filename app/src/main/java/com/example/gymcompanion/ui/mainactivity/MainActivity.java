package com.example.gymcompanion.ui.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.gymcompanion.R;
import com.example.gymcompanion.tutorial_activity;
import com.example.gymcompanion.ui.SavedVideos.SaveVideosActivity;
import com.example.gymcompanion.ui.exercise.ExercisePageActivity2;
import com.example.gymcompanion.ui.exercise.ExercisePageActivityVideoRecording;
import com.example.gymcompanion.ui.homepage.HomePageActivity;
import com.example.gymcompanion.ui.loginpage.LoginPageActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

//        String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/SavedVideos";
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//
//        assert files != null;
//        for (File file: files){
//            Log.i("tegggggg", "test: " + file.getAbsoluteFile());
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
            finish();
            return;
        }
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
    }
}