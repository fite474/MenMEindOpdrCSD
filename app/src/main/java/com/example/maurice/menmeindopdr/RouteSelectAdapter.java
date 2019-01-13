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
    ImageView trackImage;
    ImageView transferImage;
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

            trackImage = convertView.findViewById(R.id.trackImageView);
            trackImage.setImageResource(R.drawable.track);

            transferImage = convertView.findViewById(R.id.transferImageView);
            transferImage.setImageResource(R.drawable.transfer);

            departureTrack = convertView.findViewById(R.id.departureTrackTextView);
            departureTrack.setText(treinRit.getVertrekSpoor());

            transfers = convertView.findViewById(R.id.transferTextView);
            transfers.setText(String.valueOf(treinRit.getAantalOverstappen()));

            departureTime = convertView.findViewById(R.id.depTimeTV);
            String departuretime = "";
            if(treinRit.getVertrektijd().getMinutes() < 10)
            {
                departuretime = ""+ treinRit.getVertrektijd().getHours() + ":0" + treinRit.getVertrektijd().getMinutes();
            }
            else
            {
                departuretime = ""+ treinRit.getVertrektijd().getHours() + ":"+ treinRit.getVertrektijd().getMinutes();
            }
            departureTime.setText(departuretime);

            arrivalTime = convertView.findViewById(R.id.arrTimeTV);
            String arrivaltime = "";
            if(treinRit.getAankomsttijd().getMinutes() < 10)
            {
                arrivaltime = ""+ treinRit.getAankomsttijd().getHours() + ":0" + treinRit.getAankomsttijd().getMinutes();
            }
            else
            {
                arrivaltime = ""+ treinRit.getAankomsttijd().getHours() + ":"+ treinRit.getAankomsttijd().getMinutes();
            }
            arrivalTime.setText(arrivaltime);

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
            journeyDuration.setText(ritduur);

        }
        return convertView;
    }
}
