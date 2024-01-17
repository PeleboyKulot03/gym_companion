package com.example.gymcompanion.ui.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gymcompanion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.SimpleFormatter;

public class PrivacySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(user).getUid()).child("quickInformation");
        CardView username = findViewById(R.id.userName);
        CardView password = findViewById(R.id.password);
        Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);

        username.setOnClickListener(view -> reference.child("changeUsername").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                        Date date = null;
                        try {
                            date = formatter.parse(Objects.requireNonNull(snapshot.getValue(String.class)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        LocalDate d1 = Objects.requireNonNull(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
                        LocalDate d2 = LocalDate.now();
                        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
                        long diffDays = diff.toDays();
                        if (diffDays >= 30) {
                            intent.putExtra("from", "username");
                            startActivity(intent);
                            return;
                        }
                        Toast.makeText(PrivacySettingsActivity.this, "Sorry but you have to wait until " + (30 - diffDays) + " days to change your username!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    intent.putExtra("from", "username");
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
        password.setOnClickListener(view -> reference.child("changePassword").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                        Date date = null;
                        try {
                            date = formatter.parse(Objects.requireNonNull(snapshot.getValue(String.class)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        LocalDate d1 = Objects.requireNonNull(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
                        LocalDate d2 = LocalDate.now();
                        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
                        long diffDays = diff.toDays();
                        if (diffDays >= 30) {
                            intent.putExtra("from", "password");
                            startActivity(intent);
                            return;
                        }
                        Toast.makeText(PrivacySettingsActivity.this, "Sorry but you have to wait until " + (30 - diffDays) + " days to change your password!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    intent.putExtra("from", "password");
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }
}