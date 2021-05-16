package com.example.reservarmesas;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class BodyAdapter  extends RecyclerView.Adapter<BodyAdapter.BodyVH> {

    List<Body>bodyList;
    boolean visible=false;
    public BodyAdapter(List<Body>bodyList){
        this.bodyList=bodyList;
    }

    @NonNull
    @Override
    public BodyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cabecera,parent,false);
        return new BodyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BodyVH holder, int position) {
        Body b = bodyList.get(position);
        holder.head.setText(b.getHead());
        holder.body.setText(b.getBody());

        holder.expandbleLayout.setVisibility(View.GONE);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!visible){
                    holder.expandbleLayout.setVisibility(VISIBLE);
                    holder.flecha.setForeground(ContextCompat.getDrawable(v.getContext(),(R.drawable.uparrow)));
                    visible=true;
                }
                else if(visible){
                    holder.expandbleLayout.setVisibility(View.GONE);
                    holder.flecha.setForeground(ContextCompat.getDrawable(v.getContext(),(R.drawable.downarrow)));

                    visible=false;
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return bodyList.size();
    }


    public class BodyVH extends RecyclerView.ViewHolder{
        TextView head, body;
        ImageButton flecha;
        LinearLayout linearLayout;
        RelativeLayout expandbleLayout;


        public BodyVH(@NonNull View itemView){
            super(itemView);

            head=itemView.findViewById(R.id.cabecera);
            body=itemView.findViewById(R.id.cuarpo);
            flecha=itemView.findViewById(R.id.flecha);
            linearLayout=itemView.findViewById(R.id.linear_layout);
            expandbleLayout=itemView.findViewById(R.id.expadable_layout);

/*
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Body b=bodyList.get(getAdapterPosition());
                    //Toast. makeText(v.getContext(),"Roman, ¿Te quieres casar conmigo?", Toast. LENGTH_SHORT).show();
                    b.setExpandable(!b.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
 */
        }
    }
}
