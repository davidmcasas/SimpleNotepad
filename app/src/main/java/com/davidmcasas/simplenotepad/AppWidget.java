package com.davidmcasas.simplenotepad;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.davidmcasas.simplenotepad.activities.NotaActivity;
import com.davidmcasas.simplenotepad.data.NeodatisHelper;
import com.davidmcasas.simplenotepad.data.Nota;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetConfigureActivity AppWidgetConfigureActivity}
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        try {
            Nota nota = NeodatisHelper.getInstance(context).leerLinkId(appWidgetId);
            if (nota == null) {
                views.setTextViewText(R.id.editTextTitulo_widget, "(Deleted)");
                views.setTextViewText(R.id.editTextContenido_widget, "");
            } else {
                views.setTextViewText(R.id.editTextTitulo_widget, nota.getTitulo());
                views.setTextViewText(R.id.editTextContenido_widget, nota.getContenido());
            }
        } catch (Exception e) {
            views.setTextViewText(R.id.editTextTitulo_widget, "(Error)");
            views.setTextViewText(R.id.editTextContenido_widget, "");
        }
        Intent intent = new Intent(context, NotaActivity.class);
        intent.putExtra("appWidgetId", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            NeodatisHelper.getInstance(context).borrarLink(appWidgetId);
            //AppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

