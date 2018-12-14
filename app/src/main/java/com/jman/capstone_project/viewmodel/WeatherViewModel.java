package com.jman.capstone_project.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.remoteDataSource.models.WeatherInfoModel;
import com.jman.capstone_project.repository.Repository;

public class WeatherViewModel extends AndroidViewModel implements IViewModelCallback {

    // Add a private member variable to hold a reference to the repository
    private Repository mRepository;

    // reference to WeatherModelInfo object
    private WeatherInfoModel weatherInfoModel;

    public WeatherInfoModel getWeatherInfoModel() {
        return this.weatherInfoModel;
    }

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        // add reference to repository
        this.mRepository = new Repository(application, this);
    }

    // A "getter" method for the weather info. This completely hides the implementation from the UI
    // this method will be used in the WeatherFragment to access the model info
    public void makeApiCall() {

        // repo executes AsyncTask and assigns populated model to this weatherInfoModel
        mRepository.getWeatherForCity();

        // get weather info from repo
       // this.weatherInfoModel = mRepository.getWeatherInfoModel();
        //return this.weatherInfoModel;
    }

    /*
     * Create a wrapper insert() method that calls the Repository's insert() method.
     * In this way, the implementation of insert() is completely hidden from the UI
     * */
    public void insert(Place place) { mRepository.insert(place); }

    @Override
    public void passToViewModel(WeatherInfoModel weatherInfoModel) {
        this.weatherInfoModel = weatherInfoModel;


    }
}
