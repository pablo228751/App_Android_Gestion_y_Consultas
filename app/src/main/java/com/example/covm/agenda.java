package com.example.covm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class agenda extends AppCompatActivity implements View.OnClickListener {
    Button btn_fecha,btn_hora,btn_pacientes,btn_consultar,btn_guardar;
    EditText e_fecha,e_hora,e_pacientes;
    RecyclerView rv_pacientes;
    private int dia,mes,ano,hora,minutos;
    private adap_agenda agenda_adap;
    private AsyncHttpClient cliente;
    ArrayList<agenda_datos> lista2 = new ArrayList<agenda_datos>();
    ArrayList<String> listDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        btn_fecha= (Button) findViewById(R.id.btn_fecha);
        btn_hora= (Button) findViewById(R.id.btn_hora);
        btn_pacientes= (Button) findViewById(R.id.btn_paciente);
        btn_consultar= (Button) findViewById(R.id.btn_consultar);
        btn_guardar= (Button) findViewById(R.id.btn_guardar);
        e_fecha= (EditText) findViewById(R.id.e_fecha);
        e_hora= (EditText) findViewById(R.id.e_hora);
        e_pacientes=(EditText)findViewById(R.id.e_paciente);
        rv_pacientes= (RecyclerView) findViewById(R.id.rv_pacientes);
        rv_pacientes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        cliente = new AsyncHttpClient();

        metodo_agenda();
        btn_fecha.setOnClickListener(this);
        btn_hora.setOnClickListener(this);



        /////////////////METODO PARA COLOCAR SOLO HORA ACTUAL/////////////////////////////////////////
        //setFechaActual();
        btn_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(e_fecha.getText().toString().isEmpty()) {
                    Toast.makeText(agenda.this, "Debe elegir una FECHA", Toast.LENGTH_SHORT).show();
                }else {
                    String fec=(e_fecha.getText().toString());
                    Intent agenda = getIntent();
                    final String matricula = agenda.getExtras().getString("matricula");
                    Intent aconsultar =new Intent(agenda.this, agenda_consultar.class);
                    agenda.putExtra("matricula", matricula);
                    agenda.putExtra("fec", fec);
                    agenda.this.startActivity(aconsultar);
                }


                }
        });





        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(e_fecha.getText().toString().isEmpty()|| e_hora.getText().toString().isEmpty()||e_pacientes.getText().toString().isEmpty()){
                    Toast.makeText(agenda.this, "Hay Campo VACIOS!!", Toast.LENGTH_SHORT).show();

                }else {
                    String fec,hor,nom,mat;
                    fec=(e_fecha.getText().toString());
                    hor=(e_hora.getText().toString());
                    nom=(e_pacientes.getText().toString().replace(" ", "%20"));
                    Intent agenda = getIntent();
                    final String matricula = agenda.getExtras().getString("matricula");
                    mat=(matricula);

                    String url4 = comp_var.URL  + "agenda_grabar.php?valor=2&fec="+fec+"&hor="+hor+"&nom="+nom+"&mat="+mat;
                    cliente.post(url4, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {
                                cargarLista(new String(responseBody));
                                Toast.makeText(agenda.this, "Se agrego el TURNO Correctamene!", Toast.LENGTH_LONG).show();
                                e_fecha.setText("");
                                e_hora.setText("");
                                e_pacientes.setText("");

                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

                }
            }
        });



    }
/////////////////On Click PARA CALENDARIO Y RELOJ/////////////////////////////////////////

    @Override
    public void onClick(View v) {
        if (v==btn_fecha){
            final Calendar c= Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear , int dayOfMonth) {
                    e_fecha.setText(dayOfMonth+"/"+(dayOfMonth+1)+"/"+year);
                }
            },ano,mes,dia);
            datePickerDialog.show();
        }
        if (v==btn_hora){
            final Calendar c= Calendar.getInstance();
            hora=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hora, int minuto) {
                e_hora.setText(hora+":"+minuto);
            }
        },hora,minutos,false);
        timePickerDialog.show();

        }

    }
    /////////////////FIN      CALENDARIO Y RELOJ/////////////////////////////////////////

    /////////////////////////////////////FILTRAR PACIENTES ///////////////////////////////////////////////////
    private void metodo_agenda() {
 //Iniciar Conexion
        Intent agenda = getIntent();
        final String matricula = agenda.getExtras().getString("matricula");

        String url4 = comp_var.URL  + "agenda.php?valor=1&matricula="+matricula;
        cliente.post(url4, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    cargarLista(new String(responseBody));
                     // Toast.makeText(agenda.this, "Estoy aqui!!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
    private void cargarLista(String respuesta2) {


        try {

            JSONArray jsonarreglo = new JSONArray(respuesta2);

            for (int i = 0; i < jsonarreglo.length(); i++) {

                agenda_datos p = new agenda_datos();

                p.setNombre(jsonarreglo.getJSONObject(i).getString("nom_pt01"));
                p.setCodigo(jsonarreglo.getJSONObject(i).getString("nro_pt01"));

                lista2.add(p);
            }


            agenda_adap =new adap_agenda(lista2);
            agenda_adap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    e_pacientes.setText(lista2.get(rv_pacientes.getChildAdapterPosition(view)).getNombre());
                }
            });
            rv_pacientes.setAdapter(agenda_adap);

            e_pacientes.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<agenda_datos> filtrar_lista = new ArrayList<>();
                    for (agenda_datos agenda_datos : lista2) {
                        if (agenda_datos.getCodigo().toLowerCase().contains(s.toString().toLowerCase())) {
                            filtrar_lista.add(agenda_datos);
                        }
                        agenda_adap.filtrar(filtrar_lista);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /////////////////////////////////FIN FILTRAR PAIENTES //////////////////////////////////////////////////

    /////////////////////////////////METODO PARA COLOCAR FECHA ACTUAL EN EDIT TEXT//////////////////////////////////////////////////
    public void setFechaActual()
    {

        final Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(c.getTime());
        e_fecha.setText(s);
    }
    /////////////////////////////////  FIN      METODO PARA COLOCAR FECHA ACTUAL EN EDIT TEXT//////////////////////////////////////////////////

    /////////////////////////////////   METODO PARA GUARDAR//////////////////////////////////////////////////
    private void btn_guardar(){
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(e_fecha.getText().toString().isEmpty()|| e_hora.getText().toString().isEmpty()||e_pacientes.getText().toString().isEmpty()){
                    Toast.makeText(agenda.this, "Hay Campo VACIOS!!", Toast.LENGTH_SHORT).show();

                }else {
                    String fec,hor,nom,mat;
                    fec=(e_fecha.getText().toString());
                    hor=(e_hora.getText().toString());
                    nom=(e_pacientes.getText().toString());
                    Intent agenda = getIntent();
                    final String matricula = agenda.getExtras().getString("matricula");
                    mat=(matricula);

                    String url4 = comp_var.URL  + "agenda_agregar.php?valor=2&fec="+fec+"&hor="+hor+"&nom="+nom+"&mat="+mat;
                    cliente.post(url4, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {
                                cargarLista(new String(responseBody));
                                Toast.makeText(agenda.this, "Se agrego el TURNO Correctamene!", Toast.LENGTH_LONG).show();
                                e_fecha.setText("");
                                e_hora.setText("");
                                e_pacientes.setText("");

                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

                }
            }
        });
    }


    /////////////////////////////////  FIN      METODO GUARDAR//////////////////////////////////////////////////



}