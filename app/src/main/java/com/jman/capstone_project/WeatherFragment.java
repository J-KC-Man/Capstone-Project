package com.jman.capstone_project;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.viewmodel.PlacesViewModel;
import com.jman.capstone_project.widget.WeatherWidgetIntentService;

import java.util.List;

import static com.jman.capstone_project.global.Constants.DESCRIPTION_DEFAULT_SHARED_PREF;
import static com.jman.capstone_project.global.Constants.PLACE_NAME_DEFAULT_SHARED_PREF;
import static com.jman.capstone_project.global.Constants.TEMPERATURE_DEFAULT_SHARED_PREF;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private PlacesViewModel placesViewModel;

    TextView cityNameTextView;
    TextView temperatureTextView;
    TextView weatherDescriptionTextView;
    FloatingActionButton removePlacefloatingActionButton;
    TextView floatingActionButtonHint;

    private Bundle bundle;

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

    /*
     * Saves selected recipe to default shared prefs
     * */
    private void setRecipeWidgetIngredientsList(String placeName,
                                                String temperature,
                                                String description) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences
                .edit()
                .putString(PLACE_NAME_DEFAULT_SHARED_PREF, placeName)
                .putString(TEMPERATURE_DEFAULT_SHARED_PREF, temperature)
                .putString(DESCRIPTION_DEFAULT_SHARED_PREF, description)
                .apply();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Use ViewModelProviders to associate the ViewModel with the UI controller
        // this is the fragment, it serves as a view controller
        // When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        // When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        placesViewModel = ViewModelProviders.of(getActivity()).get(PlacesViewModel.class);

//        placesViewModel.getPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
////            @Override
////            public void onChanged(@Nullable Integer integer) {
////                if(integer == null) {
////                    return;
////                }
////               Place place = placesViewModel.getAllPlaces().getValue().get(integer.intValue());
////
////            }
////        });

        bundle = getArguments();
        if(bundle == null) {

            // if there are no places
            if(placesViewModel.getAllPlaces().getValue() == null) {
                cityNameTextView.setText("Get started by clicking Search!");
            }
            // get default place
            if(placesViewModel.getCityId().getValue() == null) {
                placesViewModel.getDefaultPlace().observe(getViewLifecycleOwner(), new Observer<Place>() {
                    @Override
                    public void onChanged(@Nullable Place place) {
                        if (place == null) {
                            return;
                        }
                        cityNameTextView.setText(place.getCityName() + ", " + place.getCountry());
                        temperatureTextView.setText(place.getTemperature());
                        weatherDescriptionTextView.setText(place.getWeatherDescription());
                    }
                });
            } else { // If the user searched the location
                placesViewModel.getPlaceById(placesViewModel.getCityId().getValue()).observe(getViewLifecycleOwner(), new Observer<Place>() {
                    @Override
                    public void onChanged(@Nullable Place place) {
                        if (place == null) {
                            return;
                        }
                        cityNameTextView.setText(place.getCityName() + ", " + place.getCountry());
                        temperatureTextView.setText(place.getTemperature());
                        weatherDescriptionTextView.setText(place.getWeatherDescription());

                        // for widget
                        setRecipeWidgetIngredientsList(place.getCityName(), place.getTemperature(), place.getWeatherDescription());
                    }
                });
            }
        } else { // else if the user clicked on one of their saved places
            String cityName = bundle.getString("cityName");
            String country = bundle.getString("country");
            String temperature = bundle.getString("temperature");
            String weatherDescription = bundle.getString("description");
            cityNameTextView.setText(cityName + ", " + country);
            temperatureTextView.setText(temperature);
            weatherDescriptionTextView.setText(weatherDescription);

            // for widget
            setRecipeWidgetIngredientsList(cityName, temperature, weatherDescription);
        }

        /* Update the widget with the place */
        WeatherWidgetIntentService.startActionUpdateWidget(getContext());

        /*
        * Show default place - the first place in the table if there are records in table
        * */

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

//    public void bindData(Place place) {
//        if (place != null) {
//            cityNameTextView.setText(place.getCityName() + ", " + place.getCountry());
//            temperatureTextView.setText(place.getTemperature());
//            weatherDescriptionTextView.setText(place.getWeatherDescription());
//        }
//    }

}
