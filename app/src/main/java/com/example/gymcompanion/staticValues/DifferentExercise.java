package com.example.gymcompanion.staticValues;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.content.res.AppCompatResources;
import com.example.gymcompanion.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DifferentExercise {
    private final Map<String, ArrayList<Drawable>> DRAWABLES;
    private Map<String, String> description;
    public DifferentExercise() {
        DRAWABLES = new HashMap<>();
        description = new HashMap<>();

        description.put("Dips", "Close grip dips primarily train the triceps, with major synergists being the anterior deltoid, the pectoralis muscles, and the rhomboid muscles of the back.");
        description.put("Dumbbell Shoulder Press", "The overhead press, also known as the shoulder press or military press, is an upper-body weight training exercise in which the trainee presses a weight overhead while seated or standing.");
        description.put("Flat Bench Press", "lift or exercise in which a weight is raised by extending the arms upward while lying on a bench.While the exercise does engage the lower chest, it also targets the upper and middle chest as well as the shoulders and triceps.");
        description.put("Incline Bench Press", "The incline of the bench means that it targets the upper chest and deltoids more, specifically hitting the clavicular head of the pec major.The incline bench press is a variation that targets the upper part of the pectoral muscles.");
        description.put("Side Lateral", "The lateral raise is a strength training isolation exercise that works the shoulders (specifically the lateral deltoids), with the trapezius (upper back) supporting by stabilising the exercise. This exercise involves lifting weights away from your body, out to the side.");
        description.put("Skull Crushers", "Sometimes known as lying tricep extension, skull crushers are an effective tricep exercise that involves lying down and lowering a load towards your face, which is where the dubious name comes from. Despite its name, this exercise is safe if a manageable load is used.");

        description.put("Barbell Curls", "Isolation exercise targeting the biceps, performed with a barbell for arm strength and size.");
        description.put("Barbell Rows", "Compound movement engaging the upper back muscles and lats, enhancing overall back development.");
        description.put("Deadlift", "Full-body compound exercise involving lifting a loaded barbell from the ground, strengthening various muscle groups, including the back, glutes, and hamstrings.");
        description.put("Dumbbell Rows", "Focuses on unilateral back development, involving lifting dumbbells to strengthen and balance each side of the back.");
        description.put("Lat Pull Down", "Isolation exercise for the latissimus dorsi muscles, using a cable machine to pull a bar down in front of you.");
        description.put("Preacher Curls", "Isolation exercise for the biceps, performed on a preacher bench, isolating the arm muscles for targeted growth.");

        description.put("Squats", "A squat is a strength-building activity where the individual lowers their hips while standing and then raises them again. As they lower, the hip and knee joints bend, and the ankle joint moves upward; in contrast, during the ascent, the hip and knee joints straighten, and the ankle joint moves downward.");
        description.put("Lunges", "The lunge is a form of bodyweight exercise that focuses on the leg muscles. More specifically, it engages the quadriceps and hamstring muscles in the thigh, as well as the gluteal muscles in the buttocks, with some involvement of the lower leg muscles.");
        description.put("Romanian Deadlift", "Romanian Deadlift (RDL) is a type of deadlift where the body is inclined at the hips, and the knees remain unbent. This strength-training exercise involves lifting a loaded barbell from the ground while maintaining a stabilized, bent-over position, allowing for flexibility in the knees.");
        description.put("Leg Extension", "The leg extension is a resistance weight training exercise focused on isolating and strengthening the quadriceps. This single-joint exercise specifically targets the quadriceps muscles located at the front of the thigh.");
    }
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
        DRAWABLES.put("Lat Pull Down", new ArrayList<Drawable>(){{
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

    public Map<String, String> getDescription() {
        return description;
    }

    public Map<String, ArrayList<Drawable>> getDRAWABLES() {
        return DRAWABLES;
    }


}
