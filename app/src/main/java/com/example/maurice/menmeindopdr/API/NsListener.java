package com.example.maurice.menmeindopdr.API;

import com.example.maurice.menmeindopdr.NSData.Station;

import java.util.ArrayList;

public interface NsListener {
    public void onStationsAvailable(ArrayList<Station> stations);
    void noStationAvailable();



//    public void onLampAvailable(Lamp lamp);
//    public void onLampWallError(String errorString);
//    // void onUserNameAvailable(String userName);
//    void onAllLampsAvailable(ArrayList<Lamp> lamps);
//    void onClickListener();
}
