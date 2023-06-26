package com.daedrii.bodyapp.controller.home;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
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
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class HomeController {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FirebaseAuth userInstance = FirebaseAuth.getInstance();
    private static DatabaseReference userRef = database.getReference("UserInfo").child(userInstance.getUid());
    private static DatabaseReference userBodyRef = database.getReference("BodyInfo").child(userInstance.getUid());
    private static DatabaseReference foodListsRef = database.getReference().child("FoodList Per Day").child(userInstance.getUid());
    private static String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    private static UserInfo userInfo = new UserInfo();
    private static BodyInfo userBodyInfo = new BodyInfo();

    public static void clearFoodList(ArrayList<FoodDetails> foodList, MaterialTextView irView, FoodListAdapter adapterList){
        foodList.clear();
        HomeController.getIRDay(irValue -> {
            Integer IR = irValue; // Atualizar o valor de IR
            irView.setText("IR: " + IR);
        });
        adapterList.notifyDataSetChanged();
    }

    public static void addFoodListToDB(ArrayList<FoodDetails> foodList, Context applicationContext, Integer IR){


        String newFoodListKey = foodListsRef.child(currentDate).push().getKey();
        foodListsRef.child(currentDate)
                .child(newFoodListKey)
                .setValue(foodList)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        // Envio bem-sucedido
                        Toast.makeText(applicationContext, "Lista de alimentos enviada com sucesso.",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        // Falha no envio
                        Toast.makeText(applicationContext, "Falha ao enviar lista de alimentos.",
                                Toast.LENGTH_SHORT).show();
                    }
        });

        foodListsRef.child(currentDate)
                .child("IDR")
                .setValue(userBodyInfo.getIDR());
//                .addOnCompleteListener(task -> {
//
//                });

        foodListsRef.child(currentDate)
                .child("IR")
                .setValue(IR);
//                .addOnCompleteListener(task -> {
//
//                });

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
                    // Obtém o mapa de valores do snapshot
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

                                        userInfo.setBodyInfo(userBodyInfo);
                                    }
                                }

                                // Chame o método accept() do Consumer com o UserInfo completo
                                if (callback != null) {
                                    callback.accept(userInfo);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Trate o cancelamento da leitura de dados
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Trate o cancelamento da leitura de dados
            }
        });

    }

}
