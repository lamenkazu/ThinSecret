package com.daedrii.bodyapp.view.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.home.HomeController;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.view.sign.SignActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UserFragment extends Fragment {
    FirebaseUser user;
    FirebaseAuth mAuth;
    MaterialTextView presentation;
    MaterialButton btnLogOut, btnResetData;
    LinearProgressIndicator progress;

    //Variaveis corporais
    private TextInputEditText weight, height, IMC, IDR;

    //Variaveis de Objetivo
    private Spinner goalSpinner;
    private ArrayList<String> goalForUser;
    private ArrayAdapter<String> goalDropdownAdapter;

    //Variaveis de Genero
    private Spinner genderSpinner;
    private ArrayList<String> genderForUser;
    private ArrayAdapter<String> genderDropdownAdapter;

    //Variáveis de atividade
    private Spinner actLevelSpinner;
    private ArrayList<String> actLevelForUser;
    private ArrayAdapter<String> actLevelDropdownAdapter;



    private void initComponents(View fragmentView){
        //user
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Progress Data Indicator
        progress = fragmentView.findViewById(R.id.user_progress_data);

        //Label Apresentação
        presentation = fragmentView.findViewById(R.id.lbl_presentation);

        //Init Buttons
        btnLogOut = fragmentView.findViewById(R.id.btn_logout);
        btnResetData = fragmentView.findViewById(R.id.btn_reset_data);

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
        genderSpinner = fragmentView.findViewById(R.id.genderDropdownMenu);
        genderForUser = new ArrayList<>();

        genderForUser.add(BodyInfo.Sex.MASCULINO.name());
        genderForUser.add(BodyInfo.Sex.FEMININO.name());

        genderDropdownAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, genderForUser);
        genderDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderDropdownAdapter);

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

    }


    @Override
    public void onResume() {
        super.onResume();

        setUserDataOnComponents();

    }

    private void setUserDataOnComponents(){
        HomeController.getUserData(userInfo -> {
            progress.setVisibility(View.GONE);
            DecimalFormat decimalFormat = new DecimalFormat("#");

            this.presentation.setText(userInfo.getName() + ", " + userInfo.getBodyInfo().getAge() + " anos" );

            this.weight.setText(userInfo.getBodyInfo().getWeight().toString());
            this.height.setText(userInfo.getBodyInfo().getHeight().toString());
            this.IMC.setText(decimalFormat.format(userInfo.getBodyInfo().getIMC()));
            this.IDR.setText(decimalFormat.format(userInfo.getBodyInfo().getIDR()));

            // Definir valor predefinido com base no que está definido no perfil
            goalSpinner.setSelection(userInfo.getBodyInfo().getGoal().ordinal());
            genderSpinner.setSelection(userInfo.getBodyInfo().getGender().ordinal());
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
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(fragmentView.getContext(), SignActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Atualizar valor predefinido quando um item for selecionado
                String selectedGoal = goalDropdownAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = goalDropdownAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        actLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAct = actLevelDropdownAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return fragmentView;

    }
}