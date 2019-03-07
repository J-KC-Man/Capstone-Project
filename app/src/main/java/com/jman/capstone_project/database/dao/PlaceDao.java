package com.jman.capstone_project.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jman.capstone_project.database.entities.Place;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * from places_table LIMIT 1")
    LiveData<Place> getDefaultPlace();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Place place);

    @Query("Delete FROM places_table")
    void deleteAll();

    @Delete
    void deletePlace(Place place);

    @Query("SELECT * from places_table")
    LiveData<List<Place>> getAllPlaces();

    // get one place by city id
   @Query("SELECT * from places_table WHERE city_id=:cityId")
    //@Query("SELECT * from places_table ORDER BY city_id DESC LIMIT 1")
    LiveData<Place> findPlaceByCityId(String cityId);

    // for updating weather - you need to get the right place to update before calling this method
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlace(Place place);

}
