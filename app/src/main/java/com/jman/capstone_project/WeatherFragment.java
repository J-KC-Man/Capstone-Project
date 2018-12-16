package com.jman.capstone_project;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.viewmodel.PlacesViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private PlacesViewModel placesViewModel;

    TextView cityNameTextView;
    TextView temperatureTextView;
    TextView weatherDescriptionTextView;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        cityNameTextView = rootView.findViewById(R.id.city_name_textView);
        temperatureTextView = rootView.findViewById(R.id.temperature_textView);
        weatherDescriptionTextView = rootView.findViewById(R.id.description_textView);

        // Use ViewModelProviders to associate the ViewModel with the UI controller
        // this is the fragment, it serves as a view controller
        // When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        // When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        placesViewModel = ViewModelProviders.of(this).get(PlacesViewModel.class);

        placesViewModel.getAllPlaces().observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(@Nullable final List<Place> places) {
                // Update the cached copy of the places in the adapter.
                bindData(places.get(0)); // get the first place in places_table
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // new EndpointAsyncTask(this).execute("London, UK");

    }

    public void bindData(Place place) {
        if (place != null) {
            cityNameTextView.setText(place.getCityName() + ", " + place.getCountry());
            temperatureTextView.setText(place.getTemperature());
            weatherDescriptionTextView.setText(place.getWeatherDescription());
        }
    }

}
