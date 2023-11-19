package com.example.gymcompanion.ui.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.homepage.HomePageActivity;
import com.example.gymcompanion.utils.RegistrationPageModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrationPageActivity extends AppCompatActivity implements IRegistrationPage{
    private final Calendar calendar = Calendar.getInstance();
    private final String myDateFormat = "MM/dd/yy";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(myDateFormat, Locale.US);
    private ArrayList<View> levels;
    private ArrayList<LinearLayout> prompts;
    private TextView progress, birthday, headerPrompt;
    private int currentLevel = 0, userAge = 0;
    private EditText nameET, middleNameET, surnameET, weightET, heightET, ageET, emailET, passwordET;
    private CheckBox checkBox;
    private String finalName = "", finalMiddleName = "", finalSurname = "", finalWeight = "", finalHeight = "", finalBirthday = "", finalAge = "", finalGender = "", finalExperience = "", finalEmail = "", finalPassword = "";
    private Button navButton;
    private boolean isStart = true, isValid = false;
    private RegistrationPagePresenter presenter;
    private ImageView customProgressBar;
    private Animation animation;
    private static final String REGEX_EMAIL = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);

        presenter = new RegistrationPagePresenter(this);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        LinearLayout namePrompt = findViewById(R.id.namePrompt);
        LinearLayout birthdayPrompt = findViewById(R.id.birthdayPrompt);
        LinearLayout birthdayPicker = findViewById(R.id.birthdatePicker);
        LinearLayout bmiPrompt = findViewById(R.id.bmiPrompt);
        LinearLayout genderPrompt = findViewById(R.id.genderPrompt);
        LinearLayout credentialsPrompt = findViewById(R.id.credentialsPrompt);
        LinearLayout agreementPrompt = findViewById(R.id.agreementPrompt);
        LinearLayout levelPrompt = findViewById(R.id.levelPrompt);
        LinearLayout medicalRecordsPrompt = findViewById(R.id.medicalRecordsPrompt);


        View level1 = findViewById(R.id.level1);
        View level2 = findViewById(R.id.level2);
        View level3 = findViewById(R.id.level3);
        View level4 = findViewById(R.id.level4);
        View level5 = findViewById(R.id.level5);
        View level6 = findViewById(R.id.level6);
        View level7 = findViewById(R.id.level7);
        View level8 = findViewById(R.id.level8);


        nameET = findViewById(R.id.nameET);
        middleNameET = findViewById(R.id.middleNameET);
        surnameET = findViewById(R.id.surnameET);
        weightET = findViewById(R.id.weightET);
        heightET = findViewById(R.id.heightET);
        ageET = findViewById(R.id.ageET);
        birthday = findViewById(R.id.birthday);
        headerPrompt = findViewById(R.id.headerPrompt);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        checkBox = findViewById(R.id.checkBox);
        customProgressBar = findViewById(R.id.customProgressBar);

        LinearLayout male = findViewById(R.id.male);
        LinearLayout female = findViewById(R.id.female);
        LinearLayout others = findViewById(R.id.others);

        ArrayList<LinearLayout> genders = new ArrayList<>();
        genders.add(male);
        genders.add(female);
        genders.add(others);

        LinearLayout beginner = findViewById(R.id.beginner);
        LinearLayout advance = findViewById(R.id.advance);
        LinearLayout proficient = findViewById(R.id.proficient);
        LinearLayout expert = findViewById(R.id.expert);

        ArrayList<LinearLayout> expertise = new ArrayList<>();
        expertise.add(beginner);
        expertise.add(advance);
        expertise.add(proficient);
        expertise.add(expert);

        progress = findViewById(R.id.progress);

        levels = new ArrayList<>();
        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        levels.add(level4);
        levels.add(level5);
        levels.add(level6);
        levels.add(level7);
        levels.add(level8);

        prompts = new ArrayList<>();
        prompts.add(namePrompt);
        prompts.add(bmiPrompt);
        prompts.add(birthdayPrompt);
        prompts.add(genderPrompt);
        prompts.add(levelPrompt);
        prompts.add(credentialsPrompt);
        prompts.add(medicalRecordsPrompt);
        prompts.add(agreementPrompt);

        // clicking next
        navButton = findViewById(R.id.navButton);
        navButton.setOnClickListener(view -> {
            if (isStart){
                progressView(levels.get(currentLevel), R.color.lightPrimary);
                progressLayout(prompts.get(currentLevel), 1);
                isStart = false;
                headerPrompt.setText(getString(R.string.header_prompt));
                navButton.setText(getString(R.string.next));
                String levelText = ((currentLevel + 1) * 12.5) + "%";
                progress.setText(levelText);
                return;
            }

            if (isCorrect()){
                if (currentLevel >= 7){
                    customProgressBar.startAnimation(animation);
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = new Date();
                    RegistrationPageModel model = new RegistrationPageModel(finalName, finalMiddleName, finalSurname, finalName, finalWeight, finalHeight, finalBirthday, finalAge, finalGender, finalExperience, finalEmail, formatter.format(date));
                    presenter.createNewUser(finalEmail, finalPassword, model);
                    return;
                }
                progressView(levels.get(currentLevel), R.color.white);
                progressLayout(prompts.get(currentLevel), 0);
                currentLevel++;
                String levelText = ((currentLevel + 1) * 12.5) + "%";
                progress.setText(levelText);
                progressView(levels.get(currentLevel), R.color.lightPrimary);
                progressLayout(prompts.get(currentLevel), 1);
            }

        });



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
        birthdayPicker.setOnClickListener(view -> {
            new DatePickerDialog(RegistrationPageActivity.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            ageET.setError(null);
        });

        // controls for picking gender
        male.setOnClickListener(view -> {
            finalGender = "Male";
            removePicks(genders);
            male.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
        });

        female.setOnClickListener(view -> {
            finalGender = "Female";
            removePicks(genders);
            female.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
        });

        others.setOnClickListener(view -> {
            finalGender = "Others";
            removePicks(genders);
            others.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
        });

        // controls for picking current gym experience

        beginner.setOnClickListener(view -> {
            finalExperience = "Beginner";
            removePicks(expertise);
            beginner.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
        });

        advance.setOnClickListener(view -> {
            finalExperience = "Advance";
            removePicks(expertise);
            advance.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
        });

        proficient.setOnClickListener(view -> {
            finalExperience = "Proficient";
            removePicks(expertise);
            proficient.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
        });

        expert.setOnClickListener(view -> {
            finalExperience = "Expert";
            removePicks(expertise);
            expert.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rounded_border));
        });

        toolbar.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationPageActivity.this);
            builder.setTitle("Warning Notice")
                    .setMessage("Are you sure you want to go back to sign in page?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> super.onBackPressed())
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();

            builder.show();
        });

    }

    private void removePicks(ArrayList<LinearLayout> layouts) {
        for (LinearLayout layout: layouts){
            layout.setBackground(null);
        }
    }

    private void progressView(View view, int color) {
        ViewCompat.setBackgroundTintList(view, getColorStateList(color));
    }

    private void progressLayout(LinearLayout layout, int weight) {
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight));
    }

    private boolean isCorrect(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationPageActivity.this);
        switch (currentLevel) {
            case 0:
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
                hideSoftKeyboard(surnameET);
                break;

            case 1:
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
                hideSoftKeyboard(heightET);
                break;

            case 2:
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
                break;

            case 3:
                if (finalGender.isEmpty()){
                    builder.setTitle("Warning Notice")
                            .setMessage("Sorry but gender is a required field, please select your gender to continue!")
                            .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                    builder.show();
                    return false;
                }
                break;

            case 4:
                if (finalExperience.isEmpty()){
                    builder.setTitle("Warning Notice")
                            .setMessage("Sorry but experience is a required field, please select your experience to continue!")
                            .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                    builder.show();
                    return false;
                }
                navButton.setText(getString(R.string.validate));
                break;

            case 5:
                if (finalEmail.isEmpty() && finalPassword.isEmpty()){
                    if (emailET.getText().toString().isEmpty()){
                        emailET.setError("Please add your email address first");
                        emailET.requestFocus();
                        return false;
                    }
                    if (!emailET.getText().toString().matches(REGEX_EMAIL)){
                        emailET.requestFocus();
                        emailET.setError("Please enter valid email address");
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
                    hideSoftKeyboard(passwordET);
                    if (!isValid) {
                        customProgressBar.startAnimation(animation);
                        presenter.hasUser(emailET.getText().toString());
                        return false;
                    }
                }
                break;

            case 6:
                headerPrompt.setText(getString(R.string.agreement_prompt));
                navButton.setText(getString(R.string.finish));
                finalEmail = emailET.getText().toString();
                finalPassword = passwordET.getText().toString();
                break;

            case 7:
                if (!checkBox.isChecked()){
                    builder.setTitle("Warning Notice")
                            .setMessage("Sorry but you have to agree in the terms and policy of Gym Companion in order to continue")
                            .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                    builder.show();
                    return false;
                }
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
        if (currentLevel > 0){
            progressView(levels.get(currentLevel), R.color.white);
            progressLayout(prompts.get(currentLevel), 0);
            currentLevel--;
            String levelText = ((currentLevel + 1) * 12.5) + "%";
            progress.setText(levelText);
            progressView(levels.get(currentLevel), R.color.lightPrimary);
            progressLayout(prompts.get(currentLevel), 1);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationPageActivity.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationPageActivity.this);
            builder.setTitle("Warning Notice")
                    .setMessage("Sorry but the provided email address is currently a user, please change the email address or forgot the password in login page if " +
                            "you forgot it.")
                    .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();

            builder.show();
            return;
        }
        Toast.makeText(this, "Thank you for validating, you can now continue!", Toast.LENGTH_SHORT).show();
        navButton.setText(getString(R.string.next));
        isValid = true;
    }

    @Override
    public void createNewAccount(boolean verdict, String errorMessage) {
        customProgressBar.clearAnimation();
        if (!verdict) {
            Toast.makeText(this, "Sorry but " + errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Registration Complete!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
    }
}