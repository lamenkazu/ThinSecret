package com.daedrii.bodyapp.view.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.view.sign.signup.InitSignUp;
import com.google.android.material.button.MaterialButton;

public class SignActivity extends AppCompatActivity {

    MaterialButton newSignUp, goToSignIn;

    private void setComponents(){
        newSignUp = findViewById(R.id.newUserButton);
        goToSignIn = findViewById(R.id.btn_goto_sign_in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        setComponents();

        newSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getApplicationContext(), InitSignUp.class);
                startActivity(signUpIntent);
            }
        });

        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInIntent);
            }
        });
    }
}