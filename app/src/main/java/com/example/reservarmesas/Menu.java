package com.example.reservarmesas;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String email;
    public Menu(String email) {
        this.email=email;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Menu.
     */
    // TODO: Rename and change types and number of parameters
    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu("");
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ImageButton burrito,tacos,nachos,bebidas,quesadillas;
    private ArrayList<TextView>textos=new ArrayList<TextView>();
    private ArrayList<TextView>titulos=new ArrayList<TextView>();
    ArrayList<String[]> textTituloCominda;
    ArrayList<String[]> textCominda;
    Typeface typeface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu,container,false);
        burrito=v.findViewById(R.id.cardBurrito);
        tacos=v.findViewById(R.id.cardTacos);
        nachos=v.findViewById(R.id.cardNachos);
        bebidas=v.findViewById(R.id.cardBebidas);
        quesadillas=v.findViewById(R.id.cardQuesadillas);

        typeface= ResourcesCompat.getFont(getContext(), R.font.denk_one);

        burrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int burritos=1;
                switchCarta(burritos);
            }
        });

        bebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bebidas=2;
                switchCarta(bebidas);
            }
        });

        tacos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tacos=3;
                switchCarta(tacos);
            }
        });

        quesadillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quesadillas=4;
                switchCarta(quesadillas);
            }
        });

        nachos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nachos=5;
                switchCarta(nachos);
            }
        });

        return v;
    }

    private void switchCarta(int nombre){

        String colorTexto,colorTitulo,colorFondo="";
        int menu,tituloMenu,cantidad=0;
        switch (nombre){
            case 1:
                colorFondo="#00DEB5";
                colorTitulo="#FF5000";
                colorTexto="#402FD7";
                cantidad=7;
                menu=R.array.menuBurritos;
                tituloMenu=R.array.menuBurritosTitulo;
                mostrarMenu(menu,tituloMenu,colorFondo,colorTitulo,colorTexto,cantidad);
                break;
            case 2:
                colorFondo="#3030D0";
                colorTitulo="#02D8B2";
                colorTexto="#F4A3EF";
                cantidad=12;
                menu=R.array.menuBebidas;
                tituloMenu=R.array.menuBebidasTitulo;
                mostrarMenu(menu,tituloMenu,colorFondo,colorTitulo,colorTexto,cantidad);
                break;
            case 3:
                colorFondo="#FF4B00";
                colorTitulo="#EAD001";
                colorTexto="#00DDBB";
                cantidad=2;
                menu=R.array.menuTacos;
                tituloMenu=R.array.menuTacosTitulo;
                mostrarMenu(menu,tituloMenu,colorFondo,colorTitulo,colorTexto,cantidad);
                break;
            case 4:
                colorFondo="#8A0CC5";
                colorTitulo="#EF4C07";
                colorTexto="#08E19D";
                //menu=R.array.menuBurritos;
                //tituloMenu=R.array.menuBurritosTitulo;
                //mostrarMenu(menu,tituloMenu,colorFondo,colorTitulo,colorTexto);
                break;
            case 5:
                colorFondo="#FFC501";
                colorTitulo="#F74902";
                colorTexto="#2734C1";
                cantidad=2;
                menu=R.array.menuNachos;
                tituloMenu=R.array.menuNachosTitulo;
                mostrarMenu(menu,tituloMenu,colorFondo,colorTitulo,colorTexto,cantidad);
                break;
        }
    }

    private void mostrarMenu(int comida,int tituloComida,String colorFondo,String colorTitulo,String colorTexto
    ,int cantidad) {


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.menu_emergente);
        ConstraintLayout containerPadre=dialog.findViewById(R.id.containerPadre);
        LinearLayout container=dialog.findViewById(R.id.containerMenu);

        containerPadre.setBackgroundColor(Color.parseColor(colorFondo));

        dialog.setTitle("Title...");

        textos = new ArrayList<TextView>();
        textTituloCominda = new ArrayList<String[]>();
        textCominda = new ArrayList<String[]>();
        if(textos.size()>0){
            for(int i=0;i<textos.size();i++){
                titulos.remove(i);
                textos.remove(i);

                container.removeView(titulos.get(i));
                container.removeView(textos.get(i));
            }
        }

        for (int i = 0; i < cantidad; i++) {
            textTituloCominda.add(getResources().getStringArray(tituloComida));
            TextView t= new TextView(getContext());
            titulos.add(t);
        }
        for (int i = 0; i < cantidad; i++) {
            textCominda.add(getResources().getStringArray(comida));
            TextView t= new TextView(getContext());
            textos.add(t);
        }

        for (int j = 0; j < textos.size(); j++) {
            titulos.get(j).setText(textTituloCominda.get(j)[j]);
            textos.get(j).setText(textCominda.get(j)[j]);
            titulos.get(j).setId(j);
            textos.get(j).setId(j);
            titulos.get(j).setTypeface(typeface);
            textos.get(j).setTypeface(typeface);
            titulos.get(j).setTextColor(Color.parseColor(colorTitulo));
            textos.get(j).setTextColor(Color.parseColor(colorTexto));
            titulos.get(j).setTextSize(20);
            //textos.get(j).setTextColor(Color.parseColor("#000000"));
            container.addView(titulos.get(j));
            container.addView(textos.get(j));
            //containerPadre.addView(container);
        }

        containerPadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<textos.size();i++){

                    container.removeView(titulos.get(i));
                    container.removeView(textos.get(i));
                }
                //containerPadre.removeView(container);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}