package com.example.reservarmesas;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private String email;

    public PagerAdapter( FragmentManager fm, int numOfTabs,String email) {

        super(fm);
        this.numOfTabs=numOfTabs;
        this.email=email;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Reservas(email);
            case 1:
                return new EditPerfil(email);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
