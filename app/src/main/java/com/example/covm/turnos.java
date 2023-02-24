package com.example.covm;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class turnos extends ListActivity {
    private TextView textView3;
    String IP_Server="192.168.1.50";	//IP DE NUESTRO PC
    String ID_oper="";	//
    String o_fecha="";	//
    String cel_tu00="";	//
    private EditText fec_tu00;	//
    boolean Si_SMS = true;
    String ID_matricula="dato";	//IP DE NUESTRO PC
    String URL_connect="http://"+IP_Server+"/co/android/addagenda.php";//ruta en donde estan nuestros archivos
    TurnosAdaptador adapter;
    ImageButton ibObtenerFecha;
    ImageButton butt_menos;
    ImageButton butt_mas;
    StringBuffer buffer;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    ListView lista_turnos;
    ArrayList<Turnos_data> records;
    Activity context;
    ProgressDialog pd;
    TextView tv_registro;
    TextView tv_detalle;
    TextView tv_fecha;
    TextView tv_hora;
    TextView tv_nombre;

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
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);

        post=new Httppostaux();

        Button b1=(Button)findViewById(R.id.butt_nuevo_tu);
        Button b2=(Button)findViewById(R.id.butt_calendario_tu);
        fec_tu00 =(EditText)findViewById(R.id.edit_fecha_tu);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        fec_tu00.setText(dateString);
        ibObtenerFecha = (ImageButton)findViewById(R.id.ima_calendario_tu);
        butt_menos = (ImageButton)findViewById(R.id.ima_menos_tu);
        butt_mas = (ImageButton)findViewById(R.id.ima_mas_tu);

        SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
        String IP =String.valueOf(prefe.getString("ip",""));
        String USU =String.valueOf(prefe.getString("usuario",""));
        boolean asms =  Boolean.valueOf(prefe.getBoolean("datos",true));
        URL_connect="http://"+IP+"/sistemas/co/android/addagenda.php";

        IP_Server = IP;
        ID_matricula = USU;

        context=this;
        records=new ArrayList<Turnos_data>();
        lista_turnos=(ListView)findViewById(android.R.id.list);
        adapter=new TurnosAdaptador(context, R.layout.lv_turnos, R.id.et_turnos_detalle,	records);
        lista_turnos.setAdapter(adapter);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();

        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent intent = new Intent(turnos.this, AltaTurno.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("acc_tu",1);
                intent.putExtra("reg_tu","0");
                intent.putExtra("hor_tu","");
                intent.putExtra("nom_tu","");
                intent.putExtra("det_tu","");
                intent.putExtra("fec_tu","");
                startActivity(intent);
            }
        });

        ibObtenerFecha.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        butt_menos.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String diaFormateado;
                String mesFormateado;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateInString = fec_tu00.getText().toString();;
                try {
                    Date fecha = formatter.parse(dateInString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fecha);
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    final int mes = calendar.get(Calendar.MONTH)+1;
                    final int dia = calendar.get(Calendar.DAY_OF_MONTH);
                    final int anio = calendar.get(Calendar.YEAR);
                    diaFormateado = (dia < 10)? "0" + String.valueOf(dia):String.valueOf(dia);
                    mesFormateado = (mes < 10)? "0" + String.valueOf(mes):String.valueOf(mes);
                    fec_tu00.setText(diaFormateado + "/" + mesFormateado + "/" + anio);
                    //    System.out.println(formatter.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                adapter.clear();
                BackTask bt=new BackTask();
                bt.execute();

            }
        });

        butt_mas.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String diaFormateado;
                String mesFormateado;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateInString = fec_tu00.getText().toString();;
                try {
                    Date fecha = formatter.parse(dateInString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fecha);
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    final int mes = calendar.get(Calendar.MONTH)+1;
                    final int dia = calendar.get(Calendar.DAY_OF_MONTH);
                    final int anio = calendar.get(Calendar.YEAR);
                    diaFormateado = (dia < 10)? "0" + String.valueOf(dia):String.valueOf(dia);
                    mesFormateado = (mes < 10)? "0" + String.valueOf(mes):String.valueOf(mes);
                    fec_tu00.setText(diaFormateado + "/" + mesFormateado + "/" + anio);
                    //    System.out.println(formatter.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                adapter.clear();
                BackTask bt=new BackTask();
                bt.execute();
            }

        });


        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                adapter.clear();
                BackTask bt=new BackTask();
                bt.execute();
            }
        });

    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
        adapter.clear();
        BackTask bt=new BackTask();
        bt.execute();
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        tv_registro   = (TextView) v.findViewById(R.id.et_turnos_registro);
        tv_detalle   = (TextView) v.findViewById(R.id.et_turnos_detalle);
        tv_fecha   = (TextView) v.findViewById(R.id.et_turnos_fecha);
        tv_hora   = (TextView) v.findViewById(R.id.et_turnos_hora);
        tv_nombre   = (TextView) v.findViewById(R.id.et_turnos_nombre);

        String aux_registro = tv_registro.getText().toString();
        String aux_detalle = tv_detalle.getText().toString();
        String ID_fecha = tv_fecha.getText().toString();
        String aux_hora = tv_hora.getText().toString();
        String aux_nombre = tv_nombre.getText().toString();

        String lano = ID_fecha.substring(0,4);
        String lmes = ID_fecha.substring(5,7);
        String ldia = ID_fecha.substring(8,10);

        o_fecha = (ldia+"/"+lmes+"/"+lano);
        ID_oper = aux_registro;
        Intent i3 = new Intent(this, AltaTurno .class);

        i3.putExtra("acc_tu",2);
        i3.putExtra("hor_tu",aux_hora);
        i3.putExtra("reg_tu",aux_registro);
        i3.putExtra("det_tu",aux_detalle);
        i3.putExtra("nom_tu",aux_nombre);
        i3.putExtra("fec_tu",o_fecha);
        startActivity(i3);
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
            String ID_fecha = fec_tu00.getText().toString();

            String lano = ID_fecha.substring(6,10);
            String lmes = ID_fecha.substring(4,5);
            String ldia = ID_fecha.substring(0,2);
            o_fecha = (lano+"-"+lmes+"-"+ldia);
            Ip_Server = "http://"+IP+"/sistemas/co/android/turnos.php?matricula="+ID_matricula+"&fecha="+o_fecha;
            Log.e("ERROR", Ip_Server);
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
                    Turnos_data p=new Turnos_data();
                    p.setpDetalle(json_data.getString("pDetalle"));
                    p.setpHora(json_data.getString("pHora"));
                    p.setpNombre(json_data.getString("pNombre"));
                    p.setpFecha(json_data.getString("pFecha"));
                    p.setpRegistro(json_data.getString("pRegistro"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // updates the date in the TextView
    private void updateDisplay() {
        String mes, dia, fechal;
        if (mMonth + 1 < 10){
            mes = "0"+String.valueOf(mMonth + 1);
        }else{
            mes = String.valueOf(mMonth + 1);
        }
        if (mDay < 10){
            dia = "0"+String.valueOf(mDay);
        }else{
            dia = String.valueOf(mDay);
        }
        fechal = String.valueOf(dia+"/"+mes+"/"+mYear);
        o_fecha = String.valueOf(mYear+"-"+mes+"-"+dia);
        fec_tu00.setText(fechal);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
}