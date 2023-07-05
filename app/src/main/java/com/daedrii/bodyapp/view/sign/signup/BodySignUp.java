package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
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

                decide(heightS, weightS);


            }
        });
    }

    public Boolean decide(String heightS, String weightS){

        Boolean success = false;
        try{
            if(heightS.equals("") || weightS.equals(""))
                throw new EmptyFieldException(getString(R.string.exception_empty_field));
            else{

                Double heighInM = Double.parseDouble(heightS) / 100;
                Integer givenHeight = Integer.parseInt(heightS);
                Integer givenWeight = Integer.parseInt(weightS);

                Double generatedIMC = (givenWeight) / (heighInM * heighInM);

                SignUpController.setBodyData(givenHeight, givenWeight, generatedIMC);

                success = true;

                Intent intent = new Intent(BodySignUp.this, AgeSignUp.class);
                startActivity(intent);
            }
        }catch (EmptyFieldException e){
            Toast.makeText(BodySignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        return success;
    }
}