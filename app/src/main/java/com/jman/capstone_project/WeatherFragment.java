package com.jman.capstone_project;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use ViewModelProviders to associate the ViewModel with the UI controller
        // this is the fragment, it serves as a view controller
        // When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        // When the activity is re-created, the ViewModelProviders return the existing ViewModel.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        cityNameTextView = rootView.findViewById(R.id.city_name_textView);
        temperatureTextView = rootView.findViewById(R.id.temperature_textView);
        weatherDescriptionTextView = rootView.findViewById(R.id.description_textView);



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Use ViewModelProviders to associate the ViewModel with the UI controller
        // this is the fragment, it serves as a view controller
        // When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        // When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        placesViewModel = ViewModelProviders.of(getActivity()).get(PlacesViewModel.class);

        placesViewModel.getPlaceById(placesViewModel.getCityId().getValue()).observe(getViewLifecycleOwner(), new Observer<Place>() {
                    @Override
                    public void onChanged(@Nullable Place place) {
                        if(place == null) {
                            return;
                        }
                        cityNameTextView.setText(place.getCityName() + ", " + place.getCountry());
                        temperatureTextView.setText(place.getTemperature());
                        weatherDescriptionTextView.setText(place.getWeatherDescription());
                    }
                });

//        placesViewModel.getCityId().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String cityId) {
//                if(cityId == null) {
//                    return;
//                }
//                // get the record from the db
//                Log.d("Get the cityId", "CityId is " + cityId);
//               // cityNameTextView.setText(cityId);
//               // placesViewModel.getPlaceById(cityId);
//            //    cityNameTextView.setText(placesViewModel.getPlaceById(cityId).getValue().getCityName());
//
//            }
//        });
    }

    public void bindData(Place place) {
        if (place != null) {
            cityNameTextView.setText(place.getCityName() + ", " + place.getCountry());
            temperatureTextView.setText(place.getTemperature());
            weatherDescriptionTextView.setText(place.getWeatherDescription());
        }
    }

}
