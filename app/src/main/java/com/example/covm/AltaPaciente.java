package com.example.covm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.content.Context;
import android.content.SharedPreferences;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AltaPaciente extends Activity {
    String IP_Server = "";
    String cuenta = "";
    EditText cta_pac;
    EditText dir_pac;
    EditText te_pac;
    EditText mail_pac;
    EditText contacto_pac;
    EditText cuit_pac;
    EditText nom_pac;
    EditText doc_pac;
    String iva_pac="Cons. Final";
    /** Called when the activity is first created. */
    private static final String BS_PACKAGE = "com.google.zxing.client.android";
    protected static final int REQUEST_CODE1 = 1;
    private EditText edit_latitud;
    private EditText edit_longitud;
    // GPSTracker class
    GPSTracker gps;
    double latitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altapaciente);

///////////////Recibo datos
        Bundle datos = this.getIntent().getExtras();
        String ino_cta = datos.getString("cta_ver");
        String ino_acc = datos.getString("acc_ver");
        String ino_tit = datos.getString("tit_ver");
        String ino_dir = datos.getString("dir_ver");

        Button b1=(Button)findViewById(R.id.butt_grabar_verpac);
        Button b2=(Button)findViewById(R.id.butt_volver_verpac);

        cta_pac=(EditText)findViewById(R.id.edi_cta_pac);
        nom_pac=(EditText)findViewById(R.id.edi_nom_pac);
        dir_pac=(EditText)findViewById(R.id.edi_dir_pac);
        mail_pac=(EditText)findViewById(R.id.edi_mail_pac);
        te_pac=(EditText)findViewById(R.id.edi_te_pac);
        contacto_pac=(EditText)findViewById(R.id.edi_doc_pac);
        cuit_pac=(EditText)findViewById(R.id.edi_cuit_pac);


// create class object
        gps = new GPSTracker(AltaPaciente.this);
// check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{
            gps.showSettingsAlert();
        }

        Spinner sp = (Spinner) findViewById(R.id.spin_iva_pac);
        ArrayAdapter adaptador = ArrayAdapter.createFromResource(
                this, R.array.tipo_iva, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptador);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                iva_pac = parentView.getItemAtPosition(position).toString();

                Toast.makeText(parentView.getContext(), "Has seleccionado " +

                        iva_pac, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        if(ino_acc.equals("1")){
            b2.setEnabled(false);
            cta_pac.setText("");
            nom_pac.setText("");
            dir_pac.setText("");
        }else{
            b2.setEnabled(true);
            cta_pac.setText(ino_cta);
            nom_pac.setText(ino_tit);
            dir_pac.setText(ino_dir);

        }

        b1.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast") @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
                String IP =String.valueOf(prefe.getString("ip",""));
                String nom_c = nom_pac.getText().toString();
                String dir_c = dir_pac.getText().toString();
                String te_c = te_pac.getText().toString();
                String mail_c = mail_pac.getText().toString();
                String contacto_c = contacto_pac.getText().toString();
                String cuit_c = cuit_pac.getText().toString();
                new Alta(AltaPaciente.this).execute();
                finish();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                SharedPreferences prefe=getSharedPreferences("co", Context.MODE_PRIVATE);
                String IP =String.valueOf(prefe.getString("ip",""));
                String dir_c = dir_pac.getText().toString();
                String te_c = te_pac.getText().toString();
                String mail_c = mail_pac.getText().toString();
                String contacto_c = contacto_pac.getText().toString();

                IP_Server ="http://"+ IP +"/sistemas/co/android/borra_paciente.php?cuenta="+cuenta;

                Log.e("Verdatos","Ip "+IP_Server);

                try{
                    httpGetData(IP_Server);
                    Toast.makeText(getApplicationContext(), "El dato ha sido enviado correctamente", 1000).show();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error en el envio de la informacion, verifique su conexion a internet y vuelva a intentarlo.", 1000).show();
                }

                finish();
            }
        });
    }

    //Egreso del Usuario
    private boolean alta(){
        // INgreso SQLite

        SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
        String nro_ip =String.valueOf(prefe.getString("ip","192.168.1.8"));
        String nro_movil =String.valueOf(prefe.getString("cel_aviso","3510000000"));
        String USU =String.valueOf(prefe.getString("usuario",""));

        IP_Server = "http://"+nro_ip+"/sistemas/co/android/alta_paciente.php";

        Log.e("Alta","Ip "+IP_Server);
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(IP_Server); // Url del Servidor
        nameValuePairs = new ArrayList<NameValuePair>(7);
        nameValuePairs.add(new BasicNameValuePair("nombre",nom_pac.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("dir",dir_pac.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("te",te_pac.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("mail",mail_pac.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("contacto",contacto_pac.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("cuit",cuit_pac.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("iva",iva_pac.trim()));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
            return true;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    //AsyncTask para Alta
    class Alta extends AsyncTask<String,String,String> {
        private Activity context;
        Alta(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if(alta())
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Alta ingresado con ?xito", Toast.LENGTH_LONG).show();
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Error. Alta NO ingresado", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
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
