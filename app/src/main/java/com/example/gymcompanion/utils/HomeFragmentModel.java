package com.example.gymcompanion.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeFragmentModel {
    private String program;
    private int set;
    private int reps;
    private double weight;
    private boolean done;
    private double accuracy;
    private String time;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private boolean hasInfo = false;
    private String currentDay;
    private String date;

    public HomeFragmentModel(){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
    }
    public HomeFragmentModel(int set, int reps, double weight, boolean done, double accuracy, String time) {
        this.reps = reps;
        this.set = set;
        this.weight = weight;
        this.done = done;
        this.accuracy = accuracy;
        this.time = time;
    }

    public boolean getDone() {
        return done;
    }

    public double getWeight() {
        return weight;
    }

    public String getProgram() {
        return program;
    }

    public int getSet() {
        return set;
    }

    public int getReps() {
        return reps;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public String getTime() {
        return time;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void getExercise(final onGetExercise onGetExercise) {
        ArrayList<HomeFragmentModel> models = new ArrayList<>();
        ArrayList<HomeFragmentModel> tempModels = new ArrayList<>();

        final int[] count = {0};

        databaseReference.child("informations").child("age").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) != null) {
                    hasInfo = true;
                }

                databaseReference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("currentExercise").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot exercise: snapshot.getChildren()){
                            HomeFragmentModel model = exercise.getValue(HomeFragmentModel.class);
                            Objects.requireNonNull(model).setProgram(exercise.getKey());
                            if (model.getDone()){
                                tempModels.add(model);
                                count[0]++;
                                continue;
                            }
                            models.add(model);
                        }

                        models.addAll(tempModels);
                        databaseReference = databaseReference.getRoot();
                        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("quickInformation").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                date = snapshot.child("date").getValue(String.class);
                                currentDay = snapshot.child("currentDay").getValue(String.class);
                                onGetExercise.isSuccess(true, models, count[0], hasInfo, currentDay, date);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onGetExercise.isSuccess(false, models, count[0], hasInfo, "", "");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setNewExercise(final onGetExercise onGetExercise, String day, String date) {

        ArrayList<HomeFragmentModel> models = new ArrayList<>();
        databaseReference.child(auth.getCurrentUser().getUid()).child("currentExercise").removeValue();
        databaseReference.child(auth.getCurrentUser().getUid()).child("currentProgram").child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot program: snapshot.getChildren()) {
                    if (program.hasChildren()) {
                        int set = program.child("set").getValue(Integer.class);
                        int reps = program.child("reps").getValue(Integer.class);
                        double weight = program.child("weight").getValue(Integer.class);
                        HomeFragmentModel model = new HomeFragmentModel(set, reps, weight, false, 0.0, "");
                        model.setProgram(program.getKey());
                        models.add(model);
                    }
                }

                for (HomeFragmentModel model: models) {
                    databaseReference.child(auth.getCurrentUser().getUid()).child("currentExercise").child(model.getProgram()).setValue(model);
                }
                databaseReference.child(auth.getCurrentUser().getUid()).child("quickInformation").child("currentDay").setValue(day);
                databaseReference.child(auth.getCurrentUser().getUid()).child("quickInformation").child("date").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onGetExercise.isDone(true);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onGetExercise.isDone(false);
            }
        });
    }

    public void getCurrentProgram(final onGetExercise onGetExercise) {
        databaseReference.child(auth.getCurrentUser().getUid()).child("quickInformation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date = snapshot.child("date").getValue(String.class);
                currentDay = snapshot.child("currentDay").getValue(String.class);
                onGetExercise.onGetProgram(date, currentDay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onGetExercise.onGetProgram(null, null);
            }
        });
    }

    public interface onGetExercise {
        void isSuccess(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date);
        void isDone(boolean verdict);
        void onGetProgram(String date, String day);
    }

}