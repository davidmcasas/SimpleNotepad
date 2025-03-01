package com.davidmcasas.simplenotepad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import com.davidmcasas.simplenotepad.data.Categoria;
import com.davidmcasas.simplenotepad.activities.CategoriasActivity;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;
import com.davidmcasas.simplenotepad.R;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle(categoria.getNombre());
                builder.setMessage("Type a new name for this category.");
                final EditText input = new EditText(context);
                input.setText(categoria.getNombre());
                LinearLayout parentLayout = new LinearLayout(view.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(80,20,80,20);
                input.setLayoutParams(lp);
                parentLayout.addView(input);
                builder.setView(parentLayout);

                // Añadimos el botón OK y definimos su funcionalidad
                builder.setPositiveButton("OK"/*android.R.string.ok*/,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                categoria.setNombre(input.getText().toString());
                                NeodatisHelper.getInstance(context).guardarCategoria(categoria);
                                ((CategoriasActivity)context).cargarListaCategorias();
                            }
                        });

                // Mostramos el diálogo
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle(categoria.getNombre());
                builder.setMessage("Delete this category? Notes under this category won't be deleted.");
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
