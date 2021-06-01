package com.example.reservarmesas;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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


    String email = "";

    public Inicio(String email) {
        // Required empty public constructor
        this.email = email;
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
    private Button eliminarReserva, hacerReserva, eliminarPedido, hacerPedido;
    private ImageButton verPerfil;
    private TextView name, reserva, pedido;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Intent tabs;
    Typeface typeface;
    private String bodyReserva, bodyPedido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mis_reservas, container, false);

        perfil = (LinearLayout) v.findViewById(R.id.layoutPerfil);

        name = (TextView) v.findViewById(R.id.textPerfil);
        pedido = (TextView) v.findViewById(R.id.textPedido);
        reserva = (TextView) v.findViewById(R.id.textReserva);

        eliminarReserva = (Button) v.findViewById(R.id.button_Eliminar);
        hacerReserva = (Button) v.findViewById(R.id.button_reserva);
        eliminarPedido = (Button) v.findViewById(R.id.button_EliminarPedido);
        hacerPedido = (Button) v.findViewById(R.id.button_pedido);
        verPerfil = (ImageButton) v.findViewById(R.id.button_perfil);

        tabs = new Intent(getActivity(), Tabs.class);

        EditPerfil p = new EditPerfil(email);

        buscarReserva();

        typeface = ResourcesCompat.getFont(getContext(), R.font.denk_one);
        p.buscarNombre(name, "");
        name.setTypeface(typeface);

        eliminarReserva.setTypeface(typeface);
        hacerReserva.setTypeface(typeface);
        hacerReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirTab(2);
            }
        });
        hacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirTab(1);
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

        eliminarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarDatos("Booking", eliminarReserva);
            }
        });

        eliminarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarDatos("Order", eliminarPedido);
            }
        });
        return v;
    }

    private void buscarReserva() {
        db.collection("Booking").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    if (value.contains("date") && value.contains("people") && value.contains("time")) {
                        reserva.setText("-Fecha: " + value.getString("date")
                                + "\n-Personas: " + value.getString("people") +
                                "\n-Tiempo: " + value.getString("time"));
                        eliminarReserva.setEnabled(true);
                    }
                } else {
                    reserva.setText("No tiene ninguna reserva aun");
                    eliminarReserva.setEnabled(false);
                }
            }
        });

        db.collection("Order").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    if (value.contains("price") && value.contains("products")) {
                        String lista = value.get("products").toString().substring(1, value.get("products").toString().length() - 1);
                        pedido.setText("-Productos: " + lista + "\n-" + value.getString("price"));
                        eliminarPedido.setEnabled(true);
                    }
                } else {
                    pedido.setText("No tienes ninguna compra aun");
                    eliminarPedido.setEnabled(false);
                }
            }
        });

    }

    public void abrirTab(int numTab) {
        getContext().sendBroadcast(tabs);
        TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        tabhost.getTabAt(numTab).select();
    }

    public void eliminarDatos(String tabla, Button boton) {
        db.collection(tabla).document(email)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        boton.setEnabled(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}