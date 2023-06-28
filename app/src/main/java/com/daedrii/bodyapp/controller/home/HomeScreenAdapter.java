package com.daedrii.bodyapp.controller.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.daedrii.bodyapp.view.home.diary.DiaryFragment;
import com.daedrii.bodyapp.view.home.report.ReportFragment;
import com.daedrii.bodyapp.view.home.body.UserFragment;

import java.util.ArrayList;

public class HomeScreenAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public HomeScreenAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentArrayList.add(new DiaryFragment());
        fragmentArrayList.add(new ReportFragment());
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
