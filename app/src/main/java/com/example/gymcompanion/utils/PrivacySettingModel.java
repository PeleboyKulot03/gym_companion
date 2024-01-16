package com.example.gymcompanion.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PrivacySettingModel {
    private String birthday, age, displayName, height, weight, firstName, middleName, surname, username, email;
    private boolean isFromGoogle;
    private DatabaseReference reference;
    private FirebaseUser user;
    private boolean isDone;
    public PrivacySettingModel() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("informations");
        }
    }

    public PrivacySettingModel(boolean isFromGoogle, String email, String birthday, String age, String displayName, String height, String weight, String firstName, String middleName, String surname, String username, String currentPassword) {
        this.birthday = birthday;
        this.isFromGoogle = isFromGoogle;
        this.email = email;
        this.age = age;
        this.displayName = displayName;
        this.height = height;
        this.weight = weight;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAge() {
        return age;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
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

    public String getUsername() {
        return username;
    }

    public boolean isFromGoogle() {
        return isFromGoogle;
    }

    public void setFromGoogle(boolean fromGoogle) {
        isFromGoogle = fromGoogle;
    }

    public void getInformation(final onGetData onGetData) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PrivacySettingModel model = snapshot.getValue(PrivacySettingModel.class);
                Objects.requireNonNull(model).setFromGoogle(user.getPhotoUrl() != null);
                onGetData.onGetInformation(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onGetData.onGetInformation(null);
            }
        });
    }

    public void reAuthenticate(final onGetData onGetData, String email, String password) {
        AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                onGetData.reAuthenticate(true);
            }
        }).addOnFailureListener(e -> onGetData.reAuthenticate(false));
    }
    public void updateName(final onGetData onGetData, String middleName, String surname) {

    }

    public void hasUser(final onGetData onGetData, String value) {
        isDone = false;
        reference.getRoot().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user: snapshot.getChildren()) {
                    Log.i("testerrr", "onDataChange: " + user.child("informations").child("username"));
                    if (Objects.equals(user.child("informations").child("username").getValue(String.class), value)){
                        onGetData.hasUser(false);
                        isDone = true;
                        break;
                    }
                }
                if (!isDone) {
                    onGetData.hasUser(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onGetData.hasUser(false);
            }
        });
    }

    public void changePassword(final onGetData onGetData, String password) {
        user.updatePassword(password).addOnCompleteListener(task -> {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            Date date = new Date();
            reference.getRoot().child("users").child(user.getUid()).child("quickInformation").child("changePassword").setValue(formatter.format(date)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("testerrr", "onComplete: ");
                    if (task.isSuccessful()) {
                        onGetData.onChangeInfo(true, "");
                    }
                }
            }).addOnFailureListener(e -> {
                onGetData.onChangeInfo(false, e.getLocalizedMessage());
            });
        }).addOnFailureListener(e -> onGetData.onChangeInfo(false, e.getLocalizedMessage()));
    }

    public interface onGetData {
        void onGetInformation(PrivacySettingModel model);
        void reAuthenticate(boolean verdict);
        void hasUser(boolean verdict);
        void onChangeInfo(boolean verdict, String message);
    }
}
