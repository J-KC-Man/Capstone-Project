package com.jman.capstone_project.remoteDataSource.models;

public class Weather {

    private String id;

    private String icon;

    private String description;

    private String main;

    public String getId ()
    {
        return id;
    }

    public String getIcon ()
    {
        return icon;
    }

    public String getDescription ()
    {
        return description;
    }

    public String getMain ()
    {
        return main;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", icon = "+icon+", description = "+description+", main = "+main+"]";
    }
}
