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
                String burritos="burritos";
                mostrarMenu(burritos);
            }


        });

        return v;
    }

    private void mostrarMenu(String comida) {


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.menu_emergente);
        ConstraintLayout containerPadre=dialog.findViewById(R.id.containerPadre);
        LinearLayout container=dialog.findViewById(R.id.containerMenu);

        containerPadre.setBackgroundColor(Color.parseColor("#FFC500"));

        dialog.setTitle("Title...");

        textos = new ArrayList<TextView>();
        textTituloCominda = new ArrayList<String[]>();
        textCominda = new ArrayList<String[]>();
        for (int i = 0; i < 7; i++) {
            textTituloCominda.add(getResources().getStringArray(R.array.menuBurritosTitulo));
            TextView t= new TextView(getContext());
            titulos.add(t);
        }
        for (int i = 0; i < 7; i++) {
            textCominda.add(getResources().getStringArray(R.array.menuBurritos));
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
            titulos.get(j).setTextColor(Color.parseColor("#000000"));
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