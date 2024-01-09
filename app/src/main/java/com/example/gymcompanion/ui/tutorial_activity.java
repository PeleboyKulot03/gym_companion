package com.example.gymcompanion.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gymcompanion.R;
import com.example.gymcompanion.staticValues.DifferentExercise;
import com.example.gymcompanion.ui.CustomViews.CustomViewActivity;
import com.example.gymcompanion.ui.Exercise.ExercisePageActivityLiveFeed;
import com.example.gymcompanion.ui.Exercise.ExercisePageActivityVideoRecording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class tutorial_activity extends AppCompatActivity {
    private ImageView imageView;
    private AnimationDrawable anim;
    private Map<String, ArrayList<Drawable>> DRAWABLES;
    private int setNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);



        Intent intent = getIntent();
        DifferentExercise differentExercise = new DifferentExercise(getApplicationContext());

        imageView = findViewById(R.id.action_image);
        ImageView back = findViewById(R.id.back);

        TextView program = findViewById(R.id.program);
        Button startLiveFeed = findViewById(R.id.startLiveFeed);
        Button startRecording = findViewById(R.id.startRecord);


        String exercise = intent.getStringExtra("exercise");
        setNumber = intent.getIntExtra("sets", 0);
        boolean isDone = intent.getBooleanExtra("isDone", false);


        DRAWABLES = new HashMap<>();
        DRAWABLES.putAll(differentExercise.getDRAWABLES());

        if (isDone) {
            startLiveFeed.setVisibility(View.GONE);
            startRecording.setBackgroundColor(getColor(R.color.gray));
            startRecording.setText(getString(R.string.completed));
            startRecording.setEnabled(false);
        }

        imageView.setBackgroundResource(R.drawable.tutorial_animation);
        program.setText(exercise);

        imageView.post(() -> {
            anim = (AnimationDrawable) imageView.getBackground();
            for (Drawable frame: Objects.requireNonNull(DRAWABLES.get(exercise))){
                anim.addFrame(frame, 1000);
            }
            anim.start();
        });

        startLiveFeed.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), ExercisePageActivityLiveFeed.class);

            intent1.putExtra("exercise", exercise);
            intent1.putExtra("sets", setNumber);
            SharedPreferences dialogPreferences = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);
            String check = dialogPreferences.getString("showDialog", "");
            if (check.equals("1")) {
                startActivity(intent1);
            } else {
                CustomViewActivity cdd = new CustomViewActivity(tutorial_activity.this, intent1);
                Objects.requireNonNull(cdd.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });

        startRecording.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), ExercisePageActivityVideoRecording.class);
            intent1.putExtra("exercise", exercise);
            startActivity(intent1);
        });

        back.setOnClickListener(v -> finish());
    }
}