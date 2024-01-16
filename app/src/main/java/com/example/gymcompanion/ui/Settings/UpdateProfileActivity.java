package com.example.gymcompanion.ui.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.gymcompanion.R;

public class UpdateProfileActivity extends AppCompatActivity {
    private LinearLayout name, birthdate, weightAndHeight, displayName, password;
    private String layoutFrom = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        name = findViewById(R.id.name);
        birthdate = findViewById(R.id.birthdate);
        weightAndHeight = findViewById(R.id.weightAndHeight);
        displayName = findViewById(R.id.displayName);
        password = findViewById(R.id.password);
        Intent intent = getIntent();
        if (intent.hasExtra("from")) {
            layoutFrom = intent.getStringExtra("from");
        }
        openLayout();
    }

    private void openLayout() {
        if (layoutFrom.equals("name")) {
            name.setVisibility(View.VISIBLE);
        }
        if (layoutFrom.equals("birthdate")) {
            birthdate.setVisibility(View.VISIBLE);
        }
        if (layoutFrom.equals("weightAndHeight")) {
            weightAndHeight.setVisibility(View.VISIBLE);
        }
        if (layoutFrom.equals("displayName")) {
            displayName.setVisibility(View.VISIBLE);
        }
        if (layoutFrom.equals("password")) {
            password.setVisibility(View.VISIBLE);
        }
    }
}