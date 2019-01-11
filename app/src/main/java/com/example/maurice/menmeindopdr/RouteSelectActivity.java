package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.maurice.menmeindopdr.API.NsAPIHandler;
import com.example.maurice.menmeindopdr.API.NsListener;
import com.example.maurice.menmeindopdr.NSData.Station;

import java.util.ArrayList;
import java.util.List;

public class RouteSelectActivity extends AppCompatActivity implements NsListener {

    private static final String TAG = "RouteSelectActivity";
    // private static BlindWallsBreda blindWallsBreda;
    ListView stationsListView;
    ArrayAdapter stationsAdapter;
    ArrayList<Station> stationsArray;

    private List<Station> wallList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_pickroute);

//
//        String json = JsonUtil.loadJSONFromAsset(MainActivity.this);
////        Log.d(TAG, "onCreate: json from asset " + json );
//
//        blindWallsBreda = BlindWallsBreda.createFromJson(json);
//
//        Log.d(TAG, "onCreate: " + blindWallsBreda);
//        displayBlindWalls();
//    }
//
//    private void displayBlindWalls() {
//        TextView blindWallsTextView = findViewById(R.id.activity_main_text_id);
//        blindWallsTextView.setText(blindWallsBreda.printAllWalls());
        stationsArray = new ArrayList<>();
        stationsListView = (ListView) findViewById(R.id.stationListView);

        stationsAdapter = new RouteSelectAdapter(
                getApplicationContext(),
                stationsArray
        );



        stationsListView.setAdapter(stationsAdapter);


        NsAPIHandler api = new NsAPIHandler(
                this.getApplicationContext(),
                this,
                "ipadress hier plaatsen");
        //api.HandleAPICall();

        stationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("BLINDWALL-TAG", "" + position);


                Intent intent = new Intent(
                        getApplicationContext(),
                        MapsActivity.class
                );

                Station station = stationsArray.get(position);
                intent.putExtra("BLINDWALL_OBJECT", station);

                startActivity(intent);
            }
        });


    }

//    @Override
//    public void onBlindWallAvailable(BlindWall blindWall) {
//        Log.i("", "");
//
//        blindWallsArray.add(blindWall);
//        blindWallsAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onBlindWallError(String errorString) {
//        Log.i("","");
//    }
}