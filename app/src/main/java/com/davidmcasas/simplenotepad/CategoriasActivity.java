package com.davidmcasas.simplenotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cargarListaCategorias();
    }

    public void cargarListaCategorias() {
        RecyclerView categoriasView = findViewById(R.id.listaCategorias);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        categoriasView.setLayoutManager(linearLayoutManager);
        categoriasView.setHasFixedSize(true);

        ArrayList<Categoria> categorias = NeodatisHelper.getInstance(this).getCategorias();
        if (categorias.size() > 0) {
            categoriasView.setVisibility(View.VISIBLE);
            CategoriaAdapter mAdapter = new CategoriaAdapter(this, categorias);
            categoriasView.setAdapter(mAdapter);
        }
        else {
            categoriasView.setVisibility(View.GONE);
        }
    }

    public void botonNuevaCategoria(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("New Category");
        builder.setMessage("Type a name for this category.");
        final EditText input = new EditText(this);
        LinearLayout parentLayout = new LinearLayout(view.getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(80,20,80,20);
        input.setLayoutParams(lp);
        parentLayout.addView(input);
        builder.setView(parentLayout);

        final Context context = this;

        // Añadimos el botón OK y definimos su funcionalidad
        builder.setPositiveButton("OK"/*android.R.string.ok*/,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Categoria categoria = new Categoria(input.getText().toString());
                        NeodatisHelper.getInstance(context).guardarCategoria(categoria);
                        cargarListaCategorias();
                    }
                });

        // Mostramos el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}