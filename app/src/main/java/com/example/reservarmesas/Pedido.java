package com.example.reservarmesas;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class Pedido extends AppCompatActivity {

    ArrayList<TextView> datos = new ArrayList<>();
    private LinearLayout container;
    private Button continuar, volver;
    private TextView total, textTarjeta, confirmacion;
    private EditText tarjeta;
    private ImageView logo, check;
    private ProgressBar spinner;
    Bundle bundle;
    Typeface typeface;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido);

        getSupportActionBar().hide();

        bundle = getIntent().getExtras();
        container = findViewById(R.id.listaPedido);
        typeface = ResourcesCompat.getFont(this, R.font.denk_one);

        rellenarLista();

    }

    private void rellenarLista() {
        for (int i = 0; i < bundle.getStringArrayList("pedido").size(); i++) {
            TextView t = new TextView(this);
            datos.add(t);
        }
        for (int j = 0; j < datos.size(); j++) {
            datos.get(j).setText(bundle.getStringArrayList("pedido").get(j));
            datos.get(j).setTextSize(20);
            datos.get(j).setTextColor(Color.parseColor("#3C169D"));
            datos.get(j).setTypeface(typeface);
            container.addView(datos.get(j));
        }
        total = new TextView(this);
        total.setText("Total: " + bundle.getDouble("total") + "0€\n");
        total.setTextSize(30);
        total.setTypeface(typeface);
        total.setTextColor(Color.parseColor("#F15614"));
        container.addView(total);

        textTarjeta = new TextView(this);
        textTarjeta.setText("Introduce tu tarjeta de credito: ");
        textTarjeta.setTextSize(20);
        textTarjeta.setTypeface(typeface);
        textTarjeta.setTextColor(Color.parseColor("#000000"));
        container.addView(textTarjeta);

        tarjeta = new EditText(this);
        tarjeta.setTypeface(typeface);
        tarjeta.setInputType(InputType.TYPE_CLASS_NUMBER);
        container.addView(tarjeta);

        continuar = new Button(this);
        continuar.setTypeface(typeface);
        continuar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2FD4A5")));
        continuar.setTextColor(Color.parseColor("#FFFFFF"));
        continuar.setText("Continuar");
        container.addView(continuar);

        logo = new ImageView(this);
        logo.setImageResource(R.drawable.portada);
        container.addView(logo);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tarjeta.getText().toString().length() == 16) {
                    final Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.confirmacion);
                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.textConfirmacion);
                    text.setText("¿Estas seguro de hacer la compra?");

                    Button confirmar = (Button) dialog.findViewById(R.id.bConfirmar);
                    Button cancelar = (Button) dialog.findViewById(R.id.bCancelar);
                    // if button is clicked, close the custom dialog
                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> pedido = new HashMap<>();

                            db.collection("Order").document(bundle.getString("email"))
                                    .set(pedido)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error writing document", e);
                                        }
                                    });

                            pedido.put("price", total.getText().toString());
                            String[] item = bundle.getStringArrayList("pedido").toArray(new String[bundle.getStringArrayList("pedido").size()]);
                            pedido.put("products", Arrays.asList(item));

                            db.collection("Order").document(bundle.getString("email"))
                                    .set(pedido);

                            Toast.makeText(v.getContext(), "Pago realizada", Toast.LENGTH_SHORT).show();


                            tarjeta.setVisibility(View.GONE);
                            continuar.setVisibility(View.GONE);
                            textTarjeta.setVisibility(View.GONE);
                            logo.setVisibility(View.GONE);
                            container.removeView(tarjeta);
                            container.removeView(continuar);
                            container.removeView(textTarjeta);
                            container.removeView(logo);
                            //finish();


                            dialog.dismiss();
                            confirmarPedido();
                        }
                    });
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(), "Reserva cancelada", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(v.getContext(), "Error, rellena los campos correctamente", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void confirmarPedido() {
        spinner=new ProgressBar(this);
        container.addView(spinner);
        spinner.setVisibility(View.VISIBLE);

        check=new ImageView(this);
        container.addView(check);

        confirmacion = new TextView(this);
        confirmacion.setTextSize(20);
        confirmacion.setTypeface(typeface);
        confirmacion.setTextColor(Color.parseColor("#000000"));
        container.addView(confirmacion);

        volver=new Button(this);
        volver.setText("Volver");
        volver.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2FD4A5")));
        volver.setTextColor(Color.parseColor("#FFFFFF"));
        container.addView(volver);
        volver.setVisibility(View.INVISIBLE);

        logo = new ImageView(this);
        logo.setImageResource(R.drawable.portada);
        container.addView(logo);

        confirmacion.setText("Su pedido se esta realizando... ");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                confirmacion.setText("Su pedido se ha realizado correctamente ");
                check.setImageResource(R.drawable.fui_ic_check_circle_black_128dp);
                spinner.setVisibility(View.GONE);
                volver.setVisibility(View.VISIBLE);

            }
        }, 3000);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });    }
}
