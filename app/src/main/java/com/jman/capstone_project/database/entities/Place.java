package com.jman.capstone_project.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "places_table")
public class Place {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "city_id")
    private String mCityId;

    @NonNull
    @ColumnInfo(name = "city_name")
    private String mCityName;

    @NonNull
    @ColumnInfo(name = "country")
    private String mCountry;

    @NonNull
    @ColumnInfo(name = "temperature")
    private String mTemperature;

    @NonNull
    @ColumnInfo(name = "description")
    private String mWeatherDescription;

    public Place(@NonNull String mCityId,
                 @NonNull String mCityName,
                 @NonNull String mCountry,
                 @NonNull String mTemperature,
                 @NonNull String mWeatherDescription) {
        this.mCityId = mCityId;
        this.mCityName = mCityName;
        this.mCountry = mCountry;
        this.mTemperature = mTemperature;
        this.mWeatherDescription = mWeatherDescription;
    }

    public String getCityId() {
        return mCityId;
    }

    public String getCityName() {
        return mCityName;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public String getWeatherDescription() {
        return mWeatherDescription;
    }
}
