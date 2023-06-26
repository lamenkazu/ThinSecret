package com.daedrii.bodyapp.view.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.home.HomeController;
import com.daedrii.bodyapp.controller.home.HomeScreenAdapter;
import com.daedrii.bodyapp.view.Splash;
import com.daedrii.bodyapp.view.sign.SignActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeScreen extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth mAuth;
    private HomeScreenAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public void goToSignScreen(){
        Intent intent = new Intent(HomeScreen.this, SignActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(user == null){
            goToSignScreen();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_BodyApp_WithActionBar); // Aplica o novo tema
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();

        adapter = new HomeScreenAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



    }


}