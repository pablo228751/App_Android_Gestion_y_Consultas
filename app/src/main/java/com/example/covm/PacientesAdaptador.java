package com.example.covm;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PacientesAdaptador extends ArrayAdapter<Pacientes_data> {
    int groupid;
    ArrayList<Pacientes_data> records;
    Context context;

    public PacientesAdaptador(Context context, int vg, int id, ArrayList<Pacientes_data>
            records) {

        super(context, vg, id, records);
        this.context = context;
        groupid = vg;
        this.records = records;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);
        TextView textNombre = (TextView) itemView.findViewById(R.id.text_nom_pac);
        textNombre.setText(records.get(position).getpNombre());
        TextView textTelefono = (TextView) itemView.findViewById(R.id.text_cel_pac);
        textTelefono.setText(records.get(position).getpTelefono());
        TextView textMail = (TextView) itemView.findViewById(R.id.text_mai_pac);
        textMail.setText(records.get(position).getpMail());
        TextView textDir = (TextView) itemView.findViewById(R.id.text_dir_pac);
        textDir.setText(records.get(position).getpDir());
        TextView textNos = (TextView) itemView.findViewById(R.id.text_nos_pac);
        textNos.setText(records.get(position).getpNos());
        TextView textCuenta = (TextView) itemView.findViewById(R.id.text_cta_pac);
        textCuenta.setText(" "+records.get(position).getpCuenta());
        TextView textAfi = (TextView) itemView.findViewById(R.id.text_nro_pac);
        textAfi.setText(" "+records.get(position).getpAfi());
        TextView textSocial = (TextView) itemView.findViewById(R.id.text_soc_pac);
        textSocial.setText(" "+records.get(position).getpSocial());
        return itemView;
    }
}