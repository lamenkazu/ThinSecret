package com.daedrii.bodyapp.controller.sign;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.model.user.UserInfo;
import com.daedrii.bodyapp.view.sign.SignInActivity;
import com.daedrii.bodyapp.view.sign.signup.UserSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SignUpController {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference userRef = database.getReference("UserInfo");
    private static DatabaseReference bodyRef = database.getReference("BodyInfo");
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static BodyInfo newBodyInfo = new BodyInfo();
    public static UserInfo newUserInfo = new UserInfo();

    private static final String USER_PREF_ID = "UserPref";



    public static void handleUserDataSignUp(String userName, String userPhone, String userMail, String userPassword, LinearProgressIndicator progressIndicator, Context applicationContext){
        newUserInfo.setEmail(userMail);
        newUserInfo.setName(userName);
        newUserInfo.setPhone(userPhone);
        newUserInfo.setBodyInfo(newBodyInfo);


        mAuth.createUserWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressIndicator.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            userRef.child(mAuth.getCurrentUser().getUid())
                                    .setValue(newUserInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            bodyRef.child(mAuth.getCurrentUser().getUid())
                                                    .setValue(newBodyInfo)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(applicationContext, "Conta criada com Sucesso.",
                                                                    Toast.LENGTH_SHORT).show();

                                                            mAuth.signOut();
                                                        }
                                                    });
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(applicationContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static Boolean handleAgeInfos(Long selection){
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.systemDefault()).toLocalDate();

        String date = birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        long idade = ChronoUnit.YEARS.between(birthDate, currentDate);

        if (idade < 14) {
            return false;
        } else {
            newBodyInfo.setAge((int) idade);
            newUserInfo.setBirthDate(date);

            finalizaCalculosCorporais();

            return true;
        }
    }

    private static void finalizaCalculosCorporais(){
        //Finaliza Calculos Corporais
        BodyInfo bodyInfo = newBodyInfo;
        Double metBasal = bodyInfo.getActLevel().getMetBasal();
        Double step1, step2, step3, somaSteps;

        if(bodyInfo.getGender() == BodyInfo.Sex.MASCULINO){
            step1 = 13.7 * bodyInfo.getWeight();
            step2 = 5.0 * bodyInfo.getHeight();
            step3 = 6.8 * bodyInfo.getAge();
            somaSteps = 66 + (step1 + step2 - step3);
            newBodyInfo.setIDR(metBasal * somaSteps);

        }else if(bodyInfo.getGender() == BodyInfo.Sex.FEMININO){
            step1 = 9.6 * bodyInfo.getWeight();
            step2 = 1.8 * bodyInfo.getHeight();
            step3 = 4.7 * bodyInfo.getAge();
            somaSteps = 655 + (step1 + step2 - step3);
            newBodyInfo.setIDR(metBasal * somaSteps);
        }
    }

    public static Boolean setActLevel(RadioGroup radioGroup){

        int selectedBtnId = radioGroup.getCheckedRadioButtonId();

        if(selectedBtnId == -1)
            return false;
        else{
            if(selectedBtnId == R.id.btn_sedentary)
                newBodyInfo.setActLevel(BodyInfo.ActLevel.SEDENTARY);

            else if(selectedBtnId == R.id.btn_low)
                newBodyInfo.setActLevel(BodyInfo.ActLevel.LOW_ACTIVE);

            else if(selectedBtnId == R.id.btn_medium)
                newBodyInfo.setActLevel(BodyInfo.ActLevel.ACTIVE);

            else if(selectedBtnId == R.id.btn_high)
                newBodyInfo.setActLevel(BodyInfo.ActLevel.HIGH_ACTIVE);

            else if(selectedBtnId == R.id.btn_extreme)
                newBodyInfo.setActLevel(BodyInfo.ActLevel.EXTREME_ACTIVE);

            return true;
        }
    }


    public static void setBodyData(String height, String weight){

        Double heighInM = Double.parseDouble(height) / 100;
        Integer givenHeight = Integer.parseInt(height);
        Integer givenWeight = Integer.parseInt(weight);

        Double IMC = (givenWeight) / (heighInM * heighInM);

        newBodyInfo.setHeight(givenHeight);
        newBodyInfo.setWeight(givenWeight);
        newBodyInfo.setIMC(IMC);
    }

    public static Boolean setGoal(RadioGroup radioGroup){
        int selectedBtnId = radioGroup.getCheckedRadioButtonId();

        if(selectedBtnId == -1)
            return false;
        else{
            if(selectedBtnId == R.id.btn_loss)
                newBodyInfo.setGoal(BodyInfo.DietGoal.LOSS);
            else if(selectedBtnId == R.id.btn_keep)
                newBodyInfo.setGoal(BodyInfo.DietGoal.KEEP);
            else if(selectedBtnId == R.id.btn_gain)
                newBodyInfo.setGoal(BodyInfo.DietGoal.GAIN);

            return true;
        }
    }

    public static void handleGenderChance(int checkedId, boolean isChecked,
                                          MaterialButton boyButton, MaterialButton girlButton,
                                          Resources resources, Resources.Theme theme){
        if (checkedId == R.id.toggle_boy) {
            if(isChecked){
                girlButton.setChecked(false);
                boyButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainPrimary, theme));

                SignUpController.newBodyInfo.setSex(BodyInfo.Sex.MASCULINO);

            }else{
                boyButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainSecond, theme));
                SignUpController.newBodyInfo.setSex(BodyInfo.Sex.NULO);
            }

        } else if (checkedId == R.id.toggle_girl) {
            if(isChecked){
                boyButton.setChecked(false);
                girlButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainPrimary, theme));

                SignUpController.newBodyInfo.setSex(BodyInfo.Sex.FEMININO);

            }else{
                girlButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainSecond, theme));
                SignUpController.newBodyInfo.setSex(BodyInfo.Sex.NULO);
            }

        }
    }

    public static Boolean genderNull(){
        return newBodyInfo.getGender() == BodyInfo.Sex.NULO;
    }



}
