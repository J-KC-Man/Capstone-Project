package com.jman.capstone_project.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.jman.capstone_project.database.dao.PlaceDao;
import com.jman.capstone_project.database.dao.WeatherDao;
import com.jman.capstone_project.database.entities.Place;
import com.jman.capstone_project.database.entities.Weather;

@Database(entities = {Place.class, Weather.class}, version = 2, exportSchema = false)
public abstract class PlacesRoomDatabase extends RoomDatabase {

    // Define the DAOs that work with the database
    public abstract PlaceDao placeDao();
    public abstract WeatherDao weatherDao();

    //volatile keyword is used to mark a Java variable as "being stored in main memory"
    // guarantees visibility of changes to variables across threads
    private static volatile PlacesRoomDatabase INSTANCE;

    /*
    * To delete all content and repopulate the database whenever the app is started,
    * you create a RoomDatabase.Callback and override onOpen().
    * */
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);

                    // TODO: remove this line eventually
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    // Implements Singleton to only ever get one instance of the DB
    public static PlacesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlacesRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlacesRoomDatabase.class, "places_database")
                            .addCallback(sRoomDatabaseCallback)
                            //.fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PlaceDao mDao;
        private final WeatherDao mWeatherDao;

        PopulateDbAsync(PlacesRoomDatabase db) {
            mDao = db.placeDao();
            mWeatherDao = db.weatherDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Place place = new Place("2643743",
                    "London",
                    "GB");
            mDao.insert(place);

            Weather weather = new Weather(
                    "2643743",
                    "283.69",
                    "overcast clouds" );
            mWeatherDao.insert(weather);

            return null;
        }
    }
}
