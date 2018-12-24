package com.jman.capstone_project.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.remoteDataSource.IAsyncTaskCallback;
import com.jman.capstone_project.repository.Repository;

import java.net.URL;
import java.util.List;

public class PlacesViewModel extends AndroidViewModel implements IAsyncTaskCallback {

    // Add a private member variable to hold a reference to the repository
    private Repository mRepository;

    // Add a private LiveData member variable to cache the list of places
    private LiveData<List<Place>> mAllPlaces;

    // Add a private LiveData member variable to cache the list of places
    private LiveData<Place> mPlaceById;

    // Add a private LiveData member variable to cache the searched cityId
    private MutableLiveData<String> cityId = new MutableLiveData<>();

    // a private LiveData member variable to cache the clicked position on places fragment
    private MutableLiveData<Integer> position = new MutableLiveData<>();

    public LiveData<Integer> getPosition() {
        return position;
    }

    // sets the livedata object when place is selected on PlacesFragment
    public void setPosition(int position) {
        this.position.setValue(new Integer(position));
    }

    public LiveData<String> getCityId() {
        return cityId;
    }

    public PlacesViewModel(@NonNull Application application) {
        super(application);
        // add reference to repository
        mRepository = new Repository(application);
        mAllPlaces = mRepository.getmAllPlaces();
    }

    // A "getter" method for all the places. This completely hides the implementation from the UI
    public LiveData<List<Place>> getAllPlaces() { return mAllPlaces; }

    // A "getter" method for places with cityId. This completely hides the implementation from the UI
    public LiveData<Place> getPlaceById(String cityId) {
        mPlaceById = mRepository.getWeatherInfoForCity(cityId);
        return mPlaceById;
    }

    public LiveData<Place> getDefaultPlace() {
        return mRepository.getDefaultPlace();
    }

    /*
    * delete place
    * */
    public void deletePlace(Place place) {mRepository.deletePlace(place);}

    /*
    * entry point to invoking AsyncTask
    * */
    public void makeApiCall(String params) {

        mRepository.getWeatherForPlace(params, this);
    }

    @Override
    public void onResultReceived(String cityId) {
        this.cityId.setValue(cityId);
    }



//    /*
//    * Get default weather for place
//    * */
//    public LiveData<Place> getWeather() {
//        this.weather = mRepository.getWeather();
//        // this.weather = mRepository.getWeather("2643743");
//        return this.weather;
//    }


}
