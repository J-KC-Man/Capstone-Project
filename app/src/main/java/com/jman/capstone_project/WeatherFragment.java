package com.jman.capstone_project;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.viewmodel.PlacesViewModel;
import com.jman.capstone_project.widget.WeatherWidgetIntentService;


import static com.jman.capstone_project.global.Constants.DESCRIPTION_DEFAULT_SHARED_PREF;
import static com.jman.capstone_project.global.Constants.PLACE_NAME_DEFAULT_SHARED_PREF;
import static com.jman.capstone_project.global.Constants.TEMPERATURE_DEFAULT_SHARED_PREF;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private PlacesViewModel placesViewModel;

    private Unbinder unbinder;

    @Nullable
    @BindView(R.id.city_name_textView) TextView cityNameTextView;
    @Nullable
    @BindView(R.id.temperature_textView) TextView temperatureTextView;
    @Nullable
    @BindView(R.id.description_textView) TextView weatherDescriptionTextView;

    private Bundle bundle;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        unbinder = ButterKnife.bind(this, rootView);


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

        bundle = getArguments();
        if(bundle == null) {

            // if there are no places
            if(placesViewModel.getAllPlaces().getValue() == null) {
                cityNameTextView.setText(R.string.get_started_text);
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
                        temperatureTextView.setText(place.getTemperature() +  " \u2103");
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
                        temperatureTextView.setText(place.getTemperature() + " \u2103");
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
            temperatureTextView.setText(temperature + " \u2103");
            weatherDescriptionTextView.setText(weatherDescription);

            // for widget
            setRecipeWidgetIngredientsList(cityName, temperature, weatherDescription);
        }

        /* Update the widget with the place */
        WeatherWidgetIntentService.startActionUpdateWidget(getContext());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    public void bindData(Place place) {
//        if (place != null) {
//            cityNameTextView.setText(place.getCityName() + ", " + place.getCountry());
//            temperatureTextView.setText(place.getTemperature());
//            weatherDescriptionTextView.setText(place.getWeatherDescription());
//        }
//    }

}
