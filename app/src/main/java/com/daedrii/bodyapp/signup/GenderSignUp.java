package com.daedrii.bodyapp.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.SignActivity;
import com.daedrii.bodyapp.BodyInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class GenderSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_sign_up);

        MaterialButtonToggleGroup orientationToggle = findViewById(R.id.toggle_orientation);
        MaterialButton orientationButton = findViewById(R.id.toggle_orientation_button);
        MaterialButton boyButton = findViewById(R.id.toggle_boy);
        MaterialButton girlButton = findViewById(R.id.toggle_girl);
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggle_group_gender);
        MaterialButton next = findViewById(R.id.gender_next);


        orientationToggle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.toggle_orientation_button){
                    if(isChecked){
                        orientationButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.pink, getTheme()));
                    }else{
                        orientationButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
                    }
                }
            }
        });

        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.toggle_boy) {
                    if(isChecked){
                        girlButton.setChecked(false);
                        boyButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainPrimary, getTheme()));

                        InitSignUp.newUserBodyInfo.setSex(BodyInfo.Sex.MASCULINO);

                    }else{
                        boyButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainSecond, getTheme()));
                        InitSignUp.newUserBodyInfo.setSex(BodyInfo.Sex.NULO);
                    }

                } else if (checkedId == R.id.toggle_girl) {
                    if(isChecked){
                        boyButton.setChecked(false);
                        girlButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainPrimary, getTheme()));

                        InitSignUp.newUserBodyInfo.setSex(BodyInfo.Sex.FEMININO);

                    }else{
                        girlButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainSecond, getTheme()));
                        InitSignUp.newUserBodyInfo.setSex(BodyInfo.Sex.NULO);
                    }

                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InitSignUp.newUserBodyInfo.getGender() == BodyInfo.Sex.NULO)
                    Toast.makeText(GenderSignUp.this, "Selecione um genero para prosseguir", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(GenderSignUp.this, ActLevelSignUp.class);
                    startActivity(intent);
                }


            }
        });

    }



}








