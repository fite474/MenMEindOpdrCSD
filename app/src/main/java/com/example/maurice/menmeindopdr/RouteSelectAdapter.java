package com.example.maurice.menmeindopdr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maurice.menmeindopdr.NSData.TreinReis;
import com.example.maurice.menmeindopdr.NSData.TreinType;

import java.util.ArrayList;

public class RouteSelectAdapter extends ArrayAdapter<TreinReis> {

    ImageView trainImage;
    ImageView trackImage;
    ImageView transferImage;
    TextView departureTrack;
    TextView transfers;
    TextView departureTime;
    TextView arrivalTime;
    TextView journeyDuration;
    TextView destination;

    public RouteSelectAdapter(Context context, ArrayList<TreinReis> items) {
        super( context, 0, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // Reis ophalen
        TreinReis treinReis = getItem(position);

        // View aanmaken of herbruiken
        if( convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_pickjourney,
                    parent,
                    false
            );
        }
        if(treinReis != null)
        {
            destination = convertView.findViewById(R.id.destinationTV);
            destination.setText(treinReis.getLegs().get(0).getDestination());

            trainImage = convertView.findViewById(R.id.trainIconDetail);
            chooseSetTrainImage(treinReis);

            trackImage = convertView.findViewById(R.id.trackIcon);
            trackImage.setImageResource(R.drawable.track);

            transferImage = convertView.findViewById(R.id.transferIcon);
            transferImage.setImageResource(R.drawable.transfer);

            departureTrack = convertView.findViewById(R.id.trackText);
            departureTrack.setText(treinReis.getVertrekSpoor());

            transfers = convertView.findViewById(R.id.transferText);
            transfers.setText(String.valueOf(treinReis.getAantalOverstappen()));

            departureTime = convertView.findViewById(R.id.depTimeTextView);
            departureTime.setText(treinReis.getVertrektijd().toString());

            arrivalTime = convertView.findViewById(R.id.arrTimeTV);
            arrivalTime.setText(treinReis.getAankomsttijd().toString());

            journeyDuration = convertView.findViewById(R.id.durTV);
            journeyDuration.setText(treinReis.getRitDuration().toString());

        }
        return convertView;
    }

    private void chooseSetTrainImage(TreinReis treinReis)
    {
        TreinType type = treinReis.getLegs().get(0).getType();
        TimeStamp departure = treinReis.getVertrektijd();
        if(departure.getHours() <7 || departure.getHours() >18)
        {
            if (type == TreinType.INTERCITY)
            {
                trainImage.setImageResource(R.drawable.trainicon_ic_night);
            }
            else if(type == TreinType.SPRINTER)
            {
                trainImage.setImageResource(R.drawable.trainicon_spr_night);
            }
            else
            {
                trainImage.setImageResource(R.drawable.trainicon_icd_night);
            }
        }
        else
        {
            if (type == TreinType.INTERCITY)
            {
                trainImage.setImageResource(R.drawable.trainicon_ic_day);
            }
            else if(type == TreinType.SPRINTER)
            {
                trainImage.setImageResource(R.drawable.trainicon_spr_day);
            }
            else
            {
                trainImage.setImageResource(R.drawable.trainicon_icd_day);
            }
        }


    }
}
