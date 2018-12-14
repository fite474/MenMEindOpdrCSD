package com.example.maurice.menmeindopdr.NSData;

import com.google.android.gms.maps.model.LatLng;

public class Station
{
    private String code;
    private StationType type;
    private String[] namen;
    private String country;
    private int uicCode;
    private double latitude;
    private double longitude;
    private LatLng coordinate;

    public Station(String code, StationType type, String[] namen, String country, int uicCode, double latitude, double longitude)
    {
        this.code = code;
        this.type = type;
        this.namen = namen;
        this.country = country;
        this.uicCode = uicCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public StationType getType()
    {
        return type;
    }

    public void setType(StationType type)
    {
        this.type = type;
    }

    public String[] getNames()
    {
        return namen;
    }

    public void setNames(String[] namen)
    {
        this.namen = namen;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public int getUicCode()
    {
        return uicCode;
    }

    public void setUicCode(int uicCode)
    {
        this.uicCode = uicCode;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
}
