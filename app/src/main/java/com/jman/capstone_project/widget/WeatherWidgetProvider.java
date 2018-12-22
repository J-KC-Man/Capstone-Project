package com.jman.capstone_project.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.jman.capstone_project.MainActivity;
import com.jman.capstone_project.R;

import static com.jman.capstone_project.global.Constants.DESCRIPTION_DEFAULT_SHARED_PREF;
import static com.jman.capstone_project.global.Constants.PLACE_NAME_DEFAULT_SHARED_PREF;
import static com.jman.capstone_project.global.Constants.TEMPERATURE_DEFAULT_SHARED_PREF;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews remoteView = getRemoteView(context);

        // Get the recipe name from default shared preferences on the device
        String widgetCityName = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PLACE_NAME_DEFAULT_SHARED_PREF, "");

        String widgetTemperature = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(TEMPERATURE_DEFAULT_SHARED_PREF, "");

        String widgetDescription = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(DESCRIPTION_DEFAULT_SHARED_PREF, "");

        // set recipe name in the widget
        remoteView.setTextViewText(R.id.placename_widget_textView, widgetCityName);
        remoteView.setTextViewText(R.id.temperature_widget_textView, widgetCityName);
        remoteView.setTextViewText(R.id.description_widget_textView, widgetCityName);

        //Trigger data update to handle the GridView widgets and force a data refresh, calls onDataSetChanged in RecipesWidgetRemoteViewsService
        appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetId, R.id.widget_grid_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteView);
    }

    private static RemoteViews getRemoteView(Context context) {
        // create RemoteView object with a GridView layout
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget_provider);
        //connect RemoteView to the RemoteViewsService that will bind the remote view with the adapter
        // by creating an intent pointing to the RecipesWidgetRemoteViewsService
        // then call the setRemoteAdapter linking the GridView with the intent
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intentAdapter = new Intent(context, WeatherWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intentAdapter);
        // Set the MainActivity intent to launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_provider_title, appPendingIntent);
        //views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);

        return views;
    }

    // Cycles through available widgets and updates them
    public static void updateAppWidgets(Context context, AppWidgetManager widgetManager,
                                        int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, widgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

