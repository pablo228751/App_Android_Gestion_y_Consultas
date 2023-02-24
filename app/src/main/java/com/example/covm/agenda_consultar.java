package com.example.covm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class agenda_consultar extends AppCompatActivity {
    RecyclerView rv_consultar;

    private adap_agenda_consultar agenda_adap_consultar;
    private AsyncHttpClient cliente;
    ArrayList<agenda_consultar_datos> lista2 = new ArrayList<agenda_consultar_datos>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_consultar);
        rv_consultar= (RecyclerView) findViewById(R.id.rv_consultar);
        rv_consultar.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        cliente = new AsyncHttpClient();





        String url4 = comp_var.URL  + "agenda_consultar.php?valor=3&matricula=9115&fec=24/25/2019";
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

                agenda_consultar_datos p = new agenda_consultar_datos();

                p.setFecha(jsonarreglo.getJSONObject(i).getString("nom_pt01"));
                p.setHora(jsonarreglo.getJSONObject(i).getString("nro_pt01"));
                p.setNombre(jsonarreglo.getJSONObject(i).getString("nro_pt01"));

                lista2.add(p);
            }


            agenda_adap_consultar =new adap_agenda_consultar(lista2);




        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
