package com.example.reservarmesas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorListView extends ArrayAdapter {
    private Activity context;
    private ArrayList<ArrayList<String>>datos;

    public AdaptadorListView(Activity context, ArrayList<ArrayList<String>> datos){
        super(context,R.layout.listview,datos);
        this.context=context;
        this.datos=datos;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View item=inflater.inflate(R.layout.listview,null);

        ((TextView) item.findViewById(R.id.textName)).setText(datos.get(position).get(0));
        ((TextView) item.findViewById(R.id.textReserve)).setText(datos.get(position).get(1));
        ((TextView) item.findViewById(R.id.textPhoneNumber)).setText(datos.get(position).get(2));

        return item;
    }
}
