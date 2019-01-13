package com.example.maurice.menmeindopdr.Fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.maurice.menmeindopdr.DrawingRoute.GetDetailsFromPath;
import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.google.android.gms.location.LocationServices;
import com.example.maurice.menmeindopdr.App;
import com.example.maurice.menmeindopdr.DrawingRoute.GetPathFromLocation;
import com.example.maurice.menmeindopdr.MapsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, android.location.LocationListener {

    private final static String TAG = MapsFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap map;

    private boolean locationPermissionGranted;
    private Queue<Runnable> runnables;


    private FusedLocationProviderClient mFuseLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15;
    private Location previousLocation;


    private LocationManager mLocationManager;
    private ArrayList<Marker> mapMarkers;
    private static final long LOCATION_INTERVAL = 30000;
    private static final float LOCATION_DISTANCE = 20;
    GetPathFromLocation getPathFromLocation;
    //Station startStation;
    LatLng startingStation;
    TreinReis chosenTreinReis;

    int totalMeters = -1;
    int durationTime = -1;

    List<LatLng> toFollowRoute;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runnables = new LinkedBlockingQueue<>();


        getMapAsync(this);

        startingStation = new LatLng(getArguments().getDouble("startingStationLat"),
                getArguments().getDouble("startingStationLong"));//TODO

        System.out.println("test");


     //   toFollowRoute = new ArrayList<>(); //is voor het aangeven als je van de route afwijkt
    }

    public int getTotalMeters()
    {
        return totalMeters;
    }

    public int getDurationTime()
    {
        return durationTime;
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
        Polyline line = map.addPolyline(polyLine);
    }







    public void drawLinesToMap(LatLng targetStation){//List<PointOfInterest> pointOfInterests){
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



                            try {


                                    new GetPathFromLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), targetStation,
                                                polyLine -> {
                                                    addPolyLine(polyLine);
                                                }).execute();

                                    new GetDetailsFromPath(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), targetStation,
                                            integers -> {
                                                ((MapsActivity) getActivity()).setDetailText(integers.get(0), integers.get(1));
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

        //startTrackingUser();

    }

    private void startTrackingUser() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
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
//        try {
//            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(App.getContext(), R.raw.map_style_json));
//            if (!success) {
//                Log.e("STYLE_ERROR", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("STYLE_ERROR", "Can't find style. Error: ", e);
//        }

        this.map = googleMap;
        //map.setOnMarkerClickListener(this);
//        map.setOnInfoWindowClickListener(this);
//        map.setInfoWindowAdapter(new CustomInfoWindow(getActivity()));

        //setMarkers();

        startTrackingUser();

        getLocationPermission();
        getDeviceLocation();

//        LatLng[] boundsLatLngArray = new LatLng[]{
//                new LatLng(51.551859, 4.698705),
//                new LatLng(51.556769, 4.843869),
//                new LatLng(51.622466, 4.843183),
//                new LatLng(51.634188, 4.765637),
//                new LatLng(51.620122, 4.688058),
//                new LatLng(51.551859, 4.698705)
//        };
//
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (LatLng latLng : boundsLatLngArray) builder.include(latLng);
//        LatLngBounds bounds = builder.build();

        //map.setLatLngBoundsForCameraTarget(bounds);
        map.setIndoorEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMinZoomPreference(13);

        Iterator<Runnable> iterator = runnables.iterator();
        while (iterator.hasNext()) {
            Runnable runnable = iterator.next();
            runnable.run();
        }
    }

    private void setMarkers() {
//        Log.d(TAG, "setMarkers: " + routeSelected);
//        switch(routeSelected){
//            case SelectedRoute.BLIND_WALLS:
//                model.getPointOfInterests(routeSelected).observe(this, pointOfInterests -> {
//                    if(pointOfInterests != null && !pointOfInterests.isEmpty()) {
//                        addBlindWallsToMap(pointOfInterests);
//                        this.pointsOfInterest = pointOfInterests;
//                        Log.d(TAG, "setMarkers: " + pointOfInterests);
//                        drawLinesToMap(pointOfInterests);
//                    }
//                });
//                break;
//
//            case SelectedRoute.HISTORIC_KM:
//                model.getPointOfInterests(routeSelected).observe(this, pointOfInterests -> {
//                    if(pointOfInterests != null && !pointOfInterests.isEmpty()){
//                        addHistoricsToMap(pointOfInterests);
//                        this.pointsOfInterest = pointOfInterests;
//                        Log.d(TAG, "setMarkers: " + pointOfInterests);
//                        drawLinesToMap(pointOfInterests);
//                    }
//                });
//                break;
        //}
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

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation:");

        mFuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(App.getContext());

        try {
            if(locationPermissionGranted) {
                Task location = mFuseLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            try {
                                Log.d(TAG, "onComplete: ");
                                Location currentLocation = (Location) task.getResult();
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                                previousLocation = currentLocation;
                                drawLinesToMap(startingStation);

                            }
                            catch(Exception e){
//                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
//                                dlgAlert.setMessage("Please turn on GPS before you continue");
//                                dlgAlert.setTitle("GPS Error");
//                                dlgAlert.setPositiveButton("OK", null);
//                                dlgAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//
//                                    public void onClick(DialogInterface arg0, int arg1) {
//                                        Intent intent = new Intent(getContext(), MainActivity.class);
//                                        startActivity(intent);
//
//                                    }
//                                });
//                                dlgAlert.setCancelable(true);
//                                dlgAlert.create().show();
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

    private void moveCamera(LatLng latLng, float zoom){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
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
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));

        }
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

//    public void drawUserLine(Location location)
//    {
//        addPolyLine(new PolylineOptions()
//                .add(new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude()),
//                        new LatLng(location.getLatitude(), location.getLongitude()))
//                .width(5)
//                .color(Color.RED));
//        previousLocation = location;
//
//    }


    public Location getPreviousLocation() {
        return previousLocation;
    }

//    public ArrayList<PointOfInterest> getPointsOfInterestOnLocationChanged() {
//        return pointsOfInterestOnLocationChanged;
//    }

    public ArrayList<Marker> getMapMarkers() {
        return mapMarkers;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(previousLocation != null)
        {
            addPolyLine(new PolylineOptions()
                    .add(new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude()),
                            new LatLng(location.getLatitude(), location.getLongitude()))
                    .width(5)
                    .color(Color.RED));
        }
        previousLocation = location;


        map.clear();
        drawLinesToMap(startingStation);

//        pointsOfInterestOnLocationChanged = (ArrayList<PointOfInterest>) model.getPointOfInterests(routeSelected).getValue();
//        PointOfInterest closestPoi = null;
//        float distance = 1000;
//        boolean newPoiVisit = false;
//        for(PointOfInterest poi : pointsOfInterest){
//            Location poiLocation = new Location(poi.getName());
//            poiLocation.setLatitude(poi.getLatitude());
//            poiLocation.setLongitude(poi.getLongitude());
//            if(location.distanceTo(poiLocation) < 25 && location.distanceTo(poiLocation) < distance && !poi.isVisited()){
//                closestPoi = poi;
//                distance = location.distanceTo(poiLocation);
////                newPoiVisit = true;
//            }
//        }
//
//
//
//        if(closestPoi != null){
//            for(Marker marker : mapMarkers){
//                if(marker.getTag().equals(closestPoi.getId())){
//                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    marker.showInfoWindow();
//                    notificationService.sendNotification(marker.getTitle(), closestPoi, routeSelected);
//
//                    for (PointOfInterest pointOfInterest : model.getPointOfInterests(routeSelected).getValue()){
//                        if (closestPoi.getName().equals(pointOfInterest.getName()))
//                            pointOfInterest.setVisited(true);
//                        newPoiVisit = true;
//                    }
//                }
//            }
//        }
//
//        if(newPoiVisit)
//        {
//            Intent intent = new Intent(
//                    App.getContext(),
//                    DetailedActivity.class
//            );
//
//            Bundle b = new Bundle();
//            b.putInt("selected_route", routeSelected);
//            b.putSerializable("clicked_poi", closestPoi);
//            b.putString("clicked_name", closestPoi.getName());
//
//            intent.putExtra("bundle_poi", b);
//
//            startActivity(intent);

        }


//        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//
//
//        if(PolyUtil.isLocationOnEdge(currentLocation, toFollowRoute, true, 45))
//        {
//            System.out.println("\n\n\n\n\n TESTTTTTTTT \n\n\n\n\n\n\n\n");
//
//            //Toast.makeText(getActivity(), "loop terug naar de route!",
//            //Toast.LENGTH_LONG).show();
//        }



    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: " + provider);
    }

    //public List<PointOfInterest> getPointsOfInterest() {
//        return pointsOfInterest;
//    }


}