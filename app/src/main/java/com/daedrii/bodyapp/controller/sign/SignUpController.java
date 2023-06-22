package com.daedrii.bodyapp.controller.sign;

import android.content.res.Resources;
import android.widget.RadioGroup;

import androidx.core.content.res.ResourcesCompat;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.model.BodyInfo;
import com.google.android.material.button.MaterialButton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SignUpController {

    public static BodyInfo newUserBodyInfo = new BodyInfo();

    public static Boolean handleAgeInfos(Long selection){
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.systemDefault()).toLocalDate();

        String date = birthDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        long idade = ChronoUnit.YEARS.between(birthDate, currentDate);

        if (idade < 14) {
            return false;
        } else {
            newUserBodyInfo.setAge((int) idade);
            newUserBodyInfo.setBirthDate(date);

            finalizaCalculosCorporais();

            return true;
        }
    }

    private static void finalizaCalculosCorporais(){
        //Finaliza Calculos Corporais
        BodyInfo bodyInfo = newUserBodyInfo;
        Double metBasal = bodyInfo.getActLevel().getMetBasal();
        Double step1, step2, step3, somaSteps;

        if(bodyInfo.getGender() == BodyInfo.Sex.MASCULINO){
            step1 = 13.7 * bodyInfo.getWeight();
            step2 = 5.0 * bodyInfo.getHeight();
            step3 = 6.8 * bodyInfo.getAge();
            somaSteps = 66 + (step1 + step2 - step3);
            newUserBodyInfo.setIDR(metBasal * somaSteps);

        }else if(bodyInfo.getGender() == BodyInfo.Sex.FEMININO){
            step1 = 9.6 * bodyInfo.getWeight();
            step2 = 1.8 * bodyInfo.getHeight();
            step3 = 4.7 * bodyInfo.getAge();
            somaSteps = 655 + (step1 + step2 - step3);
            newUserBodyInfo.setIDR(metBasal * somaSteps);
        }
    }

    public static Boolean setActLevel(RadioGroup radioGroup){

        int selectedBtnId = radioGroup.getCheckedRadioButtonId();

        if(selectedBtnId == -1)
            return false;
        else{
            if(selectedBtnId == R.id.btn_sedentary)
                newUserBodyInfo.setActLevel(BodyInfo.ActLevel.SEDENTARY);

            else if(selectedBtnId == R.id.btn_low)
                newUserBodyInfo.setActLevel(BodyInfo.ActLevel.LOW_ACTIVE);

            else if(selectedBtnId == R.id.btn_medium)
                newUserBodyInfo.setActLevel(BodyInfo.ActLevel.ACTIVE);

            else if(selectedBtnId == R.id.btn_high)
                newUserBodyInfo.setActLevel(BodyInfo.ActLevel.HIGH_ACTIVE);

            else if(selectedBtnId == R.id.btn_extreme)
                newUserBodyInfo.setActLevel(BodyInfo.ActLevel.EXTREME_ACTIVE);

            return true;
        }
    }


    public static void setBodyData(String height, String weight){

        Double heighInM = Double.parseDouble(height) / 100;
        Integer givenHeight = Integer.parseInt(height);
        Integer givenWeight = Integer.parseInt(weight);

        Double IMC = (givenWeight) / (heighInM * heighInM);

        newUserBodyInfo.setHeight(givenHeight);
        newUserBodyInfo.setWeight(givenWeight);
        newUserBodyInfo.setIMC(IMC);
    }

    public static Boolean setGoal(RadioGroup radioGroup){
        int selectedBtnId = radioGroup.getCheckedRadioButtonId();

        if(selectedBtnId == -1)
            return false;
        else{
            if(selectedBtnId == R.id.btn_loss)
                newUserBodyInfo.setGoal(BodyInfo.DietGoal.LOSS);
            else if(selectedBtnId == R.id.btn_keep)
                newUserBodyInfo.setGoal(BodyInfo.DietGoal.KEEP);
            else if(selectedBtnId == R.id.btn_gain)
                newUserBodyInfo.setGoal(BodyInfo.DietGoal.GAIN);

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

                SignUpController.newUserBodyInfo.setSex(BodyInfo.Sex.MASCULINO);

            }else{
                boyButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainSecond, theme));
                SignUpController.newUserBodyInfo.setSex(BodyInfo.Sex.NULO);
            }

        } else if (checkedId == R.id.toggle_girl) {
            if(isChecked){
                boyButton.setChecked(false);
                girlButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainPrimary, theme));

                SignUpController.newUserBodyInfo.setSex(BodyInfo.Sex.FEMININO);

            }else{
                girlButton.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainSecond, theme));
                SignUpController.newUserBodyInfo.setSex(BodyInfo.Sex.NULO);
            }

        }
    }

    public static Boolean genderNull(){
        return newUserBodyInfo.getGender() == BodyInfo.Sex.NULO;
    }



}
