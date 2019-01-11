package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    Button testButton;
    TextView currentLocation;
    LatLng currentDeviceLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDeviceLocation = new LatLng(getIntent().getDoubleExtra("currentLocationLat", 1.0),
                getIntent().getDoubleExtra("currentLocationLong", 1.0));

        testButton = findViewById(R.id.tijdelijkeTestButton);
        currentLocation = findViewById(R.id.currentLocation);

        currentLocation.setText(String.valueOf(currentDeviceLocation.latitude)+"."+String.valueOf(currentDeviceLocation.longitude));
        testButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    getApplicationContext(),
                    MapsActivity.class
            );
            //intent.putExtra(INTENT_TAG_SELECT_ROUTE, SelectedRoute.HISTORIC_KM);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed()
    {


    }
}
