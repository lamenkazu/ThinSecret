package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.google.android.material.button.MaterialButton;

public class GoalSignUp extends AppCompatActivity {

    MaterialButton next;
    RadioGroup radioGroup;

    private void startComponents(){
        next = findViewById(R.id.goal_next);
        radioGroup = findViewById(R.id.radio_goal);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_sign_up);

        startComponents();
        SignUpController.setGoal(null);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedBtnId = radioGroup.getCheckedRadioButtonId();

                if (selectedBtnId == R.id.btn_loss)
                    SignUpController.setGoal(BodyInfo.DietGoal.LOSS);
                else if (selectedBtnId == R.id.btn_keep)
                    SignUpController.setGoal(BodyInfo.DietGoal.KEEP);
                else if (selectedBtnId == R.id.btn_gain)
                    SignUpController.setGoal(BodyInfo.DietGoal.GAIN);


                decide();



            }
        });
    }

    public Boolean decide(){

        Boolean success = false;
        try{
            if (SignUpController.getNewBodyInfo().getGoal() == null){
                throw new EmptyFieldException(getString(R.string.exception_empty_field));
            }else{
                success = true;
                Intent intent = new Intent(GoalSignUp.this, DietTypeSignUp.class);
                startActivity(intent);
            }

        }catch(EmptyFieldException e){
            Toast.makeText(GoalSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        return success;
    }

}