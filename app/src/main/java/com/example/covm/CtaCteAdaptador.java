package com.example.covm;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CtaCteAdaptador extends ArrayAdapter<CtaCte_data> {
    int groupid;
    ArrayList<CtaCte_data> records;
    Context context;
    private int[] colors = new int[] { 0x30ffff00, 0x30800080 };


    public CtaCteAdaptador(Context context, int vg, int id, ArrayList<CtaCte_data>
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
        TextView textFecha = (TextView) itemView.findViewById(R.id.text_cc_fecha);
        textFecha.setText(records.get(position).getccFecha());
        TextView textObs = (TextView) itemView.findViewById(R.id.text_cc_obs);
        textObs.setText(records.get(position).getccObs());
        TextView textNro = (TextView) itemView.findViewById(R.id.text_cc_nro);
        textNro.setText(" "+records.get(position).getccNro());
        TextView textCantidad = (TextView) itemView.findViewById(R.id.text_cc_monto);
        textCantidad.setText("Importe $ "+records.get(position).getccMonto());
        TextView textAplicado = (TextView) itemView.findViewById(R.id.text_cc_apl);
        textAplicado.setText(" > $ "+records.get(position).getccApl());
        TextView textSaldo = (TextView) itemView.findViewById(R.id.text_cc_saldo);
        textSaldo.setText("Pendiente $ "+records.get(position).getccSaldo());
        TextView textOper = (TextView) itemView.findViewById(R.id.text_cc_oper);
        textOper.setText(" "+records.get(position).getccOper());

        if (records.get(position).getccSaldo() == 0){
            textSaldo.setVisibility(View.INVISIBLE);
        }

        if (records.get(position).getccMonto() < 0){
            itemView.setBackgroundColor(colors[0]);
            textCantidad.setTextColor(0x30303f9f);
        }else{
            itemView.setBackgroundColor(colors[1]);
            textCantidad.setTextColor(0x30D50000);
        }
        return itemView;
    }
}