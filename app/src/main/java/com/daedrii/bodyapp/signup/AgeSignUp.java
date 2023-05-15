package com.daedrii.bodyapp.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.daedrii.bodyapp.BodyInfo;
import com.daedrii.bodyapp.HomeScreen;
import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.SignActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class AgeSignUp extends AppCompatActivity {

    private void finalizaCalculosCorporais(){
        //Finaliza Calculos Corporais
        BodyInfo bodyInfo = InitSignUp.newUserBodyInfo;
        Double metBasal = bodyInfo.getActLevel().getMetBasal();
        Double step1, step2, step3, somaSteps;

        if(bodyInfo.getGender() == BodyInfo.Sex.MASCULINO){
            step1 = 13.7 * bodyInfo.getWeight();
            step2 = 5.0 * bodyInfo.getHeight();
            step3 = 6.8 * bodyInfo.getAge();
            somaSteps = 66 + (step1 + step2 - step3);
            InitSignUp.newUserBodyInfo.setIDR(metBasal * somaSteps);

        }else if(bodyInfo.getGender() == BodyInfo.Sex.FEMININO){
            step1 = 9.6 * bodyInfo.getWeight();
            step2 = 1.8 * bodyInfo.getHeight();
            step3 = 4.7 * bodyInfo.getAge();
            somaSteps = 655 + (step1 + step2 - step3);
            InitSignUp.newUserBodyInfo.setIDR(metBasal * somaSteps);
        }
    }

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

                Integer anoAtual = new Date().getYear();
                Integer mesAtual = new Date().getMonth();
                Integer diaAtual = new Date().getDay();

                Integer anoNasc = new Date(selection).getYear();
                Integer mesNasc = new Date(selection).getMonth();
                Integer diaNasc = new Date(selection).getDay();

                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(selection));

                LocalDate endDate = LocalDate.of(anoAtual, mesAtual, diaAtual);
                LocalDate startDate = LocalDate.of(anoNasc, mesNasc, diaNasc);

                long idade = ChronoUnit.YEARS.between(startDate, endDate);

                if(idade < 14){
                    Toast.makeText(AgeSignUp.this, "Idade muito pequena", Toast.LENGTH_SHORT).show();
                }else{

                    InitSignUp.newUserBodyInfo.setAge((int) idade);
                    InitSignUp.newUserBodyInfo.setBirthDate(date);

                    finalizaCalculosCorporais();

                    Intent intent = new Intent(AgeSignUp.this, HomeScreen.class);
                    startActivity(intent);
                }


            }
        });

        materialDatePicker.show(getSupportFragmentManager(), "tag");

//        MaterialButton next = findViewById(R.id.age_next);
//
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AgeSignUp.this, HomeScreen.class);
//                startActivity(intent);
//            }
//        });
    }
}