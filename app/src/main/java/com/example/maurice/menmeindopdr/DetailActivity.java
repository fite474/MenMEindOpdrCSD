package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.example.maurice.menmeindopdr.NSData.TreinRit;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity
{
    private TextView vanStationTV;
    private TextView naarStationTV;
    private TextView vanTV;
    private TextView naarTV;
    private TextView uitlegTV;
    private ListView legListView;
    private ImageView trainIconView;
    private Button goToMapsButton;

    private TreinReis reis;
    private ArrayList<TreinRit> legs;
    private ArrayAdapter<TreinRit> adapter;
    private LatLng startingStation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        reis = (TreinReis) getIntent().getSerializableExtra("reis");
        startingStation = new LatLng(getIntent().getDoubleExtra("startingStationLat",1.1),
                getIntent().getDoubleExtra("startingStationLong", 1.1));
        legs = new ArrayList<>();
        legs = reis.getLegs();
        goToMapsButton = findViewById(R.id.goToMapsButton);
        vanStationTV = findViewById(R.id.fromStationTV);
        vanStationTV.setText(reis.getVertrekStation());
        naarStationTV = findViewById(R.id.toStationTV);
        naarStationTV.setText(reis.getAankomstStation());
        uitlegTV = findViewById(R.id.uitlegTV);
        legListView = findViewById(R.id.travelListView);
        trainIconView = findViewById(R.id.detailTrainIcon);

        trainIconView.setImageResource(R.drawable.trainicon_ic_night);



        goToMapsButton.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(
                    getApplicationContext(),
                    MapsActivity.class
            );

            //LatLng startingStation = new LatLng(reis.get)
            intent.putExtra("startingStationLat", startingStation.latitude);
            intent.putExtra("startingStationLong", startingStation.longitude);


            intent.putExtra("reis", reis);
            startActivity(intent);
        }
    });



        adapter = new LegAdapter(
                getApplicationContext(),
                legs
        );
        legListView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        adapter.addAll(legs);

    }
}
