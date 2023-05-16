package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.BodyInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class GenderSignUp extends AppCompatActivity {
    MaterialButtonToggleGroup orientationToggle ;
    MaterialButton orientationButton ;
    MaterialButton boyButton ;
    MaterialButton girlButton ;
    MaterialButtonToggleGroup toggleGroup ;
    MaterialButton next ;

    private void startComponents(){
        orientationToggle = findViewById(R.id.toggle_orientation);
        orientationButton = findViewById(R.id.toggle_orientation_button);
        boyButton = findViewById(R.id.toggle_boy);
        girlButton = findViewById(R.id.toggle_girl);
        toggleGroup = findViewById(R.id.toggle_group_gender);
        next = findViewById(R.id.gender_next);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_sign_up);

        startComponents();


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
                SignUpController.handleGenderChance(checkedId, isChecked, boyButton, girlButton, getResources(), getTheme());
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SignUpController.genderNull())

                    Toast.makeText(GenderSignUp.this, "Selecione um genero para prosseguir", Toast.LENGTH_SHORT).show();

                else{
                    Intent intent = new Intent(GenderSignUp.this, ActLevelSignUp.class);
                    startActivity(intent);
                }


            }
        });

    }



}







