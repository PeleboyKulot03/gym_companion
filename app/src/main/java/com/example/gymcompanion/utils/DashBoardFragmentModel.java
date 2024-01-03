package com.example.gymcompanion.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashBoardFragmentModel {

    private DatabaseReference reference;
    private Map<String, Map<Double, Long>> stats;
    private Map<String, Integer> occurrence;
    private double accuracy, tempAcc = 0.0;
    private long time, tempTime = 0;


    public DashBoardFragmentModel() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("history");
        }
        stats = new HashMap<>();
        occurrence = new HashMap<>();
    }

    public DashBoardFragmentModel(double accuracy, long time) {
        this.accuracy = accuracy;
        this.time = time;
    }

    public void getData(final onGetData onGetData, String filter, String year, String month) {

        if (filter.equals("Yearly")) {
            reference.child(year).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot months: snapshot.getChildren()) {
                        for (DataSnapshot days: months.getChildren()) {
                            for (DataSnapshot exercise: days.getChildren()) {
                                String key = exercise.getKey();
                                tempAcc = 0.0;
                                tempTime = 0;
                                Map<Double, Long> temp = new HashMap<>();

                                for (DataSnapshot data: exercise.getChildren()) {
                                    DashBoardFragmentModel model = data.getValue(DashBoardFragmentModel.class);
                                    if (model != null) {
                                        tempAcc += model.getAccuracy();
                                        tempTime += model.getTime();
                                    }
                                }

                                // get the average of the accuracy and time
                                tempAcc /= 3;
                                tempTime /= 3;

                                // check if the stats already has the exercise then add it if yes.
                                if (stats.containsKey(key)) {
                                    occurrence.put(key, occurrence.get(key) + 1);
                                    // get the accuracy and time then add the current time and accuracy to the previous one
                                    for (Double tempKey: stats.get(key).keySet()){
                                        tempAcc += tempKey;
                                        tempTime += stats.get(key).get(tempKey);
                                    }
                                    temp.put(tempAcc, tempTime);
                                    stats.put(key, temp);
                                    continue;
                                }
                                occurrence.put(key, 1);
                                temp.put(tempAcc, tempTime);
                                stats.put(key, temp);
                            }
                        }
                    }
                    onGetData.isSuccess(true, stats, occurrence);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    onGetData.isSuccess(false, null, null);
                }
            });
        }
        if (filter.equals("Monthly")){
            reference.child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot days: snapshot.getChildren()) {
                        for (DataSnapshot exercise: days.getChildren()) {
                            String key = exercise.getKey();
                            tempAcc = 0.0;
                            tempTime = 0;
                            Map<Double, Long> temp = new HashMap<>();

                            for (DataSnapshot data: exercise.getChildren()) {
                                DashBoardFragmentModel model = data.getValue(DashBoardFragmentModel.class);
                                if (model != null) {
                                    tempAcc += model.getAccuracy();
                                    tempTime += model.getTime();
                                }
                            }

                            // get the average of the accuracy and time
                            tempAcc /= 3;
                            tempTime /= 3;

                            // check if the stats already has the exercise then add it if yes.
                            if (stats.containsKey(key)) {
                                occurrence.put(key, occurrence.get(key) + 1);
                                // get the accuracy and time then add the current time and accuracy to the previous one
                                for (Double tempKey: stats.get(key).keySet()){
                                    tempAcc += tempKey;
                                    tempTime += stats.get(key).get(tempKey);
                                }
                                temp.put(tempAcc, tempTime);
                                stats.put(key, temp);
                                continue;
                            }
                            occurrence.put(key, 1);
                            temp.put(tempAcc, tempTime);
                            stats.put(key, temp);
                        }
                    }
                    onGetData.isSuccess(true, stats, occurrence);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    onGetData.isSuccess(false, null, null);
                }
            });
        }
    }

    public double getAccuracy() {
        return accuracy;
    }

    public long getTime() {
        return time;
    }

    public interface onGetData {
        void isSuccess(boolean verdict, Map<String, Map<Double, Long>> data, Map<String, Integer> occurrence);
    }
}
