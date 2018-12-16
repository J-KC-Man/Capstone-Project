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



    public Place(@NonNull String mCityId,
                 @NonNull String mCityName,
                 @NonNull String mCountry) {
        this.mCityId = mCityId;
        this.mCityName = mCityName;
        this.mCountry = mCountry;

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

}
