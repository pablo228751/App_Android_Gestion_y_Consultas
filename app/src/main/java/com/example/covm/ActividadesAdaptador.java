package com.example.covm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ActividadesAdaptador extends ArrayAdapter<Act_data> {
    int groupid;
    ArrayList<Act_data> records;
    Context context;
    String palabra;
    private int[] colors = new int[] { 0x30ffff00, 0x3080fff0, 0x30800770 };

    public ActividadesAdaptador(Context context, int vg, int id, ArrayList<Act_data>
            records) {

        super(context, vg, id, records);
        this.context = context;
        groupid = vg;
        this.records = records;
    }

    public void clear() {
        records.clear();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);
        TextView textName = (TextView) itemView.findViewById(R.id.et_ac_detalle);
        textName.setText(records.get(position).getpDetalle());
        TextView textOrigen = (TextView) itemView.findViewById(R.id.et_ac_origen);
        textOrigen.setText(records.get(position).getpOrigen());
        TextView textVto = (TextView) itemView.findViewById(R.id.et_ac_fecha);
        textVto.setText(records.get(position).getpFecha());
        TextView textRegistro = (TextView) itemView.findViewById(R.id.et_ac_registro);
        textRegistro.setText(" "+records.get(position).getpRegistro());
        TextView textMom = (TextView) itemView.findViewById(R.id.et_ac_mom);
        textMom.setText(records.get(position).getpMom());


        if (records.get(position).getpMom()!= null){
            palabra = records.get(position).getpMom();
        }else{
            palabra = "/";
        }

        if (palabra.equals("+")){
            itemView.setBackgroundColor(colors[1]);
        }else{
            if (palabra.equals("-")){
                itemView.setBackgroundColor(colors[2]);
            }else{
                itemView.setBackgroundColor(colors[0]);
            }
        }
        return itemView;
    }
}
