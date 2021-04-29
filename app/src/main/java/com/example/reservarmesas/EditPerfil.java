package com.example.reservarmesas;

import android.content.Intent;
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

    private LinearLayout principal,segundario;
    private Button eliminar,edit,salir;
    private TextView saludo, reserva;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Intent login;
    Typeface typeface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ScrollView scroller = new ScrollView(getActivity());

        principal=new LinearLayout(getActivity());
        segundario=new LinearLayout(getActivity());
        saludo=new TextView(getActivity());
        reserva=new TextView(getActivity());
        eliminar=new Button(getActivity());
        edit=new Button(getActivity());
        salir=new Button(getActivity());
        login = new Intent(getActivity(), login.class);
        typeface= ResourcesCompat.getFont(getContext(), R.font.denk_one);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        segundario.addView(reserva);
        segundario.addView(eliminar);

        principal.addView(saludo);
        principal.addView(segundario);
        principal.addView(edit);
        principal.addView(salir);

        scroller.addView(principal);

        principal.setOrientation(LinearLayout.VERTICAL);
        segundario.setOrientation(LinearLayout.VERTICAL);
        principal.setGravity(Gravity.CENTER_VERTICAL);

        buscarNombre(saludo);
        //saludo.setText("Hola xxxx");
        saludo.setTypeface(typeface);
        buscarReserva(reserva);
        //reserva.setText("Su ultima reserva fue: xx xx xxxx");
        reserva.setTypeface(typeface);

        eliminar.setText("Eliminar reservar");
        eliminar.setTypeface(typeface);
        edit.setText("Editar perfil");
        edit.setTypeface(typeface);
        salir.setText("Cerrar sesion");
        salir.setTypeface(typeface);

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Booking").document(email)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                reserva.setText("No tiene ninguna reserva aun");
                                eliminar.setEnabled(false);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(mGoogleSignInClient);
                FirebaseAuth.getInstance().signOut();
                //onBackPressed();
                startActivity(login);
                getActivity().finish();
            }
        });

        return scroller;
        //return inflater.inflate(R.layout.fragment_edit_perfil, container, false);
    }

    private void buscarReserva(TextView reserva) {
        db.collection("Booking").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    if(value.contains("date")&&value.contains("people")&&value.contains("time")){
                        if(value.getString("people").equals(1+"")){
                            reserva.setText("Su ultima resrva fue: "+ value.getString("date")
                                    +" "+ value.getString("people")+ " person " + value.getString("time"));
                        }else{
                            reserva.setText("Su ultima resrva fue: "+ value.getString("date")
                                    +" "+ value.getString("people")+ "people " + value.getString("time"));
                        }

                        eliminar.setEnabled(true);
                    }
                }
                else{
                    reserva.setText("No tiene ninguna reserva aun");
                    eliminar.setEnabled(false);
                }
            }
        });
    }

    private void buscarNombre(TextView saludo) {

        db.collection("users").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    if(value.contains("user name")){
                        saludo.setText("Hola "+ value.getString("user name"));
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
}

