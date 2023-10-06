package com.example.gymcompanion.utils;

import androidx.annotation.NonNull;

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

import java.util.Objects;

public class RegistrationPageModel {
    private String firstName, middleName, surname, displayName, weight, height, birthday, age, gender, experience, email;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    public RegistrationPageModel() {
        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
    }

    public RegistrationPageModel(String firstName, String middleName, String surname, String displayName, String weight, String height, String birthday, String age, String gender, String experience, String email) {
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
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
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

    public void hasUser(final onRegister onRegister, String email) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user: snapshot.getChildren()) {
                    String usersEmail = user.child("email").getValue(String.class);
                    if (usersEmail != null){
                        if (usersEmail.equals(email)) {
                            onRegister.hasUser(false);
                            return;
                        }
                    }
                }
                onRegister.hasUser(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createNewUser(final onRegister onRegister, String email, String password, RegistrationPageModel model) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("informations").setValue(model).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(model.getDisplayName())
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        onRegister.isSuccess(true, "");
                                    }
                                }).addOnFailureListener(e -> {
                                    onRegister.isSuccess(false, e.getLocalizedMessage());
                                });
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
