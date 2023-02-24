package com.example.covm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class menu extends AppCompatActivity {
    TextView txt_matricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        txt_matricula =(TextView) findViewById(R.id.txt_matricula);
        Intent menu = getIntent();
        //int matricula=menu.getExtras().getInt("matricula"); O
        final String matricula= menu.getExtras().getString("matricula");
        //txt_matricula.setText(Integer.toString(matricula)); si es INT
        txt_matricula.setText("Matrícula " + matricula);

        Button ir_padron = (Button) findViewById(R.id.button4);
        ir_padron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent padron =new Intent(menu.this, Pacientes.class);
                padron.putExtra("id_acc","4");
                padron.putExtra("id_tit","");
                padron.putExtra("id_cta","");
                padron.putExtra("matricula", matricula);

                menu.this.startActivity(padron);
            }
        });
        Button ir_aranceles1 = (Button) findViewById(R.id.button5);
        ir_aranceles1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aranceles1 =new Intent(menu.this, aranceles.class);
                aranceles1.putExtra("matricula", matricula);
                menu.this.startActivity(aranceles1);
            }
        });
        Button ir_prestaciones = (Button) findViewById(R.id.button7);
        ir_prestaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prestaciones =new Intent(menu.this, prestaciones.class);
                prestaciones.putExtra("matricula", matricula);
                menu.this.startActivity(prestaciones);
            }
        });
        Button ir_agenda = (Button) findViewById(R.id.button6);
        ir_agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agenda =new Intent(menu.this, agenda.class);
                agenda.putExtra("matricula", matricula);
                menu.this.startActivity(agenda);
            }
        });
        Button ir_cta_cte = (Button) findViewById(R.id.cta_cte);
        ir_cta_cte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cta_corriente =new Intent(menu.this, cta_corriente.class);
                cta_corriente.putExtra("matricula", matricula);
                menu.this.startActivity(cta_corriente);
            }
        });

    }
}