package com.example.maurice.menmeindopdr.GPS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.maurice.menmeindopdr.App;
import com.example.maurice.menmeindopdr.StartingActivity;
import com.example.maurice.menmeindopdr.R;

public class GpsService extends Service {
    public final static String PREVIOUS_LOCATION_BUNDLE_TAG = "PREVIOUS_LOCATION_BUNDLE_TAG";
    public final static String POINTS_OF_INTEREST_BUNDLE_TAG = "POINTS_OF_INTEREST_BUNDLE_TAG";
    public final static String GPS_SERVICE_INTENT_TAG = "GPS_SERVICE_INTENT_TAG";

    private final static String TAG = GpsService.class.getSimpleName();
    private static final int NOTIF_ID = 1;
    private static final long LOCATION_INTERVAL = 7000;
    private static final float LOCATION_DISTANCE = 10;
    private LocationManager mLocationManager;

    private Location previousLocation;
   // private ArrayList<PointOfInterest> pointOfInterests;
   // private NotificationService notificationService = NotificationService.getInstance();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if(intent != null){
//            Bundle b = intent.getBundleExtra(GPS_SERVICE_INTENT_TAG);
//            if(b != null) {
//                previousLocation = b.getParcelable(PREVIOUS_LOCATION_BUNDLE_TAG);
//                pointOfInterests = b.getParcelableArrayList(POINTS_OF_INTEREST_BUNDLE_TAG);
//            }
//        }

        startForeground();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    private void startForeground() {
        Intent notificationIntent = new Intent(this, StartingActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        String channelId = "channel2";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = createNotificationChannel("channel2", "GPS Service");
        }
        startForeground(NOTIF_ID, new NotificationCompat.Builder(App.getContext(),
                channelId)
                .setOngoing(true)
                //.setSmallIcon(R.drawable.agslogo)
                .setContentTitle(getString(R.string.app_name))
                //.setContentText(getString(R.string.running_background))
                .setContentIntent(pendingIntent)
                .build());
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }
    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

    private class LocationListener implements android.location.LocationListener {
        private Location mLastLocation;


        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
//            addPolyLine(new PolylineOptions()
//                    .add(new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude()),
//                            new LatLng(location.getLatitude(), location.getLongitude()))
//                    .width(5)
//                    .color(Color.RED));
            previousLocation = location;

//            PointOfInterest closestPoi = null;
//            float distance = 1000;
//            if (pointOfInterests != null) {
//                for(PointOfInterest poi : pointOfInterests){
//                    Location poiLocation = new Location(poi.getName());
//                    poiLocation.setLatitude(poi.getLatitude());
//                    poiLocation.setLongitude(poi.getLongitude());
//                    if(location.distanceTo(poiLocation) < 25 && location.distanceTo(poiLocation) < distance){
//                        closestPoi = poi;
//                        distance = location.distanceTo(poiLocation);
//                    }
//                }
//            }
//
//            if(closestPoi != null){
//                for(Marker marker : mapMarkers){
//                    if(marker.getTitle().equals(closestPoi.getName())){
//                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                        marker.showInfoWindow();
//                        notificationService.sendNotification(marker.getTitle());
//                        for (PointOfInterest poi : pointOfInterests){
//                            if (poi.getName().equals(closestPoi.getName())){
//                                poi.setVisited(true);
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            Log.d(TAG, "onLocationChanged: lat: " + location.getLatitude() + " long: " + location.getLongitude());
//            mLastLocation.set(location);
        }

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
    }


    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
}
