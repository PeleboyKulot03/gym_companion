package com.example.gymcompanion.ui.CustomViews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.gymcompanion.R;

public class ExerciseFinishActivity extends Dialog {
    private Activity activity;
    private String acc, time;

    public ExerciseFinishActivity(Activity activity, String acc, String time) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.acc = acc;
        this.time = time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_finish);

        TextView totalAcc = findViewById(R.id.totalAcc);
        TextView totalTime = findViewById(R.id.totalTime);

        totalAcc.setText(acc);
        totalTime.setText(time);
        Button finish = findViewById(R.id.finish);

        finish.setOnClickListener(view -> activity.finish());
    }
}