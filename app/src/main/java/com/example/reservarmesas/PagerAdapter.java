package com.example.reservarmesas;

import android.view.MotionEvent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
                return new Inicio(email);
            case 1:
                return new Menu(email);
            case 2:
                return new Reservas(email);
            case 3:
                return new EditPerfil(email);
            case 4:
                return new Informacion(email);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
