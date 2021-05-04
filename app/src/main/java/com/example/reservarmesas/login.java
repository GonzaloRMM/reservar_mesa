package com.example.reservarmesas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button continuar;
    private EditText email, password;
    private Intent registro, filtro,tabs, admin;
    SignInButton google;
    private final int RC_SIGN_IN=1;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getSupportActionBar().hide();
        continuar= (Button)findViewById(R.id.B_Continuar);
        google =(SignInButton) findViewById(R.id.button_google);
        email = (EditText) findViewById(R.id.Edit_Correo);
        password = (EditText) findViewById(R.id.Edit_Contraseña);
        registro = new Intent(this, Registro.class);
        filtro = new Intent(this, Filtro.class);
        tabs = new Intent(this, Tabs.class);
        admin = new Intent(this, admin.class);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_google:
                        signIn(mGoogleSignInClient);
                        break;
                }
            }
        });
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference users=db.collection("users");
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.getId().equals(email.getText().toString())     ){
                                            if(document.getData().get("password").equals(password.getText().toString())){
                                                //Toast. makeText(getApplicationContext(),"ole to", Toast. LENGTH_SHORT).show();
                                                if(!document.getData().get("roll").equals("admin")){
                                                    spinner.setVisibility(View.VISIBLE);
                                                    Bundle b = new Bundle();
                                                    b.putString("email", email.getText().toString());
                                                    tabs.putExtras(b);
                                                    startActivity(tabs);
                                                    spinner.setVisibility(View.GONE);
                                                    //spinner.setVisibility(View.VISIBLE);
                                                }
                                                else{
                                                    Bundle b = new Bundle();
                                                    b.putString("email", email.getText().toString());
                                                    admin.putExtras(b);
                                                    startActivity(admin);
                                                }
                                                }
                                       }
                                        }
                                    }
                                else{
                                    Toast. makeText(getApplicationContext(),"email or password is not correct", Toast. LENGTH_SHORT).show();
                                    email.setText("");
                                    password.setText("");
                                }
                            }
                        });
            }
        });
    }
    private void signIn(GoogleSignInClient mGoogleSignInClient) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //startActivityForResult(myIntent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //Toast. makeText(getApplicationContext(),account.getEmail()+"\n", Toast. LENGTH_SHORT).show();
            Bundle b = new Bundle();

            b.putString("email", account.getEmail().toString());


            Map<String, Object> person = new HashMap<>();


            DocumentReference docRef = db.collection("users").document(account.getEmail().toString());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.getData().get("password").equals(password.getText().toString())) {
                                //Toast. makeText(getApplicationContext(),"ole to", Toast. LENGTH_SHORT).show();
                                if (!document.getData().get("roll").equals("admin")) {
                                    spinner.setVisibility(View.VISIBLE);
                                    Bundle b = new Bundle();
                                    b.putString("email", email.getText().toString());
                                    tabs.putExtras(b);
                                    startActivity(tabs);
                                    spinner.setVisibility(View.GONE);
                                    //spinner.setVisibility(View.VISIBLE);
                                } else {
                                    startActivity(admin);
                                }
                                /*
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            spinner.setVisibility(View.VISIBLE);

                            //if(db.collection("users"))
                            filtro.putExtras(b);
                            //startActivityForResult(filtro,1);
                            tabs.putExtras(b);
                            startActivity(tabs);
                                 */
                            } else {
                                db.collection("users").document(account.getEmail().toString())
                                        .set(person)
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
                                spinner.setVisibility(View.VISIBLE);
                                registro.putExtras(b);
                                startActivityForResult(registro, 1);
                                spinner.setVisibility(View.GONE);
                                email.setText("");
                                password.setText("");
                            }
                        }
                    }
                }
            });
            //updateUI(account);
        }
        catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
