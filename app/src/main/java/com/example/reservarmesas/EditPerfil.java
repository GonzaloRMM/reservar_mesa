package com.example.reservarmesas;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPerfil extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String email;
    public EditPerfil(String email) {
        // Required empty public constructor
        this.email=email;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPerfil.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPerfil newInstance(String param1, String param2) {
        EditPerfil fragment = new EditPerfil("");
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
    private LinearLayout editar,cerrar;
    private ImageButton edit,salir;
    private TextView saludo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Intent splas,perfil;
    Typeface typeface;
    private Button editarPerfil, cerrarSesion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_perfil,container,false);


        editar=(LinearLayout)v.findViewById(R.id.layoutEditar);
        cerrar=(LinearLayout)v.findViewById(R.id.layoutSalir);

        saludo=(TextView)v.findViewById(R.id.textSaludo);

        editarPerfil=(Button)v.findViewById(R.id.editar);
        cerrarSesion=(Button)v.findViewById(R.id.cerrar);

        edit=(ImageButton)v.findViewById(R.id.button_editar);
        salir=(ImageButton)v.findViewById(R.id.button_cerrarSesion);

        splas = new Intent(getActivity(), SplashScreen.class);
        perfil = new Intent(getActivity(), Perfil.class);

        typeface= ResourcesCompat.getFont(getContext(), R.font.denk_one);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        buscarNombre(saludo,"Hi ");
        saludo.setTypeface(typeface);
        saludo.setTextSize(40);
        saludo.setTextColor(Color.parseColor("#000000"));


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOff(mGoogleSignInClient);
            }


        });
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOff(mGoogleSignInClient);
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOff(mGoogleSignInClient);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
            }
        });
        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
            }
        });
        return v;
        //return inflater.inflate(R.layout.fragment_edit_perfil, container, false);
    }

    public void editar() {
        Bundle b = new Bundle();
        b.putString("email", email);
        perfil.putExtras(b);
        startActivity(perfil);
    }

    public void buscarNombre(TextView saludo,String hola) {
        db.collection("users").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    if(value.contains("user name")){
                        saludo.setText(hola + value.getString("user name"));
                    }
                }
            }
        });
        }

    private void signOut(GoogleSignInClient mGoogleSignInClient) {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    private void signOff(GoogleSignInClient mGoogleSignInClient) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.confirmacion);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textConfirmacion);
        text.setText("¿Quieres cerrar sesion? ");

        Button confirmar = (Button) dialog.findViewById(R.id.bConfirmar);
        Button cancelar = (Button) dialog.findViewById(R.id.bCancelar);
        // if button is clicked, close the custom dialog
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(mGoogleSignInClient);
                FirebaseAuth.getInstance().signOut();
                //onBackPressed();
                startActivity(splas);
                getActivity().finish();
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
}

