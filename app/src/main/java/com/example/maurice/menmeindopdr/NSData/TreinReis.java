package com.example.maurice.menmeindopdr.NSData;

import com.example.maurice.menmeindopdr.TimeStamp;

import java.io.Serializable;
import java.util.ArrayList;

public class TreinReis implements Serializable
{
    private int ritDuur;
    private int aantalOverstappen;
    private TimeStamp vertrektijd;
    private TimeStamp aankomsttijd;
    private TreinType eersteTreinType;
    private String vertrekSpoor;
    private String vertrekStation;
    private String aankomstStation;
    private String firstDestination; //TODO: later arrayList van destinations, maakt het makkelijker, voor nu eentje
    private ArrayList<TreinRit> legs = new ArrayList<>();
    private TimeStamp ritDuration;
    private boolean cancelled;


    public TreinReis(String vertrek, String aankomst, int ritDuur, int aantalOverstappen, TimeStamp vertrektijd, TimeStamp aankomsttijd, String vertrekSpoor, String firstDestination)
    {
        this.vertrekStation = vertrek;
        this.aankomstStation = aankomst;
        this.ritDuur = ritDuur;
        setRitDuration(this.ritDuur);
        this.aantalOverstappen = aantalOverstappen;
        this.vertrektijd = vertrektijd;
        this.aankomsttijd = aankomsttijd;

        this.vertrekSpoor = vertrekSpoor;
        this.firstDestination = firstDestination;
        this.cancelled = false;

    }

    public TreinReis(int ritDuur, int aantalOverstappen, TimeStamp vertrektijd, TimeStamp aankomsttijd, TreinType eersteTreinType, String aankomstStation, String firstDestination, ArrayList<TreinRit> legs, int ritDuration)
    {
        this.ritDuur = ritDuur;
        this.aantalOverstappen = aantalOverstappen;
        this.vertrektijd = vertrektijd;
        this.aankomsttijd = aankomsttijd;
        this.eersteTreinType = eersteTreinType;
        this.aankomstStation = aankomstStation;
        this.firstDestination = firstDestination;
        this.legs = legs;
        this.ritDuur = ritDuration;
        setRitDuration(ritDuur);
        this.cancelled = true;
    }

    public TimeStamp getRitDuration()
    {
        return ritDuration;
    }

    public void setRitDuration(TimeStamp ritDuration)
    {
        this.ritDuration = ritDuration;
    }

    private void setRitDuration(int minutes)
    {
        TimeStamp timeStamp = new TimeStamp(minutes);
        setRitDuration(timeStamp);
    }

    public String getVertrekStation()
    {
        return vertrekStation;
    }

    public String getAankomstStation()
    {
        return aankomstStation;
    }
    public void setLegs(ArrayList<TreinRit> legs)
    {
        this.legs = legs;
    }

    public String getFirstDestination()
    {
        return firstDestination;
    }

    public int getRitDuur()
    {
        return ritDuur;
    }

    public int getAantalOverstappen()
    {
        return aantalOverstappen;
    }

    public TimeStamp getVertrektijd()
    {
        return vertrektijd;
    }

    public void setVertrektijd(TimeStamp vertrektijd)
    {
        this.vertrektijd = vertrektijd;
    }

    public void setAankomsttijd(TimeStamp aankomsttijd)
    {
        this.aankomsttijd = aankomsttijd;
    }

    public TimeStamp getAankomsttijd()
    {
        return aankomsttijd;
    }

    public TreinType getEersteTreinType()    {        return eersteTreinType;    }
    public void setEersteTreinType(TreinType type)
    {
        this.eersteTreinType = type;
    }

    public String getVertrekSpoor()
    {
        return vertrekSpoor;
    }

    public ArrayList<TreinRit> getLegs()
    {
        return legs;
    }
}
