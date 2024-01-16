package com.example.gymcompanion.ui.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.example.gymcompanion.R;

public class PrivacySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);
        CardView displayName = findViewById(R.id.displayName);
        CardView password = findViewById(R.id.password);
        Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
        displayName.setOnClickListener(view -> {
            intent.putExtra("from", "displayName");
            startActivity(intent);
        });
        password.setOnClickListener(view -> {
            intent.putExtra("from", "password");
            startActivity(intent);
        });
    }
}