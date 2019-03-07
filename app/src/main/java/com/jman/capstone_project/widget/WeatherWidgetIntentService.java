package com.jman.capstone_project.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;

import static com.jman.capstone_project.global.Constants.ACTION_WIDGET_SET_WEATHER;

public class WeatherWidgetIntentService extends IntentService {


    public WeatherWidgetIntentService() {
        super("WeatherWidgetIntentService");
    }

    /* Updates any and all widgets */
    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, WeatherWidgetIntentService.class);
        intent.setAction(ACTION_WIDGET_SET_WEATHER);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_WIDGET_SET_WEATHER.equals(action)){
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WeatherWidgetProvider.class));


        //Now update all widgets
        WeatherWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds);
    }
}
