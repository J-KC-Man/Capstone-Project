package com.jman.capstone_project.remoteDataSource.models;

public class Main {

    private String humidity;

    private String pressure;

    private String temp_max;

    private String temp_min;

    private String temp;

    public String getHumidity ()
    {
        return humidity;
    }

    public String getPressure ()
    {
        return pressure;
    }

    public String getTemp_max ()
    {
        return temp_max;
    }

    public String getTemp_min ()
    {
        return temp_min;
    }


    public String getTemp ()
    {
        return temp;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [humidity = "+humidity+", pressure = "+pressure+", temp_max = "+temp_max+", temp_min = "+temp_min+", temp = "+temp+"]";
    }
}
