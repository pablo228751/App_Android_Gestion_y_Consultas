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

public class adap_agenda extends RecyclerView.Adapter<adap_agenda.ViewHolderAgenda> implements View.OnClickListener{
    //ArrayList<String> listDatos;
    private View.OnClickListener escuchar;
    public  List<agenda_datos> datos;
    public adap_agenda (List<agenda_datos> datos){
        this.datos= datos;
    }



    @NonNull
    @Override
    public ViewHolderAgenda onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lv_agenda,null,false);

        view.setOnClickListener(this);
        return new ViewHolderAgenda(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAgenda viewHolderAgenda, int i) {

        viewHolderAgenda.e_nombre.setText(datos.get(i).getNombre());
        viewHolderAgenda.e_codigo.setText("Código: "+datos.get(i).getCodigo());

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }
    public  void setOnClickListener (View.OnClickListener escuchar){
        this.escuchar=escuchar;
    }

    @Override
    public void onClick(View view) {
        if (escuchar !=null){
            escuchar.onClick(view);

        }

    }

    public class ViewHolderAgenda extends RecyclerView.ViewHolder {
        TextView e_codigo, e_nombre;
        public ViewHolderAgenda(@NonNull View itemView) {
            super(itemView);
            e_codigo=(TextView) itemView.findViewById(R.id.e_codigo);
            e_nombre=(TextView) itemView.findViewById(R.id.e_nombre);
        }

        public void asignarDatos(String s) {
            e_codigo.setText(s);
            e_nombre.setText(s);
        }
    }
    public void filtrar(ArrayList<agenda_datos> filtro_datos){
        this.datos = filtro_datos;
        notifyDataSetChanged();
    }
}
