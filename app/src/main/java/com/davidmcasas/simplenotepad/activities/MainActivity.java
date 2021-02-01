package com.davidmcasas.simplenotepad.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.davidmcasas.simplenotepad.data.Categoria;
import com.davidmcasas.simplenotepad.data.Nota;
import com.davidmcasas.simplenotepad.R;
import com.davidmcasas.simplenotepad.adapters.NotaAdapter;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NeodatisHelper neodatis;
    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        neodatis = NeodatisHelper.getInstance(this);

        cargarListaNotas();
        cargarSpinnerCategorias(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarSpinnerCategorias(spinner.getSelectedItemPosition());
    }

    public void cargarSpinnerCategorias(int selection) {

        final ArrayList<Categoria> categorias = NeodatisHelper.getInstance(this).getCategorias();
        ArrayList<String> lista = new ArrayList<>();
        lista.add("All Notes");
        for (Categoria c : categorias) {
            lista.add(c.getNombre());
        }
        lista.add("Uncategorized");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    cargarListaNotas();
                } else if (position == categorias.size() + 1) {
                    cargarListaNotas(null, true);
                } else {
                    cargarListaNotas(categorias.get(position - 1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (selection >= 0 && selection < spinner.getCount()) {
            try {
                spinner.setSelection(selection);
            } catch (Exception e) {
                spinner.setSelection(0);
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //neodatis.terminate();
    }

    private void cargarListaNotas() {
        cargarListaNotas(null);
    }

    private void cargarListaNotas(Categoria categoria) {
        cargarListaNotas(categoria, false);
    }

    private void cargarListaNotas(Categoria categoria, boolean onlyNullCategory) {
        RecyclerView notasView = findViewById(R.id.listaNotas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notasView.setLayoutManager(linearLayoutManager);
        notasView.setHasFixedSize(true);

        ArrayList<Nota> notas = neodatis.getNotas(categoria, onlyNullCategory);
        if (notas.size() > 0) {
            notasView.setVisibility(View.VISIBLE);
            NotaAdapter mAdapter = new NotaAdapter(this, notas);
            notasView.setAdapter(mAdapter);
        }
        else {
            notasView.setVisibility(View.GONE);
        }
    }

    public void botonNuevaNota(View view) {
        Intent intent = new Intent(this, NotaActivity.class);
        intent.putExtra("pos", spinner.getSelectedItemPosition());
        NotaActivity.nota = null;
        this.startActivity(intent);
    }

    public void botonEditarCategorias(View view) {
        Intent intent = new Intent(this, CategoriasActivity.class);
        this.startActivity(intent);
    }
}