package com.example.gymcompanion.utils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailedProfileModel {
    private DatabaseReference databaseReference;
    private Map<String, ArrayList<DetailedProfileModel>> models;
    private int set;
    private int reps;
    private int weight;
    private String exercise;

    public DetailedProfileModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        models = new HashMap<>();
    }

    public DetailedProfileModel(int set, int reps, int weight, String exercise) {
        this.set = set;
        this.reps = reps;
        this.weight = weight;
        this.exercise = exercise;
    }

    public int getSet() {
        return set;
    }

    public int getReps() {
        return reps;
    }

    public int getWeight() {
        return weight;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public void getData(final onGetData onGetData, String user) {
        databaseReference.child(user).child("currentProgram").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot program: snapshot.getChildren()) {
                    ArrayList<DetailedProfileModel> tempModel = new ArrayList<>();
                    for (DataSnapshot exercise: program.getChildren()){
                        DetailedProfileModel model = exercise.getValue(DetailedProfileModel.class);
                        if (model != null) {
                            model.setExercise(exercise.getKey());
                        }
                        tempModel.add(model);
                    }
                    models.put(program.getKey(), tempModel);
                }

                onGetData.onGetProgram(models);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onGetData.onGetProgram(null);
            }
        });
    }

    public interface onGetData{
        void onGetProgram(Map<String, ArrayList<DetailedProfileModel>> models);
    }
}
