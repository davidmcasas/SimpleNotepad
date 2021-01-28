package com.davidmcasas.simplenotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class EditNotaActivity extends AppCompatActivity {

    public static Nota nota;
    private Nota notaa;
    private EditText titulo, contenido;
    private Spinner spinner;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nota);
        this.titulo = findViewById(R.id.editTextTitulo);
        this.contenido = findViewById(R.id.editTextContenido);
        this.spinner = findViewById(R.id.spinner_nota);
        this.editing = false;
        titulo.setFocusableInTouchMode(false);
        contenido.setFocusableInTouchMode(false);
        spinner.setEnabled(false);

        Intent intent = getIntent();
        //this.nota = (Nota) intent.getSerializableExtra("nota");
        if (nota == null) {
            nota = new Nota();
            this.editing = true;
            titulo.setFocusableInTouchMode(true);
            contenido.setFocusableInTouchMode(true);
            spinner.setEnabled(true);
            Button b = findViewById(R.id.button_guardar);
            b.setText("Save");
        } else {
            titulo.setText(nota.getTitulo());
            contenido.setText(nota.getContenido());
        }
        cargarSpinnerCategorias();

        int pos = intent.getIntExtra("categoria_pos", -1);
        if (pos >= 0 && pos < spinner.getCount()) {
            this.spinner.setSelection(pos);
        }

    }

    public void botonEdit(View view) {
        if (editing) {
            nota.setTitulo(titulo.getText().toString());
            nota.setContenido(contenido.getText().toString());
            NeodatisHelper.getInstance(this).guardarNota(nota);
            finish();
        } else {
            this.editing = true;
            titulo.setFocusableInTouchMode(true);
            contenido.setFocusableInTouchMode(true);
            spinner.setEnabled(true);
            Button b = findViewById(R.id.button_guardar);
            b.setText("Save");
        }
    }

    public void cargarSpinnerCategorias() {

        final ArrayList<Categoria> categorias = NeodatisHelper.getInstance(this).getCategorias();
        ArrayList<String> lista = new ArrayList<>();
        lista.add("Uncategorized");
        int position = 0;
        for (Categoria c : categorias) {
            lista.add(c.getNombre());
            if (nota.getCategoria()!= null && nota.getCategoria() == c) {
                position = lista.size()-1;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lista);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    nota.setCategoria(null);
                } else {
                    nota.setCategoria(categorias.get(position-1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(position);

    }
}