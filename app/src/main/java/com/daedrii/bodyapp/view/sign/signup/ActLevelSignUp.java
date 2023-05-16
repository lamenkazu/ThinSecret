package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.BodyInfo;
import com.google.android.material.button.MaterialButton;

public class ActLevelSignUp extends AppCompatActivity {

    MaterialButton next;
    RadioGroup radioGroup;

    private void startComponents(){
        next = findViewById(R.id.act_next);
        radioGroup = findViewById(R.id.radio_act_level);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_sign_up);

        startComponents();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!SignUpController.setActLevel(radioGroup)){
                   Toast.makeText(ActLevelSignUp.this, "Nenhum objetivo selecionado", Toast.LENGTH_SHORT).show();
               }else{
                   Intent intent = new Intent(ActLevelSignUp.this, BodySignUp.class);
                   startActivity(intent);
               }

            }
        });
    }
}