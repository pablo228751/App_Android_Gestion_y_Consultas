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

public class adap_agenda_consultar extends RecyclerView.Adapter<adap_agenda_consultar.ViewHolderAgenda> implements View.OnClickListener{
    //ArrayList<String> listDatos;
    private View.OnClickListener escuchar;
    public  List<agenda_consultar_datos> datos;
    public adap_agenda_consultar (List<agenda_consultar_datos> datos){
        this.datos= datos;
    }



    @NonNull
    @Override
    public ViewHolderAgenda onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lv_agenda_consultar,null,false);

        view.setOnClickListener(this);
        return new ViewHolderAgenda(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAgenda viewHolderAgenda, int i) {

        viewHolderAgenda.t_fecha.setText("Fecha: "+datos.get(i).getFecha());
        viewHolderAgenda.t_hora.setText("Hora: "+datos.get(i).getHora());
        viewHolderAgenda.t_nombre.setText(datos.get(i).getNombre());

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
        TextView t_fecha, t_hora,t_nombre;
        public ViewHolderAgenda(@NonNull View itemView) {
            super(itemView);
            t_fecha=(TextView) itemView.findViewById(R.id.t_fecha);
            t_hora=(TextView) itemView.findViewById(R.id.t_hora);
            t_nombre=(TextView) itemView.findViewById(R.id.t_nombre);
        }

        public void asignarDatos(String s) {
            t_fecha.setText(s);
            t_hora.setText(s);
            t_nombre.setText(s);
        }
    }

}
