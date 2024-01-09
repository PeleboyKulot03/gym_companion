package com.example.gymcompanion.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gymcompanion.staticValues.DifferentExercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RegistrationPageModel {

    private String firstName, middleName, surname, displayName, weight, height, birthday, age, gender, experience, username, email, date, imageSrc;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private boolean isDone = false;

    public RegistrationPageModel() {
        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
    }

    public RegistrationPageModel(String firstName, String middleName, String surname, String displayName, String weight, String height, String birthday, String age, String gender, String experience, String username, String email, String date, String imageSrc) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.displayName = displayName;
        this.weight = weight;
        this.height = height;
        this.birthday = birthday;
        this.age = age;
        this.gender = gender;
        this.experience = experience;
        this.username = username;
        this.email = email;
        this.date = date;
        this.imageSrc = imageSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public String getSurname() {
        return surname;
    }
    public String getWeight() {
        return weight;
    }
    public String getHeight() {
        return height;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getAge() {
        return age;
    }
    public String getGender() {
        return gender;
    }
    public String getExperience() {
        return experience;
    }
    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public void hasUser(final onRegister onRegister, String toValid, String value) {
        isDone = false;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user: snapshot.getChildren()) {
                    if (Objects.equals(user.child("informations").child(toValid).getValue(String.class), value)){
                        onRegister.hasUser(false);
                        isDone = true;
                        break;
                    }
                }
                if (!isDone) {
                    onRegister.hasUser(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onRegister.hasUser(false);
            }
        });
    }

    public void createNewUser(final onRegister onRegister, String email, String password, RegistrationPageModel model, boolean isUser) {
        ArrayList<String> pushDay = new ArrayList<>();
        pushDay.add("Dips");
        pushDay.add("Dumbbell Shoulder Press");
        pushDay.add("Flat Bench Press");
        pushDay.add("Incline Bench Press");
        pushDay.add("Side Lateral Raises");
        pushDay.add("Skull Crushers");

        ArrayList<String> pullDay = new ArrayList<>();
        pullDay.add("Barbell Curls");
        pullDay.add("Barbell Rows");
        pullDay.add("Deadlift");
        pullDay.add("Dumbbell Rows");
        pullDay.add("Lat Pull Down");
        pullDay.add("Preacher Curls");

        ArrayList<String> legDay = new ArrayList<>();
        legDay.add("Leg Extension");
        legDay.add("Lunges");
        legDay.add("Romanian Deadlift");
        legDay.add("Squats");

        ArrayList<ExerciseModel> exerciseModels = new ArrayList<>();
        if (model.getExperience().equals("Beginner")){
            // for push day
            exerciseModels.add(new ExerciseModel(false, 12, 3, 10));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 10));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 10));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 5));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 5));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 5));

            // for pull day
            exerciseModels.add(new ExerciseModel(false, 12, 3, 10));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 10));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 10));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 5));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 5));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 5));

            // for leg day
            exerciseModels.add(new ExerciseModel(false, 12, 3, 10));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 5));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 10));
            exerciseModels.add(new ExerciseModel(false, 12, 3, 5));
        }
        if (isUser) {
            reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("informations").setValue(model).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()){
                    int counter = 0;
                    int pushCounter = 0;
                    int pullCounter = 0;
                    int legCounter = 0;

                    FirebaseUser user = auth.getCurrentUser();
                    for (int i = 0; i < pushDay.size(); i++) {
                        reference = reference.getRoot();
                        reference.child("users").child(user.getUid()).child("currentExercise").child(pushDay.get(i)).setValue(exerciseModels.get(i));
                    }
                    while (counter < pushDay.size() + pullDay.size() + legDay.size()) {
                        if (counter < pushDay.size()) {
                            reference = reference.getRoot();
                            reference.child("users").child(user.getUid()).child("currentProgram").child("pushDay").child(pushDay.get(pushCounter)).setValue(exerciseModels.get(counter));
                            counter++;
                            pushCounter++;
                            continue;
                        }
                        if (counter < pushDay.size() + pullDay.size()) {
                            reference = reference.getRoot();
                            reference.child("users").child(user.getUid()).child("currentProgram").child("pullDay").child(pullDay.get(pullCounter)).setValue(exerciseModels.get(counter));
                            counter++;
                            pullCounter++;
                            continue;
                        }
                        reference = reference.getRoot();
                        reference.child("users").child(user.getUid()).child("currentProgram").child("legDay").child(legDay.get(legCounter)).setValue(exerciseModels.get(counter));
                        counter++;
                        legCounter++;
                    }
                    reference = reference.getRoot();
                    reference.child("users").child(user.getUid()).child("quickInformation").child("currentDay").setValue("Push Day");
                    reference = reference.getRoot();
                    reference.child("users").child(user.getUid()).child("quickInformation").child("date").setValue(model.getDate());
                    onRegister.isSuccess(true, "");

                }
            }).addOnFailureListener(e -> onRegister.isSuccess(false, e.getLocalizedMessage()));
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.i("tageristo", "createNewUser: " + auth.getCurrentUser().getUid());
                reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("informations").setValue(model).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(model.getDisplayName())
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        for (int i = 0; i < pushDay.size(); i++) {
                                            reference = reference.getRoot();
                                            reference.child("users").child(user.getUid()).child("currentExercise").child(pushDay.get(i)).setValue(exerciseModels.get(i));
                                        }
                                        reference = reference.getRoot();
                                        reference.child("users").child(user.getUid()).child("quickInformation").child("currentDay").setValue("Push Day");
                                        reference = reference.getRoot();
                                        reference.child("users").child(user.getUid()).child("quickInformation").child("date").setValue(model.getDate());
                                        onRegister.isSuccess(true, "");
                                    }
                                }).addOnFailureListener(e -> onRegister.isSuccess(false, e.getLocalizedMessage()));
                        user.sendEmailVerification();
                    }
                }).addOnFailureListener(e -> onRegister.isSuccess(false, e.getLocalizedMessage()));
            }

        }).addOnFailureListener(e -> onRegister.isSuccess(false, e.getLocalizedMessage()));
    }
    public interface onRegister {
        void isSuccess(boolean verdict, String errorMessage);
        void hasUser(boolean verdict);
    }




}