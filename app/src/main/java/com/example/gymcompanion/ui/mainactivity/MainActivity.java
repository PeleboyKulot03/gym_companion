package com.example.gymcompanion.ui.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.ReturnCode;
import com.example.gymcompanion.R;
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
        String path = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "TempPictures";
        File s = new File(path);
        File[] test = s.listFiles();

        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis()) + ".mp4";


        String cmd = "ffmpeg -f image2 -i /data/user/0/com.example.gymcompanion/files/TempPictures/picture%03d.jpg /data/user/0/com.example.gymcompanion/files/test.avi";

//
//        for (File file: test){
//            Log.i("teggg", "success: " + file.getAbsolutePath());
//
//        }
//        try {
//            FFmpeg.getInstance(this).execute(cmd, new FFmpegExecuteResponseHandler() {
//                @Override
//                public void onSuccess(String message) {
//
//                }
//
//                @Override
//                public void onProgress(String message) {
//
//                }
//
//                @Override
//                public void onFailure(String message) {
//
//                }
//
//                @Override
//                public void onStart() {
//
//                }
//
//                @Override
//                public void onFinish() {
//
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            throw new RuntimeException(e);
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