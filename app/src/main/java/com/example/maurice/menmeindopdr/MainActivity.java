package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.API.NSAPICallType;
import com.example.maurice.menmeindopdr.API.NsAPIHandler;
import com.example.maurice.menmeindopdr.API.NsListener;
import com.example.maurice.menmeindopdr.NSData.Station;
import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NsListener
{
    NsAPIHandler api;
    Button zoekStationButton;
    ListView foundStationListView;
    LatLng currentDeviceLocation;
    EditText gezochtStation;
    Station closestStation;
    Station destinationStation;
    Location closestLocation;
    ArrayList<Station> stations;
    TextView foundStation;
    ImageView backgr;
    ArrayList<TreinReis> ritten;
    ArrayAdapter<String> adapter;
    ArrayList<String> foundStations;
    ArrayList<Station> foundStationList = new ArrayList<>();
    String stationToEnd;

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
        foundStationListView = findViewById(R.id.foundStationsListView);
        foundStation = findViewById(R.id.closestStationTextview);
        foundStation.setText(closestStation.getName());
        backgr = findViewById(R.id.foundImageView);
        backgr.setImageResource(R.drawable.found_station);
        gezochtStation = findViewById(R.id.searchStation);


        foundStations = new ArrayList<>();

        this.stations = new ArrayList<>();
        api.HandleAPICall(NSAPICallType.FIND_STATIONS, null, null);
        this.ritten = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listview_item_pick_station, R.id.stationNameTV, foundStations);
        adapter.setNotifyOnChange(true);
        foundStationListView.setAdapter(adapter);

        zoekStationButton.setOnClickListener(v -> {
//            final String startingStation = closestStation.getName().toLowerCase();
            if(gezochtStation.getText().length() >=1)
            {
                stationToEnd = String.valueOf(gezochtStation.getText()).toLowerCase();
                boolean validStation = false;
                adapter.clear();
                foundStations.clear();
                for (int i = 0; i < stations.size(); i++)
                {
                    Station currStation = stations.get(i);
                    if (currStation.getName().toLowerCase().contains(stationToEnd))
                    {
                        validStation = true;
                        foundStationList.add(currStation);
                        foundStations.add(currStation.getName());
                    }

                }
                if(validStation)
                {
                    //Nothing, this happens below
                }
                else
                {
                   foundStations.add("Geen overeenkomstige stations.");

                }
            }
            else
            {
                foundStations.add("Voer eerst een station in");
//                adapter.addAll(foundStations);
            }
            gezochtStation.setText("");
        });

        foundStationListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Log.i("STATION-TAG", String.valueOf(position));
                Intent intent = new Intent(
                        getApplicationContext(),
                        RouteSelectActivity.class
                );

                String stationName = foundStations.get(position);
                if(checkValidStationName(stationName))
                {
                    searchForStation(stationName);

                }
                else
                {

                }

            }

            private boolean checkValidStationName(String stationName)
            {
                boolean valid = true;
                if(stationName.equals("Geen overeenkomstige stations."))
                {
                    valid = false;
                }
                else if(stationName.equals("Voer eerst een station in"))
                {
                    valid = false;
                }
                else if (stationName.equals("Ritten worden gezocht!"))
                {
                    valid = false;
                }
                else if(stationName.equals("Ritten gevonden!"))
                {
                    valid = false;
                }
                else if(stationName.equals("Geen reizen gevonden."))
                {
                    valid = false;
                }
                return valid;
            }

            private void searchForStation(String name)
            {
                int i = 0;
                boolean found = false;
                while(!found && i < stations.size())
                {
                    Station currStation = stations.get(i);
                    if(currStation.getName().equals(name))
                    {
                        destinationStation = currStation;
                        found = true;
                    }
                    i++;
                }
                api.HandleAPICall(NSAPICallType.FROM_TO_REQUEST, closestStation.getCode(), destinationStation.getCode());
                adapter.clear();
                foundStations.clear();
                foundStations.add("Ritten worden gezocht!");

            }

            private void refillList()
            {
                foundStations.clear();
                for(int i = 0; i < foundStationList.size(); i++)
                {
                    foundStations.add(foundStationList.get(i).getName());
                }
                adapter.clear();
//                adapter.addAll(foundStations);

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
    public void onJourneysAvailable(ArrayList<TreinReis> ritten)
    {
        this.ritten = ritten;
        foundStations.clear();
        adapter.clear();
        foundStations.add("Ritten gevonden!");
//        adapter.addAll(foundStations);
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
        foundStations.clear();
        adapter.clear();
        foundStations.add("Geen reizen gevonden.");
//        adapter.addAll(foundStations);

    }


}
