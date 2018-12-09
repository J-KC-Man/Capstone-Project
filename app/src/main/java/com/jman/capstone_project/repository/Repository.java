package com.jman.capstone_project.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.jman.capstone_project.database.PlacesRoomDatabase;
import com.jman.capstone_project.database.dao.PlaceDao;
import com.jman.capstone_project.database.entities.Place;

import java.util.List;

public class Repository {

    private PlaceDao mPlaceDao;
    private LiveData<List<Place>> mAllPlaces;

    public Repository(Application application) {
        // initialise db instance - the only instance in the app
        PlacesRoomDatabase db = PlacesRoomDatabase.getDatabase(application);

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

}
