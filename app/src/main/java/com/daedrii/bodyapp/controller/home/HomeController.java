package com.daedrii.bodyapp.controller.home;

import android.util.Log;

import androidx.annotation.NonNull;

import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.Serving;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.model.user.UserInfo;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class HomeController {
    private static FirebaseDatabase database;
    private static FirebaseAuth userInstance ;
    private static DatabaseReference userRef ;
    private static DatabaseReference userBodyRef ;
    private static DatabaseReference foodListsRef ;
    private static String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    private static UserInfo userInfo = new UserInfo();
    private static BodyInfo userBodyInfo = new BodyInfo();

    public static void initFirebase(){
        database = FirebaseDatabase.getInstance();
        userInstance = FirebaseAuth.getInstance();
        userRef = database.getReference("UserInfo").child(userInstance.getUid());
        userBodyRef = database.getReference("BodyInfo").child(userInstance.getUid());
        foodListsRef = database.getReference().child("FoodList Per Day").child(userInstance.getUid());
    }

    public static void getBarChartData(String currentDate, Consumer<List<Serving>> callback){
        List<Serving> servingsOfDay = new ArrayList<>();

        DatabaseReference foodListRef = foodListsRef.child(currentDate).child("foodList");
        foodListRef.addListenerForSingleValueEvent(new ValueEventListener() { //Pega todas as listas inseridas em um dia.
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot foodListSnapShot: snapshot.getChildren()){ //Passa lista a lista do dia

                        for(DataSnapshot food: foodListSnapShot.getChildren()){ //Pega alimento por alimento de cada lista do dia

                            Map<String, Object> foodMap = (Map<String, Object>) food.getValue();
                            FoodDetails foodDetails = new FoodDetails( //Nao faz nada com esses detalhes
                                    (String) foodMap.get("foodId"),
                                    (String) foodMap.get("foodName"),
                                    (String) foodMap.get("brandName"),
                                    (String) foodMap.get("foodType"),
                                    (String) foodMap.get("foodUrl"),
                                    (List<Serving>) foodMap.get("servings")
                            );

                            List<Map<String, Object>> servingsMapList = (List<Map<String, Object>>) foodMap.get("servings");
                            List<Serving> servings = new ArrayList<>();

                            for (Map<String, Object> servingMap : servingsMapList) {
                                Serving serving = new Serving(
                                        (String) servingMap.get("servingId"),
                                        (String) servingMap.get("servingDescription"),
                                        (String) servingMap.get("servingUrl"),
                                        ((Number) servingMap.get("metricServingAmount")).doubleValue(),
                                        (String) servingMap.get("metricServingUnit"),
                                        ((Number) servingMap.get("numberOfUnits")).doubleValue(),
                                        (String) servingMap.get("measurementDescription"),
                                        ((Number) servingMap.get("calories")).intValue(),
                                        ((Number) servingMap.get("carbohydrate")).doubleValue(),
                                        ((Number) servingMap.get("protein")).doubleValue(),
                                        ((Number) servingMap.get("fat")).doubleValue(),
                                        ((Number) servingMap.get("saturatedFat")).doubleValue(),
                                        ((Number) servingMap.get("monounsaturatedFat")).doubleValue(),
                                        ((Number) servingMap.get("transFat")).doubleValue(),
                                        ((Number) servingMap.get("cholesterol")).intValue(),
                                        ((Number) servingMap.get("sodium")).intValue(),
                                        ((Number) servingMap.get("potassium")).intValue(),
                                        ((Number) servingMap.get("fiber")).doubleValue(),
                                        ((Number) servingMap.get("sugar")).doubleValue(),
                                        ((Number) servingMap.get("calcium")).intValue(),
                                        ((Number) servingMap.get("iron")).intValue()
                                );


                                servings.add(serving);
                            }
//                            Log.d("details: ", foodDetails.toString());
//                            Log.d("details: ", servings.get(0).toString());

                            Serving serving = servings.get(0);
                            servingsOfDay.add(serving);

                        }
                    }
                }
                callback.accept(servingsOfDay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void clearFoodList(ArrayList<FoodDetails> foodList, MaterialTextView irView ){
        foodList.clear();
        HomeController.getIRDay(irValue -> {
            Integer IR = irValue; // Atualizar o valor de IR
            irView.setText("IR: " + IR);
        });
    }

    public static void addFoodListToDB(ArrayList<FoodDetails> foodList, Integer IR){


        String newFoodListKey = foodListsRef.child(currentDate).push().getKey();
        foodListsRef.child(currentDate)
                .child("foodList")
                .child(newFoodListKey)
                .setValue(foodList);

        foodListsRef.child(currentDate)
                .child("IDR")
                .setValue(userBodyInfo.getIDR());

        foodListsRef.child(currentDate)
                .child("IR")
                .setValue(IR);

    }

    public static void getIRDay(Consumer<Integer> callback){

        DatabaseReference irRef = foodListsRef.child(currentDate).child("IR");
        irRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Integer irValue = snapshot.getValue(Integer.class);
                    if (callback != null && irValue != null) {
                        callback.accept(irValue);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getUserData(Consumer<UserInfo> callback){
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //  Constrói a instância de UserInfo
                    Map<String, Object> userDataMap = (Map<String, Object>) dataSnapshot.getValue();

                    // Verifica se o mapa é válido
                    if (userDataMap != null) {
                        // Constrói a instância de UserInfo
                        userInfo.setName((String) userDataMap.get("name"));
                        userInfo.setEmail((String) userDataMap.get("email"));
                        userInfo.setPhone((String) userDataMap.get("phone"));
                        userInfo.setBirthDate((String) userDataMap.get("birthDate"));


                        userBodyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot bodySnapShot) {
                                if (bodySnapShot.exists()) {
                                    // Constrói a instância de BodyInfo
                                    Map<String, Object> bodyInfoMap = (Map<String, Object>) bodySnapShot.getValue();
                                    if (bodyInfoMap != null) {

                                        userBodyInfo.setAge(((Long) bodyInfoMap.get("age")).intValue());
                                        userBodyInfo.setWeight(((Long) bodyInfoMap.get("weight")).intValue());
                                        userBodyInfo.setHeight(((Long) bodyInfoMap.get("height")).intValue());
                                        userBodyInfo.setGender(BodyInfo.Sex.valueOf((String) bodyInfoMap.get("gender")));
                                        userBodyInfo.setGoal(BodyInfo.DietGoal.valueOf((String) bodyInfoMap.get("goal")));
                                        userBodyInfo.setActLevel(BodyInfo.ActLevel.valueOf((String) bodyInfoMap.get("actLevel")));
                                        userBodyInfo.setIMC((Double) bodyInfoMap.get("imc"));
                                        userBodyInfo.setIDR((Double) bodyInfoMap.get("idr"));
                                        userBodyInfo.setDiet(BodyInfo.DietType.valueOf((String) bodyInfoMap.get("diet")));

                                        userInfo.setBodyInfo(userBodyInfo);
                                    }
                                }

                                // Retorna o userInfo completo pelo callback
                                if (callback != null) {
                                    callback.accept(userInfo);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}
