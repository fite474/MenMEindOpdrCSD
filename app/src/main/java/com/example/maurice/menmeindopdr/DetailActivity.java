package com.example.maurice.menmeindopdr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.example.maurice.menmeindopdr.NSData.TreinRit;

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

    private TreinReis reis;
    private ArrayList<TreinRit> legs;
    private ArrayAdapter<TreinRit> adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        reis = (TreinReis) getIntent().getSerializableExtra("reis");
        legs = new ArrayList<>();
        legs = reis.getLegs();
        vanStationTV = findViewById(R.id.fromStationTV);
        vanStationTV.setText(reis.getVertrekStation());
        naarStationTV = findViewById(R.id.toStationTV);
        naarStationTV.setText(reis.getAankomstStation());
        uitlegTV = findViewById(R.id.uitlegTV);
        legListView = findViewById(R.id.travelListView);
        trainIconView = findViewById(R.id.detailTrainIcon);

        trainIconView.setImageResource(R.drawable.trainicon_ic_night);



        adapter = new LegAdapter(
                getApplicationContext(),
                legs
        );
        legListView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);


    }
}
