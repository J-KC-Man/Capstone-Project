package com.jman.capstone_project.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.jman.capstone_project.database.PlacesRoomDatabase;
import com.jman.capstone_project.database.dao.PlaceDao;
import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.remoteDataSource.EndpointAsyncTask;
import com.jman.capstone_project.remoteDataSource.IAsyncTaskCallback;
import com.jman.capstone_project.remoteDataSource.models.WeatherInfoModel;
import com.jman.capstone_project.viewmodel.IViewModelCallback;

import java.util.List;

public class Repository implements IAsyncTaskCallback {

    private PlaceDao mPlaceDao;
    private LiveData<List<Place>> mAllPlaces;
    private WeatherInfoModel weatherInfoModel;

    IViewModelCallback iViewModelCallback;

    public WeatherInfoModel getWeatherInfoModel() {
        return this.weatherInfoModel;
    }

    public Repository(Application application, IViewModelCallback iViewModelCallback) {
        // initialise db instance - the only instance in the app
        PlacesRoomDatabase db = PlacesRoomDatabase.getDatabase(application);

        this.iViewModelCallback = iViewModelCallback;

        // init dao
        mPlaceDao = db.placeDao();
        mAllPlaces = mPlaceDao.getAllPlaces();
    }

    public LiveData<List<Place>> getmAllPlaces() {
        return mAllPlaces;
    }

    public void insert (Place place) {
        new insertAsyncTask(mPlaceDao).execute(place);
    }

    // create AsyncTask as you cannot do Room database operations on the UI thread
    private static class insertAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao mAsyncTaskDao;

        insertAsyncTask(PlaceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Place... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void getWeatherForCity() {
        new EndpointAsyncTask(this).execute("London, UK");
    }

    @Override
    public void onResultReceived(WeatherInfoModel weatherInfoModel) {
       // this.weatherInfoModel = weatherInfoModel;
        iViewModelCallback.passToViewModel(weatherInfoModel);
        // pass to viewmodel by interface method?
        // passtovm(weatherInfoModel)
       // return this.weatherInfoModel;
    }

}
