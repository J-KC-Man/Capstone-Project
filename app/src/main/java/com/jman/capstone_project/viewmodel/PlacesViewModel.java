package com.jman.capstone_project.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.repository.Repository;

import java.util.List;

public class PlacesViewModel extends AndroidViewModel {

    // Add a private member variable to hold a reference to the repository
    private Repository mRepository;

    // Add a private LiveData member variable to cache the list of places
    private LiveData<List<Place>> mAllPlaces;



    public PlacesViewModel(@NonNull Application application) {
        super(application);
        // add reference to repository
        mRepository = new Repository(application);
        mAllPlaces = mRepository.getmAllPlaces();
    }

    // A "getter" method for all the places. This completely hides the implementation from the UI
    public LiveData<List<Place>> getAllPlaces() { return mAllPlaces; }

//    /*
//    * Get default weather for place
//    * */
//    public LiveData<Place> getWeather() {
//        this.weather = mRepository.getWeather();
//        // this.weather = mRepository.getWeather("2643743");
//        return this.weather;
//    }


}
