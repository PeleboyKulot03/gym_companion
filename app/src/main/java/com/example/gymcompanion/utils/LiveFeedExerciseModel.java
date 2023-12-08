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
import java.util.Objects;

public class LiveFeedExerciseModel {
    private long time;
    private String accuracy;
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
    public LiveFeedExerciseModel(long time, String accuracy) {
        this.time = time;
        this.accuracy = accuracy;
    }

    public long getTime() {
        return time;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void addData(final onSuccess onSuccess, String program, LiveFeedExerciseModel model, String setNumber) {
        String uID = user.getUid();

        int set = 3 - Character.getNumericValue(setNumber.charAt(setNumber.length() - 1));
        // record the set's time and accuracy
        databaseReference.child(uID).child("currentExercise").child(program).child("sets").child(setNumber).setValue(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onSuccess.onAddData(true, "");
            }
            task.addOnFailureListener(e -> onSuccess.onAddData(false, e.getLocalizedMessage()));
        });


        // change the number of remaining set
        if (set == 0) {
            databaseReference.child(uID).child("currentExercise").child(program).child("set").setValue(3);
            databaseReference.child(uID).child("currentExercise").child(program).child("done").setValue(true);
            databaseReference.child(uID).child("currentExercise").child(program).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot models: snapshot.getChildren()){
                        LiveFeedExerciseModel exerciseModel = models.getValue(LiveFeedExerciseModel.class);
                        accuracies.add(Double.valueOf(Objects.requireNonNull(exerciseModel).getAccuracy()));
                        times.add(exerciseModel.getTime());
                    }
                    for (Long time: times) {
                        Log.i("teggss", "onDataChange: " + time);
                        aveTime += time;
                    }
                    for (Double accuracy: accuracies){
                        Log.i("teggss", "onDataChange: " + accuracy);
                        aveAccuracy += accuracy;
                    }

                    aveAccuracy /= 3;
                    aveTime /= 3;

                    databaseReference.child(uID).child("currentExercise").child(program).child("accuracy").setValue(aveAccuracy);
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
            databaseReference.child(uID).child("currentExercise").child(program).child("set").setValue(set);
        }


    }

    public interface onSuccess {
        void onAddData(boolean verdict, String message);
    }
}
