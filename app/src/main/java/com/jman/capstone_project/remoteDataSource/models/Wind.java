package com.jman.capstone_project.remoteDataSource.models;

public class Wind {

    private String speed;

    private String deg;

    public String getSpeed ()
    {
        return speed;
    }


    public String getDeg ()
    {
        return deg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [speed = "+speed+", deg = "+deg+"]";
    }
}
