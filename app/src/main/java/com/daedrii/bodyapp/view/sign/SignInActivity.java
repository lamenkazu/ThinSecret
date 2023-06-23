package com.daedrii.bodyapp.view.sign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.view.home.HomeScreen;
import com.daedrii.bodyapp.view.sign.signup.UserSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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


                if(userMail.equals("") || userPassword.equals(""))
                    Toast.makeText(SignInActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                else{

                    mAuth.signInWithEmailAndPassword(userMail, userPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressIndicator.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignInActivity.this, "Bem Vindo!",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignInActivity.this, HomeScreen.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
            }
        });

    }
}