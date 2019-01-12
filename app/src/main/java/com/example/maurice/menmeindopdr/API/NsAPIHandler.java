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

public class NsAPIHandler  implements Serializable {

    Context context;
    RequestQueue queue;
    NsListener listener;
    private String basicUrl;
    private List<Station> allStations;

    public NsAPIHandler(Context context, NsListener listener) {
        this.allStations = new ArrayList<>();
        this.context = context;
        queue = Volley.newRequestQueue(context.getApplicationContext());
        this.listener = listener;
        this.basicUrl = "https://ns-api.nl/reisinfo/api";


    }




    public void HandleAPICall(NSAPICallType type, @Nullable String codeFromStation, @Nullable String codeToStation)
    {
        switch(type)
        {
            case FROM_TO_REQUEST:
                if(codeFromStation != null && codeToStation != null)
                    findJourneys(makeJourneyUrl(codeFromStation, codeToStation));
                break;
            case FIND_STATIONS:
                getStations();
                break;
            case FIND_TRAIN:

                break;

            default:break;
        }
    }

    private void findJourneys(String url)
    {
        JsonObjectRequest journeyRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) response -> {
                    try
                    {
                        ArrayList<TreinRit> ritten = new ArrayList<>();
                        JSONArray trips = response.getJSONArray("trips");
                        for(int i = 0; i < trips.length(); i++)
                        {
                            JSONObject jsonRit = trips.getJSONObject(i);
                            int duration = jsonRit.getInt("plannedDurationInMinutes");
                            int transfers = jsonRit.getInt("transfers");
                            JSONArray legs = jsonRit.getJSONArray("legs");
                            JSONObject startLeg = legs.getJSONObject(0).getJSONObject("origin");
                            String treintype = startLeg.getJSONObject("product").getString("categoryCode");
                            String departureTrack = startLeg.getString("plannedTrack");
                            String departureTime = startLeg.getString("actualDateTime");
                            JSONObject endleg;
                            if(transfers > 0)
                            {
                                endleg = legs.getJSONObject(transfers);
                            }
                            else
                            {
                                endleg = startLeg;
                            }
                            String arrivalTime = endleg.getJSONObject("destination").getString("actualDateTime");

                            int plusIndex = arrivalTime.indexOf('+');
                            String cutArrivalTime = arrivalTime.substring(0, plusIndex-1);
                            String cutDepartTime = departureTime.substring(0, plusIndex-1);

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss");
                            Date vertrekTijd = format.parse(cutDepartTime);
                            Date aankomstTijd = format.parse(cutArrivalTime);
                            TreinType type;
                            if(treintype.equals("IC")){type = TreinType.INTERCITY;}
                            else{type = TreinType.SPRINTER;}


                            TreinRit rit = new TreinRit(duration, transfers, vertrekTijd, aankomstTijd, type);
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
                    Log.d("JOURNEYERROR", "YOU'RE NOT GOING ON AN ADVENTURE");
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
                            for(int i = 0; i < payload.length(); i++)
                            {
                                Log.d("Handling station: ", String.valueOf(i));
                                StationType type;

                                JSONObject stationJson = payload.getJSONObject(i);
                                String code = stationJson.getString("code");
                                String stationType = stationJson.getString("stationType");
                                if(stationType == "STOPTREIN_STATION")
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
                        Log.d("ERROR_GETTING_STATIONS", "Kan stations niet vinden pleurislijer");
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

//    private void getSingleLight(String lightID)
//    {
//
//        String url = basicUrl + "/lights/" + lightID;
//        JsonObjectRequest singleLightRequest = new JsonObjectRequest(
//                DownloadManager.Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response)
//                    {
//                        Log.i("tag", "msg");
//                        Lamp lamp = new Lamp(response);
//
//                        listener.onLampAvailable(lamp);
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("NRA", "NOT OK");
//                        listener.onLampWallError(error.toString());
//                    }
//                }
//        );
//        this.queue.add(singleLightRequest);
//        System.out.println("------------------------------------------------------");
//    }
//
//    private void setSingleLight(String lightID, JSONObject body)
//    {
//        String url = basicUrl+"/lights/"+lightID+ "/state";
//        JsonObjectRequest setLightRequest = new JsonObjectRequest(
//                Request.Method.PUT,
//                url,
//                body,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response)
//                    {
//                        System.out.println(response.toString());
//                    }
//                },
//                null
//        );
//        this.queue.add(setLightRequest);
//    }

    private String makeJourneyUrl(String from, String to)
    {
        return basicUrl + "/v3/trips?travelClass=2&originTransit=false&originWalk=false&originBike=false"
                +"&originCar=false&travelAssistanceTransferTime=0&searchForAccessibleTrip=false&destinationTransit=false"
                +"&destinationWalk=false&destinationBike=false&destinationCar=false&excludeHighSpeedTrains=false"
                +"&excludeReservationRequired=false&passing=false&fromStation=" + from + "&toStation=" + to;
    }
}
