package com.davidmcasas.simplenotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static NeodatisHelper neodatis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        neodatis = NeodatisHelper.getInstance(this);

        //neodatis.guardarNota(new Nota("Titulillo", "Contenidillo"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarListaNotas();
    }

    private void cargarListaNotas() {
        RecyclerView notasView = findViewById(R.id.listaNotas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notasView.setLayoutManager(linearLayoutManager);
        notasView.setHasFixedSize(true);

        ArrayList<Nota> notas = neodatis.getNotas();
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
        Intent intent = new Intent(this, EditNotaActivity.class);
        intent.putExtra("nota", new Nota());
        this.startActivity(intent);
    }
}