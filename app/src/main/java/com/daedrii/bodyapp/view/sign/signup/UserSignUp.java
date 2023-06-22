package com.daedrii.bodyapp.view.sign.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.User;
import com.daedrii.bodyapp.model.UserSingleton;
import com.daedrii.bodyapp.view.home.HomeScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class UserSignUp extends AppCompatActivity {

    MaterialButton done;

    TextInputEditText name, email, password;

    UserSingleton userSingleton = UserSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        done = findViewById(R.id.user_done);
        name = findViewById(R.id.txt_name);
        email = findViewById(R.id.txt_email);
        password = findViewById(R.id.txt_password);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = name.getText().toString();
                String userMail = email.getText().toString();
                String userPassword = password.getText().toString();


                if(userName.equals("") || userMail.equals("") || userPassword.equals(""))
                    Toast.makeText(UserSignUp.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                else{

                    userSingleton.setUser(new User(userName, userMail, userPassword, SignUpController.newUserBodyInfo));

                    Intent intent = new Intent(UserSignUp.this, HomeScreen.class);
                    startActivity(intent);
                }

            }
        });
    }
}