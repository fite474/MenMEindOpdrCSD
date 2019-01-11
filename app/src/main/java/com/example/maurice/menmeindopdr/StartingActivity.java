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
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Timer;

public class StartingActivity extends AppCompatActivity {
    private ImageView backgroundImageView;
    private final static String TAG = StartingActivity.class.getSimpleName();
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFuseLocationProviderClient;

    Location currentLocation;

    boolean locationFound = false;


    private static int TIME_OUT = 4876;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        backgroundImageView = findViewById(R.id.start_backgrImageView);

        backgroundImageView.setImageResource(R.drawable.startscreenv1);

        getLocationPermission();
        getDeviceLocation();




    }


    private void startNextActivity()
    {

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(App.getContext(), MainActivity.class);

                double currentLocationLat = currentLocation.getLatitude();
                double currentLocationLong = currentLocation.getLongitude();
                i.putExtra("currentLocationLat", currentLocationLat);
                i.putExtra("currentLocationLong", currentLocationLong);
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
            ActivityCompat.requestPermissions(StartingActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            getLocationPermission();

        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation:");

        mFuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(App.getContext());

        try {
            if(locationPermissionGranted) {
                Task location = mFuseLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            try {
                                Log.d(TAG, "onComplete: ");
                                currentLocation = (Location) task.getResult();
                                startNextActivity();
                                //moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            }
                            catch(Exception e){
//                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
//                                dlgAlert.setMessage("Please turn on GPS before you continue");
//                                dlgAlert.setTitle("GPS Error");
//                                dlgAlert.setPositiveButton("OK", null);
//                                dlgAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//
//                                    public void onClick(DialogInterface arg0, int arg1) {
//                                        Intent intent = new Intent(getContext(), MainActivity.class);
//                                        startActivity(intent);
//
//                                    }
//                                });
//                                dlgAlert.setCancelable(true);
//                                dlgAlert.create().show();
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


}
