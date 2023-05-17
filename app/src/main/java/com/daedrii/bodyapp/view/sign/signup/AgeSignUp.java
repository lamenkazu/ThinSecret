package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.BodyInfo;
import com.daedrii.bodyapp.view.HomeScreen;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class AgeSignUp extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_sign_up);

        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione seu nascimento")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                if(!SignUpController.handleAgeInfos(selection)){
                    Toast.makeText(AgeSignUp.this, "Idade muito pequena", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(AgeSignUp.this, HomeScreen.class);
                    startActivity(intent);
                }




            }
        });

        materialDatePicker.show(getSupportFragmentManager(), "tag");

    }
}