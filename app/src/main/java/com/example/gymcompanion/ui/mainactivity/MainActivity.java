package com.example.gymcompanion.ui.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.ReturnCode;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.exercise.Extractor;
import com.example.gymcompanion.ui.homepage.HomePageActivity;
import com.example.gymcompanion.ui.loginpage.LoginPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
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