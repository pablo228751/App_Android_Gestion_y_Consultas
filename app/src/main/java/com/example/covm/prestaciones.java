package com.example.covm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;


public class prestaciones extends ListActivity {
    protected static final int REQUEST_CODE = 10;
    Button  button_enviar_ve00;
    Button  button_volver_ve00;
    EditText edit_tot_ve00;
    EditText edit_can_ve00;
    String agr_ve00="Sin Agrupar";
    String agr_mes="1";
    String agr_ano="2019";
    String IP_Server="192.168.1.50";	//IP DE NUESTRO PC
    private int mYear1;
    private int mMonth1;
    private int mDay1;
    private int mYear;
    private int mMonth;
    private int mDay;
    private EditText fec_ve00;
    private EditText fe1_ve00;
    private EditText usu_ve00;
    private EditText zon_ve00;
    //private EditText ven_ve00;
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;
    long total = 0;
    int cantidad = 0;
    TextView tv_nomID;
    TextView tv_fecID;
    TextView tv_nosID;
    TextView tv_monID;
    TextView tv_opeID;
    Activity context;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;
    VerPrestaAdaptador adapter;
    ListView listaVenta;
    ArrayList<VerPresta_data> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestaciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context=this;
        records=new ArrayList<VerPresta_data>();
        listaVenta=(ListView)findViewById(android.R.id.list);
        adapter=new VerPrestaAdaptador(context, R.layout.lv_verpresta, R.id.text_cc_nombre,	records);
        listaVenta.setAdapter(adapter);

        Button b1=(Button)findViewById(R.id.button_enviar_ve00);
        Button b2=(Button)findViewById(R.id.button_volver_ve00);
        usu_ve00=(EditText)findViewById(R.id.edit_usu_ve00);
        //ven_ve00 =(EditText)findViewById(R.id.edit_ven_ve00);
        zon_ve00 =(EditText)findViewById(R.id.edit_zon_ve00);
        edit_tot_ve00 =(EditText)findViewById(R.id.edit_tot_ve00);
        edit_can_ve00 =(EditText)findViewById(R.id.edit_can_ve00);


        //Fecha actual desglosada:
        Calendar fecha = Calendar.getInstance();
        int nano = fecha.get(Calendar.YEAR)-2018;
        int nmes = fecha.get(Calendar.MONTH);

        Spinner sp = (Spinner) findViewById(R.id.spin_agr_ve00);

        ArrayAdapter adaptadorsp = ArrayAdapter.createFromResource(
                this, R.array.agr_ve00, android.R.layout.simple_spinner_item);
        adaptadorsp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptadorsp);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                agr_ve00 = parentView.getItemAtPosition(position).toString();
              //  Toast.makeText(parentView.getContext(), "Has seleccionado " + agr_ve00, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner mes = (Spinner) findViewById(R.id.spin_agr_mes);
        ArrayAdapter adaptadormes = ArrayAdapter.createFromResource(
                this, R.array.agr_mes, android.R.layout.simple_spinner_item);
        adaptadormes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mes.setAdapter(adaptadormes);
        mes.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                agr_mes = parentView.getItemAtPosition(position).toString();
                //Toast.makeText(parentView.getContext(), "Has seleccionado " + agr_mes, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mes.setSelection(nmes);

        Spinner ano = (Spinner) findViewById(R.id.spin_agr_ano);
        ArrayAdapter adaptadorano = ArrayAdapter.createFromResource(
                this, R.array.agr_ano, android.R.layout.simple_spinner_item);
        adaptadorano.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ano.setAdapter(adaptadorano);
        ano.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                agr_ano = parentView.getItemAtPosition(position).toString();
               // Toast.makeText(parentView.getContext(), "Has seleccionado " +  agr_ano, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ano.setSelection(nano);

        SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
        String Id_usuario =String.valueOf(prefe.getString("usuario","dato"));
        long date1 = System.currentTimeMillis()-15*24*60*60*1000l;
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        usu_ve00.setText(Id_usuario);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                adapter.clear();
                BackTask bt=new BackTask();
                bt.execute();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent j = new Intent(prestaciones.this, Abc_pt.class);
                j.putExtra("acc_np","1");
                j.putExtra("nro_np","0");
                j.putExtra("oper_np","0");

                startActivity(j);
            }
        });
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        tv_nomID = (TextView) v.findViewById(R.id.text_cc_nombre);
        tv_opeID = (TextView) v.findViewById(R.id.text_cc_oper);
        tv_fecID = (TextView) v.findViewById(R.id.text_cc_codigo);
        tv_nosID = (TextView) v.findViewById(R.id.text_cc_nos);
        tv_monID = (TextView) v.findViewById(R.id.text_cc_monto);
        Intent i3 = new Intent(this, VerComprobante.class);
        String aux_nomId = tv_nomID.getText().toString();
        String aux_opeId = tv_opeID.getText().toString();
        String aux_nosId = tv_nosID.getText().toString();
        String aux_fecId = tv_fecID.getText().toString();
        String aux_monId = tv_monID.getText().toString();
        i3.putExtra("id_ver",id);
        i3.putExtra("com_nom",aux_nomId);
        i3.putExtra("com_ope",aux_opeId);
        i3.putExtra("com_nos",aux_nosId);
        i3.putExtra("com_fec",aux_fecId);
        i3.putExtra("com_mon",aux_monId);
        Log.e("Posicion prestacion ","ID "+id+"// "+position+"// "+aux_nomId);
        startActivity(i3);
    }

    public void onStart(){
        super.onStart();
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
            String palabra="";

            String Ip_Server="192.168.1.50";//IP DE NUESTRO PC Cambiar segun las preferecnias
            SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
            String IP =String.valueOf(prefe.getString("ip",""));
            String aux_Usu = usu_ve00.getText().toString();
            Ip_Server = "http://"+IP+"/sistemas/co/android/consulta_pt.php?usuario="+aux_Usu+"&mes="+agr_mes+"&ano="+agr_ano+"&agrupado="+agr_ve00;

            Log.e("HTTP ", Ip_Server);

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
                total = 0;
                cantidad = 0;

                result=result.substring(result.indexOf("["));
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data =jArray.getJSONObject(i);
                    VerPresta_data p=new VerPresta_data();
                    p.setccNombre(json_data.getString("ccNombre"));
                    p.setccCodigo(json_data.getString("ccCodigo"));
                    p.setccNos(json_data.getString("ccNos"));
                    p.setccOper(json_data.getLong("ccOper"));
                    p.setccMonto(json_data.getLong("ccMonto"));
                    records.add(p);
                    total = total + json_data.getLong("ccMonto");
                    cantidad = cantidad + 1;
                    Log.e("total", String.valueOf(total));
                }
            }
            catch(Exception e){
                Log.e("ERROR", "Error al pasar los datos "+e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void result){
            if(pd!=null) pd.dismiss(); //close dialog
            Log.e("Registros ", records.size() + "TOTal "+total);
            edit_tot_ve00.setText("$"+ total);
            edit_can_ve00.setText(String.valueOf(cantidad));

            adapter.notifyDataSetChanged(); //notify the ListView to get new records
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}

