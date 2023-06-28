package com.daedrii.bodyapp.controller.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.Serving;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.model.user.UserInfo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class HomeController {

    static Integer totalCalories = 0;
    static Double totalCarbs = 0.0, totalFat = 0.0, totalProtein = 0.0;


    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FirebaseAuth userInstance = FirebaseAuth.getInstance();
    private static DatabaseReference userRef = database.getReference("UserInfo").child(userInstance.getUid());
    private static DatabaseReference userBodyRef = database.getReference("BodyInfo").child(userInstance.getUid());
    private static DatabaseReference foodListsRef = database.getReference().child("FoodList Per Day").child(userInstance.getUid());
    private static String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    private static UserInfo userInfo = new UserInfo();
    private static BodyInfo userBodyInfo = new BodyInfo();

    public static void getBarChartData(BarChart barChart, String currentDate){

        DatabaseReference foodListRef = foodListsRef.child(currentDate).child("foodList");
        foodListRef.addListenerForSingleValueEvent(new ValueEventListener() { //Pega todas as listas inseridas em um dia.
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    totalCalories = 0;
                    totalCarbs = 0.0;
                    totalFat = 0.0;
                    totalProtein = 0.0;

                    for(DataSnapshot foodListSnapShot: snapshot.getChildren()){ //Passa lista a lista do dia

                        for(DataSnapshot food: foodListSnapShot.getChildren()){ //Pega alimento por alimento de cada lista do dia

                            Map<String, Object> foodMap = (Map<String, Object>) food.getValue();
                            FoodDetails foodDetails = new FoodDetails(
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
                            totalCalories += serving.getCalories();
                            totalFat += serving.getFat();
                            totalCarbs += serving.getCarbohydrate();
                            totalProtein += serving.getProtein();


                        }


                    }
                    Log.d("FoodDetail", "Carb: " + totalCarbs + "Calories: " + totalCalories + "Fat: " + totalFat + "Protein: " + totalProtein);

                    List<BarEntry> barEntries = new ArrayList<>();

// Adicione os valores das barras empilhadas
                    barEntries.add(new BarEntry(0f, new float[]{totalCarbs.floatValue(), totalFat.floatValue(), totalProtein.floatValue()}));

                    BarDataSet dataSet = new BarDataSet(barEntries, "Nutrientes");

// Defina as cores para cada valor da barra
                    int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN};
                    dataSet.setColors(colors);

                    String[] stackLabels = new String[]{"Carboidratos", "Gorduras", "Proteinas"};
                    dataSet.setStackLabels(stackLabels);
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");

                    dataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return decimalFormat.format(value); // Formate o valor conforme o formato definido
                        }
                    });
                    dataSet.setValueTextSize(15f);

                    BarData barData = new BarData(dataSet);

                    barData.setValueTextColor(Color.BLACK); // Defina a cor do texto dos valores

                    BarChartRenderer barRenderer = (BarChartRenderer) barChart.getRenderer();
                    barRenderer.getPaintValues().setTextAlign(Paint.Align.CENTER); // Centralize o texto verticalmente

                    float barWidth = 0.3f; // Ajuste o valor conforme necessário
                    barData.setBarWidth(barWidth);

                    barChart.setData(barData);
                    barChart.invalidate();

                }else{

                    List<BarEntry> barEntries = new ArrayList<>();

// Adicione os valores das barras empilhadas

                    BarDataSet dataSet = new BarDataSet(barEntries, "Nutrientes");


                    String[] stackLabels = new String[]{"Carboidratos", "Gorduras", "Proteinas"};
                    dataSet.setStackLabels(stackLabels);

                    BarData barData = new BarData(dataSet);

                    barChart.setData(barData);
                    barChart.invalidate();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

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
                .child("foodList")
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
                                        userBodyInfo.setDiet(BodyInfo.DietType.valueOf((String) bodyInfoMap.get("diet")));

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
