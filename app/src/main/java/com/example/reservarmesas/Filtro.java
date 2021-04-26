package com.example.reservarmesas;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class Filtro extends AppCompatActivity {

    private Button menos, mas, reserve, number, cerrar;
    int num=0;
    private Intent login;
    private GridLayout contenedor;
    ArrayList<String>datos= new ArrayList<String>();
    ArrayList<View>tiempos;
    ArrayList<Button>botones;
    ArrayList<String[]>times;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText etPlannedDate;
    Bundle bundle;
    DatePickerDialog picker;
    String tiempo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filtro);

        getSupportActionBar().hide();
        etPlannedDate = (EditText) findViewById(R.id.etPlannedDate);
        login = new Intent(this, login.class);
        contenedor=(GridLayout)findViewById(R.id.contenedorTiempos);
        bundle= getIntent().getExtras();
        menos=(Button)findViewById(R.id.button_menos);
        mas=(Button)findViewById(R.id.button_mas);
        reserve=(Button)findViewById(R.id.button_reserve);
        cerrar=(Button)findViewById(R.id.button_cerrar);
        number=(Button)findViewById(R.id.button_number);
        number.setText(num+"");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        tiempos=contenedor.getTouchables();
        crearTiempos(tiempos);
        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num= Integer.parseInt(number.getText().toString());
                if(num>0){
                    num--;
                    number.setText(num+"");
                }
            }
        });
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num= Integer.parseInt(number.getText().toString());
                if(num<6){
                    num++;
                    number.setText(num+"");
                }
                else{
                    Toast. makeText(getApplicationContext(),"6 people maximum", Toast. LENGTH_SHORT).show();
                }
            }
        });
/*
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //datos.add(date.getText().toString());
                datos.add(number.getText().toString());
//                datos.add(time.getText().toString());

            }
        });
*/
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(mGoogleSignInClient);
                FirebaseAuth.getInstance().signOut();
                //onBackPressed();
                startActivity(login);
                finish();
            }
        });
    }

    private void crearTiempos(ArrayList<View> tiempos) {
        botones = new ArrayList<Button>();
        times = new ArrayList<String[]>();
        for (int i = 0; i < tiempos.size(); i++) {
            times.add(getResources().getStringArray(R.array.times));
            botones.add((Button) tiempos.get(i));
        }
        for (int j = 0; j < botones.size(); j++) {
            botones.get(j).setText(times.get(j)[j]);
            botones.get(j).setId(j);
            botones.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v.findViewById(v.getId());
                    for(int i=0;i<botones.size();i++){
                        //if(botones.get(i).isEnabled()){
                            botones.get(i).setEnabled(true);
                        //}
                    }
                    b.setEnabled(false);
                    tiempo=b.getText().toString();
                }
            });
            reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!etPlannedDate.getText().toString().equals("")&&num!=0&&!tiempo.equals("")){
                    Map<String, Object> reserva = new HashMap<>();
                    db.collection("Booking").document(bundle.getString("email"))
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
                        reserva.put("date", etPlannedDate.getText().toString());
                        reserva.put("people", number.getText().toString());
                        reserva.put("time",tiempo);
                        db.collection("Booking").document(bundle.getString("email")).update(reserva);
                    }else{
                        Toast. makeText(getApplicationContext(),"Falta algun campo por rellenar", Toast. LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void signOut(GoogleSignInClient mGoogleSignInClient) {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    private void showDatePickerDialog() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(Filtro.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                        String[] fechArray = date.split("/");

                        int dia = Integer.valueOf(fechArray[0]);
                        int mes = Integer.valueOf(fechArray[1]) - 1;
                        int anio = Integer.valueOf(fechArray[2]);


                        Calendar c1 = new GregorianCalendar(anio, mes, dia);

                        String fechaActual=Calendar.getInstance().get(5)+"/"+(Calendar.getInstance().get(2)+1)+"/"+Calendar.getInstance().get(1);

                        long hoy=GregorianCalendar.getInstance().getTimeInMillis();
                        long dateElegido=c1.getTimeInMillis();
                        if(dateElegido>=hoy||fechaActual.equals(date)){
                            etPlannedDate.setText(date);
                        }
                        else {
                            Toast. makeText(getApplicationContext(),"fecha no valida", Toast. LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
        picker.show();
    }
}
