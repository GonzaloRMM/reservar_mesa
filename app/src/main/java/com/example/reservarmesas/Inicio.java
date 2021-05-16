package com.example.reservarmesas;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String email="";
    public Inicio(String email) {
        // Required empty public constructor
        this.email=email;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisReservas.
     */
    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio("");
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

    private LinearLayout perfil;
    private Button eliminar,hacerReserva;
    private ImageButton verPerfil;
    private TextView name,reserva,ultima;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Intent tabs;
    Typeface typeface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mis_reservas,container,false);

        perfil=(LinearLayout)v.findViewById(R.id.layoutPerfil);

        name=(TextView) v.findViewById(R.id.textPerfil);
        reserva=(TextView) v.findViewById(R.id.text_reservaUltima);
        ultima=(TextView) v.findViewById(R.id.text_reserva);

        eliminar=(Button) v.findViewById(R.id.button_Eliminar);
        hacerReserva=(Button) v.findViewById(R.id.button_reserva);
        verPerfil=(ImageButton) v.findViewById(R.id.button_perfil);

        tabs = new Intent(getActivity(), Tabs.class);
        EditPerfil p= new EditPerfil(email);


        typeface= ResourcesCompat.getFont(getContext(), R.font.denk_one);

        buscarReserva(reserva);

        p.buscarNombre(name,"");
        name.setTypeface(typeface);
        reserva.setTypeface(typeface);
        ultima.setTypeface(typeface);

        eliminar.setTypeface(typeface);

        hacerReserva.setTypeface(typeface);
        hacerReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirTab(2);
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTab(3);
            }
        });
        verPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirTab(3);
            }
        });

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

        return v;
    }

    private void buscarReserva(TextView reserva) {
        db.collection("Booking").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    if(value.contains("date")&&value.contains("people")&&value.contains("time")){
                        if(value.getString("people").equals(1+"")){
                            ultima.setText("Su ultima reserva fue: ");
                            reserva.setText("\nDate: "+ value.getString("date")
                                    +"\n \nPerson: "+ value.getString("people")+ "\n \nTime: " + value.getString("time"));
                        }else{
                            ultima.setText("Su ultima reserva fue: ");
                            reserva.setText("\nDate: "+ value.getString("date")
                                    +"\n \nPeople: "+ value.getString("people")+ "\n \nTime: " + value.getString("time"));
                        }

                        eliminar.setEnabled(true);
                    }
                }
                else{
                    ultima.setText("");
                    reserva.setText("No tiene ninguna reserva aun");
                    eliminar.setEnabled(false);
                }
            }
        });
    }
    public void abrirTab(int numTab){
        getContext().sendBroadcast(tabs);
        TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        tabhost.getTabAt(numTab).select();
    }
}