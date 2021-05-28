package com.example.reservarmesas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Confirmacion extends AppCompatActivity {

        TextView datos;
        Button cancelar, confirmar;
        Bundle bundle;
        private Intent tabs;
        String date,number,time,email;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.confirmacion);

            getSupportActionBar().hide();

            datos=(TextView)findViewById(R.id.textConfirmacion);
            confirmar=(Button)findViewById(R.id.bConfirmar);
            cancelar=(Button)findViewById(R.id.bCancelar);
            tabs=new Intent(this, Tabs.class);

            bundle= getIntent().getExtras();
            email=bundle.getString("email");
            date=bundle.getString("date");
            number=bundle.getString("num");
            time=bundle.getString("time");

            datos.setText("Tu reserva: "+date+" "+ number+" personas "+ time);

            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> reserva = new HashMap<>();
                    db.collection("Booking").document(email)
                            .set(reserva)
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
                    reserva.put("date", date);
                    reserva.put("people", number);
                    reserva.put("time", time);
                    db.collection("Booking").document(email).update(reserva);

                    Toast.makeText(v.getContext(), "Reserva realizada", Toast.LENGTH_SHORT).show();
                    datos.setText("");
                    finish();
                }
            });
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Reserva cancelada", Toast.LENGTH_SHORT).show();
                    datos.setText("");
                    finish();
                }
            });
        }
}
