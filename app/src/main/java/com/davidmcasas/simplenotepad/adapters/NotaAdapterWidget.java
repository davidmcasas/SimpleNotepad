package com.davidmcasas.simplenotepad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.davidmcasas.simplenotepad.AppWidget;
import com.davidmcasas.simplenotepad.AppWidgetConfigureActivity;
import com.davidmcasas.simplenotepad.R;
import com.davidmcasas.simplenotepad.activities.MainActivity;
import com.davidmcasas.simplenotepad.activities.NotaActivity;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;
import com.davidmcasas.simplenotepad.data.Nota;

import java.util.ArrayList;

public class NotaAdapterWidget extends RecyclerView.Adapter<NotaViewHolder> {

    private Context context;
    private ArrayList<Nota> notas;

    public NotaAdapterWidget(Context context, ArrayList<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }
    @Override
    public NotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nota_list_layout, parent, false);
        return new NotaViewHolder(view);
    }
    @Override
    public void onBindViewHolder(NotaViewHolder holder, int position) {
        final Nota nota = notas.get(position);
        holder.titulo.setText(nota.getTitulo());
        holder.contenido.setText(nota.getContenido());
        if (nota.getTitulo().trim().length() == 0) {
            holder.titulo.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, NotaActivity.class);
                NotaActivity.nota = nota;
                context.startActivity(intent);*/
                AppWidgetConfigureActivity.nota = nota;
                ((AppWidgetConfigureActivity)context).finalizar();
            }
        });
    }
    @Override
    public int getItemCount() {
        return notas.size();
    }
}
