package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daedrii.bodyapp.R;
import com.google.android.material.button.MaterialButton;

public class InitSignUp extends AppCompatActivity {

    MaterialButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_sign_up);

        btnNext = findViewById(R.id.init_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitSignUp.this, GoalSignUp.class);

                startActivity(intent);
            }
        });

    }
}