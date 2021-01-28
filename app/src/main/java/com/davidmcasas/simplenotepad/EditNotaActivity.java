package com.davidmcasas.simplenotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class EditNotaActivity extends AppCompatActivity {

    private Nota nota;
    private EditText titulo, contenido;
    private boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nota);

        Intent intent = getIntent();
        this.nota = (Nota) intent.getSerializableExtra("nota");

        if (nota == null) {
            finish();
        }

        this.editing = false;
        titulo = findViewById(R.id.editTextTitulo);
        contenido = findViewById(R.id.editTextContenido);
        titulo.setText(nota.getTitulo());
        contenido.setText(nota.getContenido());
        titulo.setEnabled(false);
        contenido.setEnabled(false);

    }

    public void botonEdit() {
        this.editing = true;
        titulo.setEnabled(true);
        contenido.setEnabled(true);
    }
}