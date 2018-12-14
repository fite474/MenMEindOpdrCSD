package com.example.maurice.menmeindopdr.API;

import android.app.DownloadManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.Serializable;

public class NsAPIHandler  implements Serializable {

    Context context;
    RequestQueue queue;
    NsListener listener;
    private String basicUrl;
   // private String apiKey;


    public NsAPIHandler(Context context, NsListener listener, String ipAdress) {

        this.context = context;
        queue = Volley.newRequestQueue(context.getApplicationContext());
        this.listener = listener;
        //this.basicUrl = "http://"+ipAdress+"/api";
        this.basicUrl = "http://"+ipAdress+"/api/" + "iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB";


    }


//    public void HandleAPICall(NSAPICallType type, @Nullable String lightID, @Nullable JSONObject body)
//    {
//        switch(type)
//        {
////            case GET_ALL_LIGHTS:
////                getAllLights();
////                break;
////            case SET_ALL_LIGHTS:
////                setAllLights(body);
////                break;
////            case GET_SINGLE_LIGHT:
////
////                getSingleLight(lightID);
////                break;
////            case SET_SINGLE_LIGHT:
////                setSingleLight(lightID, body);
////                break;
//            default:break;
//        }
//    }
//
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

}
