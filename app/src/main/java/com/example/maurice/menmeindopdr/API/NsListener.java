package com.example.maurice.menmeindopdr.API;

import com.example.maurice.menmeindopdr.NSData.Station;
import com.example.maurice.menmeindopdr.NSData.TreinRit;

import java.util.ArrayList;

public interface NsListener {
    public void onStationsAvailable(ArrayList<Station> stations);
    void noStationAvailable();
    void onJourneysAvailable(ArrayList<TreinRit> ritten);
    void noJourneyAvailable();





//    public void onLampAvailable(Lamp lamp);
//    public void onLampWallError(String errorString);
//    // void onUserNameAvailable(String userName);
//    void onAllLampsAvailable(ArrayList<Lamp> lamps);
//    void onClickListener();
}
