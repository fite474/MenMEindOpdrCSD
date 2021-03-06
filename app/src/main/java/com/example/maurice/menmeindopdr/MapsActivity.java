package com.example.maurice.menmeindopdr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.Fragments.MapsFragment;
import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.time.LocalTime;
import java.util.Date;

public class MapsActivity extends AppCompatActivity {

    private final static String TAG = MapsActivity.class.getSimpleName();

    private MapsFragment mapsFragment;


    TextView duration;
    TextView distance;
    TextView tijdOverInfo;
    TextView tijdOverValue;
    Button listButton;





    TreinReis treinReis;
    LocalTime currentTime;

    TimeStamp requiredArivalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        LatLng testStation;
        testStation = new LatLng(getIntent().getDoubleExtra("startingStationLat", 1.1), getIntent().getDoubleExtra("startingStationLong", 1.1));
        duration = findViewById(R.id.routeDuration);
        distance = findViewById(R.id.routeDistance);
        tijdOverInfo = findViewById(R.id.tijdLEftInfoTxt);
        tijdOverValue = findViewById(R.id.tijdOverTxtValue);
        treinReis = (TreinReis) getIntent().getSerializableExtra("reis");
//        listButton = findViewById(R.id.button);
//
//        listButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(
//                        getApplicationContext(),
//                        DetailActivity.class
//                );
//                intent.putExtra("reis", treinReis);
//                startActivity(intent);
//            }
//        });
//        System.out.println(treinReis.getRitDuur());

//        viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
//        viewModel.setRotationDeviceListener(this);

        //arrow = findViewById(R.id.compassImageViewId);

//        selectedRoute = getIntent().getIntExtra(StartingActivity.INTENT_TAG_SELECT_ROUTE, SelectedRoute.NONE);
//
//        listButton = findViewById(R.id.list_button_mapsactivity);
//        listButton.setText(R.string.List);
//        listButton.setOnClickListener(view -> {
//            Intent intent = new Intent(App.getContext(), ListActivity.class);
//            intent.putExtra("ROUTE", selectedRoute);
//            startActivity(intent);
//        });
//
//        routename = findViewById(R.id.routename_id);
//        if (selectedRoute == SelectedRoute.HISTORIC_KM){
//            routename.setText(R.string.historischekm);
//        } else if (selectedRoute == SelectedRoute.BLIND_WALLS){
//            routename.setText(R.string.bwg);
//        } else if (selectedRoute == SelectedRoute.NONE){
//            routename.setText(R.string.googlemaps);
//            listButton.setVisibility(View.GONE);
//        }
//
//        helpButton = findViewById(R.id.helpButton_id);
//        helpButton.setText(R.string.help);
//        helpButton.setOnClickListener(v -> new HelpFragment().show(getSupportFragmentManager(), "help_fragment"));

        //LatLng testingStartStation = new LatLng(1.1,1.1);//TODO

        mapsFragment = new MapsFragment();

        Bundle bundle = new Bundle();
        //bundle.putInt(SELECTED_ROUTE_BUNDLE_TAG, selectedRoute);//TODO meegeven locaties
        bundle.putDouble("startingStationLat", testStation.latitude);
        bundle.putDouble("startingStationLong", testStation.longitude);

        mapsFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activityMapsFragment, mapsFragment).commit();


    }

    public void setDetailText(int totalMeters, int durationTime)
    {
        int seconds = durationTime % 60;
        int minutes = durationTime / 60;
        int hours = 0;//durationTime / 3600;

        TimeStamp time = new TimeStamp(minutes);
        distance.setText(getString(R.string.afstandInfo) + totalMeters + "meters");
        duration.setText(getString(R.string.tijdTotInfo) + time.toString() + getString(R.string.uur));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeLeftForRoute(durationTime);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void timeLeftForRoute(int seconds)
    {
        boolean onTime = false;
        currentTime = LocalTime.now();

        int currentUsersTime = (currentTime.getHour() * 60) + currentTime.getMinute();
        requiredArivalTime = treinReis.getVertrektijd();
        int requiredTime = (requiredArivalTime.getHours() * 60) + requiredArivalTime.getMinutes();
        int timeNeeded = (requiredTime - currentUsersTime);
        int red = Color.parseColor("#FF0000");
        int green = Color.parseColor("#228B22");

        if((timeNeeded * 60) > seconds)
        {
            onTime = true;
            setActivityBackgroundColor(green);

        }

        if(!onTime)
        {
            setActivityBackgroundColor(red);

            showTooLateDialog();
        }

        int secondsforstring = seconds % 60;
        //int minutes = timeNeeded / 60;
        int hours = 0;//durationTime / 3600;

        TimeStamp time = new TimeStamp(timeNeeded);

        tijdOverValue.setText(getString(R.string.tijdOverNog) + time + getString(R.string.uur));
    }

    private void showTooLateDialog()
    {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
        builder1.setMessage(getString(R.string.teLaatBericht) +
                getString(R.string.teLaatBerichtTwee));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getString(R.string.jaKnop),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                getString(R.string.neeKnop),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

//        while(alert11.isShowing())
//        {
//
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }



    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }
}
