package com.example.reservarmesas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static android.util.Patterns.EMAIL_ADDRESS;

public class Registro extends AppCompatActivity {


    Button cerrarSesion, guardar;
    EditText name, phone, password, password2, emailText;
    private Intent tabs, login;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        emailText = (EditText) findViewById(R.id.Edit_Email);
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

        if (!email.equals("")) {
            emailText.setText(email);
            emailText.setFocusable(false);
        }

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
                    Toast.makeText(getApplicationContext(), "nombre no valido", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().equals(password2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "la contraseña no es la misma", Toast.LENGTH_SHORT).show();
                } else if (phone.getText().length() != 9) {
                    Toast.makeText(getApplicationContext(), "telefono no valido", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().length() <= 5) {
                    Toast.makeText(getApplicationContext(), "la contrasela necesita 6 caracteres minimo", Toast.LENGTH_SHORT).show();
                } else if (!validarEmail(emailText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Email incorrecto", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> user = new HashMap<>();


                    user.put("password", password.getText().toString());
                    user.put("user name", name.getText().toString());
                    user.put("phone", phone.getText().toString());
                    user.put("roll", "user");

                    DocumentReference docRef = db.collection("users").document(emailText.getText().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    db.collection("users").document(emailText.getText().toString()).update(user);

                                } else {
                                    db.collection("users").document(emailText.getText().toString())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    db.collection("users").document(emailText.getText().toString()).update(user);
                                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error writing document", e);
                                                }
                                            });
                                }
                            }
                        }
                    });


                    Bundle b2 = new Bundle();
                    b2.putString("email", emailText.getText().toString());
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

    private boolean validarEmail(String email) {
        Pattern pattern = EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}