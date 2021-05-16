package com.example.reservarmesas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Informacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Informacion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String email="";
    public Informacion(String email) {
        // Required empty public constructor
        this.email=email;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Informacion.
     */
    // TODO: Rename and change types and number of parameters
    public static Informacion newInstance(String param1, String param2) {
        Informacion fragment = new Informacion("");
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

    RecyclerView recyclerView;
    List<Body>bodyList;
    ImageButton maps,insta,phone;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_informacion, container, false);
        View v = inflater.inflate(R.layout.elv_child,container,false);

        recyclerView=(RecyclerView)v.findViewById(R.id.recyclerView);
        maps=(ImageButton)v.findViewById(R.id.maps);
        insta=(ImageButton)v.findViewById(R.id.insta);
        phone=(ImageButton)v.findViewById(R.id.phone);
        initData();
        setRecyclrerView();

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederInternet("https://www.instagram.com/tremendoburrito/?hl=es");
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:951172535")));
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederInternet("https://www.google.es/maps/place/Calle+Cenacheros,+62/@36.7206752,-4.3721233,21z/data=!4m6!3m5!1s0xd72583f29e48c55:0xaf1409a9c8fe35e!4b1!8m2!3d36.7207569!4d-4.3723004");
            }
        });
        return v;
    }

    public void accederInternet(String url){
        Uri uri= Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    private void setRecyclrerView() {
        BodyAdapter bodyAdapter = new BodyAdapter(bodyList);
        recyclerView.setAdapter(bodyAdapter);
        //recyclerView.setHasFixedSize(true);
    }

    private void initData() {
        bodyList=new ArrayList<Body>();

        //bodyList.add(new Body("Persona","holiwi"));
        bodyList.add(new Body("APP",getString(R.string.infoAPP)));
        bodyList.add(new Body("Restaurante",getString(R.string.infoRestaurante)));
    }
}