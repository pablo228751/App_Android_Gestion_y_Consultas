package com.example.covm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.os.AsyncTask;


public class Pt extends ListActivity{
    Activity context;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;

    PtAdaptador adapter;
    ListView listaPt;
    ArrayList<Pt_data> records;
    TextView tv_miemID;
    TextView tv_titular;
    TextView tv_nro;
    TextView tv_monto;
    TextView tv_fecha;
    TextView tv_cta;

    TextView tv_operacion;
    EditText  edit_tot_np;
    EditText  edit_can_np;
    long total = 0;
    long pas_cta = 0;
    String pas_nom;
    String pas_afi;
    int cantidad = 0;
    Button  butt_np_nuevo;
    Button  butt_np_atras;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt);

        context=this;
        records=new ArrayList<Pt_data>();
        listaPt=(ListView)findViewById(android.R.id.list);
        adapter=new PtAdaptador(context, R.layout.lv_pt, R.id.text_np_nombre,	records);
        listaPt.setAdapter(adapter);
        edit_tot_np =(EditText)findViewById(R.id.edit_tot_np);
        edit_can_np =(EditText)findViewById(R.id.edit_can_np);

        Bundle datos = this.getIntent().getExtras();
        pas_cta       = datos.getLong("cta_ver");
        pas_afi       = datos.getString("afi_ver");
        pas_nom       = datos.getString("tit_ver");

        // Vamos por las imagenes
        Button atras = (Button) findViewById(R.id.button_atras_pt);
        Button nuevo = (Button) findViewById(R.id.button_nuevo_pt);

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Pt.this, Abc_pt.class);
                j.putExtra("acc_np","1");
                j.putExtra("nro_np","0");
                j.putExtra("oper_np","0");
                startActivity(j);
            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        }
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i3 = new Intent(this, Verpt.class);
        tv_miemID = (TextView) v.findViewById(R.id.text_np_nombre);
        tv_titular = (TextView) v.findViewById(R.id.text_np_titular);
        tv_nro = (TextView) v.findViewById(R.id.text_np_nro);
        tv_monto = (TextView) v.findViewById(R.id.text_np_monto);
        tv_fecha = (TextView) v.findViewById(R.id.text_np_fecha);
        tv_cta = (TextView) v.findViewById(R.id.text_np_cta);
        tv_operacion = (TextView) v.findViewById(R.id.text_np_operacion);

        String aux_miembroId = tv_miemID.getText().toString();
        String aux_titular = tv_titular.getText().toString();
        String aux_nro = tv_nro.getText().toString();
        String aux_monto = tv_monto.getText().toString();
        String aux_fecha = tv_fecha.getText().toString();
        String aux_cta = tv_cta.getText().toString();
        String aux_operacion = tv_operacion.getText().toString();

        i3.putExtra("id_ver",id);
        i3.putExtra("com_nom",aux_titular);
        i3.putExtra("com_ope",aux_operacion);
        i3.putExtra("com_nos",aux_nro);
        i3.putExtra("com_fec",aux_fecha);
        i3.putExtra("com_mon",aux_monto);
        Log.e("Posicion prestacion ","ID "+id+"// "+position+"// "+aux_operacion);
        startActivity(i3);

        Log.e("Posicion PT Cta","ID "+id+"// "+position+"// "+aux_operacion);

        startActivity(i3);
    }

    public void onStart(){
        super.onStart();
        //execute background task
        total = 0;
        cantidad = 0;
        adapter.clear();
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
            String Ip_Server="192.168.1.50";//IP DE NUESTRO PC Cambiar segun las preferecnias
            SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
            String IP =String.valueOf(prefe.getString("ip",""));
            String usuario =String.valueOf(prefe.getString("usuario",""));
            total = 0;
            cantidad = 0;
            String afiliado = pas_afi.trim();
            Ip_Server = "http://"+IP+"/sistemas/co/android/pt.php?afiliado="+afiliado;

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
                    Pt_data p=new Pt_data();

                    p.setnpNombre(json_data.getString("npNombre"));
                    p.setnpTitular(json_data.getString("npTitular"));
                    p.setnpFecha(json_data.getString("npFecha"));
                    p.setnpNro(json_data.getInt("npNro"));
                    p.setnpCta(json_data.getInt("npCta"));
                    p.setnpOper(json_data.getInt("npOper"));
                    p.setnpMonto(json_data.getLong("npMonto"));
                    total = total + json_data.getLong("npMonto");
                    cantidad = cantidad + 1;
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
            edit_tot_np.setText(String.valueOf(total));
            edit_can_np.setText(String.valueOf(cantidad));
            adapter.notifyDataSetChanged(); //notify the ListView to get new records
        }
    }
}
