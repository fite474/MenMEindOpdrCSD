package com.example.maurice.menmeindopdr.Fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.LocationServices;
import com.example.maurice.menmeindopdr.App;
import com.example.maurice.menmeindopdr.DrawingRoute.GetPathFromLocation;
import com.example.maurice.menmeindopdr.MapsActivity;
import com.example.maurice.menmeindopdr.NSData.Station;
import com.example.maurice.menmeindopdr.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 *  NOTE: GEBRUIK NIET DE VARIABLE map BUITEN DE onMapReady, gebruik bijvoorbeeld de addPolyline of de addMarker methode.
 */
public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {

    private final static String TAG = MapsFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap map;
    private boolean locationPermissionGranted;
    private Queue<Runnable> runnables;

    private FusedLocationProviderClient mFuseLocationProviderClient;
    private Location previousLocation;
    private LocationManager mLocationManager;


    List<Station> stations;

    LatLng src;
    LatLng dest;
    //private MapsViewModel model;
    private int routeSelected;

    List<LatLng> waypoints;

    GetPathFromLocation getPathFromLocation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runnables = new LinkedBlockingQueue<>();

        if (getArguments() != null) {
        //    routeSelected = getArguments().getInt(MapsActivity.SELECTED_ROUTE_BUNDLE_TAG);
        }

        getMapAsync(this);

//        model = ViewModelProviders.of(this).get(MapsViewModel.class);
//        switch(routeSelected){
//            case SelectedRoute.BLIND_WALLS:
//                model.getPointOfInterests(routeSelected).observe(this, this::addBlindWallsToMap);
//                break;
//
//            case SelectedRoute.HISTORIC_KM:
//                model.getPointOfInterests(routeSelected).observe(this, this::addHistoricsToMap);
//                break;
//
//        }
    }

    public void addMarker(final MarkerOptions marker){
        if(map == null){
            runnables.add(() -> addMarkerInternal(marker));
        }else{
            addMarkerInternal(marker);
        }
    }

    private void addMarkerInternal(final MarkerOptions marker){
        map.addMarker(marker);
    }

    public void addMarker(final MarkerOptions marker, final int tag){
        if(map == null){
            runnables.add(() -> addMarkerInternal(marker, tag));
        }else{
            addMarkerInternal(marker, tag);
        }
    }

    private void addMarkerInternal(final MarkerOptions marker, final int tag){
        map.addMarker(marker).setTag(tag);
    }

    public void addPolyLine(final PolylineOptions polyLine) {
        if (map == null) {
            runnables.add(() -> addPolyLineInternal(polyLine));
        } else {
            addPolyLineInternal(polyLine);
        }
    }

    private void addPolyLineInternal(final PolylineOptions polyLine) {
        Polyline line = map.addPolyline(polyLine);
    }

    public void addHistoricsToMap(List<Station> monuments){
//        for(PointOfInterest poi : monuments){
//            if(monuments.get(0) instanceof HistoricMonument) {
//                HistoricMonument monument = (HistoricMonument) poi;
//                if (!monument.getName().equals("")) {
//                    addMarker(new MarkerOptions()
//                                    .position(new LatLng(monument.getLatitude(), monument.getLongitude()))
//                                    .title(monument.getName())
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)),
//                            monument.getId());
//                }
//            }
//        }
        //drawLinesToMap(monuments);
    }

    public void addBlindWallsToMap(List<Station> blindWalls){

//
//        if(blindWalls.get(0) instanceof BlindWall) {
//            for (PointOfInterest blindWallPoi : blindWalls) {
//                BlindWall blindWall = (BlindWall) blindWallPoi;
//                addMarker(new MarkerOptions()
//                                .position(new LatLng(blindWall.getLatitude(), blindWall.getLongitude()))
//                                .title(blindWall.getAuthor())
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)),
//                        blindWall.getId());
//            }
//        }
//        drawLinesToMap(blindWalls);
    }

    public void drawLinesToMap(LatLng endDestination){
        mFuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(App.getContext());
        try {
            if(locationPermissionGranted) {
                Task location = mFuseLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: ");
                            Location currentLocation = (Location) task.getResult();


                            //startTrackingUser();
                            try {

                                new GetPathFromLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        endDestination,//new LatLng(1,1),//partialList.get(partialList.size() - 1).getLatitude(), partialList.get(partialList.size() - 1).getLongitude()),

                                        polyLine -> {
                                            addPolyLine(polyLine);
                                        }).execute();



                            }catch (Exception e){
                                Log.e(TAG, "onComplete: ", e);
                            }

                        } else {
                            Log.d(TAG, "current location");
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "SecurityException");
        }
        catch (Exception ex){
            Log.e(TAG, "getDeviceLocation: "+ ex.toString());
        }
    }


    private void startTrackingUser() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 700, 1,
                    this);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        try {
            boolean success = true;//googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(App.getContext(), R.raw.map_style_json));
            if (!success) {
                Log.e("STYLE_ERROR", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("STYLE_ERROR", "Can't find style. Error: ", e);
        }

        this.map = googleMap;
        map.setOnMarkerClickListener(this);

        getLocationPermission();

//        LatLng[] latLngsArray = new LatLng[]{
//                new LatLng(51.551859, 4.698705),
//                new LatLng(51.556769, 4.843869),
//                new LatLng(51.622466, 4.843183),
//                new LatLng(51.634188, 4.765637),
//                new LatLng(51.620122, 4.688058),
//                new LatLng(51.551859, 4.698705)
//        };
//
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        PolylineOptions polylineOptions = new PolylineOptions();
//        for (LatLng latLng : latLngsArray) {
//            builder.include(latLng);
//            polylineOptions.add(latLng);
//        }
//        LatLngBounds bounds = builder.build();
        PolylineOptions polylineOptions = new PolylineOptions();

        map.addPolyline(polylineOptions);
       // map.setLatLngBoundsForCameraTarget(bounds);
        map.setIndoorEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMinZoomPreference(13);

        Iterator<Runnable> iterator = runnables.iterator();
        while (iterator.hasNext()) {
            Runnable runnable = iterator.next();
            runnable.run();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
//        PointOfInterest clickedPoi = model.getPointOfInterests(routeSelected).getValue().get((int) marker.getTag()-1);
//
//        if (clickedPoi != null) {
//            Intent intent = new Intent(
//                    App.getContext(),
//                    DetailedActivity.class
//            );
//            intent.putExtra("selected_route", routeSelected);
//            intent.putExtra("clicked_poi", clickedPoi);
//            startActivity(intent);
//        }

        return false;
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: ");
        if (ContextCompat.checkSelfPermission(App.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        Log.d(TAG, "updateLocationUI: map == null: " + (map == null));
        Log.d(TAG, "updateLocationUI: locationPermissionGranted: " + locationPermissionGranted);
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
                zoomToCurrentLocation();
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e(TAG, "updateLocationUI: ", e);
        }
    }

    private void zoomToCurrentLocation(){
        LocationManager locationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = null;

        try {
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        }catch (SecurityException | NullPointerException e){
            Log.e(TAG, "zoomToCurrentLocation: ", e);
        }

        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            previousLocation = location;
            startTrackingUser();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        addPolyLine(new PolylineOptions()
                .add(new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude()),
                        new LatLng(location.getLatitude(), location.getLongitude()))
                .width(5)
                .color(Color.RED));
        previousLocation = location;

        //pointsOfInterestOnLocationChanged = (ArrayList<PointOfInterest>) model.getPointOfInterests(routeSelected).getValue();
        Station closestStation = null;
        float distance = 1000;
//        for(Station station : stations){
//            Location stationLocation = new Location(station.getCode());
//            stationLocation.setLatitude(station.getLatitude());
//            stationLocation.setLongitude(station.getLongitude());
//            if(location.distanceTo(stationLocation) < 25 && location.distanceTo(stationLocation) < distance){
//                closestStation = station;
//                distance = location.distanceTo(stationLocation);
//            }
//        }

        if(closestStation != null){
//            for(Marker marker : mapMarkers){
//                if(marker.getTag().equals(closestPoi.getId())){
//                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    marker.showInfoWindow();
//                    notificationService.sendNotification(marker.getTitle());
//
//                    for (PointOfInterest pointOfInterest : model.getPointOfInterests(routeSelected).getValue()){
//                        if (closestPoi.getName().equals(pointOfInterest.getName()))
//                            //TODO: code om aan te geven dat dit het distbijzijnde station is
//                    }
//                }
//            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
}