package com.example.gymcompanion.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordModel {

    private final DatabaseReference reference;
    private final FirebaseAuth auth;
    public ForgotPasswordModel() {
        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
    }


    public void forgotPassword(final onRegister onRegister, String email) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> onRegister.isSend(true, ""))
                .addOnFailureListener(e -> onRegister.isSend(false, e.getLocalizedMessage()));
    }
    public void hasUser(final onRegister onRegister, String email) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user: snapshot.getChildren()) {
                    String usersEmail = user.child("informations").child("email").getValue(String.class);
                    if (usersEmail != null){
                        if (usersEmail.equals(email)) {
                            onRegister.hasUser(true);
                            return;
                        }
                    }
                }
                onRegister.hasUser(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface onRegister {
        void hasUser(boolean verdict);
        void isSend(boolean verdict, String errorMessage);
    }
}
