package com.daedrii.bodyapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    Animation logoAnim;
    Animation titleAnim;
    Animation subAnim;
    ImageView image;
    TextView title;
    TextView title2;
    TextView subtitle;

    private void linkViews(){
        //Animations
        logoAnim = AnimationUtils.loadAnimation(this,R.anim.splash_logo_anim);
        titleAnim = AnimationUtils.loadAnimation(this,R.anim.splash_title_anim);
        subAnim = AnimationUtils.loadAnimation(this,R.anim.splash_subtitle_anim);

        //Components link to ID
        image = findViewById(R.id.logoImage);
        title = findViewById(R.id.title);
        title2 = findViewById(R.id.title2);
        subtitle = findViewById(R.id.subtitle);

        //SetAnimation to Components
        image.setAnimation(logoAnim);
        title.setAnimation(titleAnim);
        title2.setAnimation(titleAnim);
        subtitle.setAnimation(subAnim);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove a StatusBar durante a SplashScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        //Animations
        linkViews();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, SignActivity.class);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(title, "title");
                pairs[2] = new Pair<View, String>(title2, "title2");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash.this, pairs);

                startActivity(intent, options.toBundle());

            }
        }, 3500);

    }
}