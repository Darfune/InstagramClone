package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;

import android.os.Bundle;

import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        }
    }
}