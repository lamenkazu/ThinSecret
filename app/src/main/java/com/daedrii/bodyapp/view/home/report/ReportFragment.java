package com.daedrii.bodyapp.view.home.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.home.HomeController;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ReportFragment extends Fragment {

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

        resetDate();

        //Criação do DatePicker
        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose date to remind")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Definir o fuso horário para UTC

                String data = sdf.format(new Date(selection));
                txtDate.setText(data);

                HomeController.getBarChartData(barChart, sdfDB.format(new Date(selection)) );

            }

        });

    }

    private void resetDate(){
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-03:00")); // Definir o fuso horário para UTC
        sdfDB.setTimeZone(TimeZone.getTimeZone("GMT-03:00")); // Definir o fuso horário para UTC

        Log.d("Date",sdf.format(new Date()));

        txtDate.setText(sdf.format(new Date()));
        HomeController.getBarChartData(barChart, sdfDB.format(new Date()) );
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
            String currentDate = txtDate.getText().toString();

            try {
                Date date = sdf.parse(currentDate); // Converte a data atual em um objeto Date
                long millis = date.getTime() - (24 * 60 * 60 * 1000); // Subtrai 1 dia em milissegundos
                date.setTime(millis); // Define a nova data no objeto Date

                String newDate = sdf.format(date); // Converte a nova data de volta para uma string formatada
                txtDate.setText(newDate); // Atualiza o texto da data no TextInputEditText

                // Atualize o gráfico com a nova data
                String formattedDBDate = sdfDB.format(date);
                HomeController.getBarChartData(barChart, formattedDBDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        btnForward.setOnClickListener(v -> {
            String currentDate = txtDate.getText().toString();
            try {
                Date date = sdf.parse(currentDate); // Converte a data atual em um objeto Date
                long millis = date.getTime() + (24 * 60 * 60 * 1000); // Adiciona 1 dia em milissegundos
                date.setTime(millis); // Define a nova data no objeto Date

                String newDate = sdf.format(date); // Converte a nova data de volta para uma string formatada
                txtDate.setText(newDate); // Atualiza o texto da data no TextInputEditText

                // Atualize o gráfico com a nova data
                String formattedDBDate = sdfDB.format(date);
                HomeController.getBarChartData(barChart, formattedDBDate);
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
}


