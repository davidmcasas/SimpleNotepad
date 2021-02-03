package com.davidmcasas.simplenotepad.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidmcasas.simplenotepad.R;
import com.davidmcasas.simplenotepad.adapters.NotaAdapterWidget;
import com.davidmcasas.simplenotepad.data.Categoria;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;
import com.davidmcasas.simplenotepad.data.Nota;
import com.davidmcasas.simplenotepad.data.WidgetLink;
import com.davidmcasas.simplenotepad.widget.AppWidget;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link AppWidget AppWidget} AppWidget.
 */
public class AppWidgetConfigureActivity extends Activity {

    public static Nota nota = null;
    Spinner spinner = null;

    private static final String PREFS_NAME = "com.davidmcasas.simplenotepad.widget.AppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    //EditText mAppWidgetText;
    /*View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = AppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            //String widgetText = mAppWidgetText.getText().toString();
            //saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            AppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };*/

    public void finalizar() {

        if (nota == null) return;

        WidgetLink link = new WidgetLink(mAppWidgetId, nota);
        NeodatisHelper.getInstance(getApplicationContext()).guardarLink(link);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        AppWidget.updateAppWidget(this, appWidgetManager, mAppWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);

        /*
        SharedPreferences.Editor prefs = this.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString("titulo", nota.getTitulo());
        prefs.putString("contenido", nota.getTitulo());
        //prefs.putString("titulo" + mAppWidgetId, nota.getTitulo());
        //prefs.putString("contenido" + mAppWidgetId, nota.getTitulo());
        prefs.apply();
        */


        finish();
    }

    public void cargarSpinnerCategorias() {

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
                    cargarListaNotas(null, false);
                } else if (position == categorias.size() + 1) {
                    cargarListaNotas(null, true);
                } else {
                    cargarListaNotas(categorias.get(position - 1), false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public AppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    /*static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }*/

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    /*static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }*/

    /*static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }*/

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.app_widget_configure);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        spinner = findViewById(R.id.spinner_widget);
        cargarSpinnerCategorias();
        cargarListaNotas(null, false);
    }

    private void cargarListaNotas(Categoria categoria, boolean onlyNullCategory) {
        RecyclerView notasView = findViewById(R.id.listaNotasConfigure);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        notasView.setLayoutManager(linearLayoutManager);
        notasView.setHasFixedSize(true);

        ArrayList<Nota> notas = NeodatisHelper.getInstance(this).getNotas(categoria, onlyNullCategory);
        if (notas.size() > 0) {
            notasView.setVisibility(View.VISIBLE);
            NotaAdapterWidget mAdapter = new NotaAdapterWidget(this, notas);
            notasView.setAdapter(mAdapter);
        }
        else {
            notasView.setVisibility(View.GONE);
        }

    }
}

