package com.example.maurice.menmeindopdr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.NSData.Station;
import com.example.maurice.menmeindopdr.NSData.TreinRit;
import com.example.maurice.menmeindopdr.NSData.TreinType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RouteSelectAdapter extends ArrayAdapter<TreinRit> {

    ImageView trainImage;
    TextView departureTrack;
    TextView transfers;
    TextView departureTime;
    TextView arrivalTime;
    TextView journeyDuration;

    public RouteSelectAdapter(Context context, ArrayList<TreinRit> items) {
        super( context, 0, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // Mural ophalen
        TreinRit treinRit = getItem(position);

        // View aanmaken of herbruiken
        if( convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_pickroute,
                    parent,
                    false
            );
        }
        if(treinRit!= null)
        {
            trainImage = convertView.findViewById(R.id.trainIconImageView);
            if (treinRit.getTreinType() == TreinType.INTERCITY)
            {
                trainImage.setImageResource(R.drawable.trainicon_ic);
            }
            else
            {
                trainImage.setImageResource(R.drawable.trainicon_spr);
            }

            departureTrack = convertView.findViewById(R.id.departureTrackTextView);
            departureTrack.setText(treinRit.getVertrekSpoor());

            transfers = convertView.findViewById(R.id.transferTextView);
            transfers.setText(treinRit.getAantalOverstappen());

            departureTime = convertView.findViewById(R.id.depTimeTV);
            departureTime.setText(String.valueOf(treinRit.getVertrektijd()));

            arrivalTime = convertView.findViewById(R.id.arrTimeTV);
            arrivalTime.setText(String.valueOf(treinRit.getAankomsttijd()));

            journeyDuration = convertView.findViewById(R.id.durTV);
            int minutes = treinRit.getRitDuur() % 60;
            int hours = treinRit.getRitDuur() / 60;

            String ritduur = " ";
            if(minutes < 10)
            {
                ritduur = hours + ":0"+minutes + " uur";
            }
            else
            {
                ritduur =hours + ":"+minutes + " uur";
            }

        }
        return convertView;
    }
}
