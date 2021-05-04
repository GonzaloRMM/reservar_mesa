package com.example.reservarmesas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {


    TextView emailRecuperado, nombreRecuperado, telefonoRecuperado;
    Button cerrarSesion, recuperar, guardar, borrar;
    EditText name, phone,password, password2;
    private Intent tabs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        getSupportActionBar().hide();
        Bundle b= getIntent().getExtras();
        //emailRecuperado=(TextView)findViewById(R.id.Text_EmailRecuperado);
        cerrarSesion=(Button) findViewById(R.id.B_CerrarSesion);
        //recuperar=(Button) findViewById(R.id.B_RecuperarDatos);
        guardar=(Button) findViewById(R.id.B_GuardarDatos);
        //borrar=(Button) findViewById(R.id.B_BorrarDatos);
        name=(EditText)findViewById(R.id.Edit_Nombre);
        phone=(EditText)findViewById(R.id.Edit_Telefono);
        password2=(EditText)findViewById(R.id.edit_Password2);
        password=(EditText)findViewById(R.id.edit_Password);
        tabs = new Intent(this, Tabs.class);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("")){
                    Toast. makeText(getApplicationContext(),"invalid name", Toast. LENGTH_SHORT).show();
                }
                else if(!password.getText().toString().equals(password2.getText().toString())){
                    Toast. makeText(getApplicationContext(),"the password is not the same", Toast. LENGTH_SHORT).show();
                }
                else if(phone.getText().length()!=9){
                    Toast. makeText(getApplicationContext(),"wrong phone", Toast. LENGTH_SHORT).show();
                }
                else if(password.getText().toString().length()<=5){
                    Toast. makeText(getApplicationContext(),"password needs 6 characters", Toast. LENGTH_SHORT).show();
                }
                    else{
                    Map<String, Object> user = new HashMap<>();

                    user.put("password", password.getText().toString());
                    user.put("user name", name.getText().toString());
                    user.put("phone", phone.getText().toString());
                    user.put("roll", "user");

                    db.collection("users").document(b.getString("email")).update(user);


                    Bundle b2 = new Bundle();
                    b2.putString("email", b.getString("email"));
                    tabs.putExtras(b2);
                    startActivity(tabs);
                }
                }
        });
        /*
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                        nombreRecuperado.setText(""+document.getData().get("nombre"));
                                        telefonoRecuperado.setText("" + document.getData().get("telefono"));
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document("")
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                            }
                        });
            }
        });
         */
    }
}