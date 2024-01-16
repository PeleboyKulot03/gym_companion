package com.example.gymcompanion.ui.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import com.example.gymcompanion.R;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        CardView name = findViewById(R.id.name);
        CardView heightAndWeight = findViewById(R.id.heightAndWeight);
        CardView birthday = findViewById(R.id.birthday);
        Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
        name.setOnClickListener(view -> {
            intent.putExtra("from", "name");
            startActivity(intent);
        });
        heightAndWeight.setOnClickListener(view -> {
            intent.putExtra("from", "weightAndHeight");
            startActivity(intent);
        });
        birthday.setOnClickListener(view -> {
            intent.putExtra("from", "birthdate");
            startActivity(intent);
        });

    }
}