package com.example.maurice.menmeindopdr.NSData;

import java.io.Serializable;
import java.util.Date;

public class TreinRit implements Serializable
{                                   // leg/
    private String startStation;    //---> origin/"name"
    private String endStation;      //---> destination/"name"
    private Date departureTime;     //---> origin/"plannedDateTime"
    private Date arrivalTime;       //---> destination/"plannedDateTime"
    private String departureTrack;  //---> origin/"plannedTrack"
    private String arrivalTrack;    //---> destination/"plannedTrack"
    private String ritDuration;     //---> zelf uitrekenen
    private String crowdness;       //--->"crowdForecast"
    private TreinType type;

    public TreinRit(TreinType type, String crowdness, String startStation, String endStation, Date departureTime, Date arrivalTime, String departureTrack, String arrivalTrack, String ritDuration)
    {
        this.type = type;
        this.startStation = startStation;
        this.endStation = endStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureTrack = departureTrack;
        this.arrivalTrack = arrivalTrack;
        this.ritDuration = ritDuration;
        this.crowdness = crowdness;
    }

    public String getStartStation()
    {
        return startStation;
    }

    public String getEndStation()
    {
        return endStation;
    }

    public Date getDepartureTime()
    {
        return departureTime;
    }

    public Date getArrivalTime()
    {
        return arrivalTime;
    }

    public String getDepartureTrack()
    {
        return departureTrack;
    }

    public String getArrivalTrack()
    {
        return arrivalTrack;
    }

    public String getRitDuration()
    {
        return ritDuration;
    }

    public String getCrowdness()
    {
        return crowdness;
    }

    public TreinType getType()
    {
        return type;
    }

    public int getDepHours()
    {
        return this.departureTime.getHours();
    }
    public int getDepMinutes()
    {
        return this.departureTime.getMinutes();
    }

    public int getArrHours()
    {
        return this.arrivalTime.getHours();
    }
    public int getArrMinutes()
    {
        return this. arrivalTime.getMinutes();
    }
}
