package com.example.covm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText etusu, etpas;
    Button btnlog;
    boolean amantiene = false;
    TextView registrar;
    private AsyncHttpClient cliente;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etusu = findViewById(R.id.etUsuario);
        etpas = findViewById(R.id.etContrasena);
        btnlog = findViewById(R.id.btnLogin);
        cliente = new AsyncHttpClient();
        registrar=(TextView) findViewById(R.id.link_to_register);

//      Busco la IP valida
        SharedPreferences prefe=getSharedPreferences("co", Context.MODE_PRIVATE);
        String IP_default =String.valueOf(prefe.getString("ip",""));
        String USU_default =String.valueOf(prefe.getString("usuario",""));
        String pas_default =String.valueOf(prefe.getString("password",""));
        boolean asms =  Boolean.valueOf(prefe.getBoolean("datos",true));
        amantiene =  Boolean.valueOf(prefe.getBoolean("mantiene",true));
        registrar=(TextView) findViewById(R.id.link_to_register);
        etusu.setText(USU_default);
        if (amantiene){
            etpas.setText(pas_default);
        };

        registrar.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                Intent i = new Intent(MainActivity.this, Configuracion.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        botonLogin();



    }
    private void  iniciar(){
        this.pd = new  ProgressDialog(MainActivity.this);
    }
    private void pdialog(){
        pd.setCancelable(false);
        pd.show();
        //transparentar
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setContentView(R.layout.layout_pd);

    }

    private void botonLogin() {
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(MainActivity.this, "Conectando...", Toast.LENGTH_SHORT).show();
                iniciar();
                pdialog();

                if (etusu.getText().toString().isEmpty() || etpas.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Hay Campos En Blanco!!", Toast.LENGTH_SHORT).show();
                    etusu.setText("");
                    etpas.setText("");
                } else {
                    final String usu = etusu.getText().toString().replace(" ", "%20");
                    String pas = etpas.getText().toString().replace(" ", "%20");
                    String url =comp_var.URL+"registrarse.php?Usuario="+ usu + "&Password="+ pas;
                    cliente.post(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                            if (statusCode == 200) {

                                pd.dismiss();

                                String respuesta = new String(responseBody);
                                if (respuesta.equalsIgnoreCase("null")) {
                                    Toast.makeText(MainActivity.this, "Error De Usuario y/o Contraseña!!", Toast.LENGTH_SHORT).show();
                                    etusu.setText("");
                                    etpas.setText("");
                                } else {

                                    SharedPreferences preferencias=getSharedPreferences("co",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferencias.edit();
                                    editor.putString("password",etpas.getText().toString());
                                    editor.commit();

                                    Intent menu = new Intent(MainActivity.this, menu.class);
                                    menu.putExtra("matricula", usu);
                                    startActivity(menu);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(MainActivity.this, "Error Desconocido. Intentelo Nuevamente!!", Toast.LENGTH_SHORT).show();
                            etusu.setText("");
                            etpas.setText("");
                        }
                    });
                }
            }
        });
    }
}

