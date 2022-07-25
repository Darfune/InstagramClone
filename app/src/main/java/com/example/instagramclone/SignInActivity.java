package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private Uri imageUri;
    Boolean signUpModeActive = true;
    TextView decisionTextView;
    EditText usernameEditText;
    EditText passEditText;
    EditText rePassEditText;
    ConstraintLayout background;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        usernameEditText = findViewById(R.id.usernameEditText);
        passEditText = findViewById(R.id.passEditText);
        rePassEditText = findViewById(R.id.rePassEditText);
        background = findViewById(R.id.backgoundLayout);

        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, UserActivity.class));
        }

        decisionTextView = findViewById(R.id.decisionTextView);
        decisionTextView.setOnClickListener(this);
        rePassEditText = findViewById(R.id.rePassEditText);

        passEditText.setOnKeyListener(this);
        rePassEditText.setOnKeyListener(this);
        background.setOnClickListener(this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }


    public void onClick(View view) {
        if (view.getId() == R.id.decisionTextView) {
            Button button = findViewById(R.id.signinBtn);

            if (signUpModeActive) {
                signUpModeActive = false;

                rePassEditText.setVisibility(View.INVISIBLE);
                button.setText("Login");
                decisionTextView.setText("or, Sign Up");
            } else {
                signUpModeActive = true;
                rePassEditText.setVisibility(View.VISIBLE);
                button.setText("Sign Up");
                decisionTextView.setText("or, Login");
            }
        } else if (view.getId() == R.id.backgoundLayout) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void signUpClicked(View view) throws IOException {

        if (!signUpModeActive) {
            if (usernameEditText.getText().toString().matches("") || passEditText.getText().toString().matches("")) {
                Toast.makeText(SignInActivity.this, "You need to enter your username and password", Toast.LENGTH_LONG).show();
            } else {
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {


                            ArrayList<String> testStringArrayList = (ArrayList<String>) user.get("isFollowed");
                            String myString = testStringArrayList.get(0);
                            Log.i("In Log", myString);
                            Toast.makeText(SignInActivity.this, "Logged in!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignInActivity.this, UserActivity.class));
                        } else {
                            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            if (usernameEditText.getText().toString().matches("") || passEditText.getText().toString().matches("") || rePassEditText.getText().toString().matches("")) {
                Toast.makeText(SignInActivity.this, "You need to enter a username and a password", Toast.LENGTH_LONG).show();
            } else if (!passEditText.getText().toString().matches(rePassEditText.getText().toString())) {
                Toast.makeText(SignInActivity.this, "The passwords don't match", Toast.LENGTH_LONG).show();
            } else {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            startActivity(new Intent(SignInActivity.this, ProfileCreationActivity.class));
                        } else {
                            Toast.makeText(SignInActivity.this, "User already exists", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }

    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN && ((v.getId() == R.id.rePassEditText && signUpModeActive) || (v.getId() == R.id.passEditText && !signUpModeActive))) {
            try {
                signUpClicked(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}