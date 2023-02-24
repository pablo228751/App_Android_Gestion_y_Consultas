package com.example.covm;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class Configuracion extends Activity {
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private CheckBox check_cta;
    private CheckBox check_datos;
    private CheckBox check_pas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        // Button b2=(Button)findViewById(R.id.button_volver);


        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        //et3=(EditText)findViewById(R.id.et3);
        //et4=(EditText)findViewById(R.id.et4);
        et5=(EditText)findViewById(R.id.et5);
        check_pas = (CheckBox)findViewById(R.id.che_cfg_pas);
        check_datos = (CheckBox)findViewById(R.id.che_cfg_dat);

        SharedPreferences prefe=getSharedPreferences("co",Context.MODE_PRIVATE);
        et1.setText(prefe.getString("ip",""));
        et2.setText(prefe.getString("usuario",""));
        // et3.setText(prefe.getString("cuenta",""));
        //et4.setText(prefe.getString("menu",""));
        et5.setText(prefe.getString("cel_aviso",""));
        check_datos.setChecked(prefe.getBoolean("datos",false));
        check_pas.setChecked(prefe.getBoolean("mantiene",false));



/*
        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                finish();
            }
        });  */
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.activity_inicio, menu);
            return true;
        }
    */
    public void ejecutar(View v) {
        SharedPreferences preferencias=getSharedPreferences("co",Context.MODE_PRIVATE);
        Editor editor=preferencias.edit();
        editor.putString("ip",       et1.getText().toString());
        editor.putString("usuario",  et2.getText().toString());
        // editor.putString("cuenta", et3.getText().toString());
        // editor.putString("menu",      et4.getText().toString());
        editor.putString("cel_aviso", et5.getText().toString());
        editor.putBoolean("datos", check_datos.isChecked());
        editor.putBoolean("mantiene", check_pas.isChecked());
        editor.commit();
        finish();
    }
}
