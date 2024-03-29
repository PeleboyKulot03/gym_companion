package com.example.gymcompanion.ui.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        CardView name = findViewById(R.id.name);
        CardView heightAndWeight = findViewById(R.id.heightAndWeight);
        CardView birthday = findViewById(R.id.birthday);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(user).getUid()).child("quickInformation");
        Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
        name.setOnClickListener(view -> reference.child("changeName").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            intent.putExtra("from", "name");
                            startActivity(intent);
                            return;
                        }
                        Toast.makeText(UserSettingsActivity.this, "Sorry but you have to wait until " + (30 - diffDays) + " days to change your name!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    intent.putExtra("from", "name");
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
        heightAndWeight.setOnClickListener(view -> reference.child("changeBMI").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            intent.putExtra("from", "weightAndHeight");
                            startActivity(intent);
                            return;
                        }
                        Toast.makeText(UserSettingsActivity.this, "Sorry but you have to wait until " + (30 - diffDays) + " days to change your Height and Weight!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    intent.putExtra("from", "weightAndHeight");
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
        birthday.setOnClickListener(view -> reference.child("changeBDAY").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            intent.putExtra("from", "birthdate");
                            startActivity(intent);
                            return;
                        }
                        Toast.makeText(UserSettingsActivity.this, "Sorry but you have to wait until " + (30 - diffDays) + " days to change your Birthday!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    intent.putExtra("from", "birthdate");
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));

    }
}