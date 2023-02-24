package com.example.covm;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.text.DecimalFormat;

public class adap_cta_corriente extends ArrayAdapter<String>{

    private String[] fecha;
    private double[] debe;
    private double[] ope;
    private double[] haber;
    private String[] obs;
    private float[] saldo;
    private String[] reg;
    private Activity context;


    public adap_cta_corriente(Activity context, String[] fecha, double[] ope, double[] debe, double[] haber, String[] obs, float[] saldo, String[] reg) {
        super(context, R.layout.activity_adap_cta_corriente, fecha);
        this.context=context;
        this.fecha=fecha;
        this.debe=debe;
        this.ope=ope;
        this.haber=haber;
        this.obs= obs;
        this.saldo= saldo;
        this.reg= reg;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.activity_adap_cta_corriente,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);

        }
        else {
            viewHolder=(ViewHolder)r.getTag();


        }

        viewHolder.tvw1.setText(fecha[position]);
        //viewHolder.tvw1.setText("Nº: "+ fecha[position]);
        //viewHolder.tvw2.setText("$"+debe[position]);

        if (haber[position] >= 1) {
            viewHolder.tvw3.setText("$"+ haber[position]);
            viewHolder.tvw3.setTextColor(Color.parseColor("#D50000"));
            r.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            viewHolder.tvw3.setText("$"+ debe[position]);
            viewHolder.tvw3.setTextColor(Color.parseColor("#0D47A1"));
            r.setBackgroundColor(Color.parseColor("#69F0AE"));
        }
        viewHolder.tvw4.setText(obs[position]);
        DecimalFormat precision = new DecimalFormat("0.00");
        viewHolder.tvw5.setText("SALDO:  $"+precision.format(saldo[position]));
        viewHolder.tvw6.setText("Nº"+reg[position]);
        /*
        if (haber[position]>= 1){
            r.setBackgroundColor(Color.parseColor("#69F0AE"));
        }
        else {
            r.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }

         */


        return r;


    }

    static class ViewHolder{

        TextView tvw1;
        //TextView tvw2;
        TextView tvw3;
        TextView tvw4;
        TextView tvw5;
        TextView tvw6;

        ViewHolder(View v){
            tvw1=(TextView)v.findViewById(R.id.tvfecha);
            //tvw2=(TextView)v.findViewById(R.id.tvvto);
            tvw3=(TextView)v.findViewById(R.id.tvhaber);
            tvw4=(TextView)v.findViewById(R.id.tvobs);
            tvw5=(TextView)v.findViewById(R.id.tvsaldo);
            tvw6=(TextView)v.findViewById(R.id.tvreg);
        }

    }

}
