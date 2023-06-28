package com.daedrii.bodyapp.view.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.home.HomeController;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

public class UserSettingsActivity extends AppCompatActivity {

    private MaterialButton done, orientationButton, boyButton, girlButton ;
    private TextInputEditText name, phone, email, password, passwordConfirm;
    private LinearProgressIndicator progressIndicator;
    private MaterialButtonToggleGroup toggleGroup, orientationToggle ;

    private void initComponents(){
        done = findViewById(R.id.settings_done);
        name = findViewById(R.id.settings_name_txt);
        phone = findViewById(R.id.settings_phone_txt);
        email = findViewById(R.id.settigs_email_txt);
        password = findViewById(R.id.settings_password_txt);
        passwordConfirm = findViewById(R.id.settings_password_confirm_txt);
        progressIndicator = findViewById(R.id.settings_progress);
        orientationToggle = findViewById(R.id.settings_toggle_orientation);
        orientationButton = findViewById(R.id.settings_toggle_orientation_button);
        boyButton = findViewById(R.id.settings_toggle_boy);
        girlButton = findViewById(R.id.settings_toggle_girl);
        toggleGroup = findViewById(R.id.settings_toggle_group_gender);

        HomeController.getUserData(userInfo -> {
            name.setText(userInfo.getName());
            phone.setText(userInfo.getPhone());
            email.setText(userInfo.getEmail());

            if(userInfo.getBodyInfo().getGender() == BodyInfo.Sex.MASCULINO)
                boyToggleSet(true);
            else girlToggleSet(true);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        initComponents();

        orientationToggle.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.settings_toggle_orientation_button){
                    if(isChecked){
                        orientationButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.pink, getTheme()));
                    }else{
                        orientationButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.black, getTheme()));
                    }
                }
            }
        });

        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if (checkedId == R.id.settings_toggle_boy) {
                    boyToggleSet(isChecked);

                } else if (checkedId == R.id.settings_toggle_girl) {
                    girlToggleSet(isChecked);

                }
            }
        });


    }

    private void girlToggleSet(boolean isChecked){
        if(isChecked){
            boyButton.setChecked(false);
            girlButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainPrimary, getTheme()));

            SignUpController.newBodyInfo.setSex(BodyInfo.Sex.FEMININO);

        }else{
            girlButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainSecond, getTheme()));
            SignUpController.newBodyInfo.setSex(BodyInfo.Sex.NULO);
        }
    }
    private void boyToggleSet(boolean isChecked){
        if(isChecked){
            girlButton.setChecked(false);
            boyButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainPrimary, getTheme()));

            SignUpController.newBodyInfo.setSex(BodyInfo.Sex.MASCULINO);

        }else{
            boyButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mainSecond, getTheme()));
            SignUpController.newBodyInfo.setSex(BodyInfo.Sex.NULO);
        }
    }
}