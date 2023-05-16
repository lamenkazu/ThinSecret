package com.daedrii.bodyapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.view.sign.signup.InitSignUp;

public class HomeScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Log.d("result", InitSignUp.newUserBodyInfo.toString());
    }


}