package com.daedrii.bodyapp.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daedrii.bodyapp.BodyInfo;
import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.SignActivity;
import com.google.android.material.button.MaterialButton;

public class InitSignUp extends AppCompatActivity {

    public static BodyInfo newUserBodyInfo = new BodyInfo();

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