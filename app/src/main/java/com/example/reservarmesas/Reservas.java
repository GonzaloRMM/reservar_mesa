package com.example.reservarmesas;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;



import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reservas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reservas extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String email="";
    public Reservas(String email) {
        // Required empty public constructor
        this.email=email;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reservas.
     */
    // TODO: Rename and change types and number of parameters
    public static Reservas newInstance(String param1, String param2) {
        Reservas fragment = new Reservas("");
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

    private LinearLayout principal,segundario,personasLayout;
    private Button menos, mas, reserve, number, b1,b2,b3,b4,b5,b6,b7,b8,b9,b10;
    private TextView date,guest, time;
    int num = 0;
    private GridLayout horasGrid,fechaLayout;
    ArrayList<String> datos = new ArrayList<String>();
    ArrayList<View> tiempos;
    ArrayList<Button> botones;
    ArrayList<String[]> times;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText etPlannedDate;
    Bundle bundle;
    DatePickerDialog picker;
    String tiempo = "";
    Typeface typeface;
    Intent confirmacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_reservas, container, false);

        ScrollView scroller = new ScrollView(getActivity());
        TextView text = new TextView(getActivity());
        principal=new LinearLayout(getActivity());
        segundario=new LinearLayout(getActivity());
        fechaLayout=new GridLayout(getActivity());
        personasLayout=new LinearLayout(getActivity());

        typeface= ResourcesCompat.getFont(getContext(), R.font.denk_one);

        etPlannedDate = new EditText(getActivity());
        date=new TextView(getActivity());
        guest=new TextView(getActivity());
        time=new TextView(getActivity());
        //login = new Intent(this, login.class);
        horasGrid = new GridLayout(getActivity());

        confirmacion=new Intent(getActivity(),Confirmacion.class);

        menos = new Button(getActivity());
        mas = new Button(getActivity());
        reserve = new Button(getActivity());
        number = new Button(getActivity());


        fechaLayout.addView(date);
        fechaLayout.addView(etPlannedDate);

        personasLayout.addView(menos);
        personasLayout.addView(number);
        personasLayout.addView(mas);

        segundario.addView(fechaLayout);
        segundario.addView(guest);
        segundario.addView(personasLayout);
        segundario.addView(time);

        Space s= new Space(getContext());
        s.setX(10);
        s.setY(10);

        principal.addView(segundario);
        principal.addView(horasGrid);
        principal.addView(reserve);

        scroller.addView(principal);


        principal.setOrientation(LinearLayout.VERTICAL);
        principal.setGravity(Gravity.CENTER_HORIZONTAL);
        segundario.setOrientation(LinearLayout.VERTICAL);

        personasLayout.setOrientation(LinearLayout.HORIZONTAL);

        horasGrid.setColumnCount(4);
        fechaLayout.setColumnCount(2);
        tiempos=horasGrid.getTouchables();


        date.setText("Date:");
        date.setTypeface(typeface);

        etPlannedDate.setFocusable(false);
        etPlannedDate.setClickable(true);
        etPlannedDate.setMaxLines(1);
        etPlannedDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        etPlannedDate.setTypeface(typeface);
        etPlannedDate.setWidth(300);
        guest.setText("People:");
        guest.setTypeface(typeface);
        time.setText("Time:");
        time.setTypeface(typeface);

        reserve.setText("reserve");
        reserve.setTypeface(typeface);
        number.setText(num + "");
        number.setTypeface(typeface);
        ViewGroup.LayoutParams params = number.getLayoutParams();
        params.width = 100;
        number.setLayoutParams(params);

        number.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2FD4A5")));

        menos.setText("-");
        menos.setTypeface(typeface);
        mas.setText("+");
        mas.setTypeface(typeface);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(number.getText().toString());
                if (num > 0) {
                    num--;
                    number.setText(num + "");
                }
            }
        });
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(number.getText().toString());
                if (num < 6) {
                    num++;
                    number.setText(num + "");
                } else {
                    Toast.makeText(container.getContext(), "6 people maximum", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return scroller;
    }

    private void crearTiempos(ArrayList<View> tiempos) {
        botones = new ArrayList<Button>();
        times = new ArrayList<String[]>();
        for (int i = 0; i < 10; i++) {
            times.add(getResources().getStringArray(R.array.times));
            Button b= new Button(getContext());

            botones.add(b);
        }
        for (int j = 0; j < botones.size(); j++) {
            botones.get(j).setText(times.get(j)[j]);
            botones.get(j).setId(j);
            botones.get(j).setTypeface(typeface);
            horasGrid.addView(botones.get(j));
            //botones.get(j).setBackgroundColor(ContextCompat.getColor(getContext(),R.color.purple_200));

            botones.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v.findViewById(v.getId());
                    for (int i = 0; i < botones.size(); i++) {
                        //if(botones.get(i).isEnabled()){
                        botones.get(i).setEnabled(true);
                        //}
                    }
                    b.setEnabled(false);
                    tiempo = b.getText().toString();
                }
            });
        }
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etPlannedDate.getText().toString().equals("") && num > 0 && !tiempo.equals("")) {
                    Bundle b = new Bundle();
                    b.putString("email", email);
                    b.putString("date", etPlannedDate.getText().toString());
                    b.putString("num", num+"");
                    b.putString("time", tiempo);
                    confirmacion.putExtras(b);

                    //startActivity(confirmacion);
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.confirmacion);
                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.textConfirmacion);
                    text.setText("Tu reserva: " + etPlannedDate.getText().toString() +
                            " " + number.getText().toString() +" people" + " " + tiempo);

                    Button confirmar = (Button) dialog.findViewById(R.id.bConfirmar);
                    Button cancelar = (Button) dialog.findViewById(R.id.bCancelar);
                    // if button is clicked, close the custom dialog
                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> reserva = new HashMap<>();
                            db.collection("Booking").document(email)
                                    .set(reserva)
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
                            reserva.put("date", etPlannedDate.getText().toString());
                            reserva.put("people", number.getText().toString());
                            reserva.put("time", tiempo);
                            db.collection("Booking").document(email).update(reserva);

                            /*
                            Intent intent = new Intent(Intent.ACTION_SEND);

                            // Defino los Strings Email, Asunto y Mensaje con la función putExtra
                            intent.putExtra(Intent.EXTRA_EMAIL,
                                    new String[] { email });
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Reserva TREMENDO BURRITO");
                            intent.putExtra(Intent.EXTRA_TEXT, text.getText().toString());

                            // Establezco el tipo de Intent
                            intent.setType("message/rfc822");

                            // Lanzo el selector de cliente de Correo
                            startActivity(
                                    Intent
                                            .createChooser(intent,
                                                    "Elije un cliente de Correo:"));
                             */

                            etPlannedDate.setText("");
                            number.setText(0+"");
                            num=0;
                            for (int i =0; i<botones.size();i++){
                                botones.get(i).setEnabled(true);
                            }
                            tiempo="";
                            Toast.makeText(v.getContext(), "Reserva realizada", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(), "Reserva cancelada", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    /*
                    Map<String, Object> reserva = new HashMap<>();
                    db.collection("Booking").document(email)
                            .set(reserva)
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
                    reserva.put("date", etPlannedDate.getText().toString());
                    reserva.put("people", number.getText().toString());
                    reserva.put("time", tiempo);
                    db.collection("Booking").document(email).update(reserva);
                     */
                } else {
                    Toast.makeText(v.getContext(), "Falta algun campo por rellenar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(getView().getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                        String[] fechArray = date.split("/");

                        int dia = Integer.valueOf(fechArray[0]);
                        int mes = Integer.valueOf(fechArray[1]) - 1;
                        int anio = Integer.valueOf(fechArray[2]);


                        Calendar c1 = new GregorianCalendar(anio, mes, dia);

                        String fechaActual=Calendar.getInstance().get(5)+"/"+(Calendar.getInstance().get(2)+1)+"/"+Calendar.getInstance().get(1);

                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                        long hoy=GregorianCalendar.getInstance().getTimeInMillis();
                        long dateElegido=c1.getTimeInMillis();
                        if(dateElegido>=hoy||fechaActual.equals(date)){
                            etPlannedDate.setText(date);
                            horasGrid.removeAllViews();
                            crearTiempos(tiempos);
                            for(int i=0; i<botones.size();i++){

                                String[] horaActual=currentTime.split(":");
                                int horaAct=Integer.valueOf(horaActual[0]);
                                int minAct=Integer.valueOf(horaActual[1]);

                                String[] horaBoton=botones.get(i).getText().toString().split(":");
                                int hora=Integer.valueOf(horaBoton[0]);
                                int min=Integer.valueOf(horaBoton[1]);

                                if(horaAct>hora&&fechaActual.equals(date)){
                                    //if(botones.get(i).getText().toString().equals(hora+":"+min)){
                                    //botones.get(i).setText("");
                                    //botones.get(i).setTextColor(Color.parseColor("#F55408"));
                                    botones.get(i).setVisibility(view.INVISIBLE);
                                    //}
                                }
                                else if(horaAct==hora&&minAct>min&&fechaActual.equals(date)){
                                    botones.get(i).setVisibility(view.INVISIBLE);
                                }
                            }
                            //Toast. makeText(view.getContext(),""+currentTime, Toast. LENGTH_SHORT).show();
                        }
                        else {
                            Toast. makeText(view.getContext(),"fecha no valida", Toast. LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
        picker.show();
    }
}
