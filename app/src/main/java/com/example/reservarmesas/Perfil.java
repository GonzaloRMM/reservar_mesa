package com.example.reservarmesas;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Perfil  extends AppCompatActivity {


    private Bundle b;
    private Button volver,guardar,delete;
    private EditText name,phone,newPassword,lastPassword,numberPhone,confirmNewPassword;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String email;
    private Intent login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        getSupportActionBar().hide();

        b=getIntent().getExtras();
        email=b.getString("email");
        volver=(Button)findViewById(R.id.bVolverTabs);
        guardar=(Button)findViewById(R.id.bGuardarPerfil);
        delete=(Button)findViewById(R.id.bEliminarCuenta);

        name=(EditText) findViewById(R.id.textPersonName);
        newPassword=(EditText)findViewById(R.id.textNewPassword);
        confirmNewPassword=(EditText)findViewById(R.id.textNewPasswordConfirmada);
        lastPassword=(EditText)findViewById(R.id.textLastPassword);
        numberPhone=(EditText)findViewById(R.id.editTextPhone);

        login = new Intent(this, login.class);

        buscarDatos();
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        TextView lastPasswordText=(TextView)findViewById(R.id.LastPassword);
                        TextView nameText=(TextView)findViewById(R.id.nameText);
                        TextView phoneText=(TextView)findViewById(R.id.phoneText);
                        TextView newPasswordText=(TextView)findViewById(R.id.newPasswordText);
                        TextView confirmNewPasswordText=(TextView)findViewById(R.id.confirmNewPasswordText);
                        if (value.exists()) {
                            if (value.contains("password")) {
                                if(lastPassword.getText().toString().equals("")
                                        || !lastPassword.getText().toString().equals(value.getString("password"))){
                                    lastPassword.setText("");
                                    lastPasswordText.setTextColor(Color.parseColor("#FF0000"));
                                }
                                else{
                                    lastPasswordText.setTextColor(Color.parseColor("#000000"));
                                }
                                    if (!name.getText().toString().equals("") && !numberPhone.getText().toString().equals("")
                                            && !newPassword.getText().toString().equals("")
                                            && newPassword.getText().toString().equals(confirmNewPassword.getText().toString())
                                            && newPassword.getText().length()>=6
                                            && numberPhone.getText().length()==9
                                            && lastPassword.getText().toString().equals(value.getString("password"))) {
                                        Map<String, Object> user = new HashMap<>();

                                        user.put("password", newPassword.getText().toString());
                                        user.put("user name", name.getText().toString());
                                        user.put("phone", numberPhone.getText().toString());

                                        db.collection("users").document(email).update(user);

                                        nameText.setTextColor(Color.parseColor("#000000"));
                                        phoneText.setTextColor(Color.parseColor("#000000"));
                                        lastPasswordText.setTextColor(Color.parseColor("#000000"));
                                        newPasswordText.setTextColor(Color.parseColor("#000000"));
                                        confirmNewPasswordText.setTextColor(Color.parseColor("#000000"));

                                        //Bundle b = new Bundle();
                                        //b.putString("email", email);
                                        //tabs.putExtras(b);
                                        //startActivity(tabs);
                                        finish();
                                    }
                                    else{
                                        if(name.getText().toString().equals("")){
                                            name.setText("");
                                            nameText.setTextColor(Color.parseColor("#FF0000"));
                                        }else{
                                            nameText.setTextColor(Color.parseColor("#000000"));
                                        }
                                        if(numberPhone.getText().toString().equals("")||numberPhone.getText().length()!=9){
                                            numberPhone.setText("");
                                            phoneText.setTextColor(Color.parseColor("#FF0000"));
                                        }
                                        else{
                                            phoneText.setTextColor(Color.parseColor("#000000"));
                                        }
                                        if(newPassword.getText().toString().equals(confirmNewPassword)){
                                            newPassword.setText("");
                                            confirmNewPassword.setText("");
                                            newPasswordText.setTextColor(Color.parseColor("#FF0000"));
                                            confirmNewPasswordText.setTextColor(Color.parseColor("#FF0000"));
                                        }
                                        else{
                                            newPasswordText.setTextColor(Color.parseColor("#000000"));
                                            confirmNewPasswordText.setTextColor(Color.parseColor("#000000"));
                                        }
                                        if(newPassword.getText().length()<6){
                                            newPassword.setText("");
                                            newPasswordText.setTextColor(Color.parseColor("#FF0000"));
                                        }
                                        else{
                                            newPasswordText.setTextColor(Color.parseColor("#000000"));
                                        }
                                        Toast. makeText(getApplicationContext(),"The data is wrong", Toast. LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                });

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.confirmacion);
                dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.textConfirmacion);
                text.setText("do you want to delete this account? ");

                Button confirmar = (Button) dialog.findViewById(R.id.bConfirmar);
                Button cancelar = (Button) dialog.findViewById(R.id.bCancelar);
                // if button is clicked, close the custom dialog
                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("users").document(email).delete();
                        startActivity(login);
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void buscarDatos() {
        db.collection("users").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    if(value.contains("user name")){
                        name.setHint(""+ value.getString("user name"));
                    }
                    if(value.contains("phone")){
                        numberPhone.setHint(""+ value.getString("phone"));
                    }
                }
            }
        });
    }
}