package com.jman.capstone_project.remoteDataSource.models;

public class WeatherInfoModel {

    private String id;

    private String dt;

    private Clouds clouds;

    private Coord coord;

    private Wind wind;

    private String cod;

    private String visibility;

    private Sys sys;

    private String name;

    private String base;

    private Weather[] weather;

    private Main main;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDt ()
    {
        return dt;
    }

    public Clouds getClouds ()
    {
        return clouds;
    }

    public Coord getCoord ()
    {
        return coord;
    }


    public Wind getWind ()
    {
        return wind;
    }

    public String getCod ()
    {
        return cod;
    }


    public String getVisibility ()
    {
        return visibility;
    }


    public Sys getSys ()
    {
        return sys;
    }


    public String getName ()
    {
        return name;
    }

    public String getBase ()
    {
        return base;
    }

    public void setBase (String base)
    {
        this.base = base;
    }

    public Weather[] getWeather ()
    {
        return weather;
    }

    public Main getMain ()
    {
        return main;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", dt = "+dt+", clouds = "+clouds+", coord = "+coord+", wind = "+wind+", cod = "+cod+", visibility = "+visibility+", sys = "+sys+", name = "+name+", base = "+base+", weather = "+weather+", main = "+main+"]";
    }
}
