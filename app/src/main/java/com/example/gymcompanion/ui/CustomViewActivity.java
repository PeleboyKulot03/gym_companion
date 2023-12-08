package com.example.gymcompanion.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.gymcompanion.R;

public class CustomViewActivity extends Dialog {

    public Activity c;
    public Button okay;
    private CheckBox checkBox;
    private Intent intent;
    SharedPreferences dialogPreferences;
    String prefrencestring;
    public CustomViewActivity(Activity a, Intent intent) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.intent = intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_view);
        okay = findViewById(R.id.okay);
        checkBox = findViewById(R.id.neverShowAgain);
        okay.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                prefrencestring = "1";
                dialogPreferences = c.getSharedPreferences(c.getPackageName() + "_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editPreferences = dialogPreferences.edit();
                editPreferences.putString("showDialog", prefrencestring);
                editPreferences.apply();
            }
            dismiss();
            c.startActivity(intent);
            c.finish();

        });
    }
}