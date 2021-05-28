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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
    EditText name, phone, password, password2;
    private Intent tabs, login;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Bundle b;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        getSupportActionBar().hide();
        Bundle b = getIntent().getExtras();
        cerrarSesion = (Button) findViewById(R.id.B_CerrarSesion);
        guardar = (Button) findViewById(R.id.B_GuardarDatos);

        name = (EditText) findViewById(R.id.Edit_Nombre);
        phone = (EditText) findViewById(R.id.Edit_Telefono);
        password2 = (EditText) findViewById(R.id.edit_Password2);
        password = (EditText) findViewById(R.id.edit_Password);

        tabs = new Intent(this, Tabs.class);
        login = new Intent(this, login.class);

        email = b.getString("email");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(email).delete();
                signOut(mGoogleSignInClient);
                FirebaseAuth.getInstance().signOut();
                startActivity(login);
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "invalid name", Toast.LENGTH_SHORT).show();
                }
                else if (!password.getText().toString().equals(password2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "the password is not the same", Toast.LENGTH_SHORT).show();
                }
                else if (phone.getText().length() != 9) {
                    Toast.makeText(getApplicationContext(), "wrong phone", Toast.LENGTH_SHORT).show();
                }
                else if (password.getText().toString().length() <= 5) {
                    Toast.makeText(getApplicationContext(), "password needs 6 characters", Toast.LENGTH_SHORT).show();
                }
                else {
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
    }

    private void signOut(GoogleSignInClient mGoogleSignInClient) {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}