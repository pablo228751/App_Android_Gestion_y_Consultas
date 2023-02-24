package com.example.covm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.app.AlertDialog;
import android.app.ListActivity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import static android.util.Log.*;

public class Abc_pt extends ListActivity {
    protected static final int REQUEST_CODE = 10;
    protected static final int OK_RESULT_CODE =1;

    //	private TextView cod_cl07;
    String IP_Server="192.168.1.50";	//IP DE NUESTRO PC
    String ID_Usuario="dato";	//IP DE NUESTRO PC

    String mat_cl05="PT Carga";
    String acceso="0";
    String codigo2="";
    String mititular="";
    String micta="";


    String Pa_palabra = "";
    String Pa_categoria = "";
    private ImageButton mas;
    private ImageButton menos;
    Httppostaux post;
    String URL_connect= "";


    Integer wopera=0;	//IP DE NUESTRO PC
    EditText tot_cl05;
    EditText cta_cl05;
    EditText tit_cl05;
    float total = 0;
    private ProgressDialog pDialog;

    TextView tv_nombre;
    TextView tv_codigo;
    TextView tv_cantidad;
    TextView tv_monto;
    int oper1 = 0;
    long nro_cta = 1;
    Activity context;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;

    Tmp_ptAdaptador adapter;
    ListView listaTmp_pt;
    ArrayList<Tmp_pt_data> records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abc_pt);

        Bundle datos = this.getIntent().getExtras();
        String acc_np = datos.getString("acc_np");
        String nro_np = datos.getString("nro_np");
        String tit_np = datos.getString("tit_np");
        String cta_np = datos.getString("cta_np");
        String oper_np = datos.getString("oper_np");
        oper1 = Integer.valueOf(oper_np.trim());
        acceso = acc_np;
        mititular = tit_np;
        micta = cta_np;
        e ("titular" , "Titular en ABC "+mititular);
        e ("titular" , "2do Titular en ABC "+cta_np);

        post=new Httppostaux();

        context=this;
        records=new ArrayList<Tmp_pt_data>();
        listaTmp_pt=(ListView)findViewById(android.R.id.list);
        adapter=new Tmp_ptAdaptador(context, R.layout.lv_tmp_pt, R.id.text_tmpnp_nombre,	records);
        listaTmp_pt.setAdapter(adapter);

        Button b0=(Button)findViewById(R.id.butt_abcnp_cliente);
        Button b1=(Button)findViewById(R.id.butt_abcnp_buscar);
        Button b3=(Button)findViewById(R.id.butt_abcnp_grabar);
        Button b4=(Button)findViewById(R.id.butt_abcnp_volver);
        Button bo=(Button)findViewById(R.id.butt_abcnp_odonto);
        Button bf=(Button)findViewById(R.id.butt_abcnp_fotos);
        Button bc=(Button)findViewById(R.id.butt_abcnp_camara);

        final EditText nom_cl05 =(EditText)findViewById(R.id.edit_abcnp_mot);
        cta_cl05=(EditText)findViewById(R.id.edit_abcnp_cta);
        tit_cl05=(EditText)findViewById(R.id.edit_abcnp_tit);
        tot_cl05 =(EditText)findViewById(R.id.edit_abcnp_tot);

        SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
        String IP =String.valueOf(prefe.getString("ip",""));
        String USU =String.valueOf(prefe.getString("usuario",""));
        String MIlugar =String.valueOf(prefe.getString("lugar",""));
        String MIcuenta =String.valueOf(prefe.getString("cuenta",""));
        mat_cl05 = USU;

        IP_Server = IP;
        ID_Usuario = USU;
        URL_connect="http://"+IP_Server+"/sistemas/co/android/addpt_0.php";

        if (!MIlugar.equals("Personal")){
            tit_cl05.setText(MIlugar);
            cta_cl05.setText(MIcuenta);
        }
        if (acc_np.equals("2")) {
            wopera =  oper1;
            tit_cl05.setText(mititular);
            cta_cl05.setText(micta);
        }else{
            Random r = new Random();
            oper1 = r.nextInt(9999999 - 1000);
            wopera =  oper1;
        }

        bc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vcliente)
            {
                Intent io = new Intent(Abc_pt.this, camara.class);
                io.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(io, REQUEST_CODE);
            }
        });

        bf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vcliente)
            {
                Intent io = new Intent(Abc_pt.this, foto.class);
                io.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(io, REQUEST_CODE);
            }
        });

        bo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vcliente)
            {
                Intent io = new Intent(Abc_pt.this, odontograma.class);
                io.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(io, REQUEST_CODE);
            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vcliente)
            {
                String id_tit  =  tit_cl05.getText().toString().trim();
                String id_cta  =  cta_cl05.getText().toString().trim();

                Intent i0 = new Intent(Abc_pt.this, Pacientes.class);
                i0.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i0.putExtra("id_acc","1");
                i0.putExtra("id_tit",id_tit);
                i0.putExtra("id_cta",id_cta);
                startActivityForResult(i0, REQUEST_CODE);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                String id_cta  =  cta_cl05.getText().toString().trim();
                try {
                    nro_cta = new Long(Long.parseLong(id_cta));
                } catch (NumberFormatException nfe) {
                    nro_cta= 0;
                }

                if (nro_cta == 0)
                {
                    Toast.makeText(getApplicationContext(),"Debe elegir una cuenta para continuar", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent i1 = new Intent(Abc_pt.this, Consulta_cod_pt.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle extras = new Bundle();
                extras.putLong("Pa_opera",wopera);
                extras.putString("Pa_palabra",Pa_palabra);
                extras.putString("Pa_categoria",Pa_categoria);
                extras.putLong("Pa_cta",nro_cta);
                extras.putString("Pa_mat",mat_cl05);
                i1.putExtras(extras);
                startActivityForResult(i1, REQUEST_CODE);
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast") @Override
            public void onClick(View v) {
                e("GRABABA  ", String.valueOf(total)+" Acceso "+acceso+" Operacion "+wopera);
                if (tit_cl05.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar titular", Toast.LENGTH_LONG).show();
                    return;
                }
                if (acceso.equals("2")) {
                    e("Actualiza", String.valueOf(total)+" Acceso "+acceso+" Operacion "+wopera);
                    new Actualiza(Abc_pt.this).execute();
                    finish();
                }
                if (acceso.equals("1")) {
                    e("Alta  ", String.valueOf(total)+" Acceso "+acceso+" Operacion "+wopera);
                    String nom = nom_cl05.getText().toString();
                    String tit = tit_cl05.getText().toString();
                    String cta = cta_cl05.getText().toString();
                    String tot = tot_cl05.getText().toString();
                    String mat = mat_cl05;

                    Integer xma = 1;

// envio Host
                    String soper  = Long.toString(wopera);
                    new asyncpt().execute(tit,nom,mat,soper, cta, tot, ID_Usuario);

                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {

                                  public void onClick(View v)
                                  {
                                      AlertDialog.Builder builder = new AlertDialog.Builder(Abc_pt.this);
                                      builder.setMessage("Desea salir de la Ficha.....")
                                              .setTitle("Advertencia")
                                              .setCancelable(false)
                                              .setNegativeButton("No",
                                                      new DialogInterface.OnClickListener() {
                                                          public void onClick(DialogInterface dialog, int id) {
                                                          }
                                                      })
                                              .setPositiveButton("Si",
                                                      new DialogInterface.OnClickListener() {
                                                          public void onClick(DialogInterface dialog, int id) {
                                                              finish();
                                                          }
                                                      });
                                      AlertDialog alert = builder.create();
                                      alert.show();
                                  }
                              }
        );

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i3 = new Intent(this, Tmp_pt_editar.class);
        tv_nombre   = (TextView) v.findViewById(R.id.text_tmpnp_nombre);
        tv_codigo   = (TextView) v.findViewById(R.id.text_tmpnp_codigo);
        tv_cantidad = (TextView) v.findViewById(R.id.text_tmpnp_cantidad);
        tv_monto    = (TextView) v.findViewById(R.id.text_tmpnp_monto);
        String aux_nombre = tv_nombre.getText().toString();
        String aux_codigo = tv_codigo.getText().toString();
        String aux_cantidad = tv_cantidad.getText().toString();
        String aux_monto = tv_monto.getText().toString();
        String aux_oper = String.valueOf(wopera);
        i3.putExtra("operacion_ver",aux_oper);
        i3.putExtra("nombre_ver",aux_nombre);
        i3.putExtra("codigo_ver",aux_codigo);
        i3.putExtra("cantidad_ver",aux_cantidad);
        i3.putExtra("monto_ver",aux_monto);
        i3.putExtra("tipo_ver","on");
        startActivity(i3);
    }

    public void onStart(){
        super.onStart();
        //execute background task
        adapter.clear();
        BackTask bt=new BackTask();
        bt.execute();
    }

    //Definimos que para cuando se presione la tecla BACK no volvamos para atras
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Toast.makeText(getApplicationContext(), "Presione el Botón VOLVER para terminar sin Grabar", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_novedad, menu);
        return true;
    }

    class asyncpt extends AsyncTask< String, String, String> {

        String tit,nom,mat,soper, cta, tot, usu;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(Abc_pt.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            tit=params[0];
            nom=params[1];
            mat=params[2];
            soper=params[3];
            cta=params[4];
            tot=params[5];
            usu=params[6];

            if (envio_pt(tit,nom,mat,soper, cta, tot, usu)==true){
                return "ok"; //ingreso valido
            }else{
                return "err"; //login invalido
            }
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();//ocultamos progess dialog.
            e("onPostExecute=",""+result);
            if (result.equals("ok")){
                finish();
            }
        }
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
            total = 0;
            String Ip_Server="192.168.1.50";//IP DE NUESTRO PC Cambiar segun las preferecnias
            SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
            String IP =String.valueOf(prefe.getString("ip",""));
            String cuenta =String.valueOf(prefe.getString("cuenta",""));
            e("Entrada TMP", String.valueOf(total)+" Acceso "+acceso);

            Ip_Server = "http://"+IP+"/sistemas/co/android/tmp_pt_0.php?operacion="+wopera+"&acceso="+acceso;
            e ("http" , "Servidor "+Ip_Server); //e.printStackTrace();
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
                e("ERROR", e.getMessage());
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
                e ("respuesta" , "Vino "+result); //e.printStackTrace();
            }catch(Exception e){
                e("ERROR", "Error al Convertir lor datos "+e.toString());
            }
            //parse json data
            total = 0;
            try{
                // Remove unexpected characters that might be added to beginning of the	string
                result=result.substring(result.indexOf("["));
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data =jArray.getJSONObject(i);
                    Tmp_pt_data p=new Tmp_pt_data();

                    p.settmpNombre(json_data.getString("tmpNombre"));
                    p.settmpCodigo(json_data.getString("tmpCodigo"));
                    p.settmpCantidad(json_data.getLong("tmpCantidad"));
                    p.settmpMonto(json_data.getDouble("tmpMonto"));
                    p.settmpTotal(json_data.getDouble("tmpTotal"));
                    records.add(p);
                    total = total + json_data.getLong("tmpMonto") * json_data.getLong("tmpCantidad");
                    e("Salida total", String.valueOf(total)+" Acceso "+acceso);
                    if (acceso.equals("2")) {
                        acceso = "2";
                    }
                }
            }
            catch(Exception e){
                e("ERROR", "Error al pasar los datos "+e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void result){
            if(pd!=null) pd.dismiss(); //close dialog
            e("Registros ", records.size() + " $ " + total);
            tot_cl05.setText(String.valueOf(total));
            adapter.notifyDataSetChanged(); //notify the ListView to get new records
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        e("Resultado ",String.valueOf(resultCode));
        e("Request ",String.valueOf(requestCode));
        if (resultCode == OK_RESULT_CODE) {
            String ret_cta = data.getStringExtra("Cuenta");
            String ret_tit = data.getStringExtra("Titular");
            if (ret_cta != null && ret_cta.length()>0 ) {
                cta_cl05.setText(ret_cta);
            }
            if (ret_tit != null && ret_tit.length()>0 ) {
                tit_cl05.setText(ret_tit);
            }

            e("Devolvio  ","Cta "+ ret_cta +"/ TIT"+ ret_tit  );
//        Toast.makeText(this, "Devolvi: " + ret_cta +"//"+ ret_tit +"//"+ ret_tot, Toast.LENGTH_LONG).show();
        }
    }

    public boolean envio_pt(String titular ,String motivo,String matricula,String operacion,String cuenta,String monto,String usuario) {
        int logstatus=-1;
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
        postparameters2send.add(new BasicNameValuePair("titular",titular));
        postparameters2send.add(new BasicNameValuePair("motivo",motivo));
        postparameters2send.add(new BasicNameValuePair("matricula",matricula));
        postparameters2send.add(new BasicNameValuePair("operacion",operacion));
        postparameters2send.add(new BasicNameValuePair("cuenta",cuenta));
        postparameters2send.add(new BasicNameValuePair("monto",monto));
        postparameters2send.add(new BasicNameValuePair("usuario",usuario));

        JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);
        if (jdata!=null && jdata.length() > 0){
            JSONObject json_data; //creamos un objeto JSON
            try {
                json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                logstatus=json_data.getInt("logstatus");//accedemos al valor
                e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (logstatus==1){// [{"logstatus":"0"}]
                e("loginstatus ", "invalido");
                return false;
            }
            else{// [{"logstatus":"1"}]
                e("loginstatus ", "valido");
                return true;
            }
        }else{	//json obtenido invalido verificar parte WEB.
            e("JSON  ", "ERROR");
            return false;
        }
    }

    //Egreso del Usuario
    private boolean actualiza(){
        // INgreso SQLite

        SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
        String nro_ip =String.valueOf(prefe.getString("ip","192.168.1.8"));

        IP_Server = "http://"+nro_ip+"/sistemas/co/android/act_np_0.php";
        String wnopera =String.valueOf(wopera);
        String wntotal =String.valueOf(total);
        e("Nuevo total ",wntotal);


        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        HttpPost httppost;
        httpclient=new DefaultHttpClient();
        httppost= new HttpPost(IP_Server); // Url del Servidor
        //Aadimos nuestros datos
        nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("operacion",wnopera));
        nameValuePairs.add(new BasicNameValuePair("usuario",ID_Usuario));
        nameValuePairs.add(new BasicNameValuePair("monto",wntotal));
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
    class Actualiza extends AsyncTask<String,String,String> {
        private Activity context;
        Actualiza(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if(actualiza())
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Alta ingresado con exito", Toast.LENGTH_LONG).show();
                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Error. AltaNO ingresado", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }
}
