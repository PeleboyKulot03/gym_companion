package com.example.gymcompanion.staticValues;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DifferentExercise {
    private Map<String, ArrayList<Drawable>> DRAWABLES;
    private int shoulderPressBottomToMiddleMin = 60;
    private int shoulderPressBottomToMiddleMax = 90;
    private int shoulderPressMiddleToTopMin = 95;
    private int shoulderPressMiddleToTopeMax = 150;

    private int shoulderPressTopToMiddleMin = 116;
    private int shoulderPressTopToMiddleMax = 157;
    private int shoulderPressMiddleToBottomMin = 73;
    private int shoulderPressMiddleToBottomMax = 110;


    public DifferentExercise (Context context) {
        DRAWABLES = new HashMap<>();

        // animations for push day exercise
        DRAWABLES.put("Flat Bench Press", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.flat_bench_press_1_));
            add(AppCompatResources.getDrawable(context, R.drawable.flat_bench_press_2_));
        }});
        DRAWABLES.put("Incline Bench Press", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.incline_bench_press_1));
            add(AppCompatResources.getDrawable(context, R.drawable.incline_bench_press_2_));
        }});
        DRAWABLES.put("Close Grip Bench Press", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.close_grip_bench_1));
            add(AppCompatResources.getDrawable(context, R.drawable.close_grip_bench_2));
        }});
        DRAWABLES.put("Dips", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.dips_1));
            add(AppCompatResources.getDrawable(context, R.drawable.dips_2));
        }});
        DRAWABLES.put("Dumbbell Shoulder Press", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.dumbbell_shoulder_press_1));
            add(AppCompatResources.getDrawable(context, R.drawable.dumbbell_shoulder_press_2));
        }});
        DRAWABLES.put("Side Lateral Raises", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.side_lateral_raise_1));
            add(AppCompatResources.getDrawable(context, R.drawable.side_lateral_raise_2));
        }});
        DRAWABLES.put("Skull Crushers", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.skull_crushes_1));
            add(AppCompatResources.getDrawable(context, R.drawable.skull_crushes_2));
        }});

        // animation for pull day
        DRAWABLES.put("Deadlift", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.deadlift_1));
            add(AppCompatResources.getDrawable(context, R.drawable.deadlift_2));
        }});
        DRAWABLES.put("Lat Pull down", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.lat_pulldown_1));
            add(AppCompatResources.getDrawable(context, R.drawable.lat_pulldown_2));
        }});
        DRAWABLES.put("Barbell Rows", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.barbell_rows_1));
            add(AppCompatResources.getDrawable(context, R.drawable.barbell_rows_2));
        }});
        DRAWABLES.put("Dumbbell Rows", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.dumbbell_rows_1));
            add(AppCompatResources.getDrawable(context, R.drawable.dumbbell_rows_2));
        }});
        DRAWABLES.put("Barbell Curls", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.barbel_curl_1));
            add(AppCompatResources.getDrawable(context, R.drawable.barbel_curl_2));
        }});
        DRAWABLES.put("Preacher Curls", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.preacher_curl_1));
            add(AppCompatResources.getDrawable(context, R.drawable.preacher_curl_2));
        }});

        // leg day animations
        DRAWABLES.put("Squats", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.squat_1));
            add(AppCompatResources.getDrawable(context, R.drawable.squat_2));
        }});
        DRAWABLES.put("Leg Extension", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.leg_extension_1));
            add(AppCompatResources.getDrawable(context, R.drawable.leg_extension_2));
        }});
        DRAWABLES.put("Romanian Deadlift", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.romanian_deadlift_1));
            add(AppCompatResources.getDrawable(context, R.drawable.romanian_deadlift_2));
        }});
        DRAWABLES.put("Lunges", new ArrayList<Drawable>(){{
            add(AppCompatResources.getDrawable(context, R.drawable.lunges_1));
            add(AppCompatResources.getDrawable(context, R.drawable.lunges_2));
        }});

    }

    public Map<String, ArrayList<Drawable>> getDRAWABLES() {
        return DRAWABLES;
    }

    public int getShoulderPressBottomToMiddleMin() {
        return shoulderPressBottomToMiddleMin;
    }

    public int getShoulderPressBottomToMiddleMax() {
        return shoulderPressBottomToMiddleMax;
    }

    public int getShoulderPressMiddleToTopMin() {
        return shoulderPressMiddleToTopMin;
    }

    public int getShoulderPressMiddleToTopeMax() {
        return shoulderPressMiddleToTopeMax;
    }

    public int getShoulderPressTopToMiddleMin() {
        return shoulderPressTopToMiddleMin;
    }

    public int getShoulderPressTopToMiddleMax() {
        return shoulderPressTopToMiddleMax;
    }

    public int getShoulderPressMiddleToBottomMin() {
        return shoulderPressMiddleToBottomMin;
    }

    public int getShoulderPressMiddleToBottomMax() {
        return shoulderPressMiddleToBottomMax;
    }
}
