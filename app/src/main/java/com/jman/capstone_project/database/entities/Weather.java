package com.jman.capstone_project.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "weather_table",
        foreignKeys = @ForeignKey(entity = Place.class,
                parentColumns = "city_id",
                childColumns = "city_id",
                onDelete = CASCADE))

public class Weather {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "city_id")
    private String mCityId;

    @NonNull
    @ColumnInfo(name = "temperature")
    private String mTemperature;

    @NonNull
    @ColumnInfo(name = "description")
    private String mWeatherDescription;

    public Weather(@NonNull String mCityId,
                   @NonNull String mTemperature,
                   @NonNull String mWeatherDescription) {
        this.mCityId = mCityId;
        this.mTemperature = mTemperature;
        this.mWeatherDescription = mWeatherDescription;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    @NonNull
    public String getCityId() {
        return mCityId;
    }

    @NonNull
    public String getTemperature() {
        return mTemperature;
    }

    @NonNull
    public String getWeatherDescription() {
        return mWeatherDescription;
    }
}
