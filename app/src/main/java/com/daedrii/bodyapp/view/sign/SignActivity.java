package com.daedrii.bodyapp.view.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.view.sign.signup.InitSignUp;
import com.google.android.material.button.MaterialButton;

public class SignActivity extends AppCompatActivity {

    MaterialButton newSignUp;

    private void setComponents(){
        newSignUp = findViewById(R.id.newUserButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        setComponents();

        newSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(SignActivity.this, InitSignUp.class);
                startActivity(signUpIntent);
            }
        });
    }
}