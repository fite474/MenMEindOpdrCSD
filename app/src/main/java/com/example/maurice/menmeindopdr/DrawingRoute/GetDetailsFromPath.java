package com.example.maurice.menmeindopdr.DrawingRoute;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDetailsFromPath extends AsyncTask<String, Void, List<Integer>> {

    private String TAG = "GetPathFromLocation";
    private String API_KEY = "a4a31a0f-43a6-4e33-b743-49b556907849";
    private LatLng source, destination;
    private DirectionsDetailsListener resultCallback;
    private int durationTime;
    private int totalMeters;
    private List<Integer> details;
    // private List<PointOfInterest> wayPoints;

    public GetDetailsFromPath(LatLng source, LatLng destination, DirectionsDetailsListener resultCallback) {
        this.source = source;
        this.destination = destination;
        this.resultCallback = resultCallback;
        details = new ArrayList<>();
        //this.wayPoints = wayPoints;

    }

    public String getUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // StringBuilder str_wayPoints = new StringBuilder("waypoints=optimize:true");
//        for(PointOfInterest pointOfInterest : wayPoints)
//        {
//            str_wayPoints.append("|").append(pointOfInterest.getLatitude()).append(",").append(pointOfInterest.getLongitude());
//        }
        String mode = "mode=walking";
        String parameters = str_origin + "&" + str_dest + "&" +  mode;
        //String output = "json";

        //String urlOriginal = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + API_KEY;
        String url = "https://maps.moviecast.io/directions" + "?" + parameters + "&key=" + API_KEY;

        return url;
    }

    public int getRouteDuration()
    {

        return this.durationTime;
    }

    public int getRouteMeters()
    {

        return this.totalMeters;
    }


    @Override
    protected List<Integer> doInBackground(String... url) {

        String data;

        try {
            InputStream inputStream = null;
            HttpURLConnection connection = null;
            try {
                URL directionUrl = new URL(getUrl(source, destination));//, wayPoints));
                connection = (HttpURLConnection) directionUrl.openConnection();
                connection.connect();
                inputStream = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                data = stringBuffer.toString();
                bufferedReader.close();

            } catch (Exception e) {
                Log.e(TAG, "Exception : " + e.toString());
                return null;
            } finally {
                inputStream.close();
                connection.disconnect();
            }
            Log.e(TAG, "Background Task data : " + data);


            JSONObject jsonObject;


            try {
                jsonObject = new JSONObject(data);
                // Starts parsing data
                DirectionHelper helper = new DirectionHelper();


                durationTime = helper.parseRouteDuration(jsonObject);
                totalMeters = helper.parseRouteDistance(jsonObject);
                details.add(durationTime);
                details.add(totalMeters);
                //return details;
                Log.e(TAG, "Executing Routes : "/*, routes.toString()*/);


                // Traversing through all the routes


                // Drawing polyline in the Google Map for the i-th route
                if (details.size() > 1) {
                    return details;
                } else {
                    return null;
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception in Executing Routes : " + e.toString());
                return null;
            }

        } catch (Exception e) {
            Log.e(TAG, "Background Task Exception : " + e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Integer> details) {
        super.onPostExecute(details);
        if (resultCallback != null && details != null) {
            resultCallback.onDetails(details);
//            resultCallback.onDistance(details.get(0));
//            resultCallback.onDuration(details.get(1));

        }

    }
}