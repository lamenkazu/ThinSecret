package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class GenderSignUp extends AppCompatActivity {
    MaterialButtonToggleGroup orientationToggle ;
    MaterialButton orientationButton ;
    MaterialButton boyButton ;
    MaterialButton girlButton ;
    MaterialButtonToggleGroup genderToggle;
    MaterialButton next ;

    private void startComponents(){
        orientationToggle = findViewById(R.id.toggle_orientation);
        orientationButton = findViewById(R.id.toggle_orientation_button);
        boyButton = findViewById(R.id.toggle_boy);
        girlButton = findViewById(R.id.toggle_girl);
        genderToggle = findViewById(R.id.toggle_group_gender);
        next = findViewById(R.id.gender_next);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_sign_up);

        startComponents();
        SignUpController.getNewBodyInfo().setGender(null);
        SignUpController.getNewBodyInfo().setTransgender(false);


        orientationToggle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.toggle_orientation_button){
                    if(isChecked){
                        orientationButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.pink, getTheme()));
                        SignUpController.getNewBodyInfo().setTransgender(true);
                    }else{
                        orientationButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
                        SignUpController.getNewBodyInfo().setTransgender(false);

                    }
                }
            }
        });

        genderToggle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                BodyInfo.Sex selectedGender = null;

                if (checkedId == R.id.toggle_boy) {

                    if(isChecked){
                        girlButton.setChecked(false);
                        boyButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainPrimary, getTheme()));
                        selectedGender = BodyInfo.Sex.MASCULINO;

                    }else{
                        boyButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainSecond, getTheme()));
                        SignUpController.getNewBodyInfo().setGender(null);

                    }


                } else if (checkedId == R.id.toggle_girl) {
                    if(isChecked){
                        boyButton.setChecked(false);
                        girlButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainPrimary, getTheme()));
                        selectedGender = BodyInfo.Sex.FEMININO;


                    }else{
                        girlButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainSecond, getTheme()));
                        SignUpController.getNewBodyInfo().setGender(null);
                    }
                }
                if (selectedGender != null) {
                    SignUpController.handleGenderChange(selectedGender);
                }

            }

        });


        next.setOnClickListener(v -> {
            decide();
        });


    }

    public Boolean decide(){
        Boolean success = false;
        try{
            if(SignUpController.getNewBodyInfo().getGender() == null)
                throw new EmptyFieldException(getString(R.string.exception_empty_field));
            else {
                success = true;
                Intent intent = new Intent(GenderSignUp.this, ActLevelSignUp.class);
                startActivity(intent);
            }
        }catch (EmptyFieldException e){
            Toast.makeText(GenderSignUp.this, "Selecione um genero para prosseguir", Toast.LENGTH_SHORT).show();

        }

        return success;
    }
}








