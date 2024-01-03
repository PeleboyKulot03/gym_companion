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

import java.util.HashMap;
import java.util.Map;

public class DetailedDashboardModel {
    private double accuracy, tempAcc = 0.0, tempAccMonth = 0.0;
    private long time, tempTime = 0, tempTimeMonth = 0;
    private DatabaseReference reference;
    private Map<String, Map<Double, Long>> data;
    private int counter = 0;

    public DetailedDashboardModel() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("history");
        }
        data = new HashMap<>();
    }
    public DetailedDashboardModel(double accuracy, long time) {
        this.accuracy = accuracy;
        this.time = time;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public long getTime() {
        return time;
    }

    public void getData(final onGetData onGetData, String exercise, String filter, int year, int month) {
        if (filter.equals("Weekly")) {
            reference.child(String.valueOf(year)).child(String.valueOf(month)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot day: snapshot.getChildren()) {
                        if (day.child(exercise).exists()) {
                            tempAcc = 0.0;
                            tempTime = 0;
                            for (DataSnapshot set: day.child(exercise).getChildren()) {
                                DetailedDashboardModel model = set.getValue(DetailedDashboardModel.class);
                                if (model != null) {
                                    tempAcc += model.getAccuracy();
                                    tempTime += model.getTime();
                                }
                            }
                            Map<Double, Long> tempData = new HashMap<>();
                            tempData.put(tempAcc / 3, tempTime / 3);
                            data.put(day.getKey(), tempData);
                        }
                    }
                    onGetData.isSuccess(true, data);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    onGetData.isSuccess(false, null);
                }
            });
        }

        if (filter.equals("Monthly")) {
            reference.child(String.valueOf(year)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot monthly: snapshot.getChildren()) {
                        counter = 0;
                        tempAccMonth = 0.0;
                        tempTimeMonth = 0;
                        for (DataSnapshot day: monthly.getChildren()) {
                            if (day.child(exercise).exists()) {
                                counter += 1;
                                tempAcc = 0.0;
                                tempTime = 0;
                                for (DataSnapshot set: day.child(exercise).getChildren()) {
                                    DetailedDashboardModel model = set.getValue(DetailedDashboardModel.class);
                                    if (model != null) {
                                        Log.i("tagelista", "Accuracy: " + model.getAccuracy());
                                        Log.i("tagelista", "Time: " + model.getTime());
                                        tempAcc += model.getAccuracy();
                                        tempTime += model.getTime();
                                    }
                                }
                                tempAccMonth += tempAcc / 3;
                                tempTimeMonth += tempTime / 3;
                            }
                        }
                        Log.i("tagelista", "onGetData: " + monthly.getChildrenCount());
                        Map<Double, Long> tempData = new HashMap<>();
                        if (counter <= 1) {
                            tempData.put(tempAccMonth, tempTimeMonth);
                        }
                        else {
                            tempData.put(tempAccMonth / counter, tempTimeMonth / counter);
                        }
                        data.put(monthly.getKey(), tempData);
                    }
                    onGetData.isSuccess(true, data);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    onGetData.isSuccess(false, null);
                }
            });
        }
    }


    public interface onGetData {
        void isSuccess(boolean verdict, Map<String, Map<Double, Long>> data);
    }
}
