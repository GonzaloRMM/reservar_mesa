package com.example.reservarmesas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static com.firebase.ui.auth.AuthUI.getDefaultTheme;

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
        b1=new Button(getActivity());
        b2=new Button(getActivity());
        b3=new Button(getActivity());
        b4=new Button(getActivity());
        b5=new Button(getActivity());
        b6=new Button(getActivity());
        b7=new Button(getActivity());
        b8=new Button(getActivity());
        b9=new Button(getActivity());
        b10=new Button(getActivity());


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
        horasGrid.addView(b1);
//        horasGrid.addView(s);
        horasGrid.addView(b2);
        horasGrid.addView(b3);
        horasGrid.addView(b4);
        horasGrid.addView(b5);
        horasGrid.addView(b6);
        horasGrid.addView(b7);
        horasGrid.addView(b8);
        horasGrid.addView(b9);
        horasGrid.addView(b10);

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
        crearTiempos(tiempos);

        date.setText("Date:");
        date.setTypeface(typeface);

        etPlannedDate.setFocusable(false);
        etPlannedDate.setClickable(true);
        etPlannedDate.setMaxLines(1);
        etPlannedDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        etPlannedDate.setTypeface(typeface);
        etPlannedDate.setWidth(300);
        guest.setText("Guest:");
        guest.setTypeface(typeface);
        time.setText("Time:");
        time.setTypeface(typeface);

        reserve.setText("reserve");
        reserve.setTypeface(typeface);
        number.setText(num + "");
        number.setTypeface(typeface);
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
        for (int i = 0; i < tiempos.size(); i++) {
            times.add(getResources().getStringArray(R.array.times));
            botones.add((Button) tiempos.get(i));
        }
        for (int j = 0; j < botones.size(); j++) {
            botones.get(j).setText(times.get(j)[j]);
            botones.get(j).setId(j);
            botones.get(j).setTypeface(typeface);
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
                if (!etPlannedDate.getText().toString().equals("") && num != 0 && !tiempo.equals("")) {
                    Bundle b = new Bundle();
                    b.putString("email", email);
                    b.putString("date", etPlannedDate.getText().toString());
                    b.putString("num", num+"");
                    b.putString("time", tiempo);
                    confirmacion.putExtras(b);
                    etPlannedDate.setText("");
                    number.setText(0+"");
                    for (int i =0; i<botones.size();i++){
                        botones.get(i).setEnabled(true);
                    }
                    startActivity(confirmacion);
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

                        long hoy=GregorianCalendar.getInstance().getTimeInMillis();
                        long dateElegido=c1.getTimeInMillis();
                        if(dateElegido>=hoy||fechaActual.equals(date)){
                            etPlannedDate.setText(date);
                        }
                        else {
                            Toast. makeText(view.getContext(),"fecha no valida", Toast. LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
        picker.show();
    }
}
