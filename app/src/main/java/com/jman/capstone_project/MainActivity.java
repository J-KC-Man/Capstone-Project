package com.jman.capstone_project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.jman.capstone_project.database.entities.Place;


public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener, SearchFragment.CheckConnectivityCallback {

    private final String WEATHER_FRAGMENT_TAG = "weatherfragmenttag";
    private final String PLACES_FRAGMENT_TAG = "placesfragmenttag";
    private final String SEARCH_FRAGMENT_TAG = "searchfragmenttag";

    private String currentFragmentTag;
    //        instantiate fragment
    Fragment fragment = null;

    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // set default selected item to Home
        navigation.setSelectedItemId(R.id.navigation_home);

        // init asynctask object and make the MainActivity the callback calling object
        //new EndpointAsyncTask(this).execute("London, UK");
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        if(savedInstanceState == null){ // nothing was saved
            // first time loading up app, default fragment
            currentFragmentTag = WEATHER_FRAGMENT_TAG;
            loadFragment(new WeatherFragment(), WEATHER_FRAGMENT_TAG);
        } else { // there is stuff saved
            // get the tag from saved bundle
            currentFragmentTag = savedInstanceState.getString("currentFragment");
            switch (currentFragmentTag) {
                case PLACES_FRAGMENT_TAG:
                    fragment = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
                    loadFragment(fragment, currentFragmentTag);
                    break;
                case SEARCH_FRAGMENT_TAG:
                    fragment = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
                    loadFragment(fragment, currentFragmentTag);
                    break;
                case WEATHER_FRAGMENT_TAG:
                    fragment = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
                    loadFragment(fragment, currentFragmentTag);
                    break;
            }
        }
    }

    private boolean loadFragment(Fragment fragment, String tag) {

        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(null)
                    .commit();

            return true;
        }
        // do nothing
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("currentFragment", currentFragmentTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.navigation_search:
                fragment = new SearchFragment();
                currentFragmentTag = SEARCH_FRAGMENT_TAG;
                break;
            case R.id.navigation_home:
                fragment = new WeatherFragment();
                currentFragmentTag = WEATHER_FRAGMENT_TAG;
                break;
            case R.id.navigation_places:
                fragment = new PlacesFragment();
                currentFragmentTag = PLACES_FRAGMENT_TAG;
                break;
        }

        return loadFragment(fragment, currentFragmentTag);
    }

    public void loadWeatherFragment(Place place) {

        WeatherFragment weatherFragment = new WeatherFragment();

        Bundle arguments = new Bundle();

        arguments.putString("cityId", place.getCityId());
        arguments.putString("cityName", place.getCityName());
        arguments.putString("country", place.getCountry());
        arguments.putString("temperature", place.getTemperature());
        arguments.putString("description", place.getWeatherDescription());

        weatherFragment.setArguments(arguments);
        // set correct tag
        currentFragmentTag = WEATHER_FRAGMENT_TAG;
        // set default selected item to Home
        navigation.setSelectedItemId(R.id.navigation_home);
        loadFragment(weatherFragment, currentFragmentTag);
    }

    /*
     * The following method checks for network connection
     * The code in this method is mainly from Stack Overflow
     *
     * source: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     *
     * author: gar
     * profile: https://stackoverflow.com/users/485695/gar
     *
     * */
    @Override
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
