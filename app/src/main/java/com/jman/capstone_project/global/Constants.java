package com.jman.capstone_project.global;

import com.jman.capstone_project.BuildConfig;

public class Constants {

    public static final String BASE_URL_TEXT_SEARCH = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static final String BASE_URL_LOCATION_SEARCH = "https://api.openweathermap.org/data/2.5/weather?lat=";
    public static final String BASE_URL_LOCATION_SEARCH_LON = "&lon=";
    public static final String API_KEY = BuildConfig.ApiKey;

    // To save data in default shared prefs for widget
    public static final String PLACE_NAME_DEFAULT_SHARED_PREF = "place_name";
    public static final String TEMPERATURE_DEFAULT_SHARED_PREF = "temperature";
    public static final String DESCRIPTION_DEFAULT_SHARED_PREF = "description";

    // For Intent service
    public static final String ACTION_WIDGET_SET_WEATHER = "com.jman.capstone_project.action.update_widget";
}
