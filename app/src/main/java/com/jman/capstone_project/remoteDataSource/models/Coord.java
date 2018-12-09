package com.jman.capstone_project.remoteDataSource.models;

public class Coord {

    private String lon;

    private String lat;

    public String getLon ()
    {
        return lon;
    }

    public String getLat ()
    {
        return lat;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lon = "+lon+", lat = "+lat+"]";
    }
}
