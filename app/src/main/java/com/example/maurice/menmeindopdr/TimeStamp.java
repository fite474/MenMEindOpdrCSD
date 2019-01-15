package com.example.maurice.menmeindopdr;

import java.io.Serializable;

public class TimeStamp implements Serializable
{
    public int hours;
    public int minutes;


    public TimeStamp(int hours, int minutes)
    {
        this.hours = hours;
        this.minutes = minutes;
    }

    public TimeStamp(int minutes)
    {
        this.hours = minutes / 60;
        this.minutes = minutes - (hours * 60);
    }

    public int getTotalMinutes()
    {
        int totalMinutes = 0;
        totalMinutes += hours * 60;
        totalMinutes += minutes;
        return totalMinutes;
    }

    public int getHours()
    {
        return hours;
    }

    public void setHours(int hours)
    {
        this.hours = hours;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public void setMinutes(int minutes)
    {
        this.minutes = minutes;
    }


    @Override
    public String toString()
    {
//        return hours + ":" + minutes;
        if(hours < 10 && minutes < 10)
        {
            return "0" + hours + ":0" + minutes;
        }
        else if(hours < 10)
        {
            return "0" + hours + ":" + minutes;
        }
        else if(minutes < 10)
        {
            return hours + ":0" + minutes;
        }
        else
        {
            return hours + ":" + minutes;
        }

    }
}
