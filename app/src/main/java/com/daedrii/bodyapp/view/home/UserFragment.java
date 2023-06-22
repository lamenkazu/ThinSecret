package com.daedrii.bodyapp.view.home;

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
import com.daedrii.bodyapp.model.BodyInfo;
import com.daedrii.bodyapp.model.UserSingleton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    private UserSingleton userSingleton = UserSingleton.getInstance();

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

    }


    @Override
    public void onResume() {
        super.onResume();

        // Definir valor predefinido com base no que está definido no perfil
        goalSpinner.setSelection(userSingleton.getUser().getBodyInfo().getGoal().ordinal());
        genderSpinner.setSelection(userSingleton.getUser().getBodyInfo().getGender().ordinal());
        actLevelSpinner.setSelection(userSingleton.getUser().getBodyInfo().getActLevel().ordinal());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_user, container, false);

        initComponents(fragmentView);

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