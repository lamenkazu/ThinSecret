package com.daedrii.bodyapp.controller.home;

import android.content.Context;
import android.content.SharedPreferences;

import com.daedrii.bodyapp.model.user.BodyInfo;

public class HomeController {

    public static BodyInfo newUserBodyInfo = new BodyInfo();

    private static final String USER_PREF_ID = "UserPref";

    public static void setUserData(Context applicationContext){
        SharedPreferences pref = applicationContext.getSharedPreferences("UserPref", 0);

//        newUserBodyInfo.setAge(pref.getInt("user_age", 0));
//        newUserBodyInfo.setBirthDate(pref.getString("user_birth_date", ""));
//        newUserBodyInfo.setWeight(pref.getInt("user_weight", 0));
//        newUserBodyInfo.setHeight(pref.getInt("user_height", 0));
//        newUserBodyInfo.setGender(pref.getString("user_gender", "").equals("FEMININO") ? BodyInfo.Sex.FEMININO : BodyInfo.Sex.MASCULINO);
//
//        if(pref.getString("user_goal", "").equals(BodyInfo.DietGoal.GAIN.toString())){
//            newUserBodyInfo.setGoal(BodyInfo.DietGoal.GAIN);
//        }else if(pref.getString("user_goal", "").equals(BodyInfo.DietGoal.LOSS.toString())){
//            newUserBodyInfo.setGoal(BodyInfo.DietGoal.LOSS);
//        }else{
//            newUserBodyInfo.setGoal(BodyInfo.DietGoal.KEEP);
//        }
//
//        if(pref.getString("user_actLevel", "").equals(BodyInfo.ActLevel.EXTREME_ACTIVE.name())){
//            newUserBodyInfo.setGoal(BodyInfo.DietGoal.GAIN);
//        }else if(pref.getString("user_goal", "").equals(BodyInfo.DietGoal.LOSS.toString())){
//            newUserBodyInfo.setGoal(BodyInfo.DietGoal.LOSS);
//        }else{
//            newUserBodyInfo.setGoal(BodyInfo.DietGoal.KEEP);
//        }
//
//        newUserBodyInfo.setActLevel(pref.getString("user_actLevel", ""));
//        newUserBodyInfo.setIMC(Double.valueOf(pref.getFloat("user_IMC", 0)));
//        newUserBodyInfo.setIDR(Double.valueOf(pref.getFloat("user_IDR", 0)));
//
//        userSingleton.setUser(new UserInfo(
//                pref.getString("user_name", ""),
//                pref.getString("user_mail", "" ),
//                pref.getString("user_mail", "" ),
//                newUserBodyInfo));

    }

}
