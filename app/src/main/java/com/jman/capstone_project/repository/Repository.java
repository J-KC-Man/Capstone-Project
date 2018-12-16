package com.jman.capstone_project.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.jman.capstone_project.BuildConfig;
import com.jman.capstone_project.database.PlacesRoomDatabase;
import com.jman.capstone_project.database.dao.PlaceDao;
import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.remoteDataSource.EndpointAsyncTask;
import com.jman.capstone_project.remoteDataSource.IAsyncTaskCallback;
import com.jman.capstone_project.remoteDataSource.models.WeatherInfoModel;
import com.jman.capstone_project.viewmodel.IViewModelCallback;

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

public class Repository implements IAsyncTaskCallback {

    private PlaceDao mPlaceDao;
    private LiveData<List<Place>> mAllPlaces;
    MutableLiveData<WeatherInfoModel> weatherInfoModelLiveData = new MutableLiveData<>();;

    IViewModelCallback iViewModelCallback; // look at removing this

    public LiveData<WeatherInfoModel> getWeatherInfoModelLiveData() {
        return this.weatherInfoModelLiveData;
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
        new WeatherEndpointAsyncTask(this).execute("London, UK");
    }

    @Override
    public void onResultReceived(WeatherInfoModel weatherInfoModel) {
       // this.weatherInfoModel = weatherInfoModel;
        iViewModelCallback.passToViewModel(weatherInfoModel);
        // pass to viewmodel by interface method?
        // passtovm(weatherInfoModel)
       // return this.weatherInfoModel;
    }

    /**
     * Web client to interface with the API endpoint
     *
     * Params, the type of the parameters sent to the task upon execution.
     * Progress, the type of the progress units published during the background computation.
     * Result, the type of the result of the background computation.
     * */
    public class WeatherEndpointAsyncTask extends AsyncTask<String,String, String> {

        // this needs to be put into the strings file
        String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
        String API_KEY = BuildConfig.ApiKey;

        private IAsyncTaskCallback asyncTaskCallback;

        public WeatherEndpointAsyncTask(IAsyncTaskCallback asyncTaskCallback) {
            this.asyncTaskCallback = asyncTaskCallback;
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            URL url;
            JSONObject topLevel;
            String result = null;

            try {
                // create URL
                url = new URL(BASE_URL + URLEncoder.encode(strings[0], "UTF-8")
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
                }
                // call is not a 200 response code
                else {
                    result = "Call was not successful";
                    return result;
                }

                // add more code here to parse JSON using GSON
                result = topLevel.toString();

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
            Gson gson = new Gson();
            WeatherInfoModel weatherInfoModel = gson.fromJson(result, WeatherInfoModel.class);
            // might need to pass this to a repository class later which implements interface
            //asyncTaskCallback.onResultReceived(weatherInfoModel);

            // put it in LiveData use postValue to post a task to main thread to set the value
            weatherInfoModelLiveData.postValue(weatherInfoModel);
        }
    }

}
