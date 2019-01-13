package com.example.maurice.menmeindopdr.NSData;

import java.io.Serializable;
import java.util.Date;

public class TreinRit implements Serializable
{
    private int ritDuur;
    private int aantalOverstappen;
    private Date vertrektijd;
    private Date aankomsttijd;
    private TreinType treinType;
    private String vertrekSpoor;
    private String firstDestination; //TODO: later arrayList van destinations, maakt het makkelijker, voor nu eentje


    public TreinRit(int ritDuur, int aantalOverstappen, Date vertrektijd, Date aankomsttijd, TreinType eersteTrein, String vertrekSpoor, String firstDestination)
    {
        this.ritDuur = ritDuur;
        this.aantalOverstappen = aantalOverstappen;
        this.vertrektijd = vertrektijd;
        this.aankomsttijd = aankomsttijd;
        treinType = eersteTrein;
        this.vertrekSpoor = vertrekSpoor;
        this.firstDestination = firstDestination;

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

    public Date getVertrektijd()
    {
        return vertrektijd;
    }

    public Date getAankomsttijd()
    {
        return aankomsttijd;
    }

    public TreinType getTreinType()
    {
        return treinType;
    }

    public String getVertrekSpoor()
    {
        return vertrekSpoor;
    }
}
