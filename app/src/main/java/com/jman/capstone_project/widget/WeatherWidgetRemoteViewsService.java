package com.jman.capstone_project.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jman.capstone_project.R;

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

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 1;
        }

        /*
         * same as the onBindViewHolder method in a noraml adapter
         * */
        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.weather_widget_gridview_item);

            views.setTextViewText(R.id.placename_widget_textView, "testplace");
            views.setTextViewText(R.id.temperature_widget_textView, "8C");
            views.setTextViewText(R.id.description_widget_textView, "few clouds");


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
