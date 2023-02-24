package com.example.covm;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class Con_ptAdaptador extends ArrayAdapter<Con_pt_data> {
    int groupid;
    ArrayList<Con_pt_data> records;
    Context context;

    public Con_ptAdaptador(Context context, int vg, int id, ArrayList<Con_pt_data>
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
        TextView textTitular = (TextView) itemView.findViewById(R.id.text_cc_codigo);
        textTitular.setText(records.get(position).getccCodigo());
        TextView textMonto = (TextView) itemView.findViewById(R.id.text_cc_monto);
        textMonto.setText(" "+records.get(position).getccMonto());


        return itemView;
    }
}