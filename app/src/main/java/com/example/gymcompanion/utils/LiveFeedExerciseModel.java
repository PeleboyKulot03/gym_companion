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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class LiveFeedExerciseModel {
    private long time;
    private double accuracy;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private ArrayList<Double> accuracies;
    private ArrayList<Long> times;
    private long aveTime = 0;
    private double aveAccuracy;
    public LiveFeedExerciseModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        accuracies = new ArrayList<>();
        times = new ArrayList<>();
    }
    public LiveFeedExerciseModel(long time, double accuracy) {
        this.time = time;
        this.accuracy = accuracy;
    }

    public long getTime() {
        return time;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void addData(final onSuccess onSuccess, String program, LiveFeedExerciseModel model, int setNumber) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.UP);

        String uID = user.getUid();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int set = 3 - (setNumber - 1);
        String tempSet = "set" + set;
        Log.i("hester", "addData: " + set);
        Log.i("hester", "addData: " + setNumber);
        // record the set's time and accuracy
        databaseReference.child(uID).child("history").child(String.valueOf(year)).child(String.valueOf(month + 1)).child(String.valueOf(day)).child(program).child(tempSet).setValue(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onSuccess.onAddData(true, "");
                // change the number of remaining set
                if (setNumber-1 == 0) {
                    databaseReference.child(uID).child("currentExercise").child(program).child("set").setValue(3);
                    databaseReference.child(uID).child("currentExercise").child(program).child("done").setValue(true);
                    databaseReference.child(uID).child("history").child(String.valueOf(year)).child(String.valueOf(month + 1)).child(String.valueOf(day)).child(program).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot models: snapshot.getChildren()){
                                LiveFeedExerciseModel exerciseModel = models.getValue(LiveFeedExerciseModel.class);
                                accuracies.add(Objects.requireNonNull(exerciseModel).getAccuracy());
                                times.add(exerciseModel.getTime());
                            }
                            for (Long time: times) {
                                aveTime += time;
                            }
                            for (Double accuracy: accuracies){
                                aveAccuracy += accuracy;
                            }

                            aveAccuracy /= 3;
                            aveTime /= 3;

                            databaseReference.child(uID).child("currentExercise").child(program).child("accuracy").setValue(Double.parseDouble(decimalFormat.format(aveAccuracy)));
                            int seconds = (int) (aveTime / 1000);
                            int minutes = seconds / 60;
                            seconds = seconds % 60;

                            String tempTime = (minutes > 0 ? minutes + " Minutes and " + seconds + " Seconds": seconds + " Seconds");
                            databaseReference.child(uID).child("currentExercise").child(program).child("time").setValue(tempTime);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    databaseReference.child(uID).child("currentExercise").child(program).child("set").setValue(setNumber - 1);
                }
            }
            task.addOnFailureListener(e -> onSuccess.onAddData(false, e.getLocalizedMessage()));
        });


    }

    public interface onSuccess {
        void onAddData(boolean verdict, String message);
    }
}
