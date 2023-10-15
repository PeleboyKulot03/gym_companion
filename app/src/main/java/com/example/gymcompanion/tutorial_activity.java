package com.example.gymcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gymcompanion.staticValues.DifferentExercise;
import com.example.gymcompanion.ui.exercise.ExercisePageActivityLiveFeed;
import com.example.gymcompanion.ui.exercise.ExercisePageActivityVideoRecording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class tutorial_activity extends AppCompatActivity {
    private ImageView imageView, back;
    private AnimationDrawable anim;
    private Map<String, ArrayList<Drawable>> DRAWABLES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Intent intent = getIntent();
        DifferentExercise differentExercise = new DifferentExercise(getApplicationContext());

        imageView = findViewById(R.id.action_image);
        back = findViewById(R.id.back);

        TextView program = findViewById(R.id.program);
        Button startLiveFeed = findViewById(R.id.startLiveFeed);
        Button startRecording = findViewById(R.id.startRecord);


        String exercise = intent.getStringExtra("exercise");
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
            startActivity(new Intent(getApplicationContext(), ExercisePageActivityLiveFeed.class));
        });

        startRecording.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), ExercisePageActivityVideoRecording.class);
            intent1.putExtra("exercise", exercise);
            startActivity(intent1);
        });

        back.setOnClickListener(v -> finish());
    }
}