package com.example.maurice.menmeindopdr;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.Fragments.MapsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private final static String TAG = MapsActivity.class.getSimpleName();

    private ImageView arrow;
    private Button listButton;
    private Button helpButton;
    private TextView routename;
    //private MapsViewModel viewModel;
    private MapsFragment mapsFragment;
    private Intent i;
    public int selectedRoute;
    public final static String SELECTED_ROUTE_BUNDLE_TAG = "SELECTED_ROUTE_BUNDLE_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        LatLng testStation = new LatLng(getIntent().getDoubleExtra("startingStationLat", 1.1), getIntent().getDoubleExtra("startingStationLong", 1.1));


//        viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
//        viewModel.setRotationDeviceListener(this);

        //arrow = findViewById(R.id.compassImageViewId);

//        selectedRoute = getIntent().getIntExtra(MainActivity.INTENT_TAG_SELECT_ROUTE, SelectedRoute.NONE);
//
//        listButton = findViewById(R.id.list_button_mapsactivity);
//        listButton.setText(R.string.List);
//        listButton.setOnClickListener(view -> {
//            Intent intent = new Intent(App.getContext(), ListActivity.class);
//            intent.putExtra("ROUTE", selectedRoute);
//            startActivity(intent);
//        });
//
//        routename = findViewById(R.id.routename_id);
//        if (selectedRoute == SelectedRoute.HISTORIC_KM){
//            routename.setText(R.string.historischekm);
//        } else if (selectedRoute == SelectedRoute.BLIND_WALLS){
//            routename.setText(R.string.bwg);
//        } else if (selectedRoute == SelectedRoute.NONE){
//            routename.setText(R.string.googlemaps);
//            listButton.setVisibility(View.GONE);
//        }
//
//        helpButton = findViewById(R.id.helpButton_id);
//        helpButton.setText(R.string.help);
//        helpButton.setOnClickListener(v -> new HelpFragment().show(getSupportFragmentManager(), "help_fragment"));

        //LatLng testingStartStation = new LatLng(1.1,1.1);//TODO

        mapsFragment = new MapsFragment();

        Bundle bundle = new Bundle();
        //bundle.putInt(SELECTED_ROUTE_BUNDLE_TAG, selectedRoute);//TODO meegeven locaties
        bundle.putDouble("startingStationLat", testStation.latitude);
        bundle.putDouble("startingStationLong", testStation.longitude);

        mapsFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activityMapsFragment, mapsFragment).commit();
    }












    private GoogleMap mMap;











//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_maps);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
}
