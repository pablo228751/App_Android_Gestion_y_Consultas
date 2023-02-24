package com.example.covm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.covm.Httppostaux;
import android.os.AsyncTask;

public class Consulta_cod_pt extends ListActivity{
    Activity context;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;
    private static final int OK_RESULT_CODE = 10;
    Httppostaux post;
    Con_ptAdaptador adapter;
    ListView listaCon_cod;
    ArrayList<Con_pt_data> records;
    String devuelvo= "";
    String detalle= "";
    String IP_Server= "";
    String URL_connect= "";
    String IP;
    TextView tv_miemID;
    TextView tv_detID;
    TextView tv_cat;
    private ImageButton mas;
    private ImageButton menos;
    private ProgressDialog pDialog;
    EditText can_cl07 ;

    Integer pos = 3;
    String pas_palabra="";
    Long pas_cta;
    Long pas_opera;
    String pas_suc="";
    String pas_categoria="";
    Button butt_cod_atras;
    Button butt_cod_buscar;
    EditText cod_nom;
    String cod_cat="";	//


    String cat_pr04="";	//
    String sub_pr04="";	//
    String ID_Usuario="dato";	//IP DE NUESTRO PC
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adaptersp;
    Spinner sp;
    boolean primera = true;
    private SQLiteDatabase dbtmpnp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_cod_pt);

        post=new Httppostaux();

        SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
        IP =String.valueOf(prefe.getString("ip",""));
        URL_connect="http://"+IP+"/sistemas/co/android/addtmp_0.php";

        can_cl07 =(EditText)findViewById(R.id.edit_abcnp_cantidad);
        can_cl07.setText("1");

        Bundle datos = this.getIntent().getExtras();
        pas_opera     = datos.getLong("Pa_opera");
        pas_cta       = datos.getLong("Pa_cta");
        pas_suc       = datos.getString("Pa_suc");
        pas_palabra   = datos.getString("Pa_palabra");
        pas_categoria = datos.getString("Pa_categoria");

        Log.i("LLego datos ",pas_opera+"/"+pas_cta+"/"+pas_suc);
        tv_cat   = (TextView)findViewById(R.id.text_cod_cat);
        if (!pas_categoria.equals("")){
            tv_cat.setText("Eligi Cat(falso): "+pas_categoria);
            cod_cat = pas_categoria;
            primera = false;
            int ttuno = cod_cat.indexOf(" . ");
            int ttdos = cod_cat.indexOf(" / ");
            if (ttuno != 0){
                cat_pr04 = cod_cat.substring(ttuno+3,ttdos);
            }else{
                cat_pr04 = "";
            }
            if (ttdos != 0){
                sub_pr04 = cod_cat.substring(ttdos+3);
            }else{
                sub_pr04 = "";
            }
        }else{
            primera = true;
        }

        context=this;
        sp=(Spinner)findViewById(R.id.spin_usu_ag01);
        adaptersp=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems);
        sp.setAdapter(adaptersp);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        records=new ArrayList<Con_pt_data>();
        listaCon_cod=(ListView)findViewById(android.R.id.list);
        adapter=new Con_ptAdaptador(context, R.layout.lv_codigos_pt, R.id.text_cc_nombre,	records);
        listaCon_cod.setAdapter(adapter);

        cod_nom=(EditText)findViewById(R.id.edit_cod_nom);
        cod_nom.setText(pas_palabra);

        //Se mueve la cnatidad en mas
        mas=(ImageButton)findViewById(R.id.mas);
        mas.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Integer mas = 0;
                mas = Integer.valueOf(can_cl07.getText().toString()) + 1;
                can_cl07.setText(String.valueOf(mas));
            }
        });

        //Se mueve la cantidad en menos
        menos=(ImageButton)findViewById(R.id.menos);
        menos.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Integer menos = 0;
                menos = Integer.valueOf(can_cl07.getText().toString()) - 1;
                if (menos < 1) {
                    menos = 1;
                }
                can_cl07.setText(String.valueOf(menos));
            }
        });

        butt_cod_atras=(Button)findViewById(R.id.butt_cod_atras);
        butt_cod_atras.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Vamos por el alta de cliente
        butt_cod_buscar = (Button) findViewById(R.id.butt_cod_buscar);
        butt_cod_buscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (primera){
                    cod_cat = sp.getSelectedItem().toString();
                    tv_cat.setText("Eligi Cat: "+cod_cat);
                }else{
                    primera = true;
                    tv_cat.setText("Eligi Cat: "+cod_cat);
                }
                int ttuno = cod_cat.indexOf(" . ");
                int ttdos = cod_cat.indexOf(" / ");
                if (ttuno != 0){
                    cat_pr04 = cod_cat.substring(ttuno+3,ttdos);
                }else{
                    cat_pr04 = "";
                }
                if (ttdos != 0){
                    sub_pr04 = cod_cat.substring(ttdos+3);
                }else{
                    sub_pr04 = "";
                }

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(can_cl07.getWindowToken(), 0);

                adapter.clear();
                BackTask bt=new BackTask();
                bt.execute();
            }
        });

        String url = "http://10.0.2.2:8080/CountryWebService" + "/CountryServlet";
        spinnerCat(url);
    }


    public void spinnerCat(String url) {
        new SpinnerCat().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        tv_miemID = (TextView) v.findViewById(R.id.text_cc_codigo);
        tv_detID = (TextView) v.findViewById(R.id.text_cc_nombre);
        final String aux_miembroId = tv_miemID.getText().toString();
        final String aux_detId = tv_detID.getText().toString();
        final String aux_canId = can_cl07.getText().toString();
        String aux_detalle = aux_canId+"  "+aux_miembroId+" "+aux_detId ;

        Log.e("CLICK EN  ", aux_miembroId+"///"+"http://"+ IP +"/sistemas/co/android/addtmp_0.php?codigo="+aux_miembroId+
                "&cantidad="+can_cl07.getText()+"&operacion="+pas_opera+"&sucursal="+pas_suc+"&cuenta="+pas_cta);

        AlertDialog.Builder builder = new AlertDialog.Builder(Consulta_cod_pt.this);
        builder.setMessage(" Desea cargar en la Nota de Pedido "+aux_detalle+" ?.....")
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

                                String soper = Long.toString(pas_opera);
                                String scta = Long.toString(pas_cta);

                                new asynctmp().execute(aux_miembroId,aux_canId,pas_suc,soper, scta);

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

        adapter.clear();
        BackTask bt=new BackTask();
        bt.execute();
        /////////////////// Pongo espacios
        can_cl07.setText("1");
    }
    public String httpGetData(String mURL) {
        String response="";
        mURL=mURL.replace(" ", "%20");
        Log.i("HTTP 0","Ejecutando get 0: "+mURL);
        HttpClient httpclient = new DefaultHttpClient();
        Log.i("HTTP 1","Ejecutando get 1");
        HttpGet httppost = new HttpGet(mURL);
        Log.i(" HTTP  . . ","Ejecutando get 2");
        try {
            Log.i("Repuesta HTTP","Ejecutando get");
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            response = httpclient.execute(httppost,responseHandler);
            Log.i("Respuesta HTTP",response);
        }catch (ClientProtocolException e) {
            Log.i("HTTP ERROR 1",e.getMessage());
            // TODO Auto-generated catch block
        }catch (IOException e) {
            Log.i("HTTP ERROR 2",e.getMessage());
            // TODO Auto-generated catch block
        }
        return response;
    }

    public void onStart(){
        super.onStart();
        if (!primera){
            adapter.clear();
            BackTask bt=new BackTask();
            bt.execute();
        }
    }

    //background process to make a request to server and list product		information
    private class BackTask extends AsyncTask<Void,Void,Void>{
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setTitle("Recibiendo Codigos Datos");
            pd.setMessage("Espere por favor.");
            pd.setCancelable(true);
            pd.setIndeterminate(true);
            pd.show();
        }

        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            String sinespacios= cod_nom.getText().toString().replace(" ","@");
            Log.e("ERROR...", sinespacios);


            IP_Server = "http://"+IP+"/sistemas/co/android/consulta_cod.php?nombre="+sinespacios+"&categoria="+cat_pr04+"&subcategoria="+sub_pr04;
            try{
                httpclient=new DefaultHttpClient();
                httppost= new HttpPost(IP_Server);
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
                    Con_pt_data p=new Con_pt_data();
                    p.setccNombre(json_data.getString("ccNombre"));
                    p.setccCodigo(json_data.getString("ccCodigo"));
                    p.setccMonto(json_data.getDouble("ccMonto"));
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
            adapter.notifyDataSetChanged();
        }
    }

    private class SpinnerCat extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<String>();
        }
        protected Void doInBackground(Void...params){
            InputStream istream=null;
            String resultado="";
            SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
            String IP =String.valueOf(prefe.getString("ip",""));

            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost= new HttpPost("http://"+IP+"/sistemas/co/android/codigo_categoria.php");
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                istream = entity.getContent();
            }catch(IOException e){
                e.printStackTrace();
            }

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(istream,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    resultado+=line;
                }
                istream.close();
                //resultado=sb.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            // parse json data
            try{
                JSONArray jArray =new JSONArray(resultado);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    // add interviewee name to arraylist
                    list.add(jsonObject.getString("iname"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void resultado){
            listItems.addAll(list);
            adaptersp.notifyDataSetChanged();
        }
    }


    class asynctmp extends AsyncTask< String, String, String> {

        String codigo,cantidad, sucursal, operacion, cuenta;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(Consulta_cod_pt.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            codigo=params[0];
            cantidad=params[1];
            sucursal=params[2];
            operacion=params[3];
            cuenta=params[4];

            if (envio_tmp(codigo,cantidad, sucursal, operacion, cuenta)==true){
                return "ok"; //ingreso valido
            }else{
                return "err"; //login invalido
            }
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute=",""+result);
        }
    }

    protected void returnParams() {
        Intent intent = new Intent();
        intent.putExtra("Codigo", devuelvo);
        intent.putExtra("Detalle", detalle);
        intent.putExtra("Palabra", cod_nom.getText().toString());
        intent.putExtra("Categoria", cod_cat);

        setResult(OK_RESULT_CODE, intent);
        Log.e ("http" , "Devueldo Cod "+devuelvo);
        finish();
    }

    public boolean envio_tmp(String codigo ,String cantidad ,String sucursal,String operacion,String cuenta) {
        int logstatus=-1;

        Log.e("VOY A  ", "///"+"http://"+ IP +"/sistemas/co/android/addtmp_0.php?codigo=");
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
        postparameters2send.add(new BasicNameValuePair("codigo",codigo));
        postparameters2send.add(new BasicNameValuePair("cantidad",cantidad));
        postparameters2send.add(new BasicNameValuePair("sucursal",sucursal));
        postparameters2send.add(new BasicNameValuePair("operacion",operacion));
        postparameters2send.add(new BasicNameValuePair("cuenta",cuenta));

        JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

        if (jdata!=null && jdata.length() > 0){
            JSONObject json_data; //creamos un objeto JSON
            try {
                json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                logstatus=json_data.getInt("logstatus");//accedemos al valor
                Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (logstatus==1){// [{"logstatus":"0"}]
                Log.e("loginstatus ", "invalido");
                return false;
            }
            else{// [{"logstatus":"1"}]
                Log.e("loginstatus ", "valido");
                return true;
            }
        }else{	//json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR");
            return false;
        }
    }
}
