package com.example.gymcompanion.ui.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.PrivacySettingModel;
import com.google.android.material.appbar.AppBarLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateProfileActivity extends AppCompatActivity implements IPrivacySettings {
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String BIRTHDATE = "birthdate";
    private static final String WEIGHT_AND_HEIGHT = "weightAndHeight";
    private static final String DISPLAY_NAME = "username";
    private LinearLayout name, birthdate, weightAndHeight, displayName, password;
    private String layoutFrom = "";
    private final Calendar calendar = Calendar.getInstance();
    private final String myDateFormat = "MM/dd/yy";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(myDateFormat, Locale.US);
    private TextView birthday, note;
    private int userAge = 0;
    private EditText nameET, middleNameET, surnameET, weightET, heightET, ageET, usernameET, currentPasswordET, passwordET, confirmPasswordET;
    private boolean isValid = false, isValidUsername = false;
    private ImageView customProgressBar;
    private Animation animation;
    private PrivacySettingsPresenter privacySettingsPresenter;
    private PrivacySettingModel model;
    private Button save;
    private String finalName = "", finalMiddleName = "", finalSurname = "", finalWeight = "", finalHeight = "", finalBirthday = "", finalAge = "", finalUsername = "", finalPassword = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        privacySettingsPresenter = new PrivacySettingsPresenter(this);
        name = findViewById(R.id.name);
        save = findViewById(R.id.save);
        birthdate = findViewById(R.id.birthdate);
        weightAndHeight = findViewById(R.id.weightAndHeight);
        displayName = findViewById(R.id.displayName);
        password = findViewById(R.id.password);
        nameET = findViewById(R.id.nameET);
        middleNameET = findViewById(R.id.middleNameET);
        surnameET = findViewById(R.id.surnameET);
        weightET = findViewById(R.id.weightET);
        heightET = findViewById(R.id.heightET);
        ageET = findViewById(R.id.ageET);
        birthday = findViewById(R.id.birthday);
        usernameET = findViewById(R.id.usernameET);
        currentPasswordET = findViewById(R.id.currentPasswordET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);
        LinearLayout birthdatePicker = findViewById(R.id.birthdatePicker);
        AppBarLayout toolbar = findViewById(R.id.toolbar);
        customProgressBar = findViewById(R.id.customProgressBar);
        note = findViewById(R.id.note);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        Intent intent = getIntent();
        if (intent.hasExtra("from")) {
            layoutFrom = intent.getStringExtra("from");
        }
        openLayout();
        // initializing birthday text by the current date
        birthday.setText(dateFormat.format(calendar.getTime()));
        int curYear = calendar.get(Calendar.YEAR);

        // getting the picked birthday
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            birthday.setTextColor(getColor(R.color.black));
            birthday.setText(dateFormat.format(calendar.getTime()));
            userAge = curYear - year;
            String ageString = userAge + " yrs old";
            ageET.setText(ageString);
            finalBirthday = birthday.getText().toString();
        };


        // picking birthdate
        birthdatePicker.setOnClickListener(view -> {
            new DatePickerDialog(this, date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                    .show();
            ageET.setError(null);
        });

        toolbar.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning Notice")
                    .setMessage("Are you sure you want to go back to sign in page?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> super.onBackPressed())
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();

            builder.show();
        });
        save.setOnClickListener(v -> {
            if (isCorrect()) {

            }
        });
        privacySettingsPresenter.onGetInformation();
    }

    private void openLayout() {
        if (layoutFrom.equals(NAME)) {
            name.setVisibility(View.VISIBLE);
        }
        if (layoutFrom.equals(BIRTHDATE)) {
            birthdate.setVisibility(View.VISIBLE);
        }
        if (layoutFrom.equals(WEIGHT_AND_HEIGHT)) {
            weightAndHeight.setVisibility(View.VISIBLE);
        }
        if (layoutFrom.equals(DISPLAY_NAME)) {
            save.setText(getString(R.string.validate));
            displayName.setVisibility(View.VISIBLE);
        }
        if (layoutFrom.equals(PASSWORD)) {
            save.setText(getString(R.string.validate));
            password.setVisibility(View.VISIBLE);
        }
    }
    private boolean isCorrect(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
        switch (layoutFrom) {
            case NAME:
                if (finalName.isEmpty() && finalSurname.isEmpty()) {
                    if (nameET.getText().toString().isEmpty()){
                        nameET.setError("Please add your given name first");
                        nameET.requestFocus();
                        return false;
                    }
                    if (surnameET.getText().toString().isEmpty()){
                        surnameET.setError("Please add your surname first");
                        surnameET.requestFocus();
                        return false;
                    }
                    finalName = nameET.getText().toString();
                    finalMiddleName = middleNameET.getText().toString();
                    finalSurname = surnameET.getText().toString();
                }
                customProgressBar.startAnimation(animation);
                privacySettingsPresenter.changeName(nameET.getText().toString(), middleNameET.getText().toString(), surnameET.getText().toString());
                break;

            case WEIGHT_AND_HEIGHT:
                if (finalWeight.isEmpty() && finalHeight.isEmpty()) {
                    if (weightET.getText().toString().isEmpty()){
                        weightET.setError("Please add your weight first");
                        weightET.requestFocus();
                        return false;
                    }
                    if (heightET.getText().toString().isEmpty()){
                        heightET.setError("Please add your height first");
                        heightET.requestFocus();
                        return false;
                    }
                    finalWeight = weightET.getText().toString();
                    finalHeight = heightET.getText().toString();
                }
                customProgressBar.startAnimation(animation);
                privacySettingsPresenter.changeHeightAndWeight(weightET.getText().toString(), heightET.getText().toString());
                break;

            case BIRTHDATE:
                if (finalAge.isEmpty()){
                    if (userAge == 0){
                        builder.setTitle("Warning Notice")
                                .setMessage("Sorry but birthday is a required field, please add your birthday to continue.")
                                .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                        builder.show();
                        return false;
                    }
                    if (userAge < 17){
                        builder.setTitle("Warning Notice")
                                .setMessage("Sorry but you must be 17 or above to use this application.")
                                .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                        builder.show();
                        return false;
                    }
                    finalAge = String.valueOf(userAge);
                }
                customProgressBar.startAnimation(animation);
                privacySettingsPresenter.changeAge(birthday.getText().toString(), finalAge);
                break;


            case DISPLAY_NAME:
                if (usernameET.getText().toString().isEmpty()){
                    usernameET.setError("Username cannot be empty!");
                    usernameET.requestFocus();
                    return false;
                }
                if (usernameET.getText().toString().length() < 6){
                    usernameET.requestFocus();
                    usernameET.setError("Please enter at least 6 characters");
                    return false;
                }
                hideSoftKeyboard(usernameET);
                if (!isValid) {
                    customProgressBar.startAnimation(animation);
                    privacySettingsPresenter.hasUser(usernameET.getText().toString());
                    return false;
                }
                customProgressBar.startAnimation(animation);
                privacySettingsPresenter.changeUsername(usernameET.getText().toString());
                break;

            case PASSWORD:
                if (currentPasswordET.getText().toString().isEmpty()){
                    currentPasswordET.setError("Please add your password first");
                    currentPasswordET.requestFocus();
                    return false;
                }
                if (currentPasswordET.getText().toString().length() < 8){
                    currentPasswordET.setError("Please add at least 8 character password");
                    currentPasswordET.requestFocus();
                    return false;
                }
                if (passwordET.getText().toString().isEmpty()){
                    passwordET.setError("Please add your password first");
                    passwordET.requestFocus();
                    return false;
                }
                if (passwordET.getText().toString().length() < 8){
                    passwordET.setError("Please add at least 8 character password");
                    passwordET.requestFocus();
                    return false;
                }
                if (confirmPasswordET.getText().toString().isEmpty()){
                    confirmPasswordET.setError("Please add your password first");
                    confirmPasswordET.requestFocus();
                    return false;
                }
                if (!confirmPasswordET.getText().toString().equals(passwordET.getText().toString())){
                    confirmPasswordET.setError("Password does not match!");
                    confirmPasswordET.requestFocus();
                    return false;
                }
                if (!isValid) {
                    customProgressBar.startAnimation(animation);
                    privacySettingsPresenter.reAuthenticate(model.getEmail(), currentPasswordET.getText().toString());
                    return false;
                }
                privacySettingsPresenter.changePassword(passwordET.getText().toString());
                hideSoftKeyboard(confirmPasswordET);
            default:
                break;
        }

        return true;

    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
        builder.setTitle("Warning Notice")
                .setMessage("Are you sure you want to go back to sign in page?")
                .setPositiveButton("Yes", (dialogInterface, i) -> super.onBackPressed())
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        builder.show();
    }

    @Override
    public void hasUser(boolean verdict) {
        customProgressBar.clearAnimation();
        if (!verdict){
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
            builder.setTitle("Warning Notice")
                    .setMessage("Sorry but the provided username is currently in use, please change the username or forgot the password in login page if " +
                            "you forgot it.")
                    .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();

            builder.show();
            return;
        }
        Toast.makeText(this, "Thank you for validating, you can now continue!", Toast.LENGTH_SHORT).show();
        isValid = true;
        save.setText(getString(R.string.save));
    }

    @Override
    public void onChangeInfo(boolean verdict, String message) {
        if (verdict) {
            if (layoutFrom.equals(NAME)) {
                Toast.makeText(this, "Name changing complete!", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (layoutFrom.equals(WEIGHT_AND_HEIGHT)) {
                Toast.makeText(this, "Weight and Height changing complete!", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (layoutFrom.equals(BIRTHDATE)) {
                Toast.makeText(this, "Birthday changing complete!", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (layoutFrom.equals(DISPLAY_NAME)) {
                Toast.makeText(this, "Username changing complete!", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (layoutFrom.equals(PASSWORD)) {
                Toast.makeText(this, "Password changing complete!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onGetInformation(PrivacySettingModel model) {
        if (model != null) {
            this.model = model;
            nameET.setText(model.getFirstName());
            middleNameET.setText(model.getMiddleName());
            surnameET.setText(model.getSurname());
            heightET.setText(model.getHeight());
            weightET.setText(model.getWeight());
            birthday.setText(model.getBirthday());
            ageET.setText(model.getAge());
            usernameET.setText(model.getUsername());
            if (model.isFromGoogle() && layoutFrom.equals(PASSWORD)) {
                note.setVisibility(View.VISIBLE);
                save.setClickable(false);
                confirmPasswordET.setEnabled(false);
                currentPasswordET.setEnabled(false);
                passwordET.setEnabled(false);
            }
        }
    }

    @Override
    public void reAuthenticate(boolean verdict) {
        customProgressBar.clearAnimation();
        if (!verdict) {
            Toast.makeText(this, "Sorry but current password does not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Validation complete!", Toast.LENGTH_SHORT).show();
        save.setText(getString(R.string.save));
        isValid = true;
    }
}