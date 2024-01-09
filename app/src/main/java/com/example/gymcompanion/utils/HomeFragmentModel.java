package com.example.gymcompanion.utils;

import android.util.Log;

import androidx.annotation.NonNull;

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
    private int weight;
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
    public HomeFragmentModel(int set, int reps, int weight, boolean done, double accuracy, String time) {
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

    public int getWeight() {
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
                                date = snapshot.child("currentDate").getValue(String.class);
                                currentDay = snapshot.child("currentDay").getValue(String.class);
                                Log.i("TAGEROS", "onDataChange: " + snapshot.child("currentDate").getValue(String.class));
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

    public void getNewExercise(final onGetExercise onGetExercise, String day) {
        databaseReference.getRoot().child("exercises").child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                onGetExercise.isDone(true, snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onGetExercise.isDone(false, null);
            }
        });

    }
    public interface onGetExercise {
        void isSuccess(boolean verdict, ArrayList<HomeFragmentModel> models, int finished, boolean hasInfo, String currentDay, String date);
        void isDone(boolean verdict, DataSnapshot snapshot);
    }

}