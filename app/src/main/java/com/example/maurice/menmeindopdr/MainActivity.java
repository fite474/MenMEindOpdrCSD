package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testButton = findViewById(R.id.tijdelijkeTestButton);

        testButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    getApplicationContext(),
                    MapsActivity.class
            );
            //intent.putExtra(INTENT_TAG_SELECT_ROUTE, SelectedRoute.HISTORIC_KM);
            startActivity(intent);
        });

    }
}
