package com.example.covm;

import java.io.BufferedReader;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.os.AsyncTask;

public class Cuentacorriente extends ListActivity{
    Activity context;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;
    private static final int OK_RESULT_CODE = 1;
    CtaCteAdaptador adapter;
    ListView listaVerCliente;
    ArrayList<CtaCte_data> records;
    Button  butt_stock_buscar;
    String devuelvo= "";
    String ccuenta= "";

    TextView Tit_ver;
    TextView Sal_ver;

    TextView tv_fechaID;
    TextView tv_montoID;
    TextView tv_operID;
    TextView tv_nroID;
    TextView tv_obsID;
    TextView tv_tipoID;
    double Saldo = 0.00;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentacorriente);
        context=this;
        records=new ArrayList<CtaCte_data>();
        listaVerCliente=(ListView)findViewById(android.R.id.list);
        adapter=new CtaCteAdaptador(context, R.layout.lv_ctacte, R.id.text_cc_nombre,	records);
        listaVerCliente.setAdapter(adapter);

        Bundle datos = this.getIntent().getExtras();
        long iid = datos.getLong("id_ver");
        String cpo = datos.getString("cta_ver");
        String sal = datos.getString("sal_ver");
        String nom = datos.getString("tit_ver");
        ccuenta = cpo;

        Sal_ver = (TextView)findViewById(R.id.text_saldo_vercliente);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        tv_tipoID = (TextView) v.findViewById(R.id.text_cc_codigo);
        tv_operID = (TextView) v.findViewById(R.id.text_cc_oper);
        tv_nroID = (TextView) v.findViewById(R.id.text_cc_nro);
        tv_obsID = (TextView) v.findViewById(R.id.text_cc_obs);
        tv_fechaID = (TextView) v.findViewById(R.id.text_cc_fecha);
        tv_montoID = (TextView) v.findViewById(R.id.text_cc_monto);

        Intent i3 = new Intent(this, VerComprobante.class);
        String aux_operId = tv_operID.getText().toString();
        String aux_nroId = tv_nroID.getText().toString();
        String aux_obsId = tv_obsID.getText().toString();
        String aux_tipoId = tv_tipoID.getText().toString();
        String aux_fechaId = tv_fechaID.getText().toString();
        String aux_montoId = tv_montoID.getText().toString();
        i3.putExtra("id_ver",id);
        i3.putExtra("com_tipo",aux_tipoId);
        i3.putExtra("com_oper",aux_operId);
        i3.putExtra("com_nro",aux_nroId);
        i3.putExtra("com_obs",aux_obsId);
        i3.putExtra("com_fecha",aux_fechaId);
        i3.putExtra("com_monto",aux_montoId);
        Log.e("Posicion Cliente","ID "+id+"// "+position+"//"+aux_operId+"//"+aux_tipoId);
        startActivity(i3);
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
            pd = new ProgressDialog(context);
            pd.setTitle("Recibiendo Datos");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(true);
            pd.setIndeterminate(true);
            pd.show();
        }

        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            String vercuenta="";

            String Ip_Server="192.168.1.50";//IP DE NUESTRO PC Cambiar segun las preferecnias
            SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
            String IP = String.valueOf(prefe.getString("ip",""));
            String usuario =String.valueOf(prefe.getString("usuario",""));

            Log.e ("Cuenta" , "Cuenta"+usuario); //e.printStackTrace();

            Saldo = 0;
            try {
                String vercuentaurl = URLEncoder.encode(usuario,"UTF-8");

                Ip_Server = "http://"+IP+"/sistemas/co/android/consulta_ctacte.php?matricula="+vercuentaurl;
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
                    CtaCte_data p=new CtaCte_data();
                    p.setccNombre(json_data.getString("ccNombre"));
                    p.setccObs(json_data.getString("ccObs"));
                    p.setccCodigo(json_data.getString("ccCodigo"));
                    p.setccMonto(json_data.getDouble("ccMonto"));
                    p.setccApl(json_data.getLong("ccApl"));
                    p.setccSaldo(json_data.getDouble("ccSaldo"));
                    p.setccFecha(json_data.getString("ccFecha"));
                    p.setccOper(json_data.getLong("ccOper"));
                    p.setccNro(json_data.getLong("ccNro"));
                    records.add(p);
                    Saldo = Saldo + json_data.getDouble("ccMonto");
                }
            }
            catch(Exception e){
                Log.e("ERROR", "Error al pasar los datos "+e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void result){
            Log.e("Registros ", records.size() + "");
            Saldo = Math.round(Saldo * 100) / 100d;
            Sal_ver.setText(Double.toString(Saldo));
            if(pd!=null) pd.dismiss(); //close dialog

            adapter.notifyDataSetChanged(); //notify the ListView to get new records
        }
    }

    protected void returnParams() {
        Intent intent = new Intent();
        intent.putExtra("Codigo", devuelvo);
        setResult(OK_RESULT_CODE, intent);
        finish();
    }
}
