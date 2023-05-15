package com.daedrii.bodyapp.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.BodyInfo;
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


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedBtnId = radioGroup.getCheckedRadioButtonId();

                if(selectedBtnId == -1)
                    Toast.makeText(GoalSignUp.this, "Nenhum objetivo selecionado", Toast.LENGTH_SHORT).show();
                else{
                    if(selectedBtnId == R.id.btn_loss)
                        InitSignUp.newUserBodyInfo.setGoal(BodyInfo.DietGoal.LOSS);
                    else if(selectedBtnId == R.id.btn_keep)
                        InitSignUp.newUserBodyInfo.setGoal(BodyInfo.DietGoal.KEEP);
                    else if(selectedBtnId == R.id.btn_gain)
                        InitSignUp.newUserBodyInfo.setGoal(BodyInfo.DietGoal.GAIN);

                    Intent intent = new Intent(GoalSignUp.this, GenderSignUp.class);
                    startActivity(intent);
                }
            }
        });
    }

}