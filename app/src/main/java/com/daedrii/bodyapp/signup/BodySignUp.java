package com.daedrii.bodyapp.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.SignActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class BodySignUp extends AppCompatActivity {

    MaterialButton next;
    TextInputEditText heigh;
    TextInputEditText weight;

    private void startComponents(){
        next = findViewById(R.id.body_next);
        heigh = findViewById(R.id.heigh_field);
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

                if(heigh.getText().toString().equals("") || weight.getText().toString().equals(""))
                    Toast.makeText(BodySignUp.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                else{
                    Integer givenHeight = Integer.parseInt(heigh.getText().toString());
                    Integer givenWeight = Integer.parseInt(weight.getText().toString());

                    InitSignUp.newUserBodyInfo.setHeight(givenHeight);
                    InitSignUp.newUserBodyInfo.setWeight(givenWeight);

                    Intent intent = new Intent(BodySignUp.this, AgeSignUp.class);
                    startActivity(intent);
                }


            }
        });
    }
}