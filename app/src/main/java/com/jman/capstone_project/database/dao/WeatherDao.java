package com.jman.capstone_project.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;


import com.jman.capstone_project.database.entities.Weather;


@Dao
public interface WeatherDao {

    @Insert
    void insert(Weather weatherForCity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlace(Weather weatherForCity);
}
