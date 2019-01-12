package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
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
    Station destinationStation;
    Location closestLocation;
    ArrayList<Station> stations;
    TextView foundStation;
    ImageView backgr;
    ArrayList<TreinRit> ritten;

    private static int TIME_OUT = 2420;

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
        api.HandleAPICall(NSAPICallType.FIND_STATIONS, null, null);
        this.ritten = new ArrayList<>();



        zoekStationButton.setOnClickListener(v -> {
//            final String startingStation = closestStation.getName().toLowerCase();
            if(gezochtStation.getText().length() >=1)
            {
                String stationToEnd = String.valueOf(gezochtStation.getText()).toLowerCase();
                boolean validStation = false;
                boolean doneSearching = false;
                int i = 0;
                while(!validStation && !doneSearching)
                {
                    Station currStation = stations.get(i);
                    if (currStation.getName().toLowerCase().contains(stationToEnd))
                    {
                        validStation = true;
                        destinationStation = currStation;
                    }

                    if (i == stations.size() - 1)
                    {
                        doneSearching = true;
                    }
                    else
                    {
                        i++;
                    }
                }
                if(validStation)
                {
                    searchedStation.setText(buildText());
                    api.HandleAPICall(NSAPICallType.FROM_TO_REQUEST, closestStation.getCode(), destinationStation.getCode());
                }
                else
                {
                    searchedStation.setText("Station niet gevonden");
                    gezochtStation.setText("");
                }
            }
            else
            {
                searchedStation.setText("Voer eerst een station in");
            }

        });

    }

    private String buildText()
    {
        return "Ritten worden gezocht van station "+
                closestStation.getName() +
                " naar station " +
                destinationStation.getName() +
                ".\nEven geduld aub...";
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
        this.ritten = ritten;
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(
                        getApplicationContext(),
                        RouteSelectActivity.class
                );
                intent.putExtra("startStation", closestStation);
                intent.putExtra("destinationStation", destinationStation);
                intent.putExtra("ritten", ritten);

                startActivity(intent);
            }
        }, TIME_OUT);

    }

    @Override
    public void noJourneyAvailable()
    {
        searchedStation.setText("nope....................");
    }


}
