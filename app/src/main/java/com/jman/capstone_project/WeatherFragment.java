package com.jman.capstone_project;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jman.capstone_project.remoteDataSource.EndpointAsyncTask;
import com.jman.capstone_project.remoteDataSource.IAsyncTaskCallback;
import com.jman.capstone_project.remoteDataSource.models.WeatherInfoModel;
import com.jman.capstone_project.repository.Repository;
import com.jman.capstone_project.viewmodel.WeatherViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private WeatherInfoModel weatherInfoModel;

    TextView cityNameTextView;
    TextView temperatureTextView;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        // Use ViewModelProviders to associate the ViewModel with the UI controller
        // this is the fragment, it serves as a view controller
        // When the activity is destroyed, for example through a configuration change, the ViewModel persists.
        // When the activity is re-created, the ViewModelProviders return the existing ViewModel.
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        cityNameTextView = rootView.findViewById(R.id.city_name_textView);
        temperatureTextView = rootView.findViewById(R.id.temperature_textView);



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // new EndpointAsyncTask(this).execute("London, UK");
       // weatherInfoModel = weatherViewModel.getWeatherInfoModel();
        weatherViewModel.makeApiCall();
        weatherInfoModel = weatherViewModel.getWeatherInfoModel();
        bindData();
    }

    public void bindData() {
        if (this.weatherInfoModel != null) {
            cityNameTextView.setText(this.weatherInfoModel.getName());
            temperatureTextView.setText(this.weatherInfoModel.getMain().getTemp());
        }
    }

//    @Override
//    public void onResultReceived(WeatherInfoModel weatherInfoModel) {
//       // this.weatherInfoModel = weatherInfoModel;
//        //bindData();
//    }
}
