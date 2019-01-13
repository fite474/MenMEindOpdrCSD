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
import com.example.maurice.menmeindopdr.NSData.TreinRit;
import com.example.maurice.menmeindopdr.NSData.TreinType;

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
                        ArrayList<TreinRit> ritten = new ArrayList<>();
                        JSONArray trips = response.getJSONArray("trips");
                        for (int i = 0; i < trips.length(); i++)
                        {
                            JSONObject jsonRit = trips.getJSONObject(i);
                            int duration = jsonRit.getInt("plannedDurationInMinutes");
                            int transfers = jsonRit.getInt("transfers");
                            JSONArray legs = jsonRit.getJSONArray("legs");
                            JSONObject startLeg = legs.getJSONObject(0);
                            String treintype = startLeg.getJSONObject("product").getString("categoryCode");
                            JSONObject origin = startLeg.getJSONObject("origin");
                            String destination = startLeg.getString("direction");

                            String departureTrack = origin.getString("plannedTrack");
                            String departureTime = origin.getString("plannedDateTime");
                            JSONObject endleg;
                            if (transfers > 0)
                            {
                                endleg = legs.getJSONObject(transfers);
                            }
                            else
                            {
                                endleg = startLeg;
                            }
                            String arrivalTime = endleg.getJSONObject("destination").getString("plannedDateTime");

                            int plusIndex = arrivalTime.indexOf('+');
                            String cutArrivalTime = arrivalTime.substring(0, plusIndex - 1);
                            String cutDepartTime = departureTime.substring(0, plusIndex - 1);

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss");
                            Date vertrekTijd = format.parse(cutDepartTime);
                            Date aankomstTijd = format.parse(cutArrivalTime);
                            TreinType type;
                            if (treintype.equals("IC"))
                            {
                                type = TreinType.INTERCITY;
                            }
                            else
                            {
                                type = TreinType.SPRINTER;
                            }


                            TreinRit rit = new TreinRit(duration, transfers, vertrekTijd, aankomstTijd, type, departureTrack, destination);
                            ritten.add(rit);
                        }

                        listener.onJourneysAvailable(ritten);


                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    } catch (ParseException e)
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

                            listener.onStationsAvailable(stations);
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