package com.example.covm;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TurnosAdaptador extends ArrayAdapter<Turnos_data> {
    int groupid;
    ArrayList<Turnos_data> records;
    Context context;

    public TurnosAdaptador(Context context, int vg, int id, ArrayList<Turnos_data>
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
        TextView textName = (TextView) itemView.findViewById(R.id.et_turnos_detalle);
        textName.setText(records.get(position).getpDetalle());
        TextView textPrice = (TextView) itemView.findViewById(R.id.et_turnos_origen);
        textPrice.setText(records.get(position).getpOrigen());
        TextView textVto = (TextView) itemView.findViewById(R.id.et_turnos_fecha);
        textVto.setText(records.get(position).getpFecha());
        TextView textHora = (TextView) itemView.findViewById(R.id.et_turnos_hora);
        textHora.setText(records.get(position).getpHora());
        TextView textNombre = (TextView) itemView.findViewById(R.id.et_turnos_nombre);
        textNombre.setText(records.get(position).getpNombre());

        TextView textRegistro = (TextView) itemView.findViewById(R.id.et_turnos_registro);
        textRegistro.setText(records.get(position).getpRegistro());
        return itemView;
    }
}
