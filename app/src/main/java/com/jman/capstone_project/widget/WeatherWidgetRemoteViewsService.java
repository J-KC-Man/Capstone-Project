package com.jman.capstone_project.widget;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jman.capstone_project.R;

import static com.jman.capstone_project.global.Constants.DESCRIPTION_DEFAULT_SHARED_PREF;
import static com.jman.capstone_project.global.Constants.PLACE_NAME_DEFAULT_SHARED_PREF;
import static com.jman.capstone_project.global.Constants.TEMPERATURE_DEFAULT_SHARED_PREF;

public class WeatherWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

    /**
     * This takes the place of a traditional adapter in a normal Grid layout
     * */
    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private String mCityName;
        private String mTemperature;
        private String mDescription;

        /*
         * Context is needed for instantiating a new factory
         * */
        public GridRemoteViewsFactory(Context applicationContext) {
            this.mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            // get the data from persistent datastore
            mCityName = PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(PLACE_NAME_DEFAULT_SHARED_PREF,null);

            mTemperature = PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(TEMPERATURE_DEFAULT_SHARED_PREF,null);

            mDescription = PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(DESCRIPTION_DEFAULT_SHARED_PREF,null);

        }

        @Override
        public void onDestroy() {
            mCityName = null;
            mTemperature = null;
            mDescription = null;
        }

        @Override
        public int getCount() {
            return 1;
        }

        /*
         * same as the onBindViewHolder method in a normal adapter
         * */
        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.weather_widget_gridview_item);

            views.setTextViewText(R.id.placename_widget_textView, mCityName);
            views.setTextViewText(R.id.temperature_widget_textView, mTemperature);
            views.setTextViewText(R.id.description_widget_textView, mDescription);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
