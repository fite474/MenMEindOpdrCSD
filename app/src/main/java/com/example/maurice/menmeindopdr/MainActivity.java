package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.maurice.menmeindopdr.API.NSAPICallType;
import com.example.maurice.menmeindopdr.API.NsAPIHandler;
import com.example.maurice.menmeindopdr.API.NsListener;
import com.example.maurice.menmeindopdr.NSData.Station;
import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NsListener
{
    private ImageView backgroundImageView;
    private final static String TAG = MainActivity.class.getSimpleName();
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFuseLocationProviderClient;
    private NsAPIHandler api;

    Location currentLocation;

    boolean locationFound = false;


    private static int TIME_OUT = 2420;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        api = new NsAPIHandler(getApplicationContext(), this);

        backgroundImageView = findViewById(R.id.start_backgrImageView);

        backgroundImageView.setImageResource(R.drawable.startscreen);

        getLocationPermission();
        getDeviceLocation();




    }

    private void startNextActivity(Station closestStation, Location closestLocation)
    {

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(App.getContext(), StartingActivity.class);

                double currentLocationLat = currentLocation.getLatitude();
                double currentLocationLong = currentLocation.getLongitude();
                i.putExtra("currentLocationLat", currentLocationLat);
                i.putExtra("currentLocationLong", currentLocationLong);
                i.putExtra("closestStation", closestStation);
                i.putExtra("closestLocation", closestLocation);
                startActivity(i);
            }
        }, TIME_OUT);

            //finish();
    }



    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: ");
        if (ContextCompat.checkSelfPermission(App.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            //updateLocationUI();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            getLocationPermission();

        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation:");

        mFuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        try {
            if(locationPermissionGranted) {
                Task<Location> location = mFuseLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Location> task)
                    {
                        if (task.isSuccessful()) {
                            try {
                                Log.d(TAG, "onComplete: ");
                                currentLocation =  task.getResult();
                                api.HandleAPICall(NSAPICallType.FIND_STATIONS,  null, null);
                                //moveCamera(new LatLng(foundStationListView.getLatitude(), foundStationListView.getLongitude()), DEFAULT_ZOOM);
                            }
                            catch(Exception e){

                            }
                        } else {
                            Log.d(TAG, "current location");
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "SecurityException");

        }
        catch (Exception ex){
            Log.e(TAG, "getDeviceLocation: "+ ex.toString());

        }
    }


    @Override
    public void onStationsAvailable(ArrayList<Station> stations)
    {
        Station closestStation = stations.get(0);
        Location closestStationLocation = new Location("closest Station");
        closestStationLocation.setLatitude(closestStation.getLatitude());
        closestStationLocation.setLongitude(closestStation.getLongitude());

        float closestStationdistance = currentLocation.distanceTo(closestStationLocation);
        for(int i = 1; i < stations.size()-1; i++)
        {
          Station thisStation = stations.get(i);

          Location stationLocation = new Location("station location");
          stationLocation.setLongitude(thisStation.getLongitude());
          stationLocation.setLatitude(thisStation.getLatitude());

          float distanceToStation = currentLocation.distanceTo(stationLocation);

          if(distanceToStation < closestStationdistance)
          {
              closestStation = thisStation;
              closestStationLocation = stationLocation;
              closestStationdistance = distanceToStation;
          }
        }
        startNextActivity(closestStation, closestStationLocation);
    }
    private int i = 1;
    @Override
    public void noStationAvailable()
    {
        if(i == 1)
        {
            backgroundImageView.setImageResource(R.drawable.startscreen_notfound);

        }

        if(i == 5)
        {
            backgroundImageView.setImageResource(R.drawable.startscreen_error);
        }
        else if(i < 5)
        {
            api.HandleAPICall(NSAPICallType.FIND_STATIONS, null, null);
        }
        i++;
    }

    @Override
    public void onJourneysAvailable(ArrayList<TreinReis> ritten)
    {

    }

    @Override
    public void noJourneyAvailable()
    {

    }
}
