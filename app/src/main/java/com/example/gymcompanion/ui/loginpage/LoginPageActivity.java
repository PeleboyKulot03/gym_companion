package com.example.gymcompanion.ui.loginpage;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.forgotpassword.ForgotPasswordActivity;
import com.example.gymcompanion.ui.homepage.HomePageActivity;
import com.example.gymcompanion.ui.registration.RegistrationPageActivity;
import com.example.gymcompanion.utils.LoginPageModel;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginPageActivity extends AppCompatActivity implements ILoginPage {
    private SignInClient oneTapClient;
    private FirebaseAuth mAuth;
    private static final String TAG = "TAGERISM";
    private LoginPagePresenter presenter;
    private RelativeLayout activityRoot;
    private ImageView customProgressBar;
    private Animation animation;
    private EditText passwordET, emailET;
    private TextInputLayout textInputLayout;
    private static final String REGEX_EMAIL = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(this);
        presenter = new LoginPagePresenter(this);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        customProgressBar = findViewById(R.id.customProgressBar);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        textInputLayout = findViewById(R.id.textInputLayout);
        TextView signUp = findViewById(R.id.signUp);
        TextView forgotPassword = findViewById(R.id.forgotPassword);

        Button loginButton = findViewById(R.id.logIn);
        LinearLayout signInUsingGoogle = findViewById(R.id.signInUsingGoogle);
        LinearLayout createAccountLayout = findViewById(R.id.createAccountLayout);
        RelativeLayout divider = findViewById(R.id.divider);

        signInUsingGoogle.setOnClickListener(view -> {
            customProgressBar.startAnimation(animation);
            createSignInRequest();
        });

        // attaching layoutListener to the root view to monitor if soft keyboard is shown
        activityRoot = findViewById(R.id.activityRoot);
        activityRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            // check if the keyboard is shown
            int heightDiff = activityRoot.getRootView().getHeight() - activityRoot.getHeight();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            params.setMargins(0, 0, 0, 30);
            if (heightDiff > dpToPx(getApplicationContext(), 200)) {
                params.weight = 0;
                createAccountLayout.setLayoutParams(params);
                divider.setVisibility(View.GONE);
                return;
            }
            createAccountLayout.setLayoutParams(params);
            divider.setVisibility(View.VISIBLE);

        });


        loginButton.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            if (isInputCorrect(email, password)){
                customProgressBar.startAnimation(animation);
                presenter.signIn(email, password);
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // changing back the end icon to showing password toggle
                textInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            }
        });

        signUp.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegistrationPageActivity.class)));

        forgotPassword.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void createSignInRequest(){
        //build the google's one tap signIn options
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();

        // show the one tap signIn UI
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(LoginPageActivity.this, result -> {
                    customProgressBar.clearAnimation();
                    launcher.launch(new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build());
                    Log.e(TAG, "createSignInRequest: success");
                })
                .addOnFailureListener(LoginPageActivity.this, e -> {
                    customProgressBar.clearAnimation();
                    Log.e(TAG, "createSignInRequest: " + e.getLocalizedMessage());
                });
    }
    private final ActivityResultLauncher<IntentSenderRequest> launcher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    try {
                        // get the result credentials from tapping in google one tap signIn
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        if (idToken != null) {
                            // pass the credentials to firebase and create a new account if there is none
                            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                            mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // create a new user account in real time database
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        customProgressBar.startAnimation(animation);
                                        LoginPageModel model = new LoginPageModel(user.getDisplayName(), user.getEmail(), Objects.requireNonNull(user.getPhotoUrl()).toString());
                                        presenter.createNewUser(model, user.getUid());
                                        storePreferences(user.getDisplayName());
                                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                                        finish();
                                    }
                                }
                                else {
                                    Toast.makeText(this, "An error occurred please try again later!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (ApiException ignored) {

                    }
                }
            }
    );

    private void storePreferences(String displayName) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.display_name_key), displayName);
        editor.apply();
    }

    private boolean isInputCorrect(String username, String password){
        if (username.isEmpty()){
            emailET.setError("username cannot be empty!");
            emailET.requestFocus();
            return false;
        }
        if (!username.matches(REGEX_EMAIL)){
            emailET.requestFocus();
            emailET.setError("Please enter valid email address");
            return false;
        }
        if (password.isEmpty()){
            textInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
            passwordET.setError("password cannot be empty!");
            passwordET.requestFocus();
            return false;
        }
        hideSoftKeyboard(emailET);
        return true;
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void createNewUser(boolean verdict) {
        customProgressBar.clearAnimation();
        if (verdict){
            Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "An error occurred, please try again later!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void signResult(boolean verdict, String errorMessage, String displayName) {
        customProgressBar.clearAnimation();
        if (!verdict) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginPageActivity.this);
            builder.setTitle("Login Failed!")
                    .setMessage(errorMessage)
                    .setPositiveButton("Try again", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();

            builder.show();
            return;
        }
        storePreferences(displayName);
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
    }
}