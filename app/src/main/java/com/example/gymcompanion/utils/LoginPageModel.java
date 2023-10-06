package com.example.gymcompanion.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginPageModel {
    private static final String TAG = "TAGERISM";
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private String displayName;
    private String email;
    private String photoURL;

    public LoginPageModel() {
        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
    }

    public LoginPageModel(String displayName, String email, String photoURL) {
        this.displayName = displayName;
        this.email = email;
        this.photoURL = photoURL;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void createNewUser(final onCreateUser onCreateUser, LoginPageModel model, String userId){
        reference.child(userId).child("informations").setValue(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                onCreateUser.isSuccess(true);
                return;
            }
            onCreateUser.isSuccess(false);
        });
    }

    public void signIn(final onCreateUser onCreateUser, String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    reference.child(Objects.requireNonNull(authResult.getUser()).getUid()).child("displayName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            onCreateUser.isCorrectCredentials(true, "", snapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                })
                .addOnFailureListener(authResult -> onCreateUser.isCorrectCredentials(false, authResult.getMessage(), ""));
    }
    public interface onCreateUser {
        void isSuccess(boolean verdict);
        void isCorrectCredentials(boolean verdict, String errorMessage, String displayName);
    }


}
