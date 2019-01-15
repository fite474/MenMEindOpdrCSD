package com.example.maurice.menmeindopdr.API;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.maurice.menmeindopdr.NSData.Station;
import com.example.maurice.menmeindopdr.NSData.StationType;
import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.example.maurice.menmeindopdr.NSData.TreinRit;
import com.example.maurice.menmeindopdr.NSData.TreinType;
import com.example.maurice.menmeindopdr.TimeStamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NsAPIHandler  implements Serializable
{

    Context context;
    RequestQueue queue;
    NsListener listener;
    private String basicUrl;
    private List<Station> allStations;

    public NsAPIHandler(Context context, NsListener listener)
    {
        this.allStations = new ArrayList<>();
        this.context = context;
        queue = Volley.newRequestQueue(context.getApplicationContext());
        this.listener = listener;
        this.basicUrl = "https://ns-api.nl/reisinfo/api";


    }


    public void HandleAPICall(NSAPICallType type, @Nullable String codeFromStation, @Nullable String codeToStation)
    {
        switch (type)
        {
            case FROM_TO_REQUEST:
                if (codeFromStation != null && codeToStation != null)
                    findJourneys(makeJourneyUrl(codeFromStation, codeToStation));
                break;
            case FIND_STATIONS:
                getStations();
                break;
            case FIND_TRAIN:

                break;

            default:
                break;
        }
    }

    private void findJourneys(String url)
    {
//        JSONObject parameters makeJsonParams();
        JsonObjectRequest journeyRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) response -> {
                    try
                    {
                        Log.d("journey-RESPONSE", response.toString());
                        ArrayList<TreinReis> ritten = new ArrayList<>();
                        ArrayList<TreinRit> allLegs = new ArrayList<>();
                        JSONArray trips = response.getJSONArray("trips");
                        for (int i = 0; i < trips.length(); i++)
                        {
                            allLegs.clear();
                            JSONObject jsonRit = trips.getJSONObject(i);

                            int duration = jsonRit.getInt("plannedDurationInMinutes");
                            int transfers = jsonRit.getInt("transfers");
                            JSONArray legs = jsonRit.getJSONArray("legs");
                            JSONObject startLeg = legs.getJSONObject(0);
                            JSONObject origin = startLeg.getJSONObject("origin");
                            String destination = startLeg.getString("direction");
                            String departureTrack = origin.getString("plannedTrack");
                            String rideDepartureTime = origin.getString("plannedDateTime");
                            String cutRideDepTime = rideDepartureTime.substring(10,16);
                            String rideDepHourString = cutRideDepTime.substring(1,3);
                            String rideDepMinString = cutRideDepTime.substring(4,6);
                            int rideDepartureHour = Integer.valueOf(rideDepHourString);
                            int rideDepartureMin = Integer.valueOf(rideDepMinString);
                            TimeStamp rideDeparture = new TimeStamp(rideDepartureHour, rideDepartureMin);
                            String firstTrain = startLeg.getJSONObject("product").getString("categoryCode");
                            TreinType type;
                            if (firstTrain.equals("IC"))
                            {
                                type = TreinType.INTERCITY;
                            }
                            else if(firstTrain.equals("SPR"))
                            {
                                type = TreinType.SPRINTER;
                            }
                            else
                            {
                                type = TreinType.INTERCITY_DIRECT;
                            }


                            JSONObject finalRit = trips.getJSONObject(trips.length()-1);
                            String destinationStation = legs.getJSONObject(legs.length()-1).
                                    getJSONObject("destination").
                                    getString("name");

                            String depStation = origin.getString("name");




                            for(int y = 0; y < legs.length(); y++)
                            {
                                JSONObject leg = legs.getJSONObject(y);
                                boolean cancelled = leg.getBoolean("cancelled");
                                String legDestStationName = leg.getString("direction");
                                JSONObject legOrigin = leg.getJSONObject("origin");
                                JSONObject legDestination = leg.getJSONObject("destination");

                                String startStation = legOrigin.getString("name");
                                String legDepTime = legOrigin.getString("plannedDateTime");
                                String cutLegDepTime = legDepTime.substring(10,16);
                                String legDepHourString = cutLegDepTime.substring(1,3);
                                String legDepMinString = cutLegDepTime.substring(4,6);
                                int legDepartureHour = Integer.valueOf(legDepHourString);
                                int legDepartureMin = Integer.valueOf(legDepMinString);
                                TimeStamp legDeparture = new TimeStamp(legDepartureHour, legDepartureMin);



                                String endStation = legDestination.getString("name");
                                String legArrTime = legDestination.getString("plannedDateTime");
                                String cutLegArrTime = legArrTime.substring(10,16);
                                String legArrHour = cutLegArrTime.substring(1,3);
                                String legArrMin = cutLegArrTime.substring(4,6);
                                int legArrivalHour = Integer.valueOf(legArrHour);
                                int legArrivalMin = Integer.valueOf(legArrMin);
                                Log.d("TIME-TAG-ARRIVAL", "first cut:"+ cutLegArrTime + " ---> cut in hours and minutes: "+ legArrHour + ":"+legArrMin + "  --> cut in integers: " +legArrivalHour + ":" + legArrivalMin );

                                TimeStamp legArrival = new TimeStamp(legArrivalHour, legArrivalMin);

                                Log.d("TIME-TAG-ARRIVAL", legArrival.toString());





                                String treintype = leg.getJSONObject("product").getString("categoryCode");

                                String crowdness = leg.getString("crowdForecast");
                                int totalMinutes = (legArrival.getTotalMinutes() - legDeparture.getTotalMinutes());
                                TimeStamp rideTime = new TimeStamp(totalMinutes);

                                TreinType legTrainType;
                                if (treintype.equals("IC"))
                                {
                                    legTrainType = TreinType.INTERCITY;
                                }
                                else if(treintype.equals("SPR"))
                                {
                                    legTrainType = TreinType.SPRINTER;
                                }
                                else
                                {
                                    legTrainType = TreinType.INTERCITY_DIRECT;
                                }

                                if(cancelled)
                                {
                                    allLegs.add(new TreinRit(startStation, endStation, legDeparture, legArrival, rideTime.toString(), crowdness, legTrainType, legDestStationName));
                                }
                                else
                                {
                                    String legDepartureTrack = legOrigin.getString("plannedTrack");
                                    String legArrivalTrack = legDestination.getString("plannedTrack");
                                    allLegs.add(new TreinRit(legDestStationName, legTrainType, crowdness, startStation, endStation, legDeparture, legArrival, legDepartureTrack, legArrivalTrack, rideTime.toString()));
                                }


                            }
                            TreinReis rit = new TreinReis(depStation, destinationStation, duration, transfers, rideDeparture, null, departureTrack, destination);
                            rit.setEersteTreinType(type);
                            rit.setAankomsttijd(allLegs.get(allLegs.size()-1).getArrivalTime());
                            rit.setLegs(allLegs);

                            ritten.add(rit);
                        }

                        listener.onJourneysAvailable(ritten);


                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                },
                (Response.ErrorListener) response ->
                {
                    listener.noJourneyAvailable();
                }
        )
        {
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("x-api-key", "SYfRxOVRoY9ib6u01cXE15gFRp2FraTB7OR7xFad");
                return headers;
            }
        };
        queue.add(journeyRequest);
    }

//    private JSONObject makeJsonParams()
//    {
//        JSONObject parameters = new JSONObject();
//        try
//        {
//            parameters.put("travelClass", 2);
//            parameters.put("originTransit", false);
//            parameters.put("originWalk", false);
//            parameters.put("originBike", false);
//            parameters.put("originCar", false);
//            parameters.put("")
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//    }


    private void getStations()
    {
        String url = basicUrl + "/v2/stations";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            ArrayList<Station> stations = new ArrayList<>();
                            JSONArray payload = response.getJSONArray("payload");
                            for (int i = 0; i < payload.length(); i++)
                            {
                                StationType type;

                                JSONObject stationJson = payload.getJSONObject(i);
                                String code = stationJson.getString("code");
                                String stationType = stationJson.getString("stationType");
                                if (stationType == "STOPTREIN_STATION")
                                {
                                    type = StationType.STOP_STATION;
                                }
                                else
                                {
                                    type = StationType.IC_STATION;
                                }

                                String naam = stationJson.getJSONObject("namen").getString("lang");
                                String country = stationJson.getString("land");
                                String uicCode = stationJson.getString("UICCode");
                                double lat = stationJson.getDouble("lat");
                                double lon = stationJson.getDouble("lng");

                                Station station = new Station(code, type, naam, country, uicCode, lat, lon);
                                stations.add(station);
                            }
                            if(stations.size() >0)
                            {
                                listener.onStationsAvailable(stations);
                            }
                            else
                            {
                                listener.noStationAvailable();
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        listener.noStationAvailable();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap();
                headers.put("Accept", "application/json");
                headers.put("x-api-key", "SYfRxOVRoY9ib6u01cXE15gFRp2FraTB7OR7xFad");
                return headers;
            }
        };
        queue.add(request);
    }


    private String makeJourneyUrl(String from, String to)
    {
        return basicUrl + "/v3/trips?fromStation=" + from.toLowerCase() + "&toStation=" + to.toLowerCase();
    }
}