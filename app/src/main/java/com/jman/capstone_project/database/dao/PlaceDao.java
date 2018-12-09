package com.jman.capstone_project.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jman.capstone_project.database.entities.Place;

import java.util.List;

@Dao
public interface PlaceDao {

    @Insert
    void insert(Place place);

    @Query("Delete FROM places_table")
    void deleteAll();

    @Query("SELECT * from places_table")
    LiveData<List<Place>> getAllWords();

    // get one place by city id

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlace(Place place);

    @Delete
    void deletePlace(Place place);
}
