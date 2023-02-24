package com.example.covm;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class padron extends AppCompatActivity {



    String linea=null;
    String result=null;
    BufferedInputStream buffer;
    EditText edit_txt2, edit_txt, edit_txt3, Edit_txt;
    Button buscar_padron;
    TextView txt_matricula2;
    private AsyncHttpClient cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padron);

        edit_txt2 = (EditText) findViewById(R.id.edit_txt2);
        txt_matricula2 = findViewById(R.id.txt_matricula2);
        edit_txt = (EditText) findViewById(R.id.edit_txt);
        edit_txt3 = (EditText) findViewById(R.id.edit_txt3);
        Edit_txt = (EditText) findViewById(R.id.Edit_txt);
        buscar_padron = (Button) findViewById(R.id.buscar_padron);
        cliente= new AsyncHttpClient();

        Intent padron = getIntent();
        final String matricula2 = padron.getExtras().getString("matricula");
        txt_matricula2.setText("Matrícula Profesional " + matricula2);


        buscar_padron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metodo_buscar_padron();

            }
        });


    }

    private void metodo_buscar_padron() {
        if (edit_txt2.getText().toString().isEmpty()) {
            Toast.makeText(padron.this, "Ingrese el nùmero de Afiliado", Toast.LENGTH_SHORT).show();
            edit_txt2.setText("");

        } else {
            final String dni = edit_txt2.getText().toString().replace(" ", "%20");

            String url = comp_var.URL +"padron.php?dni="+dni;
            cliente.post(url, new AsyncHttpResponseHandler() {

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        String respuesta = new String(responseBody);
                        if (respuesta.equalsIgnoreCase("null")) {
                            Toast.makeText(padron.this, "No se encuentra en el Padron, por favor verifique", Toast.LENGTH_SHORT).show();
                            edit_txt2.setText("");

                        }
                        else {

                            try {
                                JSONObject jsonObject = null;
                                JSONArray jsonarreglo = new JSONArray(respuesta);
                                for (int i = 0; i < jsonarreglo.length(); i++) {

                                    jsonObject = jsonarreglo.getJSONObject(i);
                                    edit_txt.setText(jsonObject.getString("nom_pt01"));
                                    edit_txt3.setText(jsonObject.getString("nos_pt01"));
                                    Edit_txt.setText(jsonObject.getString("nro_pt01"));


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }



                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
    }
}
