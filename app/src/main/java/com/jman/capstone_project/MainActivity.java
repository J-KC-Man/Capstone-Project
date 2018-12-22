package com.jman.capstone_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.remoteDataSource.EndpointAsyncTask;
import com.jman.capstone_project.remoteDataSource.IAsyncTaskCallback;
import com.jman.capstone_project.remoteDataSource.models.WeatherInfoModel;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // set default selected item to Home
        navigation.setSelectedItemId(R.id.navigation_home);

        // init asynctask object and make the MainActivity the callback calling object
        //new EndpointAsyncTask(this).execute("London, UK");
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        loadFragment(new WeatherFragment());
    }

    private boolean loadFragment(Fragment fragment) {

        // is there a fragment already loaded?
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

//        instantiate fragment
        Fragment fragment = null;

        switch(menuItem.getItemId()) {
            case R.id.navigation_search:
                fragment = new SearchFragment();
                break;
            case R.id.navigation_home:
                fragment = new WeatherFragment();
                break;
            case R.id.navigation_places:
                fragment = new PlacesFragment();
                break;
        }

        return loadFragment(fragment);
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

        loadFragment(weatherFragment);
    }

}
