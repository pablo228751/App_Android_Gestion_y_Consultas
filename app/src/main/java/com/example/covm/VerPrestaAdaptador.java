package com.example.covm;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VerPrestaAdaptador extends ArrayAdapter<VerPresta_data> {
        int groupid;
        ArrayList<VerPresta_data> records;
        Context context;
        String palabra;
       // private int[] colors = new int[] { 0x30ffff00, 0x30800080 };

        public VerPrestaAdaptador(Context context, int vg, int id, ArrayList<VerPresta_data>
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
        TextView textNombre = (TextView) itemView.findViewById(R.id.text_cc_nombre);
        textNombre.setText(records.get(position).getccNombre());
        TextView textFecha = (TextView) itemView.findViewById(R.id.text_cc_codigo);
        textFecha.setText(records.get(position).getccCodigo());
        TextView textNos = (TextView) itemView.findViewById(R.id.text_cc_nos);
        textNos.setText(records.get(position).getccNos());
        TextView textRegistro = (TextView) itemView.findViewById(R.id.text_cc_oper);
        textRegistro.setText(" "+records.get(position).getccOper());
        TextView textCantidad = (TextView) itemView.findViewById(R.id.text_cc_monto);
        textCantidad.setText("$ "+records.get(position).getccMonto());

        if (records.get(position).getccMonto() < 0){
            textCantidad.setTextColor(Color.parseColor("#D50000"));
            itemView.setBackgroundColor(Color.parseColor("#69F0AE")); //#ECEFF1
        }else{

            textCantidad.setTextColor(Color.parseColor("#000000"));
            itemView.setBackgroundColor(Color.parseColor("#CFD8DC"));//#ECEFF1
        }
        return itemView;
    }
}


