package com.example.covm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class VerPaciente extends Activity {
    private TextView Ver_id;
    String IP_Server = "";
    String cuenta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altapaciente);

        Button b1=(Button)findViewById(R.id.butt_grabar_vercliente);
        Button b2=(Button)findViewById(R.id.butt_volver_vercliente);
        final EditText dir_cliente=(EditText)findViewById(R.id.edi_dir_cliente);
        final EditText mail_cliente=(EditText)findViewById(R.id.edi_mail_pac);
        final EditText te_cliente=(EditText)findViewById(R.id.edi_te_cliente);
        final EditText contacto_cliente=(EditText)findViewById(R.id.edi_contacto_cliente);

        Bundle datos = this.getIntent().getExtras();
        String ino = datos.getString("cta_ver");


        Ver_id = (TextView)findViewById(R.id.text_ver_nrocta);
        cuenta= ino;
        Log.e("Posicion","ID "+ino);

        Ver_id.setText(ino);

        b1.setOnClickListener(new View.OnClickListener() {

            @SuppressLint({"ShowToast", "WrongConstant"})
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences prefe=getSharedPreferences("unont", Context.MODE_PRIVATE);
                String IP =String.valueOf(prefe.getString("ip",""));
                String dir_c = dir_cliente.getText().toString();
                String te_c = te_cliente.getText().toString();
                String mail_c = mail_cliente.getText().toString();
                String contacto_c = contacto_cliente.getText().toString();

                IP_Server ="http://"+ IP +"/sistemas/dato5/android/modifica_cliente.php?cuenta="+cuenta+
                        "&dir="+dir_c+"&te="+te_c+"&mail="+mail_c+"&contacto="+contacto_c;

                Log.e("Verdatos","Ip "+IP_Server);

                try{
                    httpGetData(IP_Server);
                    Toast.makeText(getApplicationContext(), "El dato ha sido enviado correctamente", 1000).show();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error en el envio de la informacion, verifique su conexion a internet y vuelva a intentarlo.", 1000).show();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent intent = new Intent(VerPaciente.this, Pacientes.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    @SuppressLint("LongLogTag")
    public String httpGetData(String mURL) {
        String response="";
        mURL=mURL.replace(" ", "%20");
        Log.i("LocAndroid Response HTTP Threas","Ejecutando get 0: "+mURL);
        HttpClient httpclient = new DefaultHttpClient();

        Log.i("LocAndroid Response HTTP Thread","Ejecutando get 1");
        HttpGet httppost = new HttpGet(mURL);
        Log.i("LocAndroid Response HTTP Thread","Ejecutando get 2");
        try {
            Log.i("LocAndroid Response HTTP","Ejecutando get");
            // Execute HTTP Post Request
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            response = httpclient.execute(httppost,responseHandler);
            Log.i("LocAndroid Response HTTP",response);
        }catch (ClientProtocolException e) {
            Log.i("LocAndroid Response HTTP ERROR 1",e.getMessage());
            // TODO Auto-generated catch block
        }catch (IOException e) {

            Log.i("LocAndroid Response HTTP ERROR 2",e.getMessage());
            // TODO Auto-generated catch block
        }
        return response;

    }
}
