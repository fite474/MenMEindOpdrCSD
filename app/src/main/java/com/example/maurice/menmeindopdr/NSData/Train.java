package com.example.maurice.menmeindopdr.NSData;

import java.util.Date;

public class Train
{
    private int ritnummer;
    private Date vertrektijd;
    private int vertrekspoor;
    private Station eindbestemming;
    private TreinType treinSoort;
    private Date vertraging;
    private String vertragingTekst;
    private String routeTekstShort;
    private String vervoerder;

    public Train(int ritnummer, Date vertrektijd, int vertrekspoor, Station eindbestemming, TreinType treinSoort, String vervoerder)
    {
        this.ritnummer = ritnummer;
        this.vertrektijd = vertrektijd;
        this.vertrekspoor = vertrekspoor;
        this.eindbestemming = eindbestemming;
        this.treinSoort = treinSoort;
        this.vervoerder = vervoerder;
    }

    public Train(int ritnummer, Date vertrektijd, int vertrekspoor, Station eindbestemming, TreinType treinSoort, Date vertraging, String vertragingTekst, String vervoerder)
    {
        this.ritnummer = ritnummer;
        this.vertrektijd = vertrektijd;
        this.vertrekspoor = vertrekspoor;
        this.eindbestemming = eindbestemming;
        this.treinSoort = treinSoort;
        this.vertraging = vertraging;
        this.vertragingTekst = vertragingTekst;
        this.vervoerder = vervoerder;
    }

    public int getRitnummer()
    {
        return ritnummer;
    }

    public void setRitnummer(int ritnummer)
    {
        this.ritnummer = ritnummer;
    }

    public Date getVertrektijd()
    {
        return vertrektijd;
    }

    public void setVertrektijd(Date vertrektijd)
    {
        this.vertrektijd = vertrektijd;
    }

    public int getVertrekspoor()
    {
        return vertrekspoor;
    }

    public void setVertrekspoor(int vertrekspoor)
    {
        this.vertrekspoor = vertrekspoor;
    }

    public Station getEindbestemming()
    {
        return eindbestemming;
    }

    public void setEindbestemming(Station eindbestemming)
    {
        this.eindbestemming = eindbestemming;
    }

    public TreinType getTreinSoort()
    {
        return treinSoort;
    }

    public void setTreinSoort(TreinType treinSoort)
    {
        this.treinSoort = treinSoort;
    }

    public Date getVertraging()
    {
        return vertraging;
    }

    public void setVertraging(Date vertraging)
    {
        this.vertraging = vertraging;
    }

    public String getVertragingTekst()
    {
        return vertragingTekst;
    }

    public void setVertragingTekst(String vertragingTekst)
    {
        this.vertragingTekst = vertragingTekst;
    }

    public String getRouteTekstShort()
    {
        return routeTekstShort;
    }

    public void setRouteTekstShort(String routeTekstShort)
    {
        this.routeTekstShort = routeTekstShort;
    }

    public String getVervoerder()
    {
        return vervoerder;
    }

    public void setVervoerder(String vervoerder)
    {
        this.vervoerder = vervoerder;
    }
}
