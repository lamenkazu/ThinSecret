package com.daedrii.bodyapp.controller.sign;

import android.content.Intent;
import android.content.res.Resources;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.model.BodyInfo;
import com.daedrii.bodyapp.view.sign.signup.ActLevelSignUp;
import com.daedrii.bodyapp.view.sign.signup.BodySignUp;
import com.daedrii.bodyapp.view.sign.signup.InitSignUp;
import com.google.android.material.button.MaterialButton;

public class SignUpController {

    public static BodyInfo newUserBodyInfo = new BodyInfo();

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
