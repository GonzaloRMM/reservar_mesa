package com.example.reservarmesas;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
        this.email = email;
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

    private LinearLayout container;
    private ImageButton burrito, tacos, nachos, bebidas, quesadillas, pedido;
    private ArrayList<TextView> textos = new ArrayList<>();
    private ArrayList<CheckBox> checks = new ArrayList<>();
    private ArrayList<TextView> textPedido = new ArrayList<>();
    private ArrayList<String> checksText = new ArrayList<>();
    private ArrayList<LinearLayout> multiplicadores = new ArrayList<>();
    private ArrayList<Button> botonMenos = new ArrayList<>();
    private ArrayList<Button> botonMas = new ArrayList<>();
    private ArrayList<Button> botonNum = new ArrayList<>();
    private ArrayList<String[]> textTituloCominda;
    private ArrayList<String[]> textCominda;
    private ArrayList<String> precioString = new ArrayList<>();
    private ArrayList<Double> precioNum = new ArrayList<>();
    private ArrayList<Double> precioNumVariable = new ArrayList<>();
    private Intent pedidoActivity;
    private int num = 1;
    Typeface typeface;
    double total = 0;
    TextView precioTotal;
    private Bundle b;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        burrito = v.findViewById(R.id.cardBurrito);
        tacos = v.findViewById(R.id.cardTacos);
        nachos = v.findViewById(R.id.cardNachos);
        bebidas = v.findViewById(R.id.cardBebidas);
        quesadillas = v.findViewById(R.id.cardQuesadillas);
        pedido = v.findViewById(R.id.cardPedido);
        precioTotal = new TextView(getContext());
        pedidoActivity = new Intent(getContext(), Pedido.class);
        b = new Bundle();

        typeface = ResourcesCompat.getFont(getContext(), R.font.denk_one);

        burrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int burritos = 1;
                switchCarta(burritos);
            }
        });

        bebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bebidas = 2;
                switchCarta(bebidas);
            }
        });

        tacos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tacos = 3;
                switchCarta(tacos);
            }
        });

        quesadillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quesadillas = 4;
                switchCarta(quesadillas);
            }
        });

        nachos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nachos = 5;
                switchCarta(nachos);
            }
        });

        pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pedido = 6;
                switchCarta(pedido);
            }
        });
        return v;
    }

    private void switchCarta(int nombre) {
        borrar();
        String colorTexto, colorTitulo, colorFondo = "";
        int menu, tituloMenu, cantidad = 0;
        switch (nombre) {
            case 1:
                colorFondo = "#00DEB5";
                colorTitulo = "#FF5000";
                colorTexto = "#402FD7";
                cantidad = 7;
                menu = R.array.menuBurritos;
                tituloMenu = R.array.menuBurritosTitulo;
                mostrarMenu(menu, tituloMenu, colorFondo, colorTitulo, colorTexto, cantidad);
                break;
            case 2:
                colorFondo = "#3030D0";
                colorTitulo = "#02D8B2";
                colorTexto = "#F4A3EF";
                cantidad = 12;
                menu = R.array.menuBebidas;
                tituloMenu = R.array.menuBebidasTitulo;
                mostrarMenu(menu, tituloMenu, colorFondo, colorTitulo, colorTexto, cantidad);
                break;
            case 3:
                colorFondo = "#FF4B00";
                colorTitulo = "#EAD001";
                colorTexto = "#00DDBB";
                cantidad = 2;
                menu = R.array.menuTacos;
                tituloMenu = R.array.menuTacosTitulo;
                mostrarMenu(menu, tituloMenu, colorFondo, colorTitulo, colorTexto, cantidad);
                break;
            case 4:
                colorFondo = "#8A0CC5";
                colorTitulo = "#EF4C07";
                colorTexto = "#08E19D";
                cantidad = 3;
                menu = R.array.menuQuesadillas;
                tituloMenu = R.array.menuQuesadillasTitulo;
                mostrarMenu(menu, tituloMenu, colorFondo, colorTitulo, colorTexto, cantidad);
                break;
            case 5:
                colorFondo = "#FFC501";
                colorTitulo = "#F74902";
                colorTexto = "#2734C1";
                cantidad = 2;
                menu = R.array.menuNachos;
                tituloMenu = R.array.menuNachosTitulo;
                mostrarMenu(menu, tituloMenu, colorFondo, colorTitulo, colorTexto, cantidad);
                break;
            case 6:
                colorFondo = "#FF97D7";
                colorTitulo = "#3C169D";
                colorTexto = "#F15614";
                cantidad = checksText.size();
                if (checksText.size() > 0) {
                    mostrarMiPedido(colorFondo, colorTitulo, colorTexto, cantidad);
                } else {
                    Toast.makeText(getContext(), "No hay ningun elemento seleccionado", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void mostrarMiPedido(String colorFondo, String colorTitulo, String colorTexto, int cantidad) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.menu_emergente);
        ScrollView containerPadre = dialog.findViewById(R.id.padre);
        container = dialog.findViewById(R.id.containerMenu);

        containerPadre.setBackgroundColor(Color.parseColor(colorFondo));

        dialog.setTitle("Title...");

        textos = new ArrayList<TextView>();
        textTituloCominda = new ArrayList<String[]>();
        textCominda = new ArrayList<String[]>();


        //dialog.setTitle("Title...");

        for (int i = 0; i < cantidad; i++) {
            TextView t = new TextView(getContext());
            textPedido.add(new TextView(getContext()));
            LinearLayout l = new LinearLayout(getContext());
            multiplicadores.add(l);
            Button b1 = new Button(getContext());
            Button b2 = new Button(getContext());
            Button b3 = new Button(getContext());
            botonMas.add(b1);
            botonMenos.add(b2);
            botonNum.add(b3);
        }


        for (int j = 0; j < cantidad; j++) {
            textPedido.get(j).setText(checksText.get(j));
            botonMenos.get(j).setId(j);
            botonMas.get(j).setId(j);
            botonNum.get(j).setId(j);
            botonMenos.get(j).setText("-");
            botonMas.get(j).setText("+");
            botonNum.get(j).setText("1");
            textPedido.get(j).setTypeface(typeface);
            botonNum.get(j).setTypeface(typeface);
            botonMenos.get(j).setTypeface(typeface);
            botonMas.get(j).setTypeface(typeface);
            textPedido.get(j).setTextColor(Color.parseColor(colorTitulo));
            botonNum.get(j).setTextColor(Color.parseColor("#FFFFFF"));
            botonMas.get(j).setTextColor(Color.parseColor("#FFFFFF"));
            botonMenos.get(j).setTextColor(Color.parseColor("#FFFFFF"));
            botonNum.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorTexto)));
            botonMenos.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorTexto)));
            botonMas.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorTexto)));
            textPedido.get(j).setTextSize(20);
            container.addView(textPedido.get(j));
            multiplicadores.get(j).addView(botonMenos.get(j));
            multiplicadores.get(j).addView(botonNum.get(j));
            multiplicadores.get(j).addView(botonMas.get(j));
            multiplicadores.get(j).setOrientation(LinearLayout.HORIZONTAL);
            container.addView(multiplicadores.get(j));
        }
        for (int k = 0; k < textPedido.size(); k++) {
            for (int x = 0; x < textPedido.size(); x++) {
                if (textPedido.get(x).getText().equals(textPedido.get(k))) {
                    container.removeView(textPedido.get(x));
                    container.removeView(textos.get(x));
                }
            }
        }
        for (int i = 0; i < botonNum.size(); i++) {
            botonMenos.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v.findViewById(v.getId());
                    for (int i = 0; i < botonNum.size(); i++) {
                        if (b.getId() == botonNum.get(i).getId()) {
                            int numBoton = Integer.parseInt(botonNum.get(i).getText().toString());
                            if (numBoton > 1) {
                                //precioNum.set(i, precioNum.get(i) - precioNum.get(i));
                                numBoton--;
                                if (precioNum.size() == 1) {
                                    precioNumVariable.set(i, precioNumVariable.get(i) - precioNum.get(i));
                                    total = precioNumVariable.get(i);
                                    botonNum.get(i).setText(numBoton + "");
                                    total = (double) Math.round(total * 100d) / 100d;
                                    precioTotal.setText("Precio total: " + total + "0€");
                                } else {
                                    botonNum.get(i).setText(numBoton + "");
                                    precioNumVariable.set(i, precioNumVariable.get(i) - precioNum.get(i));
                                    total = 0;
                                    for (int j = 0; j < precioNumVariable.size(); j++) {
                                        total = precioNumVariable.get(j) + total;
                                    }
                                    total = (double) Math.round(total * 100d) / 100d;
                                    precioTotal.setText("Precio total: " + total + "0€");
                                }
                            } else {
                                Toast.makeText(getContext(), "opcion no valida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            });
        }
        for (int i = 0; i < botonNum.size(); i++) {
            botonMas.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v.findViewById(v.getId());
                    for (int i = 0; i < botonNum.size(); i++) {
                        if (b.getId() == botonNum.get(i).getId()) {
                            int numBoton = Integer.parseInt(botonNum.get(i).getText().toString());
                            numBoton++;
                            if (precioNum.size() == 1) {
                                precioNumVariable.set(i, precioNumVariable.get(i) + precioNum.get(i));
                                total = precioNumVariable.get(i);
                                botonNum.get(i).setText(numBoton + "");
                                total = (double) Math.round(total * 100d) / 100d;
                                precioTotal.setText("Precio total: " + total + "0€");
                            } else {
                                botonNum.get(i).setText(numBoton + "");
                                precioNumVariable.set(i, precioNumVariable.get(i) + precioNum.get(i));
                                total = 0;
                                for (int j = 0; j < precioNumVariable.size(); j++) {
                                    total = precioNumVariable.get(j) + total;
                                }
                                total = (double) Math.round(total * 100d) / 100d;
                                precioTotal.setText("Precio total: " + total + "0€");
                            }

                        }
                    }

                }
            });
        }

        for (int k = 0; k < textPedido.size(); k++) {
            if (textPedido.get(k).getText().toString().length() <= 0) {
            } else {
                precioString.add(textPedido.get(k).getText().toString().substring(textPedido.get(k).getText().toString().lastIndexOf('0') - 3));
            }
        }
        total = 0;
        for (int l = 0; l < precioString.size(); l++) {
            precioNum.add(Double.parseDouble(precioString.get(l).substring(0, 3)));
            precioNumVariable.add(Double.parseDouble(precioString.get(l).substring(0, 3)));
            total = precioNumVariable.get(l) + total;
        }
        total = (double) Math.round(total * 100d) / 100d;
        precioTotal.setText("Precio total: " + total + "0€");
        precioTotal.setTextColor(Color.parseColor(colorTexto));
        precioTotal.setTypeface(typeface);
        precioTotal.setTextSize(20);


        Button comprar = new Button(getContext());
        comprar.setText("Pagar");
        comprar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorTitulo)));
        comprar.setTextColor(Color.parseColor("#FFFFFF"));
        comprar.setTypeface(typeface);
        container.addView(precioTotal);
        container.addView(comprar);
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> datos = new ArrayList<>();
                for (int i = 0; i < textPedido.size(); i++) {
                    datos.add(textPedido.get(i).getText().toString());
                }
                b.putString("email", email);
                b.putStringArrayList("pedido", datos);
                b.putDouble("total", total);
                pedidoActivity.putExtras(b);
                startActivity(pedidoActivity);
                dialog.dismiss();
            }
        });

        dialog.show();
        for (int i = 0; i < textPedido.size(); i++) {
            textPedido.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    num++;
                    for (int i = 0; i < textos.size(); i++) {
                        textos.get(i).setText("Cantidad " + num + "\n");
                    }
                }
            });
        }

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < textPedido.size(); i++) {
                    container.removeView(textPedido.get(i));
                }
                for (int l = 0; l < multiplicadores.size(); l++) {
                    for (int x = 0; x < botonMas.size(); x++) {
                        multiplicadores.get(x).removeView(botonMas.get(x));
                        multiplicadores.get(x).removeView(botonMenos.get(x));
                        multiplicadores.get(x).removeView(botonNum.get(x));
                    }
                    container.removeView(multiplicadores.get(l));
                }
                for (int j = 0; j < precioNum.size(); j++) {
                    precioNum.remove(j);
                    precioNumVariable.remove(j);
                }
                for (int k = 0; k < precioString.size(); k++) {
                    precioString.remove(k);
                }
                dialog.dismiss();
            }
        });
    }

    public void borrar() {

        for (int j = 0; j < checks.size(); j++) {
            if (checks.get(j).isChecked()) {
                checksText.add(checks.get(j).getText().toString());
            }
            checks.get(j).setChecked(false);
        }
        for (int i = 0; i < textos.size(); i++) {
            container.removeView(textos.get(i));
            container.removeView(checks.get(i));
        }
        for (int x = 0; x < textPedido.size(); x++) {
            container.removeView(textPedido.get(x));
        }
        for (int k = 0; k < precioNum.size(); k++) {
            precioNum.remove(k);
            precioNumVariable.remove(k);
        }
        for (int l = 0; l < precioString.size(); l++) {
            precioString.remove(l);
        }
        for (int z = 0; z < multiplicadores.size(); z++) {
            for (int x = 0; x < botonMas.size(); x++) {
                multiplicadores.get(x).removeView(botonMas.get(x));
                multiplicadores.get(x).removeView(botonMenos.get(x));
                multiplicadores.get(x).removeView(botonNum.get(x));
            }
            container.removeView(multiplicadores.get(z));
            if (!precioTotal.getContext().equals(null)) {
                container.removeView(precioTotal);
            }
        }
    }

    private void mostrarMenu(int comida, int tituloComida, String colorFondo, String colorTitulo, String colorTexto
            , int cantidad) {
        textos = new ArrayList<TextView>();
        textTituloCominda = new ArrayList<String[]>();
        textCominda = new ArrayList<String[]>();

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.menu_emergente);
        ScrollView containerPadre = dialog.findViewById(R.id.padre);
        container = dialog.findViewById(R.id.containerMenu);

        containerPadre.setBackgroundColor(Color.parseColor(colorFondo));

        //dialog.setTitle("Title...");

        for (int i = 0; i < cantidad; i++) {
            textTituloCominda.add(getResources().getStringArray(tituloComida));
            checks.add(new CheckBox(getContext()));
        }
        for (int i = 0; i < cantidad; i++) {
            textCominda.add(getResources().getStringArray(comida));
            TextView t = new TextView(getContext());
            textos.add(t);
        }

        for (int j = 0; j < textos.size(); j++) {
            checks.get(j).setText(textTituloCominda.get(j)[j]);
            textos.get(j).setText(textCominda.get(j)[j]);
            checks.get(j).setId(j);
            textos.get(j).setId(j);
            checks.get(j).setTypeface(typeface);
            textos.get(j).setTypeface(typeface);
            checks.get(j).setTextColor(Color.parseColor(colorTitulo));
            textos.get(j).setTextColor(Color.parseColor(colorTexto));
            checks.get(j).setButtonTintList(ColorStateList.valueOf(Color.parseColor(colorTitulo)));
            checks.get(j).setTextSize(20);
            container.addView(checks.get(j));
            container.addView(textos.get(j));
        }

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < textos.size(); i++) {

                    container.removeView(checks.get(i));
                    container.removeView(textos.get(i));
                }
                //containerPadre.removeView(container);
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}