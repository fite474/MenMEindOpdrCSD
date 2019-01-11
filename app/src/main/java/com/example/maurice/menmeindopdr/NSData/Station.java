package com.example.maurice.menmeindopdr.NSData;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Station implements Serializable
{
    private String code;
    private StationType type;
    private String naam;
    private String country;
    private String uicCode;
    private double latitude;
    private double longitude;
    private LatLng coordinate;

    public Station(String code, StationType type, String naam, String country, String uicCode, double latitude, double longitude)
    {
        this.code = code;
        this.type = type;
        this.naam = naam;
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

    public String getName()
    {
        return naam;
    }

    public void setName(String name)
    {
        this.naam = name;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getUicCode()
    {
        return uicCode;
    }

    public void setUicCode(String uicCode)
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
