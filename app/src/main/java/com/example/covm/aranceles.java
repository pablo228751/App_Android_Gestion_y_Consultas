package com.example.covm;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.R.*;


public class aranceles extends AppCompatActivity {

    ///////////////////////////////////////////////////INICIO VARIABLES/////////////////////////////////////////////////

    String url = comp_var.URL + "obras.php?valor=3";
    String obras;
    String plan;
    BufferedInputStream is;
    String line = null;
    String result = null;
    private RecyclerView lvLista;
    // private ListView lvLista;
    private Spinner spobras;
    private Spinner spplan;
    TextView saldo_inicial;
    private EditText et_aran;
    private AsyncHttpClient cliente;
    private adap_aranceles aranceles_adap;
    ArrayList<aranceles_datos> lista2 = new ArrayList<aranceles_datos>();


    ///////////////////////////////////////INICIO METODO ONCREATE///////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aranceles);
        saldo_inicial= (TextView) findViewById(R.id.textinicial);
        cliente = new AsyncHttpClient();
        spobras = (Spinner) findViewById(R.id.spinner_aran_1);
        spplan = (Spinner) findViewById(R.id.spinner_aran_2);
        lvLista = (RecyclerView) findViewById(R.id.rv_aranceles);
        lvLista.setLayoutManager(new LinearLayoutManager(this));
        et_aran=(EditText) findViewById(R.id.et_aran);


        ///LLAMO A LOS METODOS

        metodo_spinner1();


        ////CAPTURO EVENTO EN EL ITEM DE LOS SPINNER

        spobras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(parent.getContext(), "Seleccione Año" , Toast.LENGTH_LONG).show();
                obras = (parent.getItemAtPosition(position).toString());
                lista2 = new ArrayList<aranceles_datos>();
                metodo_spinner2();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spplan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plan = (parent.getItemAtPosition(position).toString());

                metodo_aranceles();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    private void metodo_aranceles() {
///////////////////////////////////////////////////INICIO CONEXION/////////////////////////////////////////////////

        String obras2 = obras.toString().replace(" ", "%20");
        String plan2 = plan.toString().replace(" ", "%20");
        String url4 = comp_var.URL  + "obras.php?valor=2&obras="+obras2+"&plan=" + plan2;
        cliente.post(url4, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    cargarLista(new String(responseBody));
                    //   Toast.makeText(aranceles.this, "Estoy aqui!!", Toast.LENGTH_SHORT).show();

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

                aranceles_datos p = new aranceles_datos();
                p.setCod(jsonarreglo.getJSONObject(i).getString("cod_st00a"));
                p.setObser(jsonarreglo.getJSONObject(i).getString("nom_st00a"));
                p.setPrecio(Double.parseDouble(jsonarreglo.getJSONObject(i).getString("pr1_st00a")));
                p.setCoseguro(Double.parseDouble(jsonarreglo.getJSONObject(i).getString("pr2_st00a")));
                lista2.add(p);
            }

            //aranceles_datos2=new adap_aranceles(lista);
            // lvLista.setAdapter(aranceles_datos2);
            aranceles_adap =new adap_aranceles(lista2);
            lvLista.setAdapter(aranceles_adap);

            et_aran.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<aranceles_datos> filtrar_lista = new ArrayList<>();
                    for (aranceles_datos aranceles_datos : lista2) {
                        if (aranceles_datos.getObser().toLowerCase().contains(s.toString().toLowerCase())) {
                            filtrar_lista.add(aranceles_datos);
                        }
                        aranceles_adap.filtrar(filtrar_lista);
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

    private void metodo_spinner1() {
        ///////////////////////////////////CAPTURAR PARAMETROS PARA LLENAR SPINER 1 ////////////////////////////////////////////

        String url3 = comp_var.URL + "obras.php?valor=1";
        cliente.post(url3, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    cargarSpinner1(new String(responseBody));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
    private void cargarSpinner1(String respuesta2) {

        ArrayList<obras> lista = new ArrayList<obras>();
        try {

            JSONArray jsonarreglo = new JSONArray(respuesta2);

            for (int i = 0; i < jsonarreglo.length(); i++) {

                obras p = new obras();
                p.setObras(jsonarreglo.getJSONObject(i).getString("nom_pt02"));
                lista.add(p);
            }

            ArrayAdapter<obras> a = new ArrayAdapter<obras>(aranceles.this, layout.simple_dropdown_item_1line, lista);
            spobras.setAdapter(a);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////CAPTURAR PARAMETROS PARA LLENAR SPINER 2 ////////////////////////////////////////////

    private void metodo_spinner2() {

        String obras2 = obras.toString().replace(" ", "%20");
        String url3 = comp_var.URL + "obras.php?valor=2&obras="+obras2;
        cliente.post(url3, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    cargarSpinner2(new String(responseBody));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
    private void cargarSpinner2(String respuesta2) {

        ArrayList<obras> lista = new ArrayList<obras>();
        try {

            JSONArray jsonarreglo = new JSONArray(respuesta2);

            for (int i = 0; i < jsonarreglo.length(); i++) {

                obras p = new obras();
                p.setObras(jsonarreglo.getJSONObject(i).getString("nom_pt02a"));
                lista.add(p);
            }

            ArrayAdapter<obras> a = new ArrayAdapter<obras>(aranceles.this, layout.simple_dropdown_item_1line, lista);
            spplan.setAdapter(a);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}



