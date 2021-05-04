package com.example.reservarmesas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class admin extends AppCompatActivity {

    ArrayList<ArrayList<String>>personas;
    ArrayList<String>datos;
    Intent login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);


        crearAdapter((ListView)findViewById(R.id.listView1),recogetDatos());
        login= new Intent(this, login.class);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ((Button)findViewById(R.id.bRefrescar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearAdapter((ListView)findViewById(R.id.listView1),recogetDatos());
            }
        });
        ((Button)findViewById(R.id.bClose)).setOnClickListener(new View.OnClickListener() {
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

    private void crearAdapter(ListView viewById, ArrayList<ArrayList<String>> datos) {
        //Toast. makeText(getApplicationContext(),""+datos.get(0).get(2), Toast. LENGTH_SHORT).show();

        AdaptadorListView adapter=new AdaptadorListView(this,datos);
        viewById.setAdapter(adapter);
    }

    private ArrayList<ArrayList<String>> recogetDatos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        personas= new ArrayList<>();
        ArrayList<String>id=new ArrayList<>();
        datos=new ArrayList<>();
        CollectionReference users=db.collection("users");
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id.add(document.getId().toString());
                                Toast. makeText(getApplicationContext(),""+document.getId(), Toast. LENGTH_SHORT).show();

                                    db.collection("Booking").document(document.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (value.exists()) {
                                                if (value.contains("fecha") && value.contains("hora") && value.contains("personas")) {
                                                    datos.add("Reserva: " + value.getString("fecha") + "  " + value.getString("hora") +
                                                            "  " + value.getString("personas"));

                                                }
                                            }
                                        }
                                    });

//                                    Toast. makeText(getApplicationContext(),""+datos, Toast. LENGTH_SHORT).show();
                                db.collection("users").document(document.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if(value.exists()){
                                            if(value.contains("user name")){
                                                datos.add(""+ value.getString("user name"));
                                            }
                                            if(value.contains("phone")){
                                                datos.add(""+ value.getString("phone"));
                                            }
                                        }
                                    }
                                });
                                }
                                }
                        personas.add(datos);
                            }

                });

        return personas;
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
}
