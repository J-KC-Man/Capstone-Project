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
    public LiveData<List<Place>> getAllWords() { return mAllPlaces; }

    // todo: I may need to move the insert method into another viewmodel called WeatherViewModel
    /*
     * Create a wrapper insert() method that calls the Repository's insert() method.
     * In this way, the implementation of insert() is completely hidden from the UI
     * */
    public void insert(Place place) { mRepository.insert(place); }
}
