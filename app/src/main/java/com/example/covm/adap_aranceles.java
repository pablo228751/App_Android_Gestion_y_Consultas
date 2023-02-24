package com.example.covm;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class adap_aranceles extends RecyclerView.Adapter<adap_aranceles.ViewHolder> {

    double [] precio;
    double [] coseguro;

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView observ_aran,cod_aran, coseguro_aran, precio_aran;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            observ_aran=(TextView)itemView.findViewById(R.id.observ_aran);
            cod_aran=(TextView)itemView.findViewById(R.id.cod_aran);
            coseguro_aran=(TextView)itemView.findViewById(R.id.coseguro_aran);
            precio_aran=(TextView)itemView.findViewById(R.id.precio_aran);
        }
    }
    public  List<aranceles_datos> datos;
    public adap_aranceles (List<aranceles_datos> datos){
        this.datos= datos;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adap_aranceles,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.observ_aran.setText(datos.get(position).getObser());
        holder.cod_aran.setText("Código: "+datos.get(position).getCod());
        DecimalFormat precision = new DecimalFormat("0.00");

        if (datos.get(position).getCoseguro()<1){
            holder.coseguro_aran.setText("Coseguro:  -  ");

        }else {
            holder.coseguro_aran.setText("Coseguro: $"+precision.format(datos.get(position).getCoseguro()));
        }
        holder.precio_aran.setText("Precio: $"+precision.format(datos.get(position).getPrecio()));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void filtrar(ArrayList<aranceles_datos> filtro_datos){
        this.datos = filtro_datos;
        notifyDataSetChanged();
    }


}