package com.example.reservarmesas;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Tabs extends AppCompatActivity {

    TabLayout tabs;
    TabItem filtro,perfil;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
         tabs = findViewById(R.id.tabLayout);
         filtro=findViewById(R.id.tabReserva);
         perfil=findViewById(R.id.tabPerfil);
         viewPager=findViewById(R.id.viewPager);

         TabAdapter tabAdapter= new TabAdapter(getSupportFragmentManager(),tabs.getTabCount());
         viewPager.setAdapter(tabAdapter);
    }

}
