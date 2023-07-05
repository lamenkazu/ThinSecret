package com.daedrii.bodyapp.controller.home;

import androidx.annotation.NonNull;

import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.Serving;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.model.user.UserInfo;
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
import java.util.Objects;
import java.util.function.Consumer;

public class HomeController {
    private static FirebaseDatabase database;
    private static FirebaseAuth userInstance ;
    private static DatabaseReference userInfoRef;
    private static DatabaseReference userBodyRef ;
    private static DatabaseReference foodListsRef ;
    private static String currentDate;
    private static UserInfo userInfo;
    private static BodyInfo userBodyInfo;



    public static void initDatabaseLink(){
        database = FirebaseDatabase.getInstance();
        userInstance = FirebaseAuth.getInstance();
        userInfoRef = database.getReference("UserInfo").child(Objects.requireNonNull(userInstance.getUid()));
        userBodyRef = database.getReference("BodyInfo").child(userInstance.getUid());
        foodListsRef = database.getReference("FoodList Per Day").child(userInstance.getUid());

        userInfo = new UserInfo();
        userBodyInfo = new BodyInfo();
        currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    }

    public static void initDatabaseLinkForMock(FirebaseDatabase db, FirebaseAuth auth){
        database = db;
        userInstance = auth;
        userInfoRef = database.getReference("UserInfo").child(userInstance.getUid());
        userBodyRef = database.getReference("BodyInfo").child(userInstance.getUid());
        foodListsRef = database.getReference("FoodList Per Day").child(userInstance.getUid());

        userInfo = new UserInfo();
        userBodyInfo = new BodyInfo();
        currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
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
                            FoodDetails foodDetails = new FoodDetails( //Nao faz mais nada com esses detalhes
                                    (String) foodMap.get("foodId"),
                                    (String) foodMap.get("foodName"),
                                    (String) foodMap.get("brandName"),
                                    (String) foodMap.get("foodType"),
                                    (String) foodMap.get("foodUrl"),
                                    (List<Serving>) foodMap.get("servings")
                            );

                            List<Map<String, Object>> servingsMapList = (List<Map<String, Object>>) foodMap.get("servings");
                            List<Serving> servings = new ArrayList<>();

                            assert servingsMapList != null;
                            for (Map<String, Object> servingMap : servingsMapList) {
                                Serving serving = new Serving(
                                        (String) servingMap.get("servingId"),
                                        (String) servingMap.get("servingDescription"),
                                        (String) servingMap.get("servingUrl"),
                                        ((Number) Objects.requireNonNull(servingMap.get("metricServingAmount"))).doubleValue(),
                                        (String) servingMap.get("metricServingUnit"),
                                        ((Number) Objects.requireNonNull(servingMap.get("numberOfUnits"))).doubleValue(),
                                        (String) servingMap.get("measurementDescription"),
                                        ((Number) Objects.requireNonNull(servingMap.get("calories"))).intValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("carbohydrate"))).doubleValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("protein"))).doubleValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("fat"))).doubleValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("saturatedFat"))).doubleValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("monounsaturatedFat"))).doubleValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("transFat"))).doubleValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("cholesterol"))).intValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("sodium"))).intValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("potassium"))).intValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("fiber"))).doubleValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("sugar"))).doubleValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("calcium"))).intValue(),
                                        ((Number) Objects.requireNonNull(servingMap.get("iron"))).intValue()
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

    public static void addFoodListToDB(ArrayList<FoodDetails> foodList, Integer IR, Consumer<Boolean> success){

        String newFoodListKey = foodListsRef.child(currentDate).push().getKey();
        assert newFoodListKey != null;

        if(!foodList.isEmpty()){
            if (success != null) {
                foodListsRef.child(currentDate)
                        .child("foodList")
                        .child(newFoodListKey)
                        .setValue(foodList)
                        .addOnCompleteListener(foodListTask -> {
                            if(foodListTask.isSuccessful()){

                                foodListsRef.child(currentDate)
                                        .child("IDR")
                                        .setValue(userBodyInfo.getIDR())
                                        .addOnCompleteListener(IDROnDayTask -> {

                                            if(IDROnDayTask.isSuccessful()){

                                                foodListsRef.child(currentDate)
                                                        .child("IR")
                                                        .setValue(IR)
                                                        .addOnCompleteListener(IROnDayTask -> {

                                                            success.accept(IROnDayTask.isSuccessful());
                                                        });

                                            }else success.accept(false);

                                        });

                            }else success.accept(false);
                        });
            }
        }else{
            //TODO lançar exceção para lista vazia
            throw new RuntimeException();
        }
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
        userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

                                        userBodyInfo.setAge(((Long) Objects.requireNonNull(bodyInfoMap.get("age"))).intValue());
                                        userBodyInfo.setWeight(((Long) Objects.requireNonNull(bodyInfoMap.get("weight"))).intValue());
                                        userBodyInfo.setHeight(((Long) Objects.requireNonNull(bodyInfoMap.get("height"))).intValue());
                                        userBodyInfo.setGender(BodyInfo.Sex.valueOf((String) bodyInfoMap.get("gender")));
                                        String goalValue = (String) bodyInfoMap.get("goal");
                                        if (goalValue != null && !goalValue.isEmpty()) {
                                            try {
                                                userBodyInfo.setGoal(BodyInfo.DietGoal.valueOf(goalValue));
                                            } catch (IllegalArgumentException e) {
                                                // Tratar caso o valor obtido não corresponda a nenhum dos valores do enum
                                            }
                                        }
                                        String actValue = (String) bodyInfoMap.get("actLevel");
                                        if (actValue != null && !actValue.isEmpty()) {
                                            try {
                                                userBodyInfo.setActLevel(BodyInfo.ActLevel.valueOf(actValue));
                                            } catch (IllegalArgumentException e) {
                                                // Tratar caso o valor obtido não corresponda a nenhum dos valores do enum
                                            }
                                        }
                                        userBodyInfo.setIMC(Double.valueOf(Objects.requireNonNull(bodyInfoMap.get("imc")).toString()));
                                        userBodyInfo.setIDR(Double.valueOf(Objects.requireNonNull(bodyInfoMap.get("idr")).toString()));
                                        String dietValue = (String) bodyInfoMap.get("diet");
                                        if (dietValue != null && !dietValue.isEmpty()) {
                                            try {
                                                userBodyInfo.setDiet(BodyInfo.DietType.valueOf(dietValue));
                                            } catch (IllegalArgumentException e) {
                                                // Tratar caso o valor obtido não corresponda a nenhum dos valores do enum
                                            }
                                        }
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

    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static FirebaseAuth getUserInstance() {
        return userInstance;
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static BodyInfo getUserBodyInfo() {
        return userBodyInfo;
    }

    public static void setDatabase(FirebaseDatabase mockDatabase) {
    }

    public static void setAuth(FirebaseAuth mockAuth) {
    }
}
