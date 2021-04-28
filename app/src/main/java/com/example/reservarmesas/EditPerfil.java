package com.example.reservarmesas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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

    private LinearLayout principal;
    private Button salir;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ScrollView scroller = new ScrollView(getActivity());

        principal=new LinearLayout(getActivity());

        scroller.addView(principal);
        return scroller;
        //return inflater.inflate(R.layout.fragment_edit_perfil, container, false);
    }
}