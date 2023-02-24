package com.example.covm;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PtAdaptador extends ArrayAdapter<Pt_data> {
    int groupid;
    ArrayList<Pt_data> records;
    Context context;
    String palabra;
    private int[] colors = new int[] { 0x30ffff00, 0x30800080 };


    public PtAdaptador(Context context, int vg, int id, ArrayList<Pt_data>
            records) {

        super(context, vg, id, records);
        this.context = context;
        groupid = vg;
        this.records = records;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);
        TextView textNombre = (TextView) itemView.findViewById(R.id.text_np_nombre);
        textNombre.setText(records.get(position).getnpNombre());
        TextView textTitular = (TextView) itemView.findViewById(R.id.text_np_titular);
        textTitular.setText(records.get(position).getnpTitular());
        TextView textFecha = (TextView) itemView.findViewById(R.id.text_np_fecha);
        textFecha.setText(records.get(position).getnpFecha());
        TextView textNro = (TextView) itemView.findViewById(R.id.text_np_nro);
        textNro.setText( " "+records.get(position).getnpNro());
        TextView textCta = (TextView) itemView.findViewById(R.id.text_np_cta);
        textCta.setText( " "+records.get(position).getnpCta());
        TextView textOper = (TextView) itemView.findViewById(R.id.text_np_operacion);
        textOper.setText( " "+records.get(position).getnpOper());
        TextView textMonto = (TextView) itemView.findViewById(R.id.text_np_monto);
        textMonto.setText( "$ "+records.get(position).getnpMonto());

        if (records.get(position).getnpNombre()!= null){
            palabra = records.get(position).getnpNombre();
        }else{
            palabra = "";
        }
/*
        if (palabra.equals("Enviada al SERVER")){
            itemView.setBackgroundColor(colors[0]);
        }else{
            itemView.setBackgroundColor(colors[1]);

        }

        */

        return itemView;
    }
}