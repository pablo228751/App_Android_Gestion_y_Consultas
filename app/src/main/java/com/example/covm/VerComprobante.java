package com.example.covm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.os.AsyncTask;
public class VerComprobante extends ListActivity{
    Activity context;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;
    private static final int OK_RESULT_CODE = 1;
    VerComprobanteAdaptador adapter;
    ListView listaVerComprobante;
    ArrayList<VerComprobante_data> records;
    String IP_Server= "";
    String coper = "";
    TextView Ver_monto;
    TextView Ver_fecha;
    TextView Ver_nro;
    TextView Ver_oper;
    EditText Ver_obs;
    public static final int SIGNATURE_ACTIVITY = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vercomprobante);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context=this;
        records=new ArrayList<VerComprobante_data>();
        listaVerComprobante=(ListView)findViewById(android.R.id.list);
        adapter=new VerComprobanteAdaptador(context, R.layout.lv_vercomprobante, R.id.text_vercom_oper,	records);
        listaVerComprobante.setAdapter(adapter);

        Bundle datos = this.getIntent().getExtras();
        String id_ver = datos.getString("id_ver");
        String com_oper = datos.getString("com_ope");
        String com_nro = datos.getString("com_nom");
        String com_obs = datos.getString("com_nos");
        String com_fecha = datos.getString("com_fec");
        String com_monto = datos.getString("com_mon");
        coper = com_oper;

        Ver_monto = (TextView)findViewById(R.id.text_vercom_total);
        Ver_monto.setText(com_monto);
        Ver_oper = (TextView)findViewById(R.id.text_vercom_oper);
        Ver_oper.setText(com_obs);
        Ver_nro = (TextView)findViewById(R.id.text_vercom_nro);
        Ver_nro.setText(com_nro);
        Ver_fecha = (TextView)findViewById(R.id.text_vercom_fecha);
        Ver_fecha.setText(com_fecha);


        Button getSignature = (Button) findViewById(R.id.button_firmar_pt);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(VerComprobante.this, Firma.class);
                startActivityForResult(intent,SIGNATURE_ACTIVITY);
            }
        });


    }

    public void onStart(){
        super.onStart();

        //execute background task

        BackTask bt=new BackTask();
        bt.execute();
    }

    //background process to make a request to server and list product		information
    private class BackTask extends AsyncTask<Void,Void,Void>{
        protected void onPreExecute(){
            super.onPreExecute();
/*		pd = new ProgressDialog(context);
		pd.setTitle("Recibiendo Datos");
		pd.setMessage("Espere por favor.");
		pd.setCancelable(true);
		pd.setIndeterminate(true);
		pd.show();*/
        }

        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            String operacion="";

            String Ip_Server="192.168.1.50";//IP DE NUESTRO PC Cambiar segun las preferecnias
            SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
            String IP =String.valueOf(prefe.getString("ip",""));

            operacion = coper;

            try {
                String simpleUrl = operacion;
                String encodedurl = URLEncoder.encode(simpleUrl,"UTF-8");
                Ip_Server = "http://"+IP+"/sistemas/co/android/consulta_verpt.php?registro='"+encodedurl+"'";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.e ("http" , "Servidor "+Ip_Server); //e.printStackTrace();

            try{
                httpclient=new DefaultHttpClient();
                httppost= new HttpPost(Ip_Server);
                response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            }catch(Exception e){
                if(pd!=null)
                    pd.dismiss(); //close the dialog if error occurs
                Log.e("ERROR", e.getMessage());
            }
            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                is.close();
                result=sb.toString();
                Log.e ("respuesta" , "Vino "+result); //e.printStackTrace();
            }catch(Exception e){
                Log.e("ERROR", "Error al Convertir lor datos "+e.toString());
            }
            //parse json data
            try{
                // Remove unexpected characters that might be added to beginning of the	string
                result=result.substring(result.indexOf("["));
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data =jArray.getJSONObject(i);
                    VerComprobante_data p=new VerComprobante_data();
                    p.setccNombre(json_data.getString("ccNombre"));
                    p.setccCodigo(json_data.getString("ccCodigo"));
                    p.setccMonto(json_data.getLong("ccMonto"));
                    p.setccCantidad(json_data.getLong("ccCantidad"));
                    records.add(p);
                }
            }
            catch(Exception e){
                Log.e("ERROR", "Error al pasar los datos "+e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void result){
            if(pd!=null) pd.dismiss(); //close dialog
            Log.e("Registros ", records.size() + "");
            adapter.notifyDataSetChanged(); //notify the ListView to get new records
        }
    }

    public String httpGetData(String mURL) {
        String response="";
        mURL=mURL.replace(" ", "%20");
        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httppost = new HttpGet(mURL);
        try {

            // Execute HTTP Post Request
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            response = httpclient.execute(httppost,responseHandler);
        }catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        }catch (IOException e) {

            // TODO Auto-generated catch block
        }
        return response;

    }

}
