package com.jman.capstone_project.database;


import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.jman.capstone_project.database.dao.PlaceDao;
import com.jman.capstone_project.database.entities.Place;


@Database(entities = {Place.class}, version = 7, exportSchema = false)
public abstract class PlacesRoomDatabase extends RoomDatabase {

    // Define the DAOs that work with the database
    public abstract PlaceDao placeDao();

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
                            .fallbackToDestructiveMigration() // only needed in db migrations
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PlaceDao mDao;

        PopulateDbAsync(PlacesRoomDatabase db) {
            mDao = db.placeDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //mDao.deleteAll();

//            Place place = new Place("2643743",
//                    "London",
//                    "GB",
//                    "3.69",
//                    "overcast clouds");
//            mDao.insert(place);

            return null;
        }
    }
}
