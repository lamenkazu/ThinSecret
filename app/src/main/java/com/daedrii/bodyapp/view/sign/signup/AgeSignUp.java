package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.InvalidAgeException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AgeSignUp extends AppCompatActivity {

    TextInputEditText txtDate;
    MaterialDatePicker<Long> materialDatePicker;
    MaterialButton next;

    static String date;
    static Long longDate;

    private void initComponent(){
        txtDate = findViewById(R.id.age_txt_date);

        next = findViewById(R.id.age_next);

        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione seu nascimento")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_sign_up);

        final int minimalAge = 14;

        initComponent();

        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!materialDatePicker.isAdded())
                        materialDatePicker.show(getSupportFragmentManager(), "tag");
                }
            }
        });


        next.setOnClickListener(v -> {

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthDate = LocalDate.parse(txtDate.getText().toString(), formatter);
            longDate = ChronoUnit.YEARS.between(birthDate, currentDate);


            decide(longDate, minimalAge, date);

        });


        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {


                LocalDate currentDate = LocalDate.now();
                LocalDate birthDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.systemDefault()).toLocalDate();

                date = birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                txtDate.setText(date);
                longDate = ChronoUnit.YEARS.between(birthDate, currentDate);

            }

        });

    }

    public Boolean decide(long idade, int minimalAge, String date) {
        Boolean success = false;

        try{
            SignUpController.handleAgeInfos(idade, date);

            Integer userAge = SignUpController.getNewBodyInfo().getAge();

            if(userAge == 0 || userAge < minimalAge){
                throw new InvalidAgeException(getString(R.string.exception_invalid_age));
            }else if (userAge >= minimalAge){
                success = true;
                Intent intent = new Intent(AgeSignUp.this, UserSignUp.class);
                startActivity(intent);

            }
        }catch (Exception e){
            Toast.makeText(AgeSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        return success;
    }
}