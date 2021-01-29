package com.davidmcasas.simplenotepad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.davidmcasas.simplenotepad.data.Categoria;
import com.davidmcasas.simplenotepad.data.Nota;
import com.davidmcasas.simplenotepad.R;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;

import java.util.ArrayList;

public class NotaActivity extends AppCompatActivity {

    public static Nota nota;
    private EditText titulo, contenido;
    private Spinner spinner;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        this.titulo = findViewById(R.id.editTextTitulo);
        this.contenido = findViewById(R.id.editTextContenido);
        this.spinner = findViewById(R.id.spinner_nota);

        if (nota == null) {
            nota = new Nota();
            this.editing = true;
            Button b = findViewById(R.id.button_guardar);
            b.setText("Save");
        } else {
            this.editing = false;
            titulo.setText(nota.getTitulo());
            contenido.setText(nota.getContenido());
            titulo.setFocusableInTouchMode(false);
            contenido.setFocusableInTouchMode(false);
            spinner.setEnabled(false);
        }
        cargarSpinnerCategorias();

        int pos = getIntent().getIntExtra("pos", -1);
        if (pos >= 0 && pos < spinner.getCount()) {
            this.spinner.setSelection(pos);
        }
        new EditText(this).requestFocus();
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
        for (Categoria categoria : categorias) {
            lista.add(categoria.getNombre());
            if (nota.getCategoria() != null && nota.getCategoria() == categoria) {
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