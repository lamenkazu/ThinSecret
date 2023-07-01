package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

                final int minimalAge = 14;

                LocalDate currentDate = LocalDate.now();
                LocalDate birthDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.systemDefault()).toLocalDate();

                String date = birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                long idade = ChronoUnit.YEARS.between(birthDate, currentDate);

                if (idade < minimalAge) {
                    Toast.makeText(AgeSignUp.this, "Idade muito pequena", Toast.LENGTH_SHORT).show();
                } else {
                    SignUpController.handleAgeInfos(idade, date);
                }

                Integer userAge = SignUpController.getNewBodyInfo().getAge();

                if(userAge == 0 || userAge < minimalAge){
                }else{
                    Intent intent = new Intent(AgeSignUp.this, UserSignUp.class);
                    startActivity(intent);
                }

            }

        });

        materialDatePicker.show(getSupportFragmentManager(), "tag");

    }
}