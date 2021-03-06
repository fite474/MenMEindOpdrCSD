package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.API.NsAPIHandler;
import com.example.maurice.menmeindopdr.API.NsListener;
import com.example.maurice.menmeindopdr.NSData.Station;
import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RouteSelectActivity extends AppCompatActivity implements NsListener {

    private static final String TAG = "RouteSelectActivity";
    ListView journeyListView;
    TextView beginTxtView;
    TextView eindTxtView;
    ArrayAdapter journeyAdapter;
    ArrayList<TreinReis> journeyArray;
    Button testStationButton;
    NsAPIHandler api;
    Station startingStation;
    Station destinationStation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_pickroute);
        api = new NsAPIHandler(getApplicationContext(), this);
        startingStation = (Station) getIntent().getSerializableExtra("startStation");
        destinationStation = (Station) getIntent().getSerializableExtra("destinationStation");
        LatLng testStation = new LatLng(51.563983, 5.079380);
        beginTxtView = findViewById(R.id.beginStationTxt);
        eindTxtView = findViewById(R.id.eindStationTxt);
        beginTxtView.setText(getString(R.string.vanInfo)+startingStation.getName());
        eindTxtView.setText(getString(R.string.naarInfo)+destinationStation.getName());

        //TODO ending station vergegelijken en routes zoeken voor de listview


        journeyArray = new ArrayList<>();
        journeyListView = (ListView) findViewById(R.id.journeysListView);

        journeyAdapter = new RouteSelectAdapter(
                getApplicationContext(),
                journeyArray
        );
        journeyListView.setAdapter(journeyAdapter);
        journeyAdapter.setNotifyOnChange(true);
        journeyArray = (ArrayList<TreinReis>) getIntent().getSerializableExtra("ritten");
        journeyAdapter.addAll(journeyArray);

        journeyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("JOURNEY-TAG", "" + position);

                Intent intent = new Intent(
                        getApplicationContext(),
                        DetailActivity.class
                );

                double startingStationLat = startingStation.getLatitude();
                double startingStationLong = startingStation.getLongitude();

                intent.putExtra("startingStationLat", startingStationLat);
                intent.putExtra("startingStationLong", startingStationLong);

                TreinReis treinReis = journeyArray.get(position);
                //intent.putExtra("startingStationLat", startingStation.getLatitude());
                //intent.putExtra("startingStationLong", startingStation.getLongitude());
                intent.putExtra("reis", treinReis);

                //intent.putExtra("treinReis", journeyArray);
               // intent.putExtra("treinRitIndex", position);


                startActivity(intent);
            }
        });


    }



    @Override
    public void onStationsAvailable(ArrayList<Station> stations)
    {

    }

    @Override
    public void noStationAvailable()
    {

    }

    @Override
    public void onJourneysAvailable(ArrayList<TreinReis> ritten)
    {
        this.journeyArray = ritten;
    }

    @Override
    public void noJourneyAvailable()
    {

    }


}