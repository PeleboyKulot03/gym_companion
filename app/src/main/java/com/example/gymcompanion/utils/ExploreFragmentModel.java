package com.example.gymcompanion.utils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExploreFragmentModel {
    private String ID, displayName, weight, height, experience, imageSrc;
    private FirebaseUser user;
    private DatabaseReference reference;

    public ExploreFragmentModel() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            reference = FirebaseDatabase.getInstance().getReference("users");
        }
    }

    public ExploreFragmentModel(String ID, String displayName, String weight, String height, String experience, String imageSrc) {
        this.ID = ID;
        this.displayName = displayName;
        this.weight = weight;
        this.height = height;
        this.experience = experience;
        this.imageSrc = imageSrc;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }
    public String getExperience() {
        return experience;
    }
    public String getImageSrc() {
        return imageSrc;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void getUser(final onGetUsers onGetUsers) {
        ArrayList<ExploreFragmentModel> models = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot users: snapshot.getChildren()) {
                    if (!user.getUid().equals(users.getKey())) {
                        ExploreFragmentModel model = users.child("informations").getValue(ExploreFragmentModel.class);
                        if (model != null) {
                            model.setID(users.getKey());
                        }
                        models.add(model);
                    }
                }
                onGetUsers.onGetData(true, models);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onGetUsers.onGetData(false, null);
            }
        });
    }

    public interface onGetUsers {
        void onGetData(boolean verdict, ArrayList<ExploreFragmentModel> models);
    }
}
