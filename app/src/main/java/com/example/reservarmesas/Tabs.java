package com.example.reservarmesas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Tabs extends AppCompatActivity {

    TabLayout tabs;
    TabItem filtro, perfil;
    ViewPager viewPager;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        tabs = findViewById(R.id.tabLayout);
        filtro = findViewById(R.id.tabReserva);
        perfil = findViewById(R.id.tabPerfil);
        viewPager = findViewById(R.id.viewPager);
        bundle = getIntent().getExtras();

        PagerAdapter tabAdapter = new PagerAdapter(getSupportFragmentManager(), tabs.getTabCount(), bundle.getString("email"));

        viewPager.beginFakeDrag();
        viewPager.setAdapter(tabAdapter);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    }
}

