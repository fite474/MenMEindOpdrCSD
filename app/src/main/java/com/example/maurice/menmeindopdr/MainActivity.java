package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.API.NSAPICallType;
import com.example.maurice.menmeindopdr.API.NsAPIHandler;
import com.example.maurice.menmeindopdr.API.NsListener;
import com.example.maurice.menmeindopdr.NSData.Station;
import com.example.maurice.menmeindopdr.NSData.TreinRit;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NsListener
{
    NsAPIHandler api;
    Button zoekStationButton;
    TextView searchedStation;
    LatLng currentDeviceLocation;
    EditText gezochtStation;
    Station closestStation;
    Location closestLocation;
    ArrayList<Station> stations;
    TextView foundStation;
    ImageView backgr;

    MultiAutoCompleteTextView completeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.api = new NsAPIHandler(getApplicationContext(), this);
        currentDeviceLocation = new LatLng(getIntent().getDoubleExtra("currentLocationLat", 1.0),
                getIntent().getDoubleExtra("currentLocationLong", 1.0));
        closestStation = (Station) getIntent().getSerializableExtra("closestStation");
        zoekStationButton = findViewById(R.id.zoekStationButton);
        searchedStation = findViewById(R.id.foundStationTV);
        foundStation = findViewById(R.id.closestStationTextview);
        foundStation.setText(closestStation.getName());
        backgr = findViewById(R.id.foundImageView);
        backgr.setImageResource(R.drawable.found_station);
        gezochtStation = findViewById(R.id.searchStation);




        this.stations = new ArrayList<>();
        api.HandleAPICall(NSAPICallType.FIND_STATIONS, null,null);



        zoekStationButton.setOnClickListener(v -> {
            final String startingStation = closestStation.getName().toLowerCase();
            if(String.valueOf(gezochtStation.getText()) != null)
            {
                final String stationToEnd = String.valueOf(zoekStationButton.getText()).toLowerCase();
                boolean validStation = false;
                boolean doneSearching = false;
                int i = 0;
                while(!validStation && !doneSearching)
                {
                    Station currStation = stations.get(i);
                    if (currStation.getName().toLowerCase().contains(stationToEnd))
                    {
                        validStation = true;
                    }

                    if (i == stations.size() - 1)
                    {
                        doneSearching = true;
                    }
                    else
                    {
                        i++;
                    }
                    searchedStation.setText(String.valueOf(i));
                }


                if(validStation)
                {

                    Intent intent = new Intent(
                            getApplicationContext(),
                            RouteSelectActivity.class
                    );
                    intent.putExtra("gezochtStation", stationToEnd);
                    intent.putExtra("startingStation", startingStation);
                    startActivity(intent);
                }
                else
                {
                    searchedStation.setText("Station niet gevonden");
                }
            }
            else
            {
                searchedStation.setText("Voer eerst een station in");
            }

        });

    }

    @Override
    public void onBackPressed()
    {


    }

    @Override
    public void onStationsAvailable(ArrayList<Station> stations)
    {
        this.stations = stations;
    }

    @Override
    public void noStationAvailable()
    {

    }

    @Override
    public void onJourneysAvailable(ArrayList<TreinRit> ritten)
    {

    }


}
