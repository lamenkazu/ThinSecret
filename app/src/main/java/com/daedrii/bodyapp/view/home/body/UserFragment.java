package com.daedrii.bodyapp.view.home.body;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.home.HomeController;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.model.user.UserInfo;
import com.daedrii.bodyapp.view.sign.SignActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.function.Consumer;

public class UserFragment extends Fragment {
    private MaterialTextView presentation;
    private MaterialButton btnLogOut, btnResetData, btnSaveData;
    private LinearProgressIndicator progress;
    private static DecimalFormat decimalFormat3 = new DecimalFormat("#.000");
    private static DecimalFormat decimalFormat0 = new DecimalFormat("#");
    private BodyInfo actualUserBodyInfo;
    private UserInfo actualUserInfo;

    //Variaveis corporais
    private TextInputEditText weight, height, IMC, IDR;

    //Variaveis de Objetivo
    private Spinner goalSpinner;
    private ArrayList<String> goalForUser;
    private ArrayAdapter<String> goalDropdownAdapter;

    //Variaveis de Dieta
    private Spinner dietSpinner;
    private ArrayList<String> dietForUser;
    private ArrayAdapter<String> dietDropdownMenuAdapter;

    //Variáveis de atividade
    private Spinner actLevelSpinner;
    private ArrayList<String> actLevelForUser;
    private ArrayAdapter<String> actLevelDropdownAdapter;

    private void initComponents(View fragmentView){

        //Progress Data Indicator
        progress = fragmentView.findViewById(R.id.user_progress_data);

        //Label Apresentação
        presentation = fragmentView.findViewById(R.id.lbl_presentation);

        //Init Buttons
        btnLogOut = fragmentView.findViewById(R.id.btn_logout);
        btnResetData = fragmentView.findViewById(R.id.btn_reset_data);
        btnSaveData = fragmentView.findViewById(R.id.btn_save);

        //Init GoalSpinner
        goalSpinner = fragmentView.findViewById(R.id.goalDropdownMenu);
        goalForUser = new ArrayList<>();
        goalForUser.add(BodyInfo.DietGoal.LOSS.getStringGoal());
        goalForUser.add(BodyInfo.DietGoal.KEEP.getStringGoal());
        goalForUser.add(BodyInfo.DietGoal.GAIN.getStringGoal());

        goalDropdownAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, goalForUser);
        goalDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalDropdownAdapter);

        //Init GenderSpinner
        dietSpinner = fragmentView.findViewById(R.id.dietDropdownMenu);
        dietForUser = new ArrayList<>();

        dietForUser.add(BodyInfo.DietType.LowCarb.name());
        dietForUser.add(BodyInfo.DietType.MidCarb.name());
        dietForUser.add(BodyInfo.DietType.HighCarb.name());

        dietDropdownMenuAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dietForUser);
        dietDropdownMenuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietSpinner.setAdapter(dietDropdownMenuAdapter);

        //Init ActivityLevelSpinner
        actLevelSpinner = fragmentView.findViewById(R.id.actLevelDropdownMenu);
        actLevelForUser = new ArrayList<>();


        actLevelForUser.add(BodyInfo.ActLevel.SEDENTARY.name());
        actLevelForUser.add(BodyInfo.ActLevel.LOW_ACTIVE.name());
        actLevelForUser.add(BodyInfo.ActLevel.ACTIVE.name());
        actLevelForUser.add(BodyInfo.ActLevel.HIGH_ACTIVE.name());
        actLevelForUser.add(BodyInfo.ActLevel.EXTREME_ACTIVE.name());

        actLevelDropdownAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, actLevelForUser);
        actLevelDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actLevelSpinner.setAdapter(actLevelDropdownAdapter);

//      Init Body info
        this.weight = fragmentView.findViewById(R.id.user_weight_txt);
        this.height = fragmentView.findViewById(R.id.user_height_txt);
        this.IMC = fragmentView.findViewById(R.id.user_imc_txt);
        this.IDR = fragmentView.findViewById(R.id.user_idr_txt);

        setUserDataOnComponents();


    }


    @Override
    public void onResume() {
        super.onResume();

        setUserDataOnComponents();
    }

    private void setUserDataOnComponents(){
        HomeController.getUserData(userInfo -> {
            actualUserBodyInfo = userInfo.getBodyInfo();
            actualUserInfo = userInfo;

            progress.setVisibility(View.GONE);

            this.presentation.setText(userInfo.getName() + ", " + userInfo.getBodyInfo().getAge() + " anos" );

            this.weight.setText(userInfo.getBodyInfo().getWeight().toString());
            this.height.setText(userInfo.getBodyInfo().getHeight().toString());
            this.IDR.setText(decimalFormat0.format(actualUserBodyInfo.getIDR()));
            this.IMC.setText(decimalFormat3.format(actualUserBodyInfo.getIMC()));

            // Definir valor predefinido com base no que está definido no perfil
            goalSpinner.setSelection(userInfo.getBodyInfo().getGoal().ordinal());
            dietSpinner.setSelection(userInfo.getBodyInfo().getDiet().ordinal());
            actLevelSpinner.setSelection(userInfo.getBodyInfo().getActLevel().ordinal());
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_user, container, false);

        initComponents(fragmentView);

        btnResetData.setOnClickListener(v -> {
            setUserDataOnComponents();
        });

        btnSaveData.setOnClickListener(v -> {
            SignUpController.setUserData(actualUserInfo);
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(fragmentView.getContext(), SignActivity.class);
                startActivity(intent);

            }
        });

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Atualizar valor predefinido quando um item for selecionado
                String selectedGoal = goalDropdownAdapter.getItem(position);
                if(actualUserBodyInfo != null){
                    if(selectedGoal.equals("Perder Peso")){
                        actualUserBodyInfo.setGoal(BodyInfo.DietGoal.LOSS);
                    }else if(selectedGoal.equals("Manter Peso")){
                        actualUserBodyInfo.setGoal(BodyInfo.DietGoal.KEEP);
                    }else{
                        actualUserBodyInfo.setGoal(BodyInfo.DietGoal.GAIN);
                    }
                    SignUpController.handleIDRCalculation(actualUserBodyInfo);
                    IDR.setText(decimalFormat0.format(actualUserBodyInfo.getIDR()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dietSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDiet = dietDropdownMenuAdapter.getItem(position);
                if(actualUserBodyInfo != null){
                    actualUserBodyInfo.setDiet(BodyInfo.DietType.valueOf(selectedDiet));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        actLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAct = actLevelDropdownAdapter.getItem(position);
                //Log.d("Activity Level", BodyInfo.ActLevel.valueOf(selectedAct).toString());
                if(actualUserBodyInfo != null){
                    actualUserBodyInfo.setActLevel(BodyInfo.ActLevel.valueOf(selectedAct));
                    //Log.d("ActualUserBody", actualUserInfo.toString());
                    SignUpController.handleIDRCalculation(actualUserBodyInfo);
                    IDR.setText(decimalFormat0.format(actualUserBodyInfo.getIDR()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return fragmentView;

    }
}