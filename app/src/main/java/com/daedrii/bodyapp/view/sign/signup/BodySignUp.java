package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class BodySignUp extends AppCompatActivity {

    MaterialButton next;
    TextInputEditText height;
    TextInputEditText weight;

    private void startComponents(){
        next = findViewById(R.id.body_next);
        height = findViewById(R.id.heigh_field);
        weight = findViewById(R.id.weight_field);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_sign_up);

        startComponents();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String heightS = height.getText().toString();
                String weightS = weight.getText().toString();

                if(heightS.equals("") || weightS.equals(""))
                    Toast.makeText(BodySignUp.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                else{

                    SignUpController.setBodyData(heightS, weightS);

                    Intent intent = new Intent(BodySignUp.this, AgeSignUp.class);
                    startActivity(intent);
                }


            }
        });
    }
}