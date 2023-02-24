package com.example.covm;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class Tmp_ptAdaptador extends ArrayAdapter<Tmp_pt_data> {
    int groupid;
    ArrayList<Tmp_pt_data> records;
    Context context;

    public Tmp_ptAdaptador(Context context, int vg, int id, ArrayList<Tmp_pt_data>
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
        TextView textNombre = (TextView) itemView.findViewById(R.id.text_tmpnp_nombre);
        textNombre.setText(records.get(position).gettmpNombre());
        TextView textTitular = (TextView) itemView.findViewById(R.id.text_tmpnp_codigo);
        textTitular.setText(records.get(position).gettmpCodigo());
        TextView textCantidad = (TextView) itemView.findViewById(R.id.text_tmpnp_cantidad);
        textCantidad.setText( " "+records.get(position).gettmpCantidad());
        TextView textMonto = (TextView) itemView.findViewById(R.id.text_tmpnp_monto);
        textMonto.setText( " "+records.get(position).gettmpMonto());
        TextView textTotal = (TextView) itemView.findViewById(R.id.text_tmpnp_total);
        textTotal.setText( " "+records.get(position).gettmpTotal());
        return itemView;
    }
}