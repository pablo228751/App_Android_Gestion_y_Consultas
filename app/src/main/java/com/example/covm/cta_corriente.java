package com.example.covm;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import cz.msebera.android.httpclient.Header;


public class cta_corriente extends AppCompatActivity {

    ///////////////////////////////////////////////////INICIO VARIABLES/////////////////////////////////////////////////

    String url = comp_var.URL + "cta_corriente.php";
    String url_2 = comp_var.URL + "cta_corriente2.php";
    String[] fecha;
    double[] debe;
    String periodo;
    String mes;
    String anio;
    double[] haber;
    String[] obs;
    float[] saldo;
    double[] ope;
    double ope2;
    String[] reg;
    BufferedInputStream is;
    String line = null;
    String result = null;
    ListView lvLista;
    private Spinner spperiodo;
    private Spinner spperiodo2;
    TextView saldo_inicial;
    //TextView saldo_final;
    private AsyncHttpClient cliente;
    private ProgressDialog PD;

    ///////////////////////////////////////INICIO METODO ONCREATE///////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cta_corriente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        saldo_inicial= (TextView) findViewById(R.id.textinicial);
        //saldo_final= (TextView) findViewById(R.id.textfinal2);
        cliente = new AsyncHttpClient();
        spperiodo = (Spinner) findViewById(R.id.spinner2);
        spperiodo2 = (Spinner) findViewById(R.id.spinner);
        lvLista = (ListView) findViewById(R.id.lv_cta_cte);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        ///LLAMO A LOS METODOS
        metodo_cta_cte();
        metodo_spinner();

        ////CAPTURO EVENTO EN EL ITEM DE LOS SPINNER

        spperiodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Seleccione un Período" , Toast.LENGTH_LONG).show();
                mes = (parent.getItemAtPosition(position).toString());
                if (anio !="Mes"){
                    consultar_periodo();}
                else {
                    metodo_cta_cte();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spperiodo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(),"Seleccionado: "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                anio = (parent.getItemAtPosition(position).toString());
                if (mes !="Año"){
                    consultar_periodo();}
                else {
                    metodo_cta_cte();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void  iniciar(){
        this.PD = new  ProgressDialog(cta_corriente.this);
    }
    private void PDialog(){
        PD.setCancelable(false);
        PD.setTitle("Conectando con la Base de Datos");
        PD.setMessage("Por favor, espere un momento");
        PD.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PD.dismiss();
            }
        }, 3000L);
    }


    private void metodo_cta_cte() {
///////////////////////////////////////////////////INICIO CONEXION/////////////////////////////////////////////////
        try {
            Intent cta_corriente = getIntent();
            final String matricula = cta_corriente.getExtras().getString("matricula");
            URL url2 = new URL(url + "?matricula=" + matricula);
            HttpURLConnection con = (HttpURLConnection) url2.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ///////////////////////////////////////////////////CONTENIDO/////////////////////////////////////////////////
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        metodo_procesar();

    }

    private void metodo_procesar() {
///////////////////////////////////////////////////PROCESO JSON/////////////////////////////////////////////////
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            fecha = new String[ja.length()];
            debe = new double[ja.length()];
            haber = new double[ja.length()];
            obs = new String[ja.length()];
            ope = new double[ja.length()];
            saldo = new float[ja.length()];
            reg = new String[ja.length()];
            reg = new String[ja.length()];


            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                fecha[i] = jo.getString("fin_cl02");
                obs[i] = jo.getString("ori_cl02");
                ope[i] = jo.getDouble("imp_cl02");
                reg[i] = jo.getString("com_cl02");


                if (ope[i] >= 0) {
                    haber[i] = ope[i];
                } else {
                    debe[i] = ope[i];
                }

                ope2 -= (float) Math.round(ope[i] * 100) / 100;
                saldo[i] += ope2;


                DecimalFormat precision = new DecimalFormat("0.00");
                saldo_inicial.setText("SALDO INICIAL: $"+precision.format(saldo[i]));
                //saldo_final.setText("SALDO FINAL: $"+precision.format(saldo[0]));

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //////////////////////////////////////////////ENVIO VARIABLES AL ADAPTADOR//////////////////////////////////////////////
        adap_cta_corriente adap_cta_corriente = new adap_cta_corriente(this, fecha, debe,ope, haber, obs, saldo, reg);

        lvLista.setAdapter(adap_cta_corriente);
    }

    private void metodo_spinner() {
        ///////////////////////////////////CAPTURAR PARAMETROS PARA LLENAR SPINER DESDE PHP////////////////////////////////////////

        String url3 = comp_var.URL + "periodo.php";
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

        ArrayList<periodos> lista = new ArrayList<periodos>();
        ArrayList<periodos> lista2 = new ArrayList<periodos>();
        try {

            JSONArray jsonarreglo = new JSONArray(respuesta2);
            JSONArray jsonarreglo2 = new JSONArray(respuesta2);
            for (int i = 0; i < jsonarreglo.length(); i++) {

                periodos p = new periodos();
                p.setPeriodo(jsonarreglo.getJSONObject(i).getString("periodo"));
                lista.add(p);
            }
            for (int j = 0; j < jsonarreglo2.length(); j++) {

                periodos anio = new periodos();
                anio.setPeriodo(jsonarreglo2.getJSONObject(j).getString("anio"));
                lista2.add(anio);
            }
            ArrayAdapter<periodos> a = new ArrayAdapter<periodos>(this, android.R.layout.simple_dropdown_item_1line, lista);
            spperiodo.setAdapter(a);
            ArrayAdapter<periodos> b = new ArrayAdapter<periodos>(this, android.R.layout.simple_dropdown_item_1line, lista2);
            spperiodo2.setAdapter(b);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void consultar_periodo() {
        /////////////////////////////////////////////////FILTRAR CONSULTA SPINNER///////////////////////////////////////////////
        if (mes =="Todos") {
            mes = "00";
        }
        if (anio =="Todos") {
            anio = "0000";
        }
        try {
            lvLista.setAdapter(null);

            Toast.makeText(cta_corriente.this, periodo, Toast.LENGTH_LONG).show();
            Intent cta_corriente = getIntent();
            final String matricula = cta_corriente.getExtras().getString("matricula");
            URL url4 = new URL(url_2 + "?matricula=" + matricula + "&mes=" + mes+ "&anio=" + anio);
            HttpURLConnection con2 = (HttpURLConnection) url4.openConnection();
            con2.setRequestMethod("GET");
            is = new BufferedInputStream(con2.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        ////////////////////////////////////////////////CONTENIDO///////////////////////////////////////////////
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        metodo_procesar();

    }




}


