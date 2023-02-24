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
import android.content.pm.ActivityInfo;
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


public class Pacientes extends ListActivity{
    private static final int OK_RESULT_CODE = 1;
    Activity context;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;

    PacientesAdaptador adapter;
    ListView listaPacientes;
    ArrayList<Pacientes_data> records;
    TextView cta_miemID;
    TextView tit_miemID;
    TextView dir_miemID;
    EditText cli_nom;
    EditText cli_cta;
    TextView afi_miemID;
    String idd_acc = "";
    String devuelvo_cta= "";
    String devuelvo_tit= "";
    String devuelvo_dir= "";
    Button butt_alta_cliente;
    Button butt_cli_buscar;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pacientes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle datos = this.getIntent().getExtras();
        String id_acc = datos.getString("id_acc");
        String id_tit = datos.getString("id_tit");
        String id_cta = datos.getString("id_cta");
        String id_afi = datos.getString("id_dir");

        idd_acc = id_acc;
        Log.e("Recibi en pacientes  ", idd_acc+" / "+id_tit+" / "+id_cta);
        context=this;
        records=new ArrayList<Pacientes_data>();
        listaPacientes=(ListView)findViewById(android.R.id.list);
        adapter=new PacientesAdaptador(context, R.layout.lv_pacientes, R.id.text_cta_pac,	records);
        listaPacientes.setAdapter(adapter);
        cli_nom=(EditText)findViewById(R.id.edit_cli_nom);
        cli_nom.setText(id_tit);
        cli_cta=(EditText)findViewById(R.id.edit_cli_cta);
        cli_cta.setText(id_cta);
        // Vamos por el alta de cliente
        butt_cli_buscar = (Button) findViewById(R.id.butt_cli_buscar);
        butt_cli_buscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                adapter.clear();
                BackTask bt=new BackTask();
                bt.execute();
            }
        });


        // Vamos por el alta de cliente
        butt_alta_cliente = (Button) findViewById(R.id.butt_alta_cliente);
        butt_alta_cliente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent j = new Intent(Pacientes.this, AltaPaciente.class);
                j.putExtra("acc_ver","1");
                j.putExtra("cta_ver","");
                startActivity(j);
            }
        });
    }

    public void clickMe(View view){
        Button bt=(Button)view;
        Intent i3 = new Intent(this, AltaPaciente.class);
        String cta_miembroId = bt.getText().toString();
        i3.putExtra("acc_ver","2");
        i3.putExtra("cta_ver",cta_miembroId);
        Log.e(" Cliente","ID  "+cta_miembroId);
        startActivity(i3);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        cta_miemID = (TextView) v.findViewById(R.id.text_cta_pac);
        afi_miemID = (TextView) v.findViewById(R.id.text_nro_pac);
        tit_miemID = (TextView) v.findViewById(R.id.text_nom_pac);
        dir_miemID = (TextView) v.findViewById(R.id.text_cel_pac);

        Log.e("CLICK EN  ", "Click en"+idd_acc);

        if(idd_acc.equals("1"))
        {
            String cta_miembroId = cta_miemID.getText().toString();
            String tit_miembroId = tit_miemID.getText().toString();
            String dir_miembroId = dir_miemID.getText().toString();

            Log.e("CLICK EN  ", cta_miembroId);
            devuelvo_cta = cta_miembroId;
            devuelvo_tit = tit_miembroId;
            devuelvo_dir = dir_miembroId;

            returnParams();
        }
        else
        {
            Intent i3 = new Intent(this, Pt.class);
            String cta_miembroId = cta_miemID.getText().toString();
            String tit_miembroId = tit_miemID.getText().toString();
            String afi_miembroId = afi_miemID.getText().toString();
            i3.putExtra("acc_ver","2");
            i3.putExtra("afi_ver",afi_miembroId);
            i3.putExtra("tit_ver",tit_miembroId);
            i3.putExtra("cta_ver",cta_miembroId);
            Log.e("Voy a Ficha Paciente","ID "+id+"// "+position+"//"+tit_miembroId+"//"+tit_miembroId+"//"+afi_miembroId);
            startActivity(i3);
        }

    }

    public void onStart(){
        super.onStart();

        //execute background task
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
            String matricula =String.valueOf(prefe.getString("usuario",""));
            String cuenta =String.valueOf(prefe.getString("cuenta",""));
            Ip_Server = "http://"+IP+"/sistemas/co/android/pacientes.php?nombre="+cli_nom.getText()+"&cuenta="+cli_cta.getText()+"&matricula="+matricula;

            Log.e ("http" , "Servidor >> "+Ip_Server+ "Cuenta "+cli_cta.getText()+"nombre "+cli_nom.getText()); //e.printStackTrace();

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
                    Pacientes_data p=new Pacientes_data();

                    p.setpNombre(json_data.getString("pNombre"));
                    p.setpTelefono(json_data.getString("pTelefono"));
                    p.setpMail(json_data.getString("pMail"));
                    p.setpDir(json_data.getString("pDir"));
                    p.setpNos(json_data.getString("pNos"));
                    p.setpAfi(json_data.getString("pAfi"));
                    p.setpSocial(json_data.getString("pSocial"));
                    p.setpCuenta(json_data.getInt("pCuenta"));
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
    protected void returnParams() {
        Intent intent = new Intent();
        intent.putExtra("Cuenta", devuelvo_cta);
        intent.putExtra("Titular", devuelvo_tit);
        intent.putExtra("Direccion", devuelvo_dir);

        setResult(OK_RESULT_CODE, intent);
        finish();
    }

}
