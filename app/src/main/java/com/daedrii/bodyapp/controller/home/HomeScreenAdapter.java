package com.daedrii.bodyapp.controller.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.daedrii.bodyapp.view.home.DiaryFragment;
import com.daedrii.bodyapp.view.home.RelatoryFragment;
import com.daedrii.bodyapp.view.home.UserFragment;

import java.util.ArrayList;

public class HomeScreenAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public HomeScreenAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentArrayList.add(new DiaryFragment());
        fragmentArrayList.add(new RelatoryFragment());
        fragmentArrayList.add(new UserFragment());

    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
}
