package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
import com.daedrii.bodyapp.model.exceptions.FailedAccountCreation;
import com.daedrii.bodyapp.model.exceptions.PasswordNotMatchesException;
import com.daedrii.bodyapp.view.home.HomeActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicReference;

public class UserSignUp extends AppCompatActivity {

    MaterialButton done;
    TextInputEditText name, phone, email, password, passwordConfirm;
    LinearProgressIndicator progressIndicator;


    private void initComponents(){
        done = findViewById(R.id.user_done);
        name = findViewById(R.id.signup_name_txt);
        phone = findViewById(R.id.signup_phone_txt);
        email = findViewById(R.id.signup_email_txt);
        password = findViewById(R.id.signup_password_txt);
        passwordConfirm = findViewById(R.id.signup_password_confirm_txt);
        progressIndicator = findViewById(R.id.register_progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        initComponents();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressIndicator.setVisibility(View.VISIBLE);

                String userName = name.getText().toString();
                String userPhone = phone.getText().toString();
                String userMail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userPasswordConfirm = passwordConfirm.getText().toString();

                decide(userName, userPhone, userMail, userPassword, userPasswordConfirm);

            }
        });
    }

    public Boolean decide(String userName, String userPhone, String userMail, String userPassword, String userPasswordConfirm){

        AtomicReference<Boolean> decision = new AtomicReference<>(false);
        try{
            if(userPasswordConfirm.equals("") || userMail.equals("") || userPassword.equals("") || userName.equals("") || userPhone.equals("")){
                throw new EmptyFieldException(getString(R.string.exception_empty_field));
            }
            else{

                if (userPassword.equals(userPasswordConfirm)) {

                        SignUpController.handleUserDataSignUp(userName, userPhone, userMail, userPassword, success -> {
                            try{
                                if (success) {
                                    decision.set(true);
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(userMail, userPassword); //Se criou a conta, loga instantaneamente sem precisar de refazer o login.
                                    Intent intent = new Intent(UserSignUp.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    throw new FailedAccountCreation(getString(R.string.exception_failed_account_creation));
                                }
                            }catch (Exception e){
                                Toast.makeText(UserSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                } else {
                    throw new PasswordNotMatchesException(getString(R.string.exception_password_not_matches));
                }
            }
        }catch (Exception e){
            Toast.makeText(UserSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressIndicator.setVisibility(View.GONE);

        }

        return decision.get();
    }
}