package com.example.covm;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AltaTurno extends Activity {
    private TextView reg_tu00;
    String IP_Server="192.168.1.50";	//IP DE NUESTRO PC
    String nom_tu00="";	//
    String msg_tu00="";	//
    String cel_tu00="";	//
    String o_fecha="";	//

    private EditText fec_tu00;	//
    private EditText hor_tu00;	//
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "/";
    private static final String GUION = "-";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    boolean Si_SMS = true;
    String ID_matricula="dato";	//IP DE NUESTRO PC
    String URL_connect="http://"+IP_Server+"/co/android/addagenda.php";//ruta en donde estan nuestros archivos

    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adaptersp;
    private ProgressDialog pDialog;
    // ////////////////////////////////////////////////////// Notificaci?n
    NotificationManager nm;
    Notification notif;
    com.example.covm.Httppostaux post;

    static String ns = Context.NOTIFICATION_SERVICE;
    int icono_d = R.drawable.lupa;
    // //////////////////////////////////////////////////////
    Spinner sp;
    //Fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);     //Hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    ImageButton ibObtenerFecha, ibObtenerHora;
    TextView elpaciente;

    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altaturno);

        Bundle datos = this.getIntent().getExtras();
        final String fec_tu = datos.getString("fec_tu");
        final String det_tu = datos.getString("det_tu");
        final String hor_tu = datos.getString("hor_tu");
        final String nom_tu = datos.getString("nom_tu");
        final String reg_tu = datos.getString("reg_tu");
        final Integer acc_tu = datos.getInt("acc_tu");

        sp=(Spinner)findViewById(R.id.spin_paciente_tu);
        adaptersp=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems);
        sp.setAdapter(adaptersp);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                nom_tu00 = parentView.getItemAtPosition(position).toString();
                int ttuno = nom_tu00.indexOf(" . ");
                cel_tu00 = nom_tu00.substring(ttuno+3);
                cel_tu00 = nom_tu00.replace("-","");
                nom_tu00 = nom_tu00.substring(0, ttuno);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        post=new Httppostaux();

        Button b1=(Button)findViewById(R.id.butt_grabar_tu);
        Button b2=(Button)findViewById(R.id.butt_volver_tu);
        Button b3=(Button)findViewById(R.id.butt_borrar_tu);

        ibObtenerHora =(ImageButton)findViewById(R.id.ima_hora_tu);
        ibObtenerFecha = (ImageButton)findViewById(R.id.ima_calendario_tu);

        final EditText det_tu00 =(EditText)findViewById(R.id.edit_detalle_tu);
        final TextView elpaciente =(TextView)findViewById(R.id.text_paciente_tu);
        fec_tu00 =(EditText)findViewById(R.id.edit_fecha_tu);
        hor_tu00 =(EditText)findViewById(R.id.edit_hora_tu);
        reg_tu00 =(TextView)findViewById(R.id.et_registro);
        reg_tu00.setText(reg_tu);

        msg_tu00  = det_tu00.getText().toString();
        msg_tu00  = msg_tu00.replaceAll("\n", "#");

        SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
        String IP =String.valueOf(prefe.getString("ip",""));
        String USU =String.valueOf(prefe.getString("usuario",""));
        URL_connect="http://"+IP+"/sistemas/co/android/addturno.php";

        IP_Server = IP;
        ID_matricula = USU;


        b1.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast") @Override
            public void onClick(View v) {
                String ID_fecha = fec_tu00.getText().toString();
                String lano = ID_fecha.substring(6,10);
                String lmes = ID_fecha.substring(4,5);
                String ldia = ID_fecha.substring(0,2);
                o_fecha = (lano+"-"+lmes+"-"+ldia);
                String hora=hor_tu00.getText().toString();
                String registro=reg_tu;
                String accion= String.valueOf(acc_tu);
                msg_tu00  = det_tu00.getText().toString().trim();
                msg_tu00  = msg_tu00.replaceAll("\n", "  ");
                Log.e("Lfecha ","Fecha: "+o_fecha);

// Llamo a la tarea asincrona
//                if( checklogindata(fecha , msg_tu00 )==true){
                new asynclogin().execute(o_fecha,msg_tu00, nom_tu00, ID_matricula, hora,registro,accion);
 /*               }else{
                    err_login();
                }*/
                //    Notificacion
                finish();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent intent = new Intent(AltaTurno.this, turnos.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast") @Override
            public void onClick(View v) {
                String fecha= o_fecha;
                String hora=hor_tu00.getText().toString();
                String registro=reg_tu;
                msg_tu00  = det_tu00.getText().toString().trim();
                msg_tu00  = msg_tu00.replaceAll("\n", "  ");
                new asynclogin().execute(fecha,msg_tu00, nom_tu00, ID_matricula, hora,registro,"3");
                finish();
            }
        });



        Log.e("Accion >>> "," = "+acc_tu);
        if (acc_tu == 2){
            hor_tu00.setText(hor_tu);
            fec_tu00.setText(fec_tu);
            det_tu00.setText(det_tu);
            b3.setVisibility(View.VISIBLE);
            sp.setVisibility(View.INVISIBLE);
            elpaciente.setText("Paciente: "+nom_tu);
            // sp.setSelection(nom_tu);

        }else {
            Log.e("TIPO 1","Hora = "+hor_tu00.getText().toString());
            elpaciente.setText("Paciente: ");
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = sdf.format(date);
            fec_tu00.setText(dateString);
            hor_tu00.setText("00:00");
            sp.setVisibility(View.VISIBLE);
            b3.setVisibility(View.INVISIBLE);
        }

        ibObtenerFecha.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                obtenerFecha();
            }
        });
        ibObtenerHora.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                obtenerHora();
            }
        });

        String url = "192.168.0.1";
        spinnerCat(url);

    }

    public void spinnerCat(String url) {
        new BackTask().execute();
    }

    public void onStart(){
        super.onStart();
        adaptersp.clear();
/*		      BackTask bt=new BackTask();
		      bt.execute();*/
    }
    private class BackTask extends AsyncTask<Void,Void,Void> {
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
            IP_Server = IP;
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost= new HttpPost("http://"+IP_Server+"/sistemas/co/android/agenda_paciente.php?matricula="+ID_matricula);
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
        }
        return response;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username ,String password ){

        if 	(username.equals("") || password.equals("")){
            Log.e("Login ui", "checklogindata user or pass error");
            return false;

        }else{

            return true;
        }
    }

    //vibra y muestra un Toast
    public void err_login(){
        Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(),"Error en el usuario o password", Toast.LENGTH_SHORT);
        toast1.show();
    }

    public boolean loginstatus(String fecha ,String mensaje ,String nombre ,String origen, String hora, String registro, String accion) {
        int logstatus=-1;
        String saccion =String.valueOf(accion);
        /*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
         * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
        postparameters2send.add(new BasicNameValuePair("fecha",fecha));
        postparameters2send.add(new BasicNameValuePair("mensaje",mensaje));
        postparameters2send.add(new BasicNameValuePair("nombre",nombre));
        postparameters2send.add(new BasicNameValuePair("origino",origen));
        postparameters2send.add(new BasicNameValuePair("hora",hora));
        postparameters2send.add(new BasicNameValuePair("registro",registro));
        postparameters2send.add(new BasicNameValuePair("accion",saccion));

        //realizamos una peticion y como respuesta obtenes un array JSON
        JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

        //  SystemClock.sleep(950);

        //si lo que obtuvimos no es null
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

            //validamos el valor obtenido
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

    class asynclogin extends AsyncTask< String, String, String> {

        String fecha,mensaje, destino, origen, hora, registro, accion;
        protected void onPreExecute() {
     /*       //para el progress dialog
            pDialog = new ProgressDialog(Agenda_c.this);
            pDialog.setMessage("Actualizando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        protected String doInBackground(String... params) {
            //obtnemos usr y pass
            fecha=params[0];
            mensaje=params[1];
            destino=params[2];
            origen=params[3];
            hora=params[4];
            registro=params[5];
            accion=params[6];


            if (loginstatus(fecha,mensaje,destino, origen, hora, registro, accion)==true){

                return "ok"; //login valido
            }else{
                return "err"; //login invalido
            }
        }
        protected void onPostExecute(String result) {
            Log.e("onPostExecute=",""+result);
        }
    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  (hourOfDay < 9)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 9)? String.valueOf(CERO + minute):String.valueOf(minute);
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                hor_tu00.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
        }, hora, minuto, false);
        recogerHora.show();
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                fec_tu00.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                o_fecha= year+"-"+mesFormateado+"-"+diaFormateado;

            }
        },anio, mes, dia);
        recogerFecha.show();
    }
}