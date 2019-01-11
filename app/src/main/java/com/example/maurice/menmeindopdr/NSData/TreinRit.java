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

    public TreinRit(int ritDuur, int aantalOverstappen, Date vertrektijd, Date aankomsttijd, TreinType eersteTrein)
    {
        this.ritDuur = ritDuur;
        this.aantalOverstappen = aantalOverstappen;
        this.vertrektijd = vertrektijd;
        this.aankomsttijd = aankomsttijd;
        treinType = eersteTrein;

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
