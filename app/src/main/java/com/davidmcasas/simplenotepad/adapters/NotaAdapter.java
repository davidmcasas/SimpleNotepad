package com.davidmcasas.simplenotepad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import com.davidmcasas.simplenotepad.activities.NotaActivity;
import com.davidmcasas.simplenotepad.activities.MainActivity;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;
import com.davidmcasas.simplenotepad.data.Nota;
import com.davidmcasas.simplenotepad.R;

public class NotaAdapter extends RecyclerView.Adapter<NotaViewHolder> {

    private Context context;
    private ArrayList<Nota> notas;

    public NotaAdapter(Context context, ArrayList<Nota> notas) {
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
                Intent intent = new Intent(context, NotaActivity.class);
                NotaActivity.nota = nota;
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
                                NeodatisHelper.getInstance(context).borrarNota(nota);
                                ((MainActivity)context).cargarSpinnerCategorias(((MainActivity)context).spinner.getSelectedItemPosition());
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
        return notas.size();
    }
}
