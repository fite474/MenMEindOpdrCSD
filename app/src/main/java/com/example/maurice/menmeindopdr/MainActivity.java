package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.NSData.Station;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button zoekStationButton;
    TextView currentLocation;
    LatLng currentDeviceLocation;
    EditText gezochtStation;
    Station closestStation;
    Location closestLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDeviceLocation = new LatLng(getIntent().getDoubleExtra("currentLocationLat", 1.0),
                getIntent().getDoubleExtra("currentLocationLong", 1.0));
        closestStation = (Station) getIntent().getSerializableExtra("closestStation");
        zoekStationButton = findViewById(R.id.zoekStationButton);
        currentLocation = findViewById(R.id.currentLocation);

        currentLocation.setText(closestStation.getName());




        zoekStationButton.setOnClickListener(v -> {
            final String stationToEnd = String.valueOf(zoekStationButton.getText());
            final String startingStation = "";//TODO get starting/ closest station
            Intent intent = new Intent(
                    getApplicationContext(),
                    RouteSelectActivity.class
            );
            intent.putExtra("gezochtStation", stationToEnd);
            intent.putExtra("startingStation", startingStation);
            //intent.putExtra(INTENT_TAG_SELECT_ROUTE, SelectedRoute.HISTORIC_KM);
            startActivity(intent);
        });

//        zoekStationButton.setOnClickListener(v -> {
//            Intent intent = new Intent(
//                    getApplicationContext(),
//                    MapsActivity.class
//            );
//            //intent.putExtra(INTENT_TAG_SELECT_ROUTE, SelectedRoute.HISTORIC_KM);
//            startActivity(intent);
//        });


//        stationsArray = new ArrayList<>();
//        stationsListView = (ListView) findViewById(R.id.);
//
//        blindWallsAdapter = new BlindWallsAdapter(
//                getApplicationContext(),
//                blindWallsArray
//        );
//
//
//
//        listView.setAdapter(blindWallsAdapter);

    }

    @Override
    public void onBackPressed()
    {


    }
}
