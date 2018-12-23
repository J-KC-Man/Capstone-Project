package com.jman.capstone_project.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.jman.capstone_project.BuildConfig;
import com.jman.capstone_project.database.PlacesRoomDatabase;
import com.jman.capstone_project.database.dao.PlaceDao;

import com.jman.capstone_project.database.entities.Place;


import com.jman.capstone_project.remoteDataSource.EndpointAsyncTask;
import com.jman.capstone_project.remoteDataSource.IAsyncTaskCallback;
import com.jman.capstone_project.remoteDataSource.models.WeatherInfoModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static com.jman.capstone_project.global.Constants.API_KEY;
import static com.jman.capstone_project.global.Constants.BASE_URL_TEXT_SEARCH;

public class Repository {

    private PlaceDao mPlaceDao;
    private LiveData<List<Place>> mAllPlaces;
    private LiveData<Place> mDefaultWeatherInfo;
    private LiveData<Place> mWeatherInfoForCity;


    public Repository(Application application) {
        // initialise db instance - the only instance in the app
        PlacesRoomDatabase db = PlacesRoomDatabase.getDatabase(application);

        // init dao
        mPlaceDao = db.placeDao();
        mAllPlaces = mPlaceDao.getAllPlaces();

    }

    /*
    * Get default weather
    * */
    public LiveData<Place> getWeather() {
        this.mDefaultWeatherInfo = mPlaceDao.getDefaultPlace();

        return this.mDefaultWeatherInfo;
    }

    public LiveData<List<Place>> getmAllPlaces() {
        return mAllPlaces;
    }

    public LiveData<Place> getWeatherInfoForCity(String cityId) {
        this.mWeatherInfoForCity = mPlaceDao.findPlaceByCityId(cityId);

        return this.mWeatherInfoForCity;
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

    public void getWeatherForPlace(String string, IAsyncTaskCallback asyncTaskCallback) {
        new WeatherEndpointAsyncTask(mPlaceDao, asyncTaskCallback).execute(string);
    }



    /**
     * Web client to interface with the API endpoint
     *
     * Params, the type of the parameters sent to the task upon execution.
     * Progress, the type of the progress units published during the background computation.
     * Result, the type of the result of the background computation.
     * */
    public class WeatherEndpointAsyncTask extends AsyncTask<String,String, String> {

        private PlaceDao mAsyncTaskDao;
        private IAsyncTaskCallback asyncTaskCallback;

        public WeatherEndpointAsyncTask(PlaceDao dao, IAsyncTaskCallback asyncTaskCallback) {
            mAsyncTaskDao = dao;
            this.asyncTaskCallback = asyncTaskCallback;
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            URL url;
            JSONObject topLevel;
            String result = null;


            try {

                // create URL, strings[0] is the base URL
                url = new URL( strings[0]
                        + "&units=metric"
                        + "&APPID=" + API_KEY);

                // Open connection
                connection = (HttpURLConnection) url.openConnection();


                if(connection.getResponseCode() == 200) {
                    // get inputstream and convert to buffered format so it can be read in
                    InputStream stream = new BufferedInputStream(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();

                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }

                    // this hold all the response
                    topLevel = new JSONObject(builder.toString());

                    // add more code here to parse JSON using GSON
                    result = topLevel.toString();

                    // deseralise Json string here
                    Gson gson = new Gson();
                    WeatherInfoModel weatherInfoModel = gson.fromJson(result, WeatherInfoModel.class);

                    result = weatherInfoModel.getId();
                    // check if info is already in table
                    Place place = new Place(
                            weatherInfoModel.getId(),
                            weatherInfoModel.getName(),
                            weatherInfoModel.getSys().getCountry(),
                            weatherInfoModel.getMain().getTemp(),
                            weatherInfoModel.getWeather(0).getDescription() // get the only object in array
                    );

                    // Check if record is already in DB first before inserting
                    // by matching the id, if not insert, if yes update
                    // insert place into db, any issues will return -1
                    long id = mAsyncTaskDao.insert(place);
                    if (id == -1) {
                        mAsyncTaskDao.updatePlace(place);
                    }

                }
                // call is not a 200 response code
                else {
                   // result = "Call was not successful";
                    return result;
                }



            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            // deseralise Json string here
//            Gson gson = new Gson();
//            WeatherInfoModel weatherInfoModel = gson.fromJson(result, WeatherInfoModel.class);
            // might need to pass this to a repository class later which implements interface
            asyncTaskCallback.onResultReceived(result);

            // put it in LiveData use postValue to post a task to main thread to set the value
           // weatherInfoModelLiveData.postValue(weatherInfoModel);
        }
    }

}
