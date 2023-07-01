package com.daedrii.bodyapp.view.home.report;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.home.HomeController;
import com.daedrii.bodyapp.model.fatsecret.Serving;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReportFragment extends Fragment {
    private static Integer totalCalories = 0;
    private static Double totalCarbs = 0.0, totalFat = 0.0, totalProtein = 0.0;
    private BarChart barChart;
    private MaterialDatePicker<Long> materialDatePicker;
    private TextInputEditText txtDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    private MaterialButton btnBack, btnForward;
    private void initComponents(View view){

        barChart = view.findViewById(R.id.barChart);
        txtDate = view.findViewById(R.id.txt_date);
        btnBack = view.findViewById(R.id.btn_back);
        btnForward = view.findViewById(R.id.btn_forward);

        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdfDB = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        //Criação do DatePicker
        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose date to remind")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Definir o fuso horário para UTC
                sdfDB.setTimeZone(TimeZone.getTimeZone("UTC")); // Definir o fuso horário para UTC

                String data = sdf.format(new Date(selection));
                txtDate.setText(data);

                HomeController.getBarChartData(sdfDB.format(new Date(selection)), servings -> {

                    handleServingsData(servings);

                } );

            }

        });

    }
    @Override
    public void onResume() {
        super.onResume();
        resetDate();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        initComponents(view);

        btnBack.setOnClickListener(v -> {
            totalFat = 0.0;
            totalCarbs = 0.0;
            totalProtein = 0.0;
            String currentDate = txtDate.getText().toString();

            try {
                Date date = sdf.parse(currentDate); // Converte a data atual em um objeto Date
                long millis = date.getTime() - (24 * 60 * 60 * 1000); // Subtrai 1 dia em milissegundos
                date.setTime(millis); // Define a nova data no objeto Date

                String newDate = sdf.format(date); // Converte a nova data de volta para uma string formatada
                txtDate.setText(newDate); // Atualiza o texto da data no TextInputEditText

                // Atualize o gráfico com a nova data
                String formattedDBDate = sdfDB.format(date);
                HomeController.getBarChartData(formattedDBDate, servings -> {

                    handleServingsData(servings);

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        btnForward.setOnClickListener(v -> {
            totalFat = 0.0;
            totalCarbs = 0.0;
            totalProtein = 0.0;
            String currentDate = txtDate.getText().toString();
            try {
                Date date = sdf.parse(currentDate); // Converte a data atual em um objeto Date
                long millis = date.getTime() + (24 * 60 * 60 * 1000); // Adiciona 1 dia em milissegundos
                date.setTime(millis); // Define a nova data no objeto Date

                String newDate = sdf.format(date); // Converte a nova data de volta para uma string formatada
                txtDate.setText(newDate); // Atualiza o texto da data no TextInputEditText

                // Atualize o gráfico com a nova data
                String formattedDBDate = sdfDB.format(date);
                HomeController.getBarChartData(formattedDBDate, servings -> {
                    handleServingsData(servings);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(!materialDatePicker.isAdded())
                        materialDatePicker.show(getActivity().getSupportFragmentManager(), "tag");
                }
            }
        });

        txtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!materialDatePicker.isAdded())
                    materialDatePicker.show(getActivity().getSupportFragmentManager(), "tag");
                return false;
            }
        });

        return view;
    }

    private void resetDate(){
        totalFat = 0.0;
        totalCarbs = 0.0;
        totalProtein = 0.0;

        sdf.setTimeZone(TimeZone.getTimeZone("GMT-03:00")); // Definir o fuso horário para UTC
        sdfDB.setTimeZone(TimeZone.getTimeZone("GMT-03:00")); // Definir o fuso horário para UTC

        Log.d("Date",sdf.format(new Date()));

        txtDate.setText(sdf.format(new Date()));
        HomeController.getBarChartData(sdfDB.format(new Date()), servings -> {
            handleServingsData(servings);

        });
    }

    private void handleServingsData(List<Serving> servings){
        if(servings.isEmpty()){
            setBarChartEmpty();
            Log.d("Lista Vazia", "Verdadeiro");
        }else{
            Log.d("Lista Vazia", "Falso");
            for(Serving serving : servings){
                totalCalories += serving.getCalories();
                totalFat += serving.getFat();
                totalCarbs += serving.getCarbohydrate();
                totalProtein += serving.getProtein();
                populateBarChart();
            }
        }
    }

    private void setBarChartEmpty(){

        List<BarEntry> barEntries = new ArrayList<>();

// Adicione os valores das barras empilhadas

        BarDataSet dataSet = new BarDataSet(barEntries, "Nutrientes");


        String[] stackLabels = new String[]{"Carboidratos", "Gorduras", "Proteinas"};
        dataSet.setStackLabels(stackLabels);

        BarData barData = new BarData(dataSet);

        barChart.clear();  // Limpa os dados do gráfico
        barChart.setData(barData);
        barChart.notifyDataSetChanged();  // Notifica o gráfico sobre a mudança nos dados
        barChart.invalidate();
    }

    private void populateBarChart(){

        Log.d("FoodDetail", "Carb: " + this.totalCarbs + "Calories: " + totalCalories + "Fat: " + this.totalFat + "Protein: " + this.totalProtein);

        List<BarEntry> barEntries = new ArrayList<>();

        // Adicione os valores das barras empilhadas
        barEntries.add(new BarEntry(0f, new float[]{this.totalCarbs.floatValue(), this.totalFat.floatValue(), this.totalProtein.floatValue()}));

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

        barData.setValueTextColor(Color.BLACK); // Define a cor do texto dos valores

        BarChartRenderer barRenderer = (BarChartRenderer) barChart.getRenderer();
        barRenderer.getPaintValues().setTextAlign(Paint.Align.CENTER); // Centraliza o texto verticalmente

        float barWidth = 0.3f; // Ajusta o valor conforme necessário
        barData.setBarWidth(barWidth);

        barChart.setData(barData);
        barChart.invalidate();
    }
}


