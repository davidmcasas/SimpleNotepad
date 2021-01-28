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

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaViewHolder> {

    private Context context;
    private ArrayList<Categoria> categorias;

    public CategoriaAdapter(Context context, ArrayList<Categoria> categorias) {
        this.context = context;
        this.categorias = categorias;
    }
    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_list_layout, parent, false);
        return new CategoriaViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, int position) {
        final Categoria categoria = categorias.get(position);
        holder.nombre.setText(categoria.getNombre());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, EditNotaActivity.class);
                intent.putExtra("nota", nota);
                context.startActivity(intent);*/
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle(categoria.getNombre());
                builder.setMessage("Delete this category?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NeodatisHelper.getInstance(context).borrarCategoria(categoria);
                                ((CategoriasActivity)context).cargarListaCategorias();
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
        return categorias.size();
    }
}
