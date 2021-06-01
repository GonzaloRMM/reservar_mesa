package com.example.reservarmesas;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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


    String email = "";

    public Reservas(String email) {
        // Required empty public constructor
        this.email = email;
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

    private LinearLayout principal;
    private Button menos, mas, reserve, number;
    int num, contador = 0;
    private GridLayout horasGrid;
    ArrayList<String> datos = new ArrayList<String>();
    ArrayList<View> tiempos;
    ArrayList<Button> botones;
    ArrayList<String[]> times;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText etPlannedDate;
    DatePickerDialog picker;
    String tiempo, name, phone = "";
    Typeface typeface;
    Intent confirmacion;
    private Intent tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_reservas, container, false);

        principal = (LinearLayout) v.findViewById(R.id.reservaContenedor);
        typeface = ResourcesCompat.getFont(getContext(), R.font.denk_one);
        etPlannedDate = (EditText) v.findViewById(R.id.etPlannedDate);
        horasGrid = (GridLayout) v.findViewById(R.id.contenedorTiempos);
        confirmacion = new Intent(getActivity(), Confirmacion.class);
        menos = (Button) v.findViewById(R.id.button_menos);
        mas = (Button) v.findViewById(R.id.button_mas);
        number = (Button) v.findViewById(R.id.button_number);
        reserve = (Button) v.findViewById(R.id.button_reserve);
        tabs = new Intent(getActivity(), Tabs.class);
        horasGrid.setColumnCount(4);
        tiempos = horasGrid.getTouchables();

        etPlannedDate.setFocusable(false);
        etPlannedDate.setClickable(true);
        etPlannedDate.setMaxLines(1);
        etPlannedDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        etPlannedDate.setTypeface(typeface);
        etPlannedDate.setWidth(300);

        reserve.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2FD4A5")));
        reserve.setTextColor(Color.parseColor("#000000"));

        number.setText(num + "");
        number.setTypeface(typeface);
        ViewGroup.LayoutParams params = number.getLayoutParams();

        number.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));

        menos.setText("-");
        menos.setTypeface(typeface);
        menos.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8A0CC5")));
        menos.setTextColor(Color.parseColor("#FFFFFF"));

        mas.setText("+");
        mas.setTypeface(typeface);
        mas.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8A0CC5")));
        mas.setTextColor(Color.parseColor("#FFFFFF"));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        buscarDatos();

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
                } else {
                    Toast.makeText(container.getContext(), "opcion no valida", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(container.getContext(), "6 personas maximo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    private void buscarDatos() {
        db.collection("users").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    if (value.contains("user name")) {
                        name = (value.getString("user name"));

                    }
                    if (value.contains("phone")) {
                        phone = (value.getString("phone"));
                    }
                }
            }
        });
    }

    private void crearTiempos(ArrayList<View> tiempos) {
        botones = new ArrayList<Button>();
        times = new ArrayList<String[]>();
        for (int i = 0; i < 10; i++) {
            times.add(getResources().getStringArray(R.array.times));
            Button b = new Button(getContext());

            botones.add(b);
        }
        for (int j = 0; j < botones.size(); j++) {
            botones.get(j).setText(times.get(j)[j]);
            botones.get(j).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EA5114")));
            botones.get(j).setId(j);
            botones.get(j).setTypeface(typeface);
            horasGrid.addView(botones.get(j));
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

                contarReservas();
                if (!etPlannedDate.getText().toString().equals("") && num > 0 && !tiempo.equals("")) {
                    Bundle b = new Bundle();
                    b.putString("email", email);
                    b.putString("date", etPlannedDate.getText().toString());
                    b.putString("num", num + "");
                    b.putString("time", tiempo);
                    confirmacion.putExtras(b);
                    //startActivity(confirmacion);

                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.confirmacion);
                    dialog.setTitle("Title...");

                    TextView text = (TextView) dialog.findViewById(R.id.textConfirmacion);
                    text.setText("Tu reserva: " + etPlannedDate.getText().toString() +
                            " " + number.getText().toString() + " people" + " " + tiempo);

                    Button confirmar = (Button) dialog.findViewById(R.id.bConfirmar);
                    Button cancelar = (Button) dialog.findViewById(R.id.bCancelar);
                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (contador < 7) {
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
                                reserva.put("name", name);
                                reserva.put("phone", phone);

                                db.collection("Booking").document(email).update(reserva);

                                etPlannedDate.setText("");
                                number.setText(0 + "");
                                num = 0;
                                for (int i = 0; i < botones.size(); i++) {
                                    botones.get(i).setEnabled(true);
                                }
                                tiempo = "";
                                Toast.makeText(v.getContext(), "Reserva realizada", Toast.LENGTH_SHORT).show();

                                getContext().sendBroadcast(tabs);
                                TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabLayout);
                                tabhost.getTabAt(0).select();

                                dialog.dismiss();
                            } else {
                                Toast.makeText(v.getContext(), "Esa hora no esta disponible", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                for (int i = 0; i < botones.size(); i++) {
                                    if (botones.get(i).getText().toString().equals(tiempo)) {
                                        botones.get(i).setVisibility(v.INVISIBLE);
                                        contador = 0;
                                    }
                                }
                            }
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
                } else {
                    Toast.makeText(v.getContext(), "Falta algun campo por rellenar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void contarReservas() {
        CollectionReference users = db.collection("Booking");
        db.collection("Booking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("Booking").document(document.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (value.exists()) {
                                            if (value.contains("date") && value.contains("time")) {
                                                if (value.getString("date").equals(etPlannedDate.getText().toString()) &&
                                                        value.getString("time").equals(tiempo)) {
                                                    contador++;
                                                }
                                            }

                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void showDatePickerDialog() {
        int day, month, year;
        if (etPlannedDate.getText().toString().equals("")) {
            final Calendar cldr = Calendar.getInstance();
            day = cldr.get(Calendar.DAY_OF_MONTH);
            month = cldr.get(Calendar.MONTH);
            year = cldr.get(Calendar.YEAR);
        } else {
            String[] fechArray = etPlannedDate.getText().toString().split("/");

            day = Integer.valueOf(fechArray[0]);
            month = Integer.valueOf(fechArray[1]) - 1;
            year = Integer.valueOf(fechArray[2]);
        }

        picker = new DatePickerDialog(getView().getContext(), R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        String[] fechArray = date.split("/");

                        int dia = Integer.valueOf(fechArray[0]);
                        int mes = Integer.valueOf(fechArray[1]) - 1;
                        int anio = Integer.valueOf(fechArray[2]);

                        Calendar c1 = new GregorianCalendar(anio, mes, dia);

                        String fechaActual = Calendar.getInstance().get(5) + "/" + (Calendar.getInstance().get(2) + 1) + "/" + Calendar.getInstance().get(1);

                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                        long hoy = GregorianCalendar.getInstance().getTimeInMillis();
                        long dateElegido = c1.getTimeInMillis();
                        if (dateElegido >= hoy || fechaActual.equals(date)) {
                            etPlannedDate.setText(date);
                            horasGrid.removeAllViews();
                            crearTiempos(tiempos);
                            for (int i = 0; i < botones.size(); i++) {

                                String[] horaActual = currentTime.split(":");
                                int horaAct = Integer.valueOf(horaActual[0]);
                                int minAct = Integer.valueOf(horaActual[1]);

                                String[] horaBoton = botones.get(i).getText().toString().split(":");
                                int hora = Integer.valueOf(horaBoton[0]);
                                int min = Integer.valueOf(horaBoton[1]);

                                if (horaAct > hora && fechaActual.equals(date)) {
                                    botones.get(i).setVisibility(view.INVISIBLE);
                                } else if (horaAct == hora && minAct > min && fechaActual.equals(date)) {
                                    botones.get(i).setVisibility(view.INVISIBLE);
                                }
                            }
                        } else {
                            Toast.makeText(view.getContext(), "fecha no valida", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
        picker.show();
    }
}
