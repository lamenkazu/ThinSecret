package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.google.android.material.button.MaterialButton;

public class DietTypeSignUp extends AppCompatActivity {

    MaterialButton next;
    RadioGroup radioGroup;

    TextView lowCarb1 , lowCarb2, midCarb1, midCarb2, highCarb1, highCarb2;

    private void initComponents(){
        next = findViewById(R.id.diet_next);
        radioGroup = findViewById(R.id.radio_diet);

        lowCarb1 = findViewById(R.id.lbl_lowcarb1);
        lowCarb2 = findViewById(R.id.lbl_lowcarb2);
        midCarb1 = findViewById(R.id.lbl_midcarb1);
        midCarb2 = findViewById(R.id.lbl_midcarb2);
        highCarb1 = findViewById(R.id.lbl_highcarb1);
        highCarb2 = findViewById(R.id.lbl_highcarb2);
    }

    private void updateTextVisibility(int checkedId) {
        lowCarb1.setVisibility(checkedId == R.id.btn_lowcarb ? View.VISIBLE : View.GONE);
        lowCarb2.setVisibility(checkedId == R.id.btn_lowcarb ? View.VISIBLE : View.GONE);

        midCarb1.setVisibility(checkedId == R.id.btn_midcarb ? View.VISIBLE : View.GONE);
        midCarb2.setVisibility(checkedId == R.id.btn_midcarb ? View.VISIBLE : View.GONE);

        highCarb1.setVisibility(checkedId == R.id.btn_highcarb ? View.VISIBLE : View.GONE);
        highCarb2.setVisibility(checkedId == R.id.btn_highcarb ? View.VISIBLE : View.GONE);
        // Atualize a visibilidade dos outros botões de texto conforme necessário
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_type);

        initComponents();
        SignUpController.getNewBodyInfo().setDiet(null);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateTextVisibility(checkedId);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedBtnId = radioGroup.getCheckedRadioButtonId();

                if (selectedBtnId == R.id.btn_lowcarb)
                    SignUpController.setDiet(BodyInfo.DietType.LowCarb);
                else if (selectedBtnId == R.id.btn_midcarb)
                    SignUpController.setDiet(BodyInfo.DietType.MidCarb);
                else if (selectedBtnId == R.id.btn_highcarb)
                    SignUpController.setDiet(BodyInfo.DietType.HighCarb);


                decide();


            }
        });
    }

    public Boolean decide(){

        Boolean success = false;

        try{
            if (SignUpController.getNewBodyInfo().getDiet() == null){
                throw new EmptyFieldException(getString(R.string.exception_empty_field));
            }else{
                success = true;
                Intent intent = new Intent(DietTypeSignUp.this, GenderSignUp.class);
                startActivity(intent);
            }
        }catch (EmptyFieldException e){
            Toast.makeText(DietTypeSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        return success;
    }
}