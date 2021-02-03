package com.davidmcasas.simplenotepad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.davidmcasas.simplenotepad.widget.AppWidget;
import com.davidmcasas.simplenotepad.data.Categoria;
import com.davidmcasas.simplenotepad.data.Nota;
import com.davidmcasas.simplenotepad.R;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;

import java.util.ArrayList;

public class NotaActivity extends AppCompatActivity {

    public static Nota nota;
    private EditText titulo, contenido;
    private TextView fecha;
    private Spinner spinner;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        this.titulo = findViewById(R.id.editTextTitulo);
        this.contenido = findViewById(R.id.editTextContenido);
        this.fecha = findViewById(R.id.editTextFecha);
        this.spinner = findViewById(R.id.spinner_nota);

        { // si se accede desde widget
            Intent intent = getIntent();
            int appWidgetId = intent.getIntExtra("appWidgetId", 0);
            if (appWidgetId != 0) {
                nota = NeodatisHelper.getInstance(getApplicationContext()).leerLinkId(appWidgetId);
            }
        }

        if (nota == null) {
            nota = new Nota();
            this.editing = true;
            Button b = findViewById(R.id.button_guardar);
            b.setText("Save");
        } else {
            this.editing = false;
            titulo.setText(nota.getTitulo());
            contenido.setText(nota.getContenido());
            fecha.setText(nota.getFecha());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        actualizarWidgets();
    }

    @Override
    protected void onStop() {
        super.onStop();
        actualizarWidgets();

    }

    @Override
    protected void onPause() {
        super.onPause();
        actualizarWidgets();
    }

    private void actualizarWidgets() {
        Intent intent = new Intent(this, AppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), AppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
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

    // doble click en el botón atrás para salir si se está editando la nota
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce || !editing) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}