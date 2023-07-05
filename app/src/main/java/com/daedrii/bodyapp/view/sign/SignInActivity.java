package com.daedrii.bodyapp.view.sign;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
import com.daedrii.bodyapp.model.exceptions.InvalidUserException;
import com.daedrii.bodyapp.view.home.HomeActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicReference;

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    MaterialButton done;
    TextInputEditText email, password;
    LinearProgressIndicator progressIndicator;

    private void initComponents(){
        done = findViewById(R.id.btn_sign_in);
        email = findViewById(R.id.signin_email_txt);
        password = findViewById(R.id.signin_password_txt);
        mAuth = FirebaseAuth.getInstance();
        progressIndicator = findViewById(R.id.signin_progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initComponents();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressIndicator.setVisibility(View.VISIBLE);

                String userMail = email.getText().toString();
                String userPassword = password.getText().toString();

                decide(userMail, userPassword);


            }
        });

    }

    public Boolean decide(String userMail, String userPassword){
        AtomicReference<Boolean> decision = new AtomicReference<>(false);
        try {
            if(userMail.equals("") || userPassword.equals("")){
                throw new EmptyFieldException(getApplicationContext().getString(R.string.exception_empty_field));
            }else{
                SignUpController.handleSignIn(userMail, userPassword, success -> {
                    progressIndicator.setVisibility(View.GONE);

                    try{
                        if(success){
                            Toast.makeText(SignInActivity.this, "Bem Vindo!",
                                    LENGTH_SHORT).show();
                            decision.set(true);
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            throw new InvalidUserException(getString(R.string.exception_invalid_user));

                        }
                    }catch (Exception e){
                        Toast.makeText(SignInActivity.this, e.getMessage(), LENGTH_SHORT).show();
                        progressIndicator.setVisibility(View.GONE);

                    }

                });
            }

        } catch (Exception e) {
            Toast.makeText(SignInActivity.this, e.getMessage(), LENGTH_SHORT).show();
            progressIndicator.setVisibility(View.GONE);
        }
        return decision.get();
    }
}