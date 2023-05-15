package com.daedrii.bodyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.daedrii.bodyapp.signup.InitSignUp;

public class HomeScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        }
        Log.d("result", InitSignUp.newUserBodyInfo.toString());


    }
}