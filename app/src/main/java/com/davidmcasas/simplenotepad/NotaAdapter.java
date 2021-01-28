package com.davidmcasas.simplenotepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotaAdapter extends RecyclerView.Adapter<NotaViewHolder> {

    private Context context;
    private ArrayList<Nota> marcadores;

    public NotaAdapter(Context context, ArrayList<Nota> marcadores) {
        this.context = context;
        this.marcadores = marcadores;
    }
    @Override
    public NotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nota_list_layout, parent, false);
        return new NotaViewHolder(view);
    }
    @Override
    public void onBindViewHolder(NotaViewHolder holder, int position) {
        final Nota nota = marcadores.get(position);
        holder.titulo.setText(nota.getTitulo());
        holder.contenido.setText(nota.getContenido());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditNotaActivity.class);
                intent.putExtra("nota", nota);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle(nota.getTitulo());
                builder.setMessage("Delete this note?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO borrar nota
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // no hacer nada
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return marcadores.size();
    }
}
