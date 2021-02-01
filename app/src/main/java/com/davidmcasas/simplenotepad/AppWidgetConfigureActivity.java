package com.davidmcasas.simplenotepad;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidmcasas.simplenotepad.adapters.NotaAdapter;
import com.davidmcasas.simplenotepad.adapters.NotaAdapterWidget;
import com.davidmcasas.simplenotepad.data.Categoria;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;
import com.davidmcasas.simplenotepad.data.Nota;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link AppWidget AppWidget} AppWidget.
 */
public class AppWidgetConfigureActivity extends Activity {

    public static Nota nota = null;

    private static final String PREFS_NAME = "com.davidmcasas.simplenotepad.AppWidget";
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

