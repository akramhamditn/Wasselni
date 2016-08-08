package com.esprit.wasselni;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class SimpleWidgetProvider extends AppWidgetProvider {
    static int number1 ;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
        query.whereEqualTo("Type", "Chauffeur");
        query.whereEqualTo("Etat", "Connecter");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> clientList, ParseException e) {

                number1 = clientList.size();
                Log.e("number", String.valueOf(number1));
            }
        });
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            Log.e("number", String.valueOf(number1));
            String number = String.format("%03d", number1);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget);
            remoteViews.setTextViewText(R.id.textView, number);

            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }



    }
}
